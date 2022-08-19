import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.Base64;

class server_reply_monitor_thread implements Runnable {
    private final BufferedReader br;
    private Boolean isExit = false;
    private Thread t;

    server_reply_monitor_thread(SSLSocket sslSocket) throws Exception {
        this.br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        t = new Thread(this);
        t.start();
    }

    public void stopThread() {
        this.isExit = true;
    }

    @Override
    public void run() {
        while (!isExit) {
            try {
                String serverMsg = br.readLine();
                if (!serverMsg.isEmpty())
                    System.out.println("[SERVER:] " + serverMsg);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

public class Mail_send_using_socket {
    private static DataOutputStream dos;

    public static void main(String[] args) throws Exception {
        int delay = 1000;
        String username = "s1910976117@ru.ac.bd";
        String password = "your_email_password";

        // Encoded username and password by Base64
        username = new String(Base64.getEncoder().encode(username.getBytes()));
        password = new String(Base64.getEncoder().encode(password.getBytes()));

        // Creating secure socket by SSLSocketFactory
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("smtp.gmail.com", 465);

        dos = new DataOutputStream(sslSocket.getOutputStream());

        send("EHLO smtp.gmail.com\r\n");
        server_reply_monitor_thread monitor_thread = new server_reply_monitor_thread(sslSocket);
        Thread.sleep(delay);

        send("AUTH LOGIN\r\n");
        Thread.sleep(delay);

        send(username + "\r\n");
        Thread.sleep(delay);

        send(password + "\r\n");
        Thread.sleep(delay);

        send("MAIL FROM:<s1910976117@ru.ac.bd>\r\n");
        Thread.sleep(delay);

        send("RCPT TO:<chaity.paul1999@gmail.com>\r\n");
        Thread.sleep(delay);

        send("DATA\r\n");
        Thread.sleep(delay);

        send("Subject: Email sent using java-Socket\r\n");
        Thread.sleep(delay);

        send("Hi, I am Sagor. This email is send by Java-Socket using google SMTP server..!\r\n");
        Thread.sleep(delay);

        send(".\r\n");
        Thread.sleep(delay);

        send("QUIT\r\n");
        monitor_thread.stopThread();
        Thread.sleep(delay);
        sslSocket.close();
    }

    private static void send(String msg) throws Exception {
        dos.writeBytes(msg);
        System.out.println();
        System.out.print("[Client]: " + msg);
    }
}
