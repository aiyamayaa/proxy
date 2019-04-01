import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
/**
 * this test ok
 *
 * */
public class DockerMonitor4 {

    volatile int cont = -1;
    BufferedReader br = null;
    public void setCont(){
        try {
            Process p = Runtime.getRuntime().exec("docker ps");
            br  = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int n=-1;
            while((br.readLine())!=null){
                n++;
            }
            synchronized (this){
                this.cont = n;
            }
            TimeUnit.MILLISECONDS.sleep(200);
            //System.out.println(cont);
            p.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public int getCount(){

        return this.cont;
    }


    public static void main(String args[]){
        String ip = "127.0.0.1";
        int port = 12345;
        DockerMonitor4 dm = new DockerMonitor4();


        new Thread(()->{
            int c = 0;
            while(true){
                int d = dm.getCount();
                if(c!=d){
                    Thread t = new Thread(()->{
                        new F_Socket().SendMessage(ip,port,"nums:"+d);
                    });
                    t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    c=d;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            while(true){
                dm.setCont();
            }

        }).start();

    }

}
