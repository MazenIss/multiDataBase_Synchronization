package ho;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import commun.Product;

import static commun.Serialize.deserialize;


public class mainTaskHO {
    public final static String QUEUE_NAME="product_sale_queue";
    public static void main(String[] args) throws IOException , TimeoutException{
        DAOService service = new DAOService();

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();
        channel1.queueDeclare(QUEUE_NAME + "1",false,false,false,null);
        channel2.queueDeclare(QUEUE_NAME + "2",false,false,false,null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            List<Product> productList = deserialize(receivedMessage);
            try {
                service.insert(productList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        channel1.basicConsume(QUEUE_NAME + "1",true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
        channel2.basicConsume(QUEUE_NAME + "2",true,deliverCallback,consumerTag -> {
            System.out.println("ERROR");
        });
    }

}
