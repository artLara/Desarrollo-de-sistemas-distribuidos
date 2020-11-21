import java.io.IOException;
import java.rmi.Naming;
import java.lang.Exception;

public class ClienteRMI_N500{
    static int N = 500;//Tamanio de matrices
    static int[][] A = new int[N][N];//Declaracion dematriz de NxN
    static int[][] B = new int[N][N];//Declaracion dematriz de NxN
    static int[][] C = new int[N][N];//Declaracion dematriz de NxN
    static long checksum = 0;

    public static void main(String args[]) throws Exception{
        String[] urls = crearUrls(args);

        System.out.println("Urls:");
        for(String url : urls){
            System.out.println(url);
        }

        //Obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        //Dado a que el servidor 0 se ejecuta en el mismo nodo que el cliene
        //su url se mantiene constante en localhost

        InterfaceRMI r0 = (InterfaceRMI)Naming.lookup("rmi://localhost/multiplicaMatriz0");
        InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(urls[0]);
        InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(urls[1]);
        InterfaceRMI r3 = (InterfaceRMI)Naming.lookup(urls[2]);


        //Inicializamos las matrices
        inicializaMatrices();
        
        
        //Trasnponemos la matriz B
        B = transponerMatriz(B);
        
        //Obtenemos las matrices A1, A2, B1 y B2
        int[][] A1 = parte_matriz(A,0);
        int[][] A2 = parte_matriz(A,N/2);
        int[][] B1 = parte_matriz(B,0);
        int[][] B2 = parte_matriz(B,N/2);

        //Instanciamos a un objeto ClaseRMI para que el cliente ejecute
        //el metodo multiplica_matrices de A1 x B1
        ClaseRMI objCliente = new ClaseRMI();
        
        //Objetenemos la multiplicación de matrices
        int[][] C1 = objCliente.multiplica_matrices(A1,B1);
        int[][] C2 = r1.multiplica_matrices(A1,B2);
        int[][] C3 = r2.multiplica_matrices(A2,B1);
        int[][] C4 = r3.multiplica_matrices(A2,B2);

        acomoda_matriz(C,C1,0,0);
        acomoda_matriz(C,C2,0,N/2);
        acomoda_matriz(C,C3,N/2,0);
        acomoda_matriz(C,C4,N/2,N/2);

        if(N == 4){
            imprimirResultados();
        }
        
        System.out.println("Checksum="+calcularChecksum(C));
    }

    static void inicializaMatrices(){
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                A[i][j] = 2 * i - j;
                B[i][j] = 2 * i + j;
                C[i][j] = 0;
            }
        }
        
    }

    static int[][] transponerMatriz(int[][] matriz){
        for (int i = 0; i < N; i++){
            for (int j = 0; j < i; j++){
                int x = matriz[i][j];
                matriz[i][j] = matriz[j][i];
                matriz[j][i] = x;
            }
        }
        return matriz;
    }

    static int[][] parte_matriz(int[][] A,int inicio){
        int[][] M = new int[N/2][N];
        for (int i = 0; i < N/2; i++)
            for (int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }

    static void acomoda_matriz(int[][] C,int[][] A,int renglon,int columna){
        for (int i = 0; i < N/2; i++)
            for (int j = 0; j < N/2; j++)
                C[i + renglon][j + columna] = A[i][j];
    }

    static void imprimirMatriz(int[][] matriz){
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    static void imprimirResultados(){
        System.out.println("Matriz A");
        imprimirMatriz(A);
        System.out.println("Matriz B transpuesta");
        imprimirMatriz(B);
        System.out.println("Matriz C");
        imprimirMatriz(C);
    }

    static String[] crearUrls(String args[]){
        String[] urls = new String[3];

        //Cuando se desea utilizar en localhost y tener multiples servidores
        //se agrega un identificador al nombre que en este caso es un entero
        //que representa el número del nodo.
        if(args.length == 0){
            for(int i=0; i<3; i++){
                urls[i] = "rmi://localhost/multiplicaMatriz" + (i+1);
            }

            return urls;
        }

        try {
            for(int i=0; i<3; i++){
                //Cuando se utiliza en maquinas virtuales se utiliza la ip que corresponde
                //a dicha maquina virtual
                urls[i] = "rmi://" + args[i] + "/multiplicaMatriz";
            }

        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("Uso:");
            System.err.println("java ClienteRMI <ip Server1><ip Server2> <ip Server3>");
            System.err.println("java ClienteRMI(para uso en localhost con nodo 1, 2 y 3)");
            System.exit(0);
        }

        return urls;
    }

    public static long calcularChecksum(int[][] matriz) {
        long checksum = 0;
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                checksum += matriz[i][j];
            }
        }

        return checksum;
    }
}