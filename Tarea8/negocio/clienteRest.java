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
            System.out.println("Seleccione una opcion:\n" +
            "a)Alta usuario\nb)Consulta usuario\nc)Borrar usuario\n" +
            "d)Borrar todos los usuario\ne)Salir\n");

            opc = reader.readLine();
            switch(opc){
                case "a":
                    System.out.println("Alta usuario");
                    Usuario u = new Usuario();
                    System.out.print("Escriba el email del nuevo usuario:");
                    u.email = reader.readLine();
                    System.out.print("Escriba el nombre del nuevo usuario:");
                    u.nombre = reader.readLine();
                    System.out.print("Escriba el apellido paterno del nuevo usuario:");
                    u.apellido_paterno = reader.readLine();
                    System.out.print("Escriba el apellido materno del nuevo usuario:");
                    u.apellido_materno = reader.readLine();
                    System.out.print("Escriba la fecha de nacimiento del nuevo usuario:");
                    u.fecha_nacimiento = reader.readLine();
                    System.out.print("Escriba el telefono del nuevo usuario:");
                    u.telefono = reader.readLine();
                    System.out.print("Escriba el genero del nuevo usuario:");
                    u.genero = reader.readLine();
                    u.foto = null;
                    alta(u);
                    break;

                case "b":
                    System.out.println("\tConsulta usuario");
                    System.out.print("Escriba el email a consultar:");
                    String email = reader.readLine();
                    consulta(email);
                    break;

                case "c":
                    System.out.println("\tBorrar usuario");
                    System.out.print("Escriba el email a consultar:");
                    String borrarMail = reader.readLine();
                    borrar(borrarMail);
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

    public static void consulta(String email) throws IOException{
        URL url = new URL("http://localhost:8080/Servicio/rest/ws/consulta");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
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

    public static void alta(Usuario usuario) throws IOException{
        URL url = new URL("http://localhost:8080/Servicio/rest/ws/alta");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // se utiliza GSON para convertir el objeto a string en formato JSON
        Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
        String usuarioJSON = j.toJson(usuario);

        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String parametros = "usuario=" + URLEncoder.encode(usuarioJSON,"UTF-8");
        // StringEntity parametros = new StringEntity("usuario=" + usuarioJSON);
        // String parametros = "usuario=" + usuarioJSON;

        System.out.println("Prametros===>" + parametros);
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();
        System.out.println("Enviado" + usuarioJSON);
        // se debe verificar si hubo error

        // BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
        // String respuesta = br.lines().collect(Collectors.joining());
        // // el método web regresa una string en formato JSON
        // System.out.println(respuesta);
        // System.out.println(conexion.getErrorStream());
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode() + "error=" + conexion.getErrorStream());

        System.out.println("Todo cool");
        // BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
        // String respuesta = br.lines().collect(Collectors.joining());
        // // el método web regresa una string en formato JSON
        // System.out.println(respuesta);
        // try{
        //     //Usuario u = Usuario.valueOf(respuesta);
        //     Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        //     Usuario u = (Usuario)j.fromJson(respuesta,Usuario.class);
        //     System.out.println(u.email);
        // }
        // catch (Exception e) {
        //
        // }


        //while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
        conexion.disconnect();
    }

    public static void borrar(String email) throws IOException{
        URL url = new URL("http://localhost:8080/Servicio/rest/ws/borra");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
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
        // try{
        //     //Usuario u = Usuario.valueOf(respuesta);
        //     Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        //     Usuario u = (Usuario)j.fromJson(respuesta,Usuario.class);
        //     System.out.println(u.email);
        // }
        // catch (Exception e) {
        //
        // }


        //while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
        conexion.disconnect();
    }
}
