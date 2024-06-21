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

    private static final Map<String, Pair<UUID, Object>> collection = new ConcurrentHashMap<>();
    private static final Map<collectionID, ConcurrentHashMap<String, Pair<UUID, Object>>> store = new ConcurrentHashMap<>();


    //Service to create new collection
    public String createCollection(String collName, String tags){
        UUID uuid = UUID.randomUUID();
        collectionID cid = new collectionID(collName, tags, uuid);
        Object o = store.put(cid, new ConcurrentHashMap<String, Pair<UUID, Object>>());
        if(o != null)
            return "Create collection successfull";
        
        else
            return "Invalid create collection";
    }

    //Service to add new tags to existing collection
    public String addCollectionTag(String collName, String tags){
        return "";
    } 

    //Delete Tags from existing collection
    public boolean deleteCollectionTag(String collName){
        return false;
    }

    public boolean renameCollection(String oldCollName, String newCollName){
        return false;
    }

    //Service to add new key-value pair to existing collection
    public void setRecord(String key, Object value ){
        UUID uuid= UUID.randomUUID();
        collection.put(key, new Pair<>(uuid, value));
    }

    //Get specific records identified by key
    public Object getRecord(String key){
        Pair<UUID,Object> value= collection.get(key);
        return value.getValue();
    }

    //Get all the records
    public String getAllRecords(){
        return collection.toString();
    }

    //Delete existing key value pair
    public boolean deleteRecord(String key){
        Object value= collection.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }
    
    //Update existing record
    public boolean updateRecord(String key, Object newValue){
        Pair<UUID,Object> existingEntry=collection.get(key);
        if(existingEntry!=null){
            UUID uuid= existingEntry.getKey();
            collection.put(key, new Pair<>(uuid, newValue));
            return true;
        }
        else
            return false;
    }
    
    //Get UUID of record
    protected UUID getUUIDforKey(String key){
        Pair<UUID,Object> value= collection.get(key);
        return value.getKey();
    }

}
