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
    public boolean isDuplicateCollection(String collName){
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
        if(isDuplicateCollection(collName)){
            return "Collection with the name "+ collName + " already exists";
        }
        UUID uuid = UUID.randomUUID();
        collectionID cid = new collectionID(collName, tags, uuid);
        Object o = store.put(cid, new ConcurrentHashMap<String, Pair<UUID, Object>>());
        if(o != null)
            return "Create collection successfull";
        
        else
            return "Invalid create collection";
    }

    //Service to add new tags to existing collection
    public boolean addCollectionTag(String collName, String tags){
        StringBuilder tag= new StringBuilder(tags);
        boolean flag=false;
        for(collectionID cid : store.keySet()){
            if(cid.name.equals(collName)){
                StringBuilder temp = new StringBuilder(cid.tags);
                temp.append(tag);
                cid.tags = temp.toString();
                flag=true;// Denotes operation is completed
            }
        }
        return flag;
    } 

    //Delete Tags from existing collection
    public boolean deleteCollectionTag(String collName){
        StringBuilder emptyTag= new StringBuilder("");
        boolean flag=false;
        for(collectionID cid : store.keySet()){
            if(cid.name.equals(collName)){
                cid.tags = emptyTag.toString();
                flag=true;// Denotes operation is completed
            }
        }
        return flag;
    }

    public boolean renameCollection(String oldCollName, String newCollName){
        return false;
    }

    //Service to add new key-value pair to existing collection
    public boolean setRecord(String key, Object value){
        boolean flag= true;
        if(!isExistingKey(key)){
            UUID uuid= UUID.randomUUID();
            List<Object> values= new ArrayList<>();
            values.add(value);
            record.put(key, new Pair<>(uuid, values));
            return flag;
        }
        Pair<UUID, List<Object>>recordPair= record.get(key);
        List<Object>values =recordPair.getValue();
        values.add(value);
        return flag;
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
    public boolean deleteRecord(String key){
        Object value= record.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }
    
    //Update existing record
    public boolean updateRecord(String key, Object oldValue, Object newValue){
        Pair<UUID,ListObject> existingEntry=record.get(key);
        if(existingEntry!=null){
            UUID uuid= existingEntry.getKey();
            record.put(key, new Pair<>(uuid, newValue));
            return true;
        }
        else
            return false;
    }
    
    //Get UUID of record
    public UUID getUUIDforKey(String key){
        Pair<UUID,Object> value= record.get(key);
        return value.getKey();
    }

}
