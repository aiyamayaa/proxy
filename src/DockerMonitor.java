import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerMonitor {
    BufferedReader br = null;
    public void getDockerNums(){
        try {
            Process p = Runtime.getRuntime().exec("docker ps");
            br  = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int cont=-1;
            while((br.readLine())!=null){
                cont++;
            }
            System.out.println(cont);
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

    public static void main(String[] args){
        DockerMonitor dm = new DockerMonitor();
        new Thread(dm::getDockerNums,"t1").start();
    }
}
