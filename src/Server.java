import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * test ok ,can be used in controller side
 *
 * */
public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket socket = null;
        BufferedReader br = null;
        try {
            while(true){
                socket = serverSocket.accept();


                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String info = null;
                StringBuffer sb = new StringBuffer();
                while ((info = br.readLine())!=null){
                    sb.append(info);
                }
                System.out.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                serverSocket.close();
            }
            if(br !=null){
                br.close();
            }
            if(socket!=null){
                socket.close();
            }
        }

    }

}
