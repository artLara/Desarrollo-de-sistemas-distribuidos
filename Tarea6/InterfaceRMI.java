import java.rmi.RemoteException;
import java.rmi.Remote;


public interface InterfaceRMI extends Remote{
    public int[][] multiplica_matrices(int[][] A,int[][] B) throws RemoteException;
}