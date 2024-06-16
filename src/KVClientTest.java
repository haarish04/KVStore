import java.util.*;

public class KVClientTest {

    public static void main(String[] args) {
        KVStore kvStore = new KVStore();

        //Write test to create collection

        // Test adding a key-value pair
        kvStore.setRecord("abc", 100);
        System.out.println(kvStore.getRecord("abc")); // Expected output: 100

        // Test retrieving the UUID for a given key
        UUID uuidForAbc = kvStore.getUUIDforKey("abc");
        System.out.println(uuidForAbc); // Expected output: UUID of the key "abc"

        // Test updating a key-value pair
        kvStore.updateRecord("abc", 200);
        System.out.println(kvStore.getRecord("abc")); // Expected output: 200

        //Write test for getAll
        System.out.println(kvStore.getAllRecords());

        // Test deleting a key-value pair
        if (kvStore.deleteRecord("abc")) {
            System.out.println("Deleted");
        } else {
            System.out.println("Invalid delete");
        }

        // Attempt to retrieve a deleted key
        System.out.println(kvStore.getRecord("abc")); // Expected output: null

        // Test adding and deleting a non-existent key
        if (!kvStore.deleteRecord("nonExistentKey")) {
            System.out.println("Invalid delete");
        }

        //Write test for closing connection

    }
}
