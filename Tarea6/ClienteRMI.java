import java.io.IOException;
import java.rmi.Naming;
import java.lang.Exception;

public class ClienteRMI{
    static int N = 1000;//Tamanio de matrices
    static int[][] A = new int[N][N];//Declaracion dematriz de NxN
    static int[][] B = new int[N][N];//Declaracion dematriz de NxN
    static int[][] C = new int[N][N];//Declaracion dematriz de NxN
    static long checksum = 0;

    public static void main(String args[]) throws Exception{
        // en este caso el objeto remoto se llama "prueba", notar que se utiliza el puerto default 1099
        String url = "rmi://localhost/multiplicaMatriz";

        // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);

        //Inicializamos las matrices
        inicializaMatrices();
        System.out.println("Matriz B sin transpuesta");
        imprimirMatriz(B);
        //Trasnponemos la matriz B
        B = transponerMatriz(B);
        //Obtenemos las matrices A1, A2, B1 y B2
        int[][] A1 = parte_matriz(A,0);
        int[][] A2 = parte_matriz(A,N/2);
        int[][] B1 = parte_matriz(B,0);
        int[][] B2 = parte_matriz(B,N/2);

        //Objetenemos la multiplicación de matrices
        int[][] C1 = r.multiplica_matrices(A1,B1);
        int[][] C2 = r.multiplica_matrices(A1,B2);
        int[][] C3 = r.multiplica_matrices(A2,B1);
        int[][] C4 = r.multiplica_matrices(A2,B2);

        acomoda_matriz(C,C1,0,0);
        acomoda_matriz(C,C2,0,N/2);
        acomoda_matriz(C,C3,N/2,0);
        acomoda_matriz(C,C4,N/2,N/2);

        if(N == 4){
            imprimirResultados();
        }
        
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
        System.out.println("Matriz B");
        imprimirMatriz(B);
        System.out.println("Matriz C");
        imprimirMatriz(C);
    }
}