import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.Base64;

class monitorThread implements Runnable {
    BufferedReader br;
    boolean close = false;
    Thread t;

    monitorThread(SSLSocket sslSocket) throws IOException {
        br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (!close) {
            try {
                String reply = br.readLine();
                if (reply != null)
                    System.out.println("[SERVER]: " + reply);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class mailsent {
    static DataOutputStream dos;

    public static void main(String[] args) throws IOException, InterruptedException {
        String username = "s1910976117@ru.ac.bd";
        String password = "your_mail_pasword";

        username = new String(Base64.getEncoder().encode(username.getBytes()));
        password = new String(Base64.getEncoder().encode(password.getBytes()));

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("smtp.gmail.com", 465);

        dos = new DataOutputStream(sslSocket.getOutputStream());

        monitorThread monitor = new monitorThread(sslSocket);

        send("EHLO smtp.gmail.com");
        send("AUTH LOGIN");
        send(username);
        send(password);
        send("MAIL FROM:<s1910976117@ru.ac.bd>");
        send("RCPT TO:<dear.testmail@gmail.com>");
        send("DATA");
        send("Subject: Testing Socket Mailing");
        send("[Test-attempt] 3");
        send(".");
        send("QUIT");

        monitor.close = true;
        sslSocket.close();
    }

    public static void send(String msg) throws IOException, InterruptedException {
        dos.writeBytes(msg + "\r\n");
        System.out.println("[MSG]: " + msg);
        Thread.sleep(1000);
        System.out.println();
    }
}
