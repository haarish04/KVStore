import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;


public class KVServices {

    private class collectionID{
        String name;
        UUID uuid;
        String tags;

        collectionID(String collName, String tags, UUID uuid){
            this.name = collName;
            this.uuid = uuid;
            this.tags = tags;
        }
    }

    private static final Map<String, Pair<UUID, List<Object>>> record = new ConcurrentHashMap<>();
    private static final Map<collectionID, ConcurrentHashMap<String, Pair<UUID, Object>>> store = new ConcurrentHashMap<>();

    //Helper method to prevent duplicate collections
    public boolean isExistingCollection(String collName){
        boolean flag= false;
        for(collectionID cid : store.keySet()){
            if(cid.name == collName)
                flag=true;
        }
        return flag;
    }


    //Helper method to check if key already exists
    public boolean isExistingKey(String key){
        return record.containsKey(key);
    }


    //Service to create new collection
    public String createCollection(String collName, String tags){
        if(isExistingCollection(collName)){
            return "Collection with the name "+ collName + " already exists";
        }
        final UUID uuid = UUID.randomUUID();
        collectionID cid = new collectionID(collName, tags, uuid);
        Object o = store.put(cid, new ConcurrentHashMap<String, Pair<UUID, Object>>());
        if(o != null)
            return "Create collection successfull";
        
        else
            return "Invalid create collection";
    }


    //Service to add new tags to existing collection
    public boolean addCollectionTag(String collName, String tags){
        boolean flag=false;
        for(collectionID cid : store.keySet()){
            if(cid.name.equals(collName)){
                ConcurrentHashMap<String, Pair<UUID, Object>> collectionToUpdate = store.get(cid);
                store.remove(cid);
                String updatedTags = cid.tags + "," + tags;
                cid.tags= updatedTags;
                store.put(cid, collectionToUpdate);
                flag=true;// Denotes operation is completed
            }
        }
        return flag;
    } 


    //Delete Tags from existing collection
    public boolean deleteCollectionTag(String collName){
        boolean flag=false;
        for(collectionID cid : store.keySet()){
            if(cid.name.equals(collName)){
                ConcurrentHashMap<String, Pair<UUID, Object>> collectionToUpdate = store.get(cid);
                store.remove(cid);
                cid.tags = "";
                store.put(cid, collectionToUpdate);
                flag=true;// Denotes operation is completed
            }
        }
        return flag;
    }

    //Rename existing collection
    public boolean renameCollection(String oldCollName, String newCollName){
        boolean flag = false;
        for (collectionID cid: store.keySet()){
            if(cid.name.equals(oldCollName)){
                ConcurrentHashMap<String, Pair<UUID, Object>> collectionToUpdate = store.get(cid);
                store.remove(cid);
                cid.name = newCollName;
                store.put(cid, collectionToUpdate);
                flag=true;// Denotes operation is completed
            }
        }
        return flag;
    }


    //Service to add new key-value pair to existing collection
    public int setRecord(String key, Object value){
        if(!isExistingKey(key)){
            final UUID uuid= UUID.randomUUID();
            List<Object> values= new ArrayList<>();
            values.add(value);
            record.put(key, new Pair<>(uuid, values));
            return 0;
        }
        Pair<UUID, List<Object>>recordPair= record.get(key);
        List<Object>values =recordPair.getValue();
        values.add(value);
        record.put(key, recordPair);
        return 1;
    }


    //Get specific records identified by key
    public Object getRecord(String key){
        Pair<UUID,List<Object>> value= record.get(key);
        return value.getValue();
    }

    //Get all the records
    public String getAllRecords(){
        return record.toString();
    }

    //Delete existing key value pair
    public boolean deleteKey(String key){
        Object value= record.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }

    //Delete value from list of values associated with the key
    public boolean deleteValue(String key, Object value){
        Pair<UUID, List<Object>> deleteFromValues= record.get(key);
        List<Object> valueList= deleteFromValues.getValue();
        if(valueList.remove(value))
            return true;
        
            return false;
    }

    //Update existing key-value
    public boolean updateRecord(String key,Object oldValue, Object newValue){
        Pair<UUID, List<Object>> updateExistingValue= record.get(key);
        List <Object> valueList = updateExistingValue.getValue();
        for (Object val : valueList){
            if(val.equals(oldValue))
            valueList.set(valueList.indexOf(oldValue), newValue);// Find index of oldValue and replace with newValue
            return true;
        }
        return false;
    }
    
    //Get UUID of record
    public UUID getUUIDforKey(String key){
        Pair<UUID,List<Object>> value= record.get(key);
        return value.getKey();
    }

    //Get UUID for collection
    public UUID getUUIDforCollection(String collName){
        for (collectionID cid: store.keySet()){
            if(cid.name == collName)
                return cid.uuid;
        }
        return null;
    }

}
