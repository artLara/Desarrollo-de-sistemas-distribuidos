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

public class cliente
{
    public static void main(String[] args) throws IOException
    {
        URL url = new URL("http://sisdis.sytes.net:8080/Servicio/rest/ws/prueba");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
        conexion.setRequestMethod("POST");

        // indica que la petición estará codificada como URL
        conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
        String a = "a=" + URLEncoder.encode("100","UTF-8") + "&";
        String b = "b=" + URLEncoder.encode("200","UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(a.getBytes());
        os.write(b.getBytes());

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
