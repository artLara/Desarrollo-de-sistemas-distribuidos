import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.net.MulticastSocket;
import java.io.BufferedReader; 
import java.io.InputStreamReader; 

class Chat
{
    static int tamanioMaximoMensaje = 100;
    static class Worker extends Thread {
        public void run() {
            // En un ciclo infinito se recibirán los mensajes enviados al grupo 
            // 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
            byte[] a;
            while(true){
                try {
                    InetAddress grupo = InetAddress.getByName("230.0.0.0");
                    MulticastSocket socket = new MulticastSocket(50000);
                    socket.joinGroup(grupo);
                    a = recibe_mensaje(socket,tamanioMaximoMensaje);
                    System.out.println(new String(a,"UTF-8"));
                } catch (Exception e) {
                    //TODO: handle exception
                }
                
            }
        }
    }

    static void envia_mensaje(byte[] buffer,String ip,int puerto) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName(ip);
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length,grupo,puerto);
        socket.send(paquete);
        socket.close();
    }

    static byte[] recibe_mensaje(MulticastSocket socket,int longitud) throws IOException
    {
        byte[] buffer = new byte[longitud];
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        return buffer;
    }

    public static void main(String[] args) throws Exception{
        Worker w = new Worker();
        w.start();
        String nombre = args[0];
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        // En un ciclo infinito se leerá los mensajes del teclado y se enviarán
        // al grupo 230.0.0.0 a través del puerto 50000.
        //byte[] mensaje = new byte[tamanioMaximoMensaje];
        byte[] mensaje;
        while(true){
            mensaje = (nombre + " escribe: " + b.readLine()).getBytes();
            envia_mensaje(mensaje, "230.0.0.0", 50000);
        }
        
    }
}