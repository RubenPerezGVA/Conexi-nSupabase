package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    // Conexión a Supabase
    private static final String URL =
            "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:6543/postgres?pgbouncer=true";
    private static final String USER = "postgres.aynbxcwcbznmlbdhkjyh";
    private static final String PASSWORD = "Test_123..2aa";

    // Ruta del JSON
    private static final String JSON_FILE = "personas.json";

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)
        ) {
            System.out.println("Conexión correcta con Supabase.");

            crearTablaSiNoExiste(conn);

            JsonNode root = mapper.readTree(new File(JSON_FILE));

            if (root.isArray()) {
                for (JsonNode persona : root) {
                    insertarPersona(conn, persona);
                }
            } else if (root.isObject()) {
                insertarPersona(conn, root);
            } else {
                System.out.println("El JSON no tiene un formato válido.");
                return;
            }

            mostrarPersonas(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void crearTablaSiNoExiste(Connection conn) throws Exception {
        String sql = """
                CREATE TABLE IF NOT EXISTS personas (
                    id INTEGER PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    edad INTEGER NOT NULL
                )
                """;

        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            System.out.println("Tabla 'personas' creada o ya existente.");
        }
    }

    private static void insertarPersona(Connection conn, JsonNode persona) throws Exception {
        int id = persona.get("id").asInt();
        String nombre = persona.get("nombre").asText();
        String email = persona.get("email").asText();
        int edad = persona.get("edad").asInt();

        String sql = """
                INSERT INTO personas (id, nombre, email, edad)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (id)
                DO UPDATE SET
                    nombre = EXCLUDED.nombre,
                    email = EXCLUDED.email,
                    edad = EXCLUDED.edad
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, email);
            ps.setInt(4, edad);

            int filas = ps.executeUpdate();
            System.out.println("Insert/Update realizado. Filas afectadas: " + filas);
        }
    }

    private static void mostrarPersonas(Connection conn) throws Exception {
        String sql = "SELECT id, nombre, email, edad FROM personas ORDER BY id";

        try (
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {
            System.out.println("\nContenido de la tabla personas:");
            while (rs.next()) {
                System.out.println(
                        "ID: " + rs.getInt("id") +
                                ", Nombre: " + rs.getString("nombre") +
                                ", Email: " + rs.getString("email") +
                                ", Edad: " + rs.getInt("edad")
                );
            }
        }
    }
}