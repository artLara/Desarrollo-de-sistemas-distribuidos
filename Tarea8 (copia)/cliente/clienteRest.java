package negocio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;


import java.net.*;
import java.util.stream.Collectors;
import com.google.gson.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.*;

public class clienteRest
{
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opc = "";

        while(true){
            System.out.println("Seleccione una opcin:\n" +
            "a)Alta usuario\nb)Consulta usuario\nc)Borrar usuario\n" +
            "d)Borrar todos los usuario\ne)Salir\n");

            opc = reader.readLine();
            switch(opc){
                case "a":
                    System.out.println("Alta usuario");
                    break;

                case "b":
                    System.out.println("Consulta usuario");

                    consulta();
                    break;

                case "c":
                    System.out.println("Borrar usuario");
                    break;

                case "d":
                    System.out.println("Borrar todos los usuario");
                    break;

                case "e":
                    System.out.println("Salir");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opcion no valida");
            }
        }

    }

    public static void consulta() throws IOException{
        URL url = new URL("http://localhost:8080/Servicio/rest/ws/consulta");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String parametros = "email=" + URLEncoder.encode("test@test.com","UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();

        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());

        BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
        String respuesta = br.lines().collect(Collectors.joining());
        // el método web regresa una string en formato JSON
        System.out.println(respuesta);
        try{
            //Usuario u = Usuario.valueOf(respuesta);
            Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
            Usuario u = (Usuario)j.fromJson(respuesta,Usuario.class);
            System.out.println(u.email);
        }
        catch (Exception e) {

        }


        //while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
        conexion.disconnect();
    }
}
