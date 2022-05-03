package ho;


import commun.Product;

import java.sql.*;
import java.util.List;

public class DAOService {
    public String url = "jdbc:mysql://localhost:3306/ho";
    public String user = "azer";
    public String password = "azer2000";
    public String query = "INSERT INTO ho.product_sale( date , region, product, qty, cost, amt, tax, total, dbNumber) values(?,?,?,?,?,?,?,?,?)";

    public void insert(List<Product> productList) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = connection.prepareStatement(query)
        ) {
            for (Product p : productList) {
                pst.setDate(1, new Date(p.getDate().getTime()));
                pst.setString(2, p.getRegion());
                pst.setString(3, p.getProduct());
                pst.setInt(4, p.getQty());
                pst.setFloat(5, p.getCost());
                pst.setDouble(6, p.getAmt());
                pst.setFloat(7, p.getTax());
                pst.setDouble(8, p.getTotal());
                pst.setInt(9, p.getDbNumber());
                pst.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}