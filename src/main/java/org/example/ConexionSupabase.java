import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionSupabase {

    //# Connect to Supabase via connection pooling
    //DATABASE_URL="postgresql://postgres.aynbxcwcbznmlbdhkjyh:[YOUR-PASSWORD]@aws-1-eu-west-1.pooler.supabase.com:6543/postgres?pgbouncer=true"
    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:6543/postgres?pgbouncer=true";
        String user = "postgres.aynbxcwcbznmlbdhkjyh";
        String password = "ponttupassword";

        try (Connection basededatos = DriverManager.getConnection(url, user, password);
             Statement stmt = basededatos.createStatement()) {

            System.out.println("Conexión correcta a Supabase");

            String createTable = """
                CREATE TABLE IF NOT EXISTS alumnos (
                    id BIGSERIAL PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    email VARCHAR(120) UNIQUE NOT NULL,
                    nota DECIMAL(4,2) NOT NULL
                )
            """;
            stmt.executeUpdate(createTable);

            String insertAlumno = """
                INSERT INTO alumnos (nombre, email, nota)
                VALUES ('Lucia', 'lucia@correo.com', 9.50)
                ON CONFLICT (email) DO NOTHING
            """;
            stmt.executeUpdate(insertAlumno);

            String selectAlumnos = "SELECT * FROM alumnos ORDER BY id";
            ResultSet rs = stmt.executeQuery(selectAlumnos);

            System.out.println("Listado de alumnos:");
            while (rs.next()) {
                long id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                double nota = rs.getFloat("nota");

                System.out.println(id + " | " + nombre + " | " + email + " | " + nota);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}