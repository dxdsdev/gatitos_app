/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowlabs.gatitos_app.service;

import com.crowlabs.gatitos_app.model.Gato;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
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
        Image image = null;
        try {
            URL url = new URL(gatos.getUrl());

            //
            
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());

            ImageIcon fondogato = new ImageIcon(bufferedImage);

           

            if (fondogato.getIconWidth() > 800) {
                Image fondo = fondogato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondogato = new ImageIcon(modificada);
            }
            String menu = " Opciones : \n"
                    + " 1. Ver Otra imagen \n"
                    + " 2. Favorito \n"
                    + " 3. Volver \n";

            String[] botones = {"Ver Otra imagen", "Favorito", "Volver"};
            String id_gato = gatos.getId();

            String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, JOptionPane.INFORMATION_MESSAGE, fondogato, botones, botones[0]);

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

        /*
        System.out.println(responseJson);
        System.out.println("");
        System.out.println(response);
        System.out.println("");
        System.out.println(responseBody.string());
        System.out.println("");
         */
        /**
         * [
         * {
         * "breeds": [], "id":"58l",
         * "url":"https://cdn2.thecatapi.com/images/58l.jpg", "width":385,
         * "height":500 } ]
         */
    }

    public static void favoritoGato(Gato gato) {

    }

}
