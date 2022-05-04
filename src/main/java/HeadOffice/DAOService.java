package HeadOffice;


import commun.Product;

import java.sql.*;
import java.util.List;

public class DAOService {
    public String url = "jdbc:mysql://localhost:3306/ho";
    public String user = "root";
    public String password = "";
    public String query = "INSERT INTO ho.product_sale( id,date , region, product, qty, cost, amt, tax, total, dbNumber) values(?,?,?,?,?,?,?,?,?,?)";

    public void insert(List<Product> productList) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = connection.prepareStatement(query)
        ) {
            for (Product p : productList) {
                pst.setInt(1,p.getId());
                pst.setDate(2, new Date(p.getDate().getTime()));
                pst.setString(3, p.getRegion());
                pst.setString(4, p.getProduct());
                pst.setInt(5, p.getQty());
                pst.setFloat(6, p.getCost());
                pst.setDouble(7, p.getAmt());
                pst.setFloat(8, p.getTax());
                pst.setDouble(9, p.getTotal());
                pst.setInt(10, p.getDbNumber());
                pst.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void update(List<Product> toUpdate) throws SQLException {
        String updateQuery ="UPDATE product_sale set date = ?, region = ?,product =?,qty=?,cost=?,amt=?,tax=?,total=? where id = ?";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(updateQuery);
        ){
            for (Product p : toUpdate) {
                pst.setDate(1, new Date(p.getDate().getTime()));
                pst.setString(2, p.getRegion());
                pst.setString(3, p.getProduct());
                pst.setInt(4, p.getQty());
                pst.setFloat(5, p.getCost());
                pst.setDouble(6, p.getAmt());
                pst.setFloat(7, p.getTax());
                pst.setDouble(8, p.getTotal());
                pst.setInt(9, p.getId());
                pst.executeUpdate();
            }
        }
    }
}