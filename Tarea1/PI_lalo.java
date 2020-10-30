import java.net.*;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.net.ServerSocket;
import java.lang.Thread;

class PI
{
  static Object lock = new Object();
  static double pi = 0;
  static class Worker extends Thread{   
    Socket conexion;
    Worker(Socket conexion)
    {
      this.conexion = conexion;
    }
    public void run()
    {
      try {
            // Algoritmo 1
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        double x = entrada.readDouble();
        System.out.println("Entro:"+ x);
        synchronized(lock){
            pi= x + pi;
        }
        entrada.close();
        salida.close();
        conexion.close();
      } catch (Exception e) {
          //TODO: handle exception
      }


    }
  }
  public static void main(String[] args) throws Exception
  {
    if (args.length != 1)
    {
      System.err.println("Uso:");
      System.err.println("java PI <nodo>");
      System.exit(0);
    }
    int nodo = Integer.valueOf(args[0]);
    if (nodo == 0)
    {
        // Algoritmo 2
        ServerSocket servidor = new ServerSocket(50000);
        Worker w[]  = new Worker[4];
        int i=0;
        do{
            Socket conexion = servidor.accept();
            w[i] = new Worker(conexion);
            w[i].start();
            i++;
            System.out.println("i es: "+ i +".\n");
        }while(i!= 3);
        double suma = 0;
        i = 0;
        do{
            if( i == 10000000)
                break;
            else
                suma = 4.0/(8*i+1)+ suma;
            i++;
        }while(i != 10000000);
        synchronized(lock){
            pi = pi + suma;
        }
        i = 0;
        do{
            w[i].join();
            i++;
        }while(i !=3);
        System.out.println("\n El valor de PI es:" + pi +".");
    }else
    {
        // Algoritmo 3
        Socket conexion = null;
        for(;;)
        try {
            conexion = new Socket("localhost", 50000);
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            double suma = 0;
            for( int i =0; i <= 10000000; i++){
                suma = 4.0/(8*i+2*(nodo-1)+3)+suma;
            }
        suma = nodo%2==0?suma:-suma;
        salida.writeDouble(suma);
        System.out.println("Salio:"+suma);
        
        entrada.close();
        salida.close();

        conexion.close();
        } catch (Exception e) {
            //TODO: handle exception
            Thread.sleep(100);
        }
        
        
    }
  }
}