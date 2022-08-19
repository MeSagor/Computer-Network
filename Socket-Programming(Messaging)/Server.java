import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("[Server Started]...");

        Socket socket = serverSocket.accept();
        System.out.println("[Client Connected]...");

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        while (true) {
            try {
                String client_msg = (String) ois.readObject();
                System.out.println("[CLIENT]: " + client_msg);
                oos.writeObject(client_msg.toUpperCase());
            } catch (Exception e) {
                socket.close();
                System.out.println(e);
            }
        }
    }
}
