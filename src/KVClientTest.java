import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class KVClientTest {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send a SET command
            out.println("SET key1 value1");
            System.out.println(in.readLine()); // Should print "Record added"

            // Send a GET command
            out.println("GET key1");
            System.out.println(in.readLine()); // Should print "Value: value1" or similar

            // Send an UPDATE command
            out.println("UPDATE key1 newValue1");
            System.out.println(in.readLine()); // Should print "Update Success"

            // Send a DELETE command
            out.println("DELETE key1");
            System.out.println(in.readLine()); // Should print "Record Deleted"

            // Attempt to GET the deleted key
            out.println("GET key1");
            System.out.println(in.readLine()); // Should indicate the key is not found

            // Close the connection gracefully
            out.println("BYE");
            System.out.println(in.readLine()); // Should print "Closing connection..."

        } catch (Exception e) {
            System.err.println("An error occurred while communicating with the server.");
            e.printStackTrace();
        }
    }
}
