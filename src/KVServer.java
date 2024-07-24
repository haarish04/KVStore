import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class KVServer{
    private static final int port = 8000;
    private KVServices KVServices = new KVServices();
    private ServerSocket serverSocket;

    //Method to initialize new threads and expose server to the port via the clientHandler
    public void start() throws IOException{
        serverSocket= new ServerSocket(port);
        System.out.println("Server started on port "+ port);

        while(true){
            Socket clientSocket = serverSocket.accept();
            new Thread(new KVClientHandler(clientSocket, KVServices)).start();
        }
    }

    //Method to handle shutting down of thread(s)
    public void shutdown() throws IOException{
        if(serverSocket != null){
            serverSocket.close();
            System.out.println("Server socket closed");
        }
    }

    public static void main(String[]args) throws IOException{
        KVServer kvserver = new KVServer();
        kvserver.start();
        try{
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
