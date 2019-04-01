import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class F_Socket {

    public void SendMessage(String ip,int port,String message)  {
        Socket socket = null;
        try {
            socket = new Socket(ip,port);
            OutputStream out = socket.getOutputStream();
            out.write(message.getBytes());
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
