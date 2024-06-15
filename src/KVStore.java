import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

public class KVStore {

    private static final Map<String, Pair<UUID, Object>> store = new ConcurrentHashMap<>();

    public void set(String key, Object value ){
        UUID uuid= generateUUID();
        store.put(key, new Pair<>(uuid, value));
    }

    public Object get(String key){
        Pair<UUID,Object> value= store.get(key);
        return value.getValue();
    }

    public String getAll(){
        return store.toString();
    }

    public boolean delete(String key){
        Object value= store.remove(key);
        if(value!= null)
            return true;
        else
            return false;
    }

    public boolean update(String key, Object newValue){
        Pair<UUID,Object> existingEntry=store.get(key);
        if(existingEntry!=null){
            UUID uuid= existingEntry.getKey();
            store.put(key, new Pair<>(uuid, newValue));
            return true;
        }
        else
            return false;
    }

    private UUID generateUUID(){
        return UUID.randomUUID();
    }

    protected UUID getUUIDforKey(String key){
        Pair<UUID,Object> value= store.get(key);
        return value.getKey();
    }

}
