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
            d.insert(3);
            d.print(5);
        } catch (SQLException e) {
            System.out.println("Error 1: " + e.getMessage());
        }
        System.out.println("End.");
    }

    private void init() throws SQLException {
        con = DriverManager.getConnection(url,user,psw);
        
        if (con == null) {
            throw new SQLException("Connection is null");
        }
        st = con.createStatement();
        ps = con.prepareStatement(
                "SELECT * FROM a WHERE id = ?");
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

    private void insert(int num) throws SQLException {
        // нажождение максимального значения индекса в массиве
        int max = -1;
        String sql = "SELECT MAX (id) AS \"Maximum Value \" FROM a";
        rs = st.executeQuery(sql);
        if (rs != null && rs.next()) {
           max = rs.getInt(1);
        }
        for (int i  = 0; i <= num; i++) {
            sql  = "INSERT INTO a VALUES (" + (i + max) + ", '" + String.valueOf(i+max) + " record)'";
            st.addBatch(sql);
        }

        int[] res = st.executeBatch();
        int s = 0;
        for (int t: res) {
            s += t;
        }
        System.out.println("Inserted " + res + " records.");
        st.clearBatch();
        
    }

    private void print(int index) throws SQLException {
        ps.setInt(1, index);
        rs = ps.executeQuery();
        if (rs != null && rs.next()) {
            System.out.println("id: " + rs.getInt("id") + ", title: "
             + rs.getString(2));
        } else {
            System.out.println("Record " + index + " not found.");
        }
    }
}