import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zsh on 2017/11/3.
 */
public class Sender {

    static final int SEND_NUMBER = 10000;

    public static void main(String[] args) {

        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;

        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://ip:61616");
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
//            session = connection.createSession(true, javax.jms.Session.SESSION_TRANSACTED);

            destination = session.createQueue("queueName_zsh");

            messageProducer = session.createProducer(destination);

//          messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

            sendMessage(session, messageProducer);

//            session.commit(); //when use "session = connection.createSession(Boolean.FALSE, Session.SESSION_TRANSACTED);" use this line

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=connection) connection.close();
            } catch (Throwable throwable) {

            }
        }
    }

    public static void sendMessage(Session session, MessageProducer messageProducer) throws Exception {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage textMessage = session.createTextMessage("the message of server...is ..."+SEND_NUMBER+ "messsage");
            messageProducer.send(textMessage);
        }
    }
}