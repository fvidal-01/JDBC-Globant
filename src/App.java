import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class App {
    public static void main(String[] args) throws Exception {
        Connection conexion = getConnection();
        cerrarConexion(conexion);
    }

    public static Connection getConnection(){
        String host = "127.0.0.1";
        String port = "3306";
        String name = "root";
        String password = "root";
        String database = "vivero";
        String zona = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + zona;

        Connection conexion = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(url,name,password);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el conector JDBC: " + e.getMessage());
        } catch (SQLException e){
            System.out.println("ERROR de conexión: " + e.getMessage());
        }

        return conexion;
    }

    public static void cerrarConexion(Connection conexion){
        if(conexion != null){
            try{
                conexion.close();
                System.out.println("La conexión se cerró correctamente.");
            } catch(SQLException e){
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
