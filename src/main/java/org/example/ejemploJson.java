package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ejemploJson {
    public static void main(String[] args) {
        String ruta = "personas.json";
        HashMap<String, String> mapa = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            StringBuilder json = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                json.append(linea.trim());
            }

            String contenido = json.toString();

            // Quitar llaves
            contenido = contenido.replace("{", "").replace("}", "");

            // Separar pares clave:valor
            String[] pares = contenido.split(",");

            for (String par : pares) {
                String[] partes = par.split(":");

                if (partes.length == 2) {
                    String clave = partes[0].trim().replace("\"", "");
                    String valor = partes[1].trim().replace("\"", "");
                    mapa.put(clave, valor);
                }
            }

            System.out.println("HashMap cargado:");
            for (Map.Entry<String, String> entry : mapa.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}