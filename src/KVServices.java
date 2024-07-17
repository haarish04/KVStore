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
    private static final Map<collectionID, ConcurrentHashMap<String, Pair<UUID, List<Object>>>> store = new ConcurrentHashMap<>();

    //Helper method to prevent duplicate collections
    public boolean isExistingCollection(String collName){
        boolean flag= false;
        for(collectionID cid : store.keySet()){
            if(cid.name.equals(collName))
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
        store.put(cid, new ConcurrentHashMap<String, Pair<UUID, List<Object>>>());
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> retrievedCollection = store.get(cid);
    
        // Custom message to indicate success or failure based on the presence of the collection
        return retrievedCollection != null ? "Collection created successfully (currently empty)" : "Failed to create collection";

    }

    //Service to get the entire collection
    public Object getCollection(String collName){
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName)){
                ConcurrentHashMap<String, Pair<UUID, List<Object>>> getCollection = store.get(cid);
                return getCollection;
            }
        }
        return null;
        
    }

    //Service to get the tags of a collection
    public String getCollectionTag(String collName){
        String collTags="";
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName)){
                collTags=cid.tags;
            }
        }
        return collTags;
    }


    //Service to add new tags to existing collection
    public boolean addCollectionTag(String collName, String tags){
        boolean flag=false;
        for(collectionID cid : store.keySet()){
            if(cid.name.equals(collName)){
                ConcurrentHashMap<String, Pair<UUID, List<Object>>> collectionToUpdate = store.get(cid);
                store.remove(cid);
                String updatedTags = cid.tags + ", " + tags;
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
                ConcurrentHashMap<String, Pair<UUID, List<Object>>> collectionToUpdate = store.get(cid);
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
                ConcurrentHashMap<String, Pair<UUID, List<Object>>> collectionToUpdate = store.get(cid);
                store.remove(cid);
                cid.name = newCollName;
                store.put(cid, collectionToUpdate);
                flag=true;// Denotes operation is completed
            }
        }
        return flag;
    }


    //Service to add new key-value pair to existing collection
    public int setRecord(String collName, String key, Object value){
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection = null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            else
                return -1;
            }
        if(!isExistingKey(key)){
            final UUID uuid= UUID.randomUUID();
            List<Object> values= new ArrayList<>();
            values.add(value);
            Collection.put(key, new Pair<>(uuid, values));
            record.put(key, new Pair<>(uuid, values));
            return 0;
        }
        Pair<UUID, List<Object>>recordPair= record.get(key);
        List<Object>values =recordPair.getValue();
        values.add(value);
        Collection.put(key, recordPair);
        record.put(key, recordPair);
        return 1;
    }


    //Get specific records identified by key
    public Object getRecord(String collName, String key) throws NullPointerException{
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection = null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            }
        Pair<UUID,List<Object>> value= Collection.get(key);
        //Pair<UUID,List<Object>> value= record.get(key);
        try{
            return value.getValue();
        }
        catch(Exception e){
            return "No record found";
        }
    }

    //Get all the records
    public String getAllRecords(String collName){
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection = null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            }
        StringBuilder allRecords= new StringBuilder();
        for(Map.Entry<String, Pair<UUID, List<Object>>> entry : Collection.entrySet()){
            String name= entry.getKey();
            List<Object> values = entry.getValue().getValue();

            allRecords.append("Name: ").append(name).append(", Values: ").append(values).append("\n");
        }
        return allRecords.toString();
    }

    //Delete existing key value pair
    public boolean deleteKey(String collName, String key){
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection = null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            }
        Object value= Collection.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }

    //Delete value from list of values associated with the key
    public boolean deleteValue(String collName, String key, Object value){
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection = null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            }
        Pair<UUID, List<Object>> deleteFromValues= Collection.get(key);
        List<Object> valueList= deleteFromValues.getValue();
        if(valueList.remove(value))
            return true;
        
            return false;
    }

    //Update existing key-value
    public boolean updateRecord(String collName, String key,Object oldValue, Object newValue){
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection= null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            }
        Pair<UUID, List<Object>> updateExistingValue= Collection.get(key);
        UUID UUIDforRecord= updateExistingValue.getKey();
        List <Object> valueList = updateExistingValue.getValue();
        for (Object val : valueList){
            if(val.equals(oldValue))
            valueList.set(valueList.indexOf(oldValue), newValue);// Find index of oldValue and replace with newValue
            Pair<UUID, List<Object>> updatedRecord= new Pair<UUID,List<Object>>(UUIDforRecord, valueList);
            Collection.put(key, updatedRecord);
            return true;
        }
        return false;
    }
    
    //Get UUID of record
    public UUID getUUIDforKey(String collName, String key){
        ConcurrentHashMap<String, Pair<UUID, List<Object>>> Collection = null;
        for(collectionID cid: store.keySet()){
            if(cid.name.equals(collName))
                Collection = store.get(cid);
            }
        Pair<UUID,List<Object>> value= Collection.get(key);
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
