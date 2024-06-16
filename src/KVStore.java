import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;


public class KVStore {

    private class collectionID{
        String name;
        UUID uuid;
        String tag;
    }

    private static final Map<String, Pair<UUID, Object>> collection = new ConcurrentHashMap<>();
    private static final Map<collectionID, ConcurrentHashMap<String, Pair<UUID, Object>>> store = new ConcurrentHashMap<>();


    public void createCollection(String collName){
        UUID uuid = UUID.randomUUID();
        collectionID cid = new collectionID();
        cid.name = collName;
        cid.uuid = uuid;
        store.put(cid, new ConcurrentHashMap<String, Pair<UUID, Object>>());
    }

    public void setRecord(String key, Object value ){
        UUID uuid= UUID.randomUUID();
        collection.put(key, new Pair<>(uuid, value));
    }

    public Object getRecord(String key){
        Pair<UUID,Object> value= collection.get(key);
        return value.getValue();
    }

    public String getAllRecords(){
        return collection.toString();
    }

    public boolean deleteRecord(String key){
        Object value= collection.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }

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

    protected UUID getUUIDforKey(String key){
        Pair<UUID,Object> value= collection.get(key);
        return value.getKey();
    }

}
