import java.util.*;

public class KVClientTest {

    public static void main(String[] args) {
        KVStore kvStore = new KVStore();

        // Test adding a key-value pair
        kvStore.set("abc", 100);
        System.out.println(kvStore.get("abc")); // Expected output: 100

        // Test retrieving the UUID for a given key
        UUID uuidForAbc = kvStore.getUUIDforKey("abc");
        System.out.println(uuidForAbc); // Expected output: UUID of the key "abc"

        // Test updating a key-value pair
        kvStore.update("abc", 200);
        System.out.println(kvStore.get("abc")); // Expected output: 200

        // Test deleting a key-value pair
        if (kvStore.delete("abc")) {
            System.out.println("Deleted");
        } else {
            System.out.println("Invalid delete");
        }

        // Attempt to retrieve a deleted key
        System.out.println(kvStore.get("abc")); // Expected output: null

        // Test adding and deleting a non-existent key
        if (!kvStore.delete("nonExistentKey")) {
            System.out.println("Invalid delete");
        }
    }
}
