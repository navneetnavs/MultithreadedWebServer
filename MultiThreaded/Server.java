import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;


public class Server {
    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try(PrintWriter toScoket = new PrintWriter(clientSocket.getOutputStream(), true))
            {
                toScoket.println("Hello Navneet from Server" +clientSocket.getInetAddress());
                toScoket.close();
                clientSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        };
    }

    public static void main(String args[]){
      int port = 8010;
      Server server = new Server();

      try
      {
          ServerSocket serverSocket = new ServerSocket(port);
          serverSocket.setSoTimeout(70000);
          while(true)
          {
              Socket clientSocket = serverSocket.accept();
              // create a new thread everytime
              Thread thread = new Thread(()->server.getConsumer().accept(clientSocket));
              thread.start();
          }
      }
      catch (IOException e)
      {
          e.printStackTrace();
      }
    }
}