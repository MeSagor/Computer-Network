import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        System.out.println("[Connected with server]...");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter your MSG: ");
                String msg = sc.nextLine();
                oos.writeObject(msg);
                Object fromServer = ois.readObject();
                System.out.println("[SERVER]: " + (String) fromServer);
            } catch (Exception e) {
                socket.close();
                System.out.println(e);
                break;
            }
        }
    }
}
