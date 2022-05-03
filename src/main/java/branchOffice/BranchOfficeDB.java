package branchOffice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import commun.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import static commun.Serialize.serialize;


public class BranchOfficeDB {
    public final static String QUEUE_NAME="product_sale_queue";
    public static void main(String[] args) throws IOException, SQLException {

        int DBNumber = Integer.parseInt(args[0]);
        DAOService service = new DAOService(DBNumber);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        TimerTask task = new TimerTask() {
            public void run(){
                try {
                    List<Product> productList = service.getNonSyncedProducts();
                    String message = serialize(productList);
                    if(productList.size()>0) {
                        try (Connection connection = connectionFactory.newConnection()) {
                            Channel channel = connection.createChannel();
                            channel.queueDeclare(QUEUE_NAME + Integer.toString(DBNumber), false, false, false, null);

                            channel.basicPublish("", QUEUE_NAME + Integer.toString(DBNumber), null, message.getBytes());
                            System.out.println(" [x] sent '" + message + "' at " + LocalDateTime.now().toString());
                            service.updateSyncedProducts(productList);
                        } catch (TimeoutException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e){ e.printStackTrace(); }
            }
        };
        Timer timer = new Timer("Sync");
        timer.schedule(task,0, 60*1000L);
    }
}
