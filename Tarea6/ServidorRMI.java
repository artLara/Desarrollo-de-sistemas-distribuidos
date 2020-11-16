import java.rmi.Naming;
import java.lang.Exception;


public class ServidorRMI{
    public static void main(String[] args) throws Exception{
        String url = "rmi://localhost/multiplicaMatriz";
        ClaseRMI obj = new ClaseRMI();
        System.out.println("Server start sucesfull");
        // registra la instancia en el rmiregistry
        Naming.bind(url,obj);
    }
}