import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {
    private final ExecutorService threadPool;
    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }
    public void handleClient(Socket clientSocket) throws IOException
    {
        try(PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true))
        {
            toSocket.println("Hello Navneet from Server" + clientSocket.getInetAddress());
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 10;
        Server server = new Server(poolSize);
        try
        {
            // Socket open from Server side
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("Server is Listening on port" +port);
            while(true){
                // Socket open from Client Side
                Socket clientSocket = serverSocket.accept();

                // use this threadpool to handle the client
                server.threadPool.execute(() -> {
                    try {
                        server.handleClient(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            // Shutdown the thread pool when the server exits
            server.threadPool.shutdown();
        }
    }
}