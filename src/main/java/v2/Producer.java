package v2;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by zsh on 2017/11/4.
 */
public class Producer {
    private Session session;
    private MessageProducer producer;
    private Connection connection;

    public void init() throws JMSException{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.104.160.225:61616");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("test_mgs");
        producer = session.createProducer(destination);
        connection.start();
    }

    public void sendText(String message){
        try{
            TextMessage text = session.createTextMessage(message);
            producer.send(text);
            System.out.println("send message:"+text.getText());
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
    public void submit() throws JMSException{
        session.commit();
    }
    public void close() throws JMSException{
        System.out.println("producer:->closing connetion");
        if (producer!=null) producer.close();
        if (session!=null) session.close();
        if (connection!=null)connection.close();
    }

    public static void main(String [] args) throws Exception{
        Producer producer = new Producer();
        producer.init();
        for(int i = 0; i< 5; i++){
            producer.sendText("HelloWord!......"+i);
        }
        producer.close();
        System.out.println("send over!");
    }

}
