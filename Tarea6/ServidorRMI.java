import java.rmi.Naming;
import java.lang.Exception;


public class ServidorRMI{
    public static void main(String[] args) throws Exception{
        String url = "rmi://localhost/multiplicaMatriz";
        try {
            if(esNumero(args[0])){
                //Cuando se desea utilizar en localhost y tener multiples servidores
                //se agrega un identificador al nombre que en este caso es un entero
                //que representa el número del nodo.
                url = "rmi://localhost/multiplicaMatriz" + args[0];
            } else{
                //Cuando se utiliza en maquinas virtuales se utiliza la ip que corresponde
                //a dicha maquina virtual
                url = "rmi://" + args[0] + "/multiplicaMatriz";
            }

        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("Uso:");
            System.err.println("java ServidorRMI <ip de server>");
            System.err.println("java ServidorRMI <nodo>");
            System.exit(0);
        }

        System.out.println("Url: " + url);
        ClaseRMI obj = new ClaseRMI();
        // registra la instancia en el rmiregistry
        Naming.bind(url,obj);
        System.out.println("Servidor "+ args[0] +" iniciado con exito!");
    }

    public static boolean esNumero(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}