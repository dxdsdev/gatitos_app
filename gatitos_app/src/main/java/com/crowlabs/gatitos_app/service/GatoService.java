/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowlabs.gatitos_app.service;

import com.crowlabs.gatitos_app.model.Gato;
import com.crowlabs.gatitos_app.model.GatoFav;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author croweloper
 */
public class GatoService {

    public static void verGatos() throws IOException {

        //1. vamos a traer los gatos de la api
        OkHttpClient client = new OkHttpClient();//.newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();

        //Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
        Response response = client.newCall(request).execute();

        String responseJson = response.body().string();

        //cortar corchetes
        responseJson = responseJson.substring(1, responseJson.length());
        responseJson = responseJson.substring(0, responseJson.length() - 1);

        //creamos un objeto de GSON
        Gson gson = new Gson();
        Gato gatos = gson.fromJson(responseJson, Gato.class);
        System.out.println(gatos.getUrl());
        //Redimensionar en caso de necesitar

        try {
            String menu = " Opciones : \n"
                    + " 1. Ver Otra imagen \n"
                    + " 2. Favorito \n"
                    + " 3. Volver \n";

            String[] botones = {"Ver Otra imagen", "Agregar a Favorito", "Volver"};
            String id_gato = gatos.getId();

            String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, JOptionPane.INFORMATION_MESSAGE, obtenerImagen(gatos.getUrl()), botones, botones[0]);

            int seleccion = -1;

            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }
            
            switch (seleccion) {
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void favoritoGato(Gato gato) {

        try {

            OkHttpClient client = new OkHttpClient();//.newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json,text/plain");
            RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"image_id\":\"" + gato.getId() + "\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void verFavoritos() throws IOException {

        Gato gato = new Gato();
        OkHttpClient client = new OkHttpClient();//.newBuilder().build();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", gato.getApikey())
                .build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();
        Gson gson = new Gson();

        GatoFav[] gatosArray = gson.fromJson(elJson, GatoFav[].class);

        if (gatosArray.length > 0) {
            int min = 1;
            int max = gatosArray.length;

            int aleatorio = (int) (Math.random() * ((max - min) - 1)) + min;
            int indice = aleatorio - 1;

            GatoFav gatofav = gatosArray[indice];

            try {

                String menu = " Opciones : \n"
                        + " 1. Ver Otra imagen \n"
                        + " 2. Eliminar Favorito \n"
                        + " 3. Volver \n";

                String[] botones = {"Ver Otra imagen", "Eliminar Favorito", "Volver"};
                String id_gato = gatofav.getId();

                String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, JOptionPane.INFORMATION_MESSAGE, obtenerImagen(gatofav.getImage().getUrl()), botones, botones[0]);

                int seleccion = -1;

                for (int i = 0; i < botones.length; i++) {
                    if (opcion.equals(botones[i])) {
                        seleccion = i;

                    }
                }

                switch (seleccion) {
                    case 0:
                        verFavoritos();
                        break;
                    case 1:
                        borrarFavorito(gatofav);
                        break;
                    default:
                        break;

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public static void borrarFavorito(GatoFav gatofav) {

        try {
            OkHttpClient client = new OkHttpClient();//.newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + gatofav.getId())
                    .method("DELETE", body)
                    .addHeader("x-api-key", gatofav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
        }
       
    }

    public static ImageIcon obtenerImagen(String urlimg) throws IOException {

        URL url = new URL(urlimg);
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "");
        BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
        ImageIcon fondogato = new ImageIcon(bufferedImage);
        if (fondogato.getIconWidth() > 800) {
            Image fondo = fondogato.getImage();
            Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
            fondogato = new ImageIcon(modificada);
        }

        return fondogato;

    }

}
