import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DockerMonitor2 {
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
            this.cont = n;
            //System.out.println(cont);
        } catch (IOException e) {
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


    public static void main(String[] args){
        DockerMonitor2 dm = new DockerMonitor2();
        CountDownLatch cdl = new CountDownLatch(1);
        new Thread(()->{
            dm.setCont();
            int c = dm.getCount();
            System.out.println("current count"+c);
            if(c==dm.getCount()){
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //notify opendaylight
            System.out.println("notify opendaylight");

        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            int c = dm.getCount();
            while(true) {
                dm.setCont();
                if(c!=dm.getCount()){
                    cdl.countDown();
                    //c = dm.getCount();
                    break;  //needed to be deleted
                }
                System.out.println("----");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            }
        },"t1").start();


    }
}
