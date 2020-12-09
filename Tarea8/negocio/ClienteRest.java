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

public class ClienteRest
{
    static Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opc = "";

        while(true){
            System.out.println("\n\nSeleccione una opción:\n" +
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
                    System.out.print("Escriba la fecha de nacimiento del nuevo usuario en formato yyyy/mm/dd:");
                    u.fecha_nacimiento = reader.readLine();
                    System.out.print("Escriba el telefono del nuevo usuario:");
                    u.telefono = reader.readLine();
                    System.out.print("Escriba el género del nuevo usuario (F para Femenino y M para Masculino):");
                    u.genero = reader.readLine();
                    u.foto = null;
                    alta(u);
                    break;

                case "b":
                    System.out.println("\tConsulta usuario");
                    System.out.print("Escriba el email del usuario a consultar:");
                    String email = reader.readLine();
                    consulta(email);
                    break;

                case "c":
                    System.out.println("\tBorrar usuario");
                    System.out.print("Escriba el email del usuario a borrar:");
                    String borrarMail = reader.readLine();
                    borrar(borrarMail);
                    break;

                case "d":
                    System.out.println("Borrar todos los usuario");
                    borrar_usuarios();
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

        String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();
        os.close();


        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta = br.lines().collect(Collectors.joining());
            Mensaje mensaje = j.fromJson(respuesta, Mensaje.class);
            // el método web regresa una string en formato JSON
            System.out.println("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.println("El servidor dice: "  + mensaje.message);
        }else{
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta = br.lines().collect(Collectors.joining());
            // el método web regresa una string en formato JSON
            System.out.println("Usuario:\n");
            Usuario u = (Usuario)j.fromJson(respuesta,Usuario.class);
            imprimirUsuario(u);
        }

        conexion.disconnect();
    }

    public static void alta(Usuario usuario) throws IOException{
        // se utiliza GSON para convertir el objeto a string en formato JSON
        // Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        String usuarioJSON = j.toJson(usuario);

        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String parametros = "usuario=" + URLEncoder.encode(usuarioJSON,"UTF-8");


        URL url = new URL("http://localhost:8080/Servicio/rest/ws/alta");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());
        os.flush();

        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta = br.lines().collect(Collectors.joining());
            Mensaje mensaje = j.fromJson(respuesta, Mensaje.class);
            // el método web regresa una string en formato JSON
            System.out.println("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.println("El servidor dice: "  + mensaje.message);

        }else{
            System.out.println("OK");
        }

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
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta = br.lines().collect(Collectors.joining());
            Mensaje mensaje = j.fromJson(respuesta, Mensaje.class);
            // el método web regresa una string en formato JSON
            System.out.println("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.println("El servidor dice: "  + mensaje.message);
        }else{
            System.out.println("OK");
        }

        conexion.disconnect();
    }

    public static void borrar_usuarios() throws IOException{
        URL url = new URL("http://localhost:8080/Servicio/rest/ws/borrar_usuarios");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // se debe verificar si hubo error
        if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta = br.lines().collect(Collectors.joining());
            Mensaje mensaje = j.fromJson(respuesta, Mensaje.class);
            // el método web regresa una string en formato JSON
            System.out.println("Codigo de error HTTP: " + conexion.getResponseCode());
            System.out.println("El servidor dice: "  + mensaje.message);
        }else{
            System.out.println("OK");
        }

        conexion.disconnect();
    }

    public static void imprimirUsuario(Usuario usuario){
        System.out.println("Email: " + usuario.email);
        System.out.println("Nombre: " + usuario.nombre);
        System.out.println("Apellido paterno: " + usuario.apellido_paterno);
        System.out.println("Apellido materno: " + usuario.apellido_materno);
        System.out.println("Fecha de nacimiento: " + usuario.fecha_nacimiento);
        System.out.println("Telefono: " + usuario.telefono);
        System.out.println("Genero: " + usuario.genero);
        System.out.println("Foto: " + usuario.foto);

    }

    static class Mensaje{
        String message;
    }
}
