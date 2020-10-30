class MultiplicaMatriz{
    static int N = 300;//Tamanio de matrices
    static int[][] A = new int[N][N];//Declaracion dematriz de NxN
    static int[][] B = new int[N][N];//Declaracion dematriz de NxN
    static int[][] C = new int[N][N];//Declaracion dematriz de NxN

    public static void main(String[] args){
        long t1 = System.currentTimeMillis();
        // inicializa las matrices A y B
        System.out.println("N="+N);
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                A[i][j] = 2 * i - j;
                B[i][j] = i + 2 * j;
                C[i][j] = 0;
            }
        }
        // multiplica la matriz A y la matriz B, el resultado queda en la matriz C

        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                for (int k = 0; k < N; k++){
                    C[i][j] += A[i][k] * B[k][j];//Uso ineficiente de Cache
                }
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2 - t1) + "ms");
    }
}