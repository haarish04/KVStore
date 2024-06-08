import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class KVServer{
    private static final int port = 8000;
    private KVStore KVStore = new KVStore();
    private ServerSocket serverSocket;

    public void start() throws IOException{
        serverSocket= new ServerSocket(port);
        System.out.println("Server started on port "+ port);

        while(true){
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket, KVStore)).start();
        }
    }

    public void shutdown() throws IOException{
        if(serverSocket != null){
            serverSocket.close();
            System.out.println("Server socket closed");
        }
    }

    public static void main(String[]args) throws IOException{
        new KVServer().start();
    }

}
