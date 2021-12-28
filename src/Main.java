
import modeles.Product;
import repositories.ProductRepository;

import java.sql.*;
import java.util.List;


/**
 *
 * @author dkory
 */
public class Main {
    public static void main(String[] args) throws SQLException {
       /*
        ProductRepository rep = new ProductRepository();
        List<Product> products = rep.getProducts();
        for (Product p:products) {
            String color = p.getColor();
            if (color == null)
                color="";
            System.out.printf("%s;%s;%s;%d;%d%n", p.getArticle(),
                    p.getName(), color, p.getPrice(), p.getBalance());
        }

        printOrdersContent(1,2,3);



        System.out.println(sum(1,4,6,7,8));

         */
        registerOrder("John Smith", "(911)098-76-54", null, "Politechnicheskay 23-3-999",
                new String[] {"3251619","3251620"},
                new int[] {4,4});
    }

    static void printOrdersContent(int... orderIds) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/demo", "demo", "demo");
        PreparedStatement pstmt = conn.prepareStatement(
                "select NAME, COLOR\n" +
                "from PRODUCTS pr\n" +
                "join POSITIONS pp on pr.ARTICLE = pp.ARTICLE\n" +
                        "where pp.ORDER_ID = ?")) {
            for (int id : orderIds) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("Содержимое заказа с id=" + id);
                    while (rs.next()) {
                        String name = rs.getString(1),
                                color = rs.getString(2);
                        if (color != null)
                            name += " (" + color + ")";
                        System.out.println(name);
                    }
                    System.out.println("\n");
                }
            }
        }
    }

    static void registerOrder(String fio, String phone, String email,
                              String deliveryAddr, String[] articles, int[] amount) throws SQLException {
        if (articles == null)
            throw new IllegalArgumentException("'articles' is null.");
        if (amount == null)
            throw new IllegalArgumentException("'amount' is null.");
        if (amount.length != articles.length)
            throw new IllegalArgumentException("'articles' and 'amount' arrays must be have the same size");

        try (Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/demo", "demo", "demo")) {
            conn.setAutoCommit(false);
            int maxOrderId;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("select max (ID) from ORDERS")) {
                rs.next();
                maxOrderId = rs.getInt(1);
            }
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "insert into ORDERS (ID, REG_DATE, CUSTOMER_FIO, PHONE, EMAIL, DELIVERY_ADDRESS, STATUS)\n" +
                            "values (?, CURRENT_DATE, ?,?,?,?,'P')")) {
                pstmt.setInt(1, maxOrderId + 1);
                pstmt.setString(2, fio);
                pstmt.setString(3, phone);
                pstmt.setString(4, email);
                pstmt.setString(5, deliveryAddr);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "insert into POSITIONS\n" +
                            "select ?, ARTICLE, PRICE, ? from PRODUCTS where ARTICLE = ?")) {
                pstmt.setInt(1, maxOrderId + 1);
                for (int i = 0; i < articles.length; i++) {
                    pstmt.setInt(2, amount[i]);
                    pstmt.setString(3, articles[i]);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            try {
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
            }
        }
    }

    /*
    static int sum(int n1, int... others) {
        int res = n1;
        for (int n: others) {
            res +=n;
        }
        return res;
    }


    static void printAll(String prefix, Object... vals) {
        System.out.println(prefix + ": ");
        for (int i = 0; i < vals.length; i++) {
            System.out.println(", ");
            System.out.print(vals[i]);
        }
    }

     */
}
