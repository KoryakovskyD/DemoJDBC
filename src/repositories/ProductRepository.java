package repositories;

import modeles.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public List<Product> getProducts() throws SQLException {
        try (Connection conn = createConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select ARTICLE, NAME, COLOR, PRICE, BALANCE from PRODUCTS")) {
            List<Product> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Product(rs.getString("ARTICLE"),
                        rs.getString("NAME"),
                        rs.getString("COLOR"),
                        rs.getInt("PRICE"),
                        rs.getInt("BALANCE")
                ));
            }
            return res;
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:derby://localhost:1527/demo", "demo", "demo");
    }
}
