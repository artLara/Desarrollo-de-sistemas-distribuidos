import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.Thread;


class MultiplicaMatrizDistribuido_Eficiente{
    static int N = 1000;//Tamanio de matrices
    static int[][] A = new int[N][N];//Declaracion dematriz de NxN
    static int[][] B = new int[N][N];//Declaracion dematriz de NxN
    static int[][] C = new int[N][N];//Declaracion dematriz de NxN
    static Object lock = new Object();
    static long checksum = 0;

    static class Worker extends Thread{
        Socket conexion;
        int id;
        Worker(Socket conexion, int id){
            this.conexion = conexion;
            this.id = id;
        }

        public void run(){
            try{
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                //double x = entrada.readDouble();
                //Enviar la Matriz correspondiente 
                if(id == 1){//Enviar matriz A1 y B1
                    //System.out.println("Enviar al nodo " + id);
                    ByteBuffer a1 = ByteBuffer.allocate((N/2)*N*8);
                    ByteBuffer b1 = ByteBuffer.allocate((N/2)*N*8);

                    //Para A1
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N; j++){
                            a1.putInt(A[i][j]);
                            //System.out.println("Escrito en a1:" + A[i][j]);
                            b1.putInt(B[i][j]);
                        }
                    }
                    salida.write(a1.array());
                    salida.write(b1.array());

                    //Leer la matriz C
                    byte[] packetC = new byte[N*N/4*8];
                    read(entrada,packetC,0,N*N/4*8);

                    //Conversion a array
                    ByteBuffer arrayC = ByteBuffer.wrap(packetC);

                    //LLenado de la porcion de la matriz c1
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N/2; j++){
                            C[i][j] = arrayC.getInt();
                        }
                    }

                }

                if(id == 2){//Enviar matriz A1 y B2
                    ByteBuffer a1 = ByteBuffer.allocate((N/2)*N*8);
                    ByteBuffer b2 = ByteBuffer.allocate((N/2)*N*8);

                    int axis=N/2;
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N; j++){
                            a1.putInt(A[i][j]);
                            //System.out.println("Escrito en a1:" + A[i][j]);
                            b2.putInt(B[i+axis][j]);
                        }
                    }
                    
                    salida.write(a1.array());
                    salida.write(b2.array());

                    //Leer la matriz C
                    byte[] packetC = new byte[N*N/4*8];
                    read(entrada,packetC,0,N*N/4*8);

                    //Conversion a array
                    ByteBuffer arrayC = ByteBuffer.wrap(packetC);

                    //LLenado de la porcion de la matriz c2
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N/2; j++){
                            C[i][j+axis] = arrayC.getInt();
                        }
                    }
                }

                if(id == 3){//Enviar matriz A2 y B1
                    ByteBuffer a2 = ByteBuffer.allocate((N/2)*N*8);
                    ByteBuffer b1 = ByteBuffer.allocate((N/2)*N*8);

                    int axis=N/2;
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N; j++){
                            a2.putInt(A[i+axis][j]);
                            //System.out.println("Escrito en a1:" + A[i][j]);
                            b1.putInt(B[i][j]);
                        }
                    }

                    salida.write(a2.array());
                    salida.write(b1.array());

                    //Leer la matriz C
                    byte[] packetC = new byte[N*N/4*8];
                    read(entrada,packetC,0,N*N/4*8);

                    //Conversion a array
                    ByteBuffer arrayC = ByteBuffer.wrap(packetC);

                    //LLenado de la porcion de la matriz c3
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N/2; j++){
                            C[i+axis][j] = arrayC.getInt();
                        }
                    }
                }

                if(id == 4){//Enviar matriz A2 y B2
                    ByteBuffer a2 = ByteBuffer.allocate((N/2)*N*8);
                    ByteBuffer b2 = ByteBuffer.allocate((N/2)*N*8);

                    int axis=N/2;
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N; j++){
                            a2.putInt(A[i+axis][j]);
                            //System.out.println("Escrito en a1:" + A[i][j]);
                            b2.putInt(B[i+axis][j]);
                        }
                    }

                    salida.write(a2.array());
                    salida.write(b2.array());

                    //Leer la matriz C
                    byte[] packetC = new byte[N*N/4*8];
                    read(entrada,packetC,0,N*N/4*8);

                    //Conversion a array
                    ByteBuffer arrayC = ByteBuffer.wrap(packetC);

                    //LLenado de la porcion de la matriz c4
                    for (int i = 0; i < N/2; i++){
                        for (int j = 0; j < N/2; j++){
                            C[i+axis][j+axis] = arrayC.getInt();
                        }
                    }
                }

                
                synchronized(lock){
                    //pi+=x;
                }

                salida.close();
                entrada.close();
                conexion.close();
            }catch (Exception e) {
                
            }

        }
    }

    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }

    public static void main(String[] args) throws Exception{
        if (args.length != 1){
          System.err.println("Uso:");
          System.err.println("java MultiplicaMatrizdistribuido_Eficiente <nodo>");
          System.exit(0);
        }

        int nodo = Integer.valueOf(args[0]);
        if (nodo == 0){
            // Nodo 0
            System.out.println("Nodo "+nodo);
            System.out.println("N="+N);
            // Inicializa las matrices A y B
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    A[i][j] = 2 * i - j;
                    B[i][j] = 2 * i + j;
                    C[i][j] = 0;
                }
            }
            //Transpone la matriz B, la matriz traspuesta queda en B
            for (int i = 0; i < N; i++){
                for (int j = 0; j < i; j++){
                    int x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }
            }

            if(N==4){
                System.out.println("Matriz A en servidor");
                for (int i = 0; i < N; i++){
                    for (int j = 0; j < N; j++){
                        System.out.print(A[i][j] + "\t");
                    }
                    System.out.println("");
                }

                System.out.println("Matriz B en servidor");
                for (int i = 0; i < N; i++){
                    for (int j = 0; j < N; j++){
                        System.out.print(B[i][j] + "\t");
                    }
                    System.out.println("");
                }
            }

            ServerSocket servidor = new ServerSocket(50000);
            Worker[] w = new Worker[4];
            int i =0;
            while(true){
                if(i == 4){
                    break;
                }
                Socket conexion = servidor.accept();
                w[i] = new Worker(conexion, i+1);
                w[i].start();
                i++;
            }


            i=0;
            while(true){
                if(i == 4){
                    break;
                }
                w[i].join();
                i++;
            }

            if(N == 4){
                System.out.println("Matriz C:");
                synchronized(lock){
                    for (i = 0; i < N; i++){
                        for (int j = 0; j < N; j++){
                            System.out.print(C[i][j]+"\t");
                        }
                        System.out.println("");
                    }
                }
            }

            synchronized(lock){
                for (i = 0; i < N; i++){
                    for (int j = 0; j < N; j++){
                        checksum += C[i][j];
                    }
                }
            }
            
            System.out.println("Checksum="+checksum);

        }else{
            // CLIENTE
            Socket conexion = null;
            while(true){
                try{
                    conexion = new Socket("localhost",50000);
                    break;
                }catch (Exception e){
                    Thread.sleep(100);
                }
            }
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            //Instanciar matices
            int[][] matrizA = new int[N/2][N];
            int[][] matrizB = new int[N/2][N];
            int[][] matrizC = new int[N/2][N/2];
            
            //Leer la matriz A
            byte[] packetA = new byte[N/2*N*8];
            read(entrada,packetA,0,(N/2)*N*8);

            //Leer la matriz B
            byte[] packetB = new byte[(N/2)*N*8];
            read(entrada,packetB,0,(N/2)*N*8);

            //Conversion a array
            ByteBuffer arrayA = ByteBuffer.wrap(packetA);
            ByteBuffer arrayB = ByteBuffer.wrap(packetB);

            //Inicializacion de matrices A y B
            for (int i = 0; i < N/2; i++){
                for (int j = 0; j < N; j++){
                    matrizA[i][j] = arrayA.getInt();
                    matrizB[i][j] = arrayB.getInt();
                }
            }

            //Inicializacion de matriz C
            for (int i = 0; i < N/2; i++){
                for (int j = 0; j < N/2; j++){
                    matrizC[i][j] = 0;
                }
            }

            for (int i = 0; i < N/2; i++){
                for (int j = 0; j < N/2; j++){
                    for (int k = 0; k < N; k++){
                        matrizC[i][j] += matrizA[i][k] * matrizB[j][k];//Uso eficiente de Cache
                    }
                }
            }

            //Envio de la matrizC
            ByteBuffer c_frac = ByteBuffer.allocate(N*N/4*8);

            for (int i = 0; i < N/2; i++){
                for (int j = 0; j < N/2; j++){
                    c_frac.putInt(matrizC[i][j]);
                }
            }
            
            salida.write(c_frac.array());


            salida.close();
            entrada.close();
            conexion.close();
        }
    }
}
