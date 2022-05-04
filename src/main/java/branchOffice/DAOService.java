package branchOffice;

import commun.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOService {
    private int DBNumber;
    public String url ;
    public String user = "root";
    public String password = "";
    public String queryGet="SELECT * FROM product_sale where synchd = 0";
    public String queryUpdate = "UPDATE product_sale set synchd = 1  where id = ?";
    public DAOService(int DBNumber) {
        this.DBNumber = DBNumber;
        this.url ="jdbc:mysql://localhost:3306/bo"+Integer.toString(DBNumber);
    }
    public List<Product> getNonSyncedProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(queryGet);
            ResultSet rs = pst.executeQuery()
        ) {

            while(rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setDate(rs.getDate("date"));
                product.setRegion(rs.getString("region"));
                product.setProduct(rs.getString("product"));
                product.setQty(rs.getInt("qty"));
                product.setCost(rs.getFloat("cost"));
                product.setAmt(rs.getDouble("amt"));
                product.setTax(rs.getFloat("tax"));
                product.setTotal(rs.getDouble("total"));
                product.setDbNumber(DBNumber);
                product.setUpdated(rs.getInt("updated"));
                products.add(product);
            }

            return products;
        }

    }

    public void updateSyncedProducts(List<Product> productList) throws SQLException {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = connection.prepareStatement(queryUpdate)
        ){
            for (Product product : productList) {
                pst.setInt(1, product.getId());
                pst.executeUpdate();
            }
        }
    }
}
