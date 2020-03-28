/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowlabs.gatitos_app;

import com.crowlabs.gatitos_app.service.GatoService;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author croweloper
 */
public class Inicio {

    public static void main(String[] args) throws IOException {

        int option_menu = -1;
        String[] botones = {" 1. Ver Gatos", " 2. Ver Favoritos"," 3. Salir"};

        do {
            String opcion = (String) JOptionPane.showInputDialog(null, "Gatitos Java", "Menu Principal", JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]);

            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    option_menu = i;

                }
            }

            switch (option_menu) {
                case 0:
                    GatoService.verGatos();
                    break;
                case 1:
                    GatoService.verFavoritos();
                    break;
                default:
                    break;
            }

        } while (option_menu != 1);

    }

}
