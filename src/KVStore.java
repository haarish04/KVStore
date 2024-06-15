import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

public class KVStore {

    private static final Map<String, Pair<UUID, Object>> store = new ConcurrentHashMap<>();

    public void createCollection(String collName){

    }

    public void setRecord(String key, Object value ){
        UUID uuid= UUID.randomUUID();
        store.put(key, new Pair<>(uuid, value));
    }

    public Object getRecord(String key){
        Pair<UUID,Object> value= store.get(key);
        return value.getValue();
    }

    public String getAllRecords(){
        return store.toString();
    }

    public boolean deleteRecord(String key){
        Object value= store.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }

    public boolean updateRecord(String key, Object newValue){
        Pair<UUID,Object> existingEntry=store.get(key);
        if(existingEntry!=null){
            UUID uuid= existingEntry.getKey();
            store.put(key, new Pair<>(uuid, newValue));
            return true;
        }
        else
            return false;
    }

    protected UUID getUUIDforKey(String key){
        Pair<UUID,Object> value= store.get(key);
        return value.getKey();
    }

}
