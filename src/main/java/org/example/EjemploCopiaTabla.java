package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class EjemploCopiaTabla {

    private static final String DIRECT_URL =
            "jdbc:postgresql://aws-1-eu-central-2.pooler.supabase.com:5432/postgres"
                    + "?user=postgres.ivqrreujqatnlamcgabf"
                    + "&password=<PonTuPassword>";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DIRECT_URL)) {

            conn.setAutoCommit(false);

            crearTablaOriginal(conn);
            insertarDatos(conn);
            crearTablaCopia(conn);
            copiarDatos(conn);

            System.out.println("=== TABLA personas ===");
            mostrarTabla(conn, "personas");

            System.out.println("\n=== TABLA personas_copia ===");
            mostrarTabla(conn, "personas_copia");

            conn.commit();
            System.out.println("\nProceso realizado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void crearTablaOriginal(Connection conn) throws SQLException {
        String drop = "DROP TABLE IF EXISTS personas CASCADE";
        String create = "CREATE TABLE personas ("
                + "id SERIAL PRIMARY KEY, "
                + "nombre VARCHAR(100) NOT NULL, "
                + "edad INT NOT NULL"
                + ")";

        try (Statement st = conn.createStatement()) {
            st.executeUpdate(drop);
            st.executeUpdate(create);
            System.out.println("Tabla personas creada.");
        }
    }

    private static void insertarDatos(Connection conn) throws SQLException {
        String sql = "INSERT INTO personas (nombre, edad) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            insertarPersona(ps, "Ana", 24);
            insertarPersona(ps, "Luis", 31);
            insertarPersona(ps, "Marta", 28);
            insertarPersona(ps, "Pablo", 35);
            System.out.println("Datos insertados en personas.");
        }
    }

    private static void insertarPersona(PreparedStatement ps, String nombre, int edad) throws SQLException {
        ps.setString(1, nombre);
        ps.setInt(2, edad);
        ps.executeUpdate();
    }

    private static void crearTablaCopia(Connection conn) throws SQLException {
        String drop = "DROP TABLE IF EXISTS personas_copia CASCADE";
        String create = "CREATE TABLE personas_copia ("
                + "id SERIAL PRIMARY KEY, "
                + "nombre VARCHAR(100) NOT NULL, "
                + "edad INT NOT NULL"
                + ")";


        try (Statement st = conn.createStatement()) {
            st.executeUpdate(drop);
            st.executeUpdate(create);
            System.out.println("Tabla personas_copia creada con la misma estructura.");
        }
    }

    private static void copiarDatos(Connection conn) throws SQLException {
        String sql = "INSERT INTO personas_copia "
                + "SELECT * FROM personas";

        try (Statement st = conn.createStatement()) {
            int filas = st.executeUpdate(sql);
            System.out.println("Datos copiados a personas_copia. Filas: " + filas);
        }
    }

    private static void mostrarTabla(Connection conn, String nombreTabla) throws SQLException {
        String sql = "SELECT * FROM " + nombreTabla + " ORDER BY id";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        "id=" + rs.getInt("id")
                                + ", nombre=" + rs.getString("nombre")
                                + ", edad=" + rs.getInt("edad")
                );
            }
        }
    }
}
