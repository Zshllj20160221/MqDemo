import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zsh on 2017/11/3.
 */
public class Receiver {
    public static void main(String[] ars) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;

        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://ip:61616");

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("queueName_zsh");
            consumer = session.createConsumer(destination);
            while (true) {
                //consumer会一直进行监听
                TextMessage message = (TextMessage) consumer.receive();
                if (message != null) {
                    System.out.println("receive:" + message.getText());
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) connection.close();
            } catch (Throwable throwable) {

            }
        }
    }
}
