import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

    // 02 - Recuperar Información de la BBDD.
    public static void buscarClientes(Connection conexion){
        String sql = "SELECT nombre_contacto, apellido_contacto, telefono FROM cliente";

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;

            while(rs.next()){
                String nombre = rs.getString("nombre_contacto");
                String apellido = rs.getString("apellido_contacto");
                String telefono = rs.getString("telefono");
                count++;
                
                System.out.println(count + " - " + nombre + " " + apellido + " - " + telefono);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static void buscarClientePorEmpleado(Connection conexion, int codigoEmpleado){
        String sql = "SELECT cliente.* FROM cliente JOIN empleado ON empleado.id_empleado = cliente.id_empleado WHERE empleado.codigo_empleado = " + codigoEmpleado;
        
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            
            while(rs.next()){
                int codigoCliente = rs.getInt("codigo_empleado");
                String nombre = rs.getString("nombre_contacto");
                String apellido = rs.getString("apellido_contacto");
                String telefono = rs.getString("telefono");
                String ciudad = rs.getString("ciudad");
                String pais = rs.getString("pais");
                double limiteCredito = rs.getDouble("limite_credito");
                
                count ++;

                System.out.println("Cliente: " + count);
                System.out.println("Codigo cliente: " + codigoCliente);
                System.out.println("Nombre: " + nombre + " " + apellido);
                System.out.println("Teléfono: " + telefono);
                System.out.println("Ciudad: " + ciudad);
                System.out.println("País: " + pais);
                System.out.println("Límite de crédito: " + limiteCredito);
            }

            if(count == 0){
                System.out.println("No se encontraron clientes con ese código de empleado.");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static void buscarClientePorCodigo(Connection conexion, int codigo){
        String sql = "SELECT * FROM cliente WHERE codigo_cliente =" + codigo;
        boolean encontrado = false;

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                String nombre = rs.getString("nombre_contacto");
                String apellido = rs.getString("apellido_contacto");
                String ciudad = rs.getString("ciudad");
                String pais = rs.getString("pais");
                double limiteCredito = rs.getDouble("limite_credito");
                
                System.out.println("Cliente con código " + codigo + ":\n"
                                    + "Nombre: " + nombre + " Apellido: " + apellido + " Ciudad: " 
                                    + ciudad + " País: " + pais + "Límite Crédito: " + limiteCredito);

                encontrado = true;
            }

            if(!encontrado){
                System.out.println("Con el código ingresado no se encontraron clientes.");
            }
            
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static void getProductosParaReponer(Connection conexion, int numReponer){
        String sql = "SELECT id_producto, codigo_producto, nombre, cantidad_en_stock FROM producto WHERE cantidad_en_stock <" + numReponer;

        try{
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int count = 0;

            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                int codigoProducto = rs.getInt("codigo_producto");
                String nombre = rs.getString("nombre");
                int cantStock = rs.getInt("cantidad_en_stock");

                System.out.println("ID: " + idProducto + " Código: " + codigoProducto + " Nombre: " + nombre + " Stock: " + cantStock);

                count++;
            }

            if(count == 0){
                System.out.println("No se encontraron productos con menos de esa cantidad en stock.");
            }

            stmt.close();
            rs.close();

        }catch (SQLException e){
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static void getProductosGama(Connection conexion, String gama){
        String sql = "SELECT producto.codigo_producto, producto.nombre, gama_producto.id_gama. gama_producto.gama FROM Producto"
                   + "INNER JOIN gama_producto ON producto.id_gama = gama_producto.id_gama WHERE gama.gama = " + gama;
        
        try{
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;

            while(rs.next()){
                int codigoProd = rs.getInt("codigo_producto");
                String nombreProd = rs.getString("nombre");
                int idGama = rs.getInt("id_gama");
                String gamaNombre = rs.getString("gama");

                System.out.println("Codigo: " + codigoProd + " Producto: " + nombreProd + " ID Gama: " + idGama + " Gama: " + gamaNombre);
                count ++;
            }

            if(count == 0){
                System.out.println("No hay productos con esa gama.");
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e){
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }
}