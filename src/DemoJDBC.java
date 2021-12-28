import java.sql.*;
import java.util.Enumeration;

/**
 *
 * @author PROBOOK
 */
public class DemoJDBC implements AutoCloseable{
    private String url;
    private String user;
    private String psw;
    private Connection con;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;

    public DemoJDBC(){

    }

    public DemoJDBC(String url, String user, String psw) {
        this.url = url;
        this.user = user;
        this.psw = psw;
    }

    public static void main(String[] args) {
        System.out.println("Start...");

        try (DemoJDBC d = new DemoJDBC("jdbc:derby://localhost:1527/tmp","tmp","tmp")) {
            d.info();
            d.init();
            d.insert();
        } catch (SQLException e) {
            System.out.println("Error 1: " + e.getMessage());
        }
        System.out.println("End.");
    }

    private void insert() throws SQLException {
        int index = 5;
        String sql = "SELECT MAX (id) AS \"Maximum Value\" FROM a";
        rs = st.executeQuery(sql);
        if (rs != null && rs.next()) {

        }
        sql = "INSERT INTO a VALUES (" + index + ", 'fifth record')";
        int res = st.executeUpdate(sql);
        System.out.println("Inserted " + res + " records.");
    }

    private void init() throws SQLException {
        con = DriverManager.getConnection(url,user,psw);
        st = con.createStatement();
        if (con == null) {
            throw new SQLException("Connection is null");
        }
    }

    private void info() {
        Enumeration<Driver> e = DriverManager.getDrivers();
        Driver drv = null;
        while (e.hasMoreElements()) {
            drv = e.nextElement();
            System.out.println(drv.getClass().getCanonicalName());
        }
    }

    @Override
    public void close() throws SQLException {
        if (con != null && con.isValid(0)) {
            con.close();
        }
    }
}