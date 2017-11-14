package v2;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zsh on 2017/11/4.
 * 同步接收消息
 */
public class ConsumerSyn {

    private String name = "";
    private Destination destination = null;
    private Connection connection = null;
    private Session session = null;
    private MessageConsumer consumer = null;

    ConsumerSyn(String name) {
        this.name = name;
    }

    public void init() throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.104.160.225:61616");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("test_mgs");
        consumer = session.createConsumer(destination);

        connection.start();
    }

    public void receive() {
        try {
            int count = 0;
            init();
            while (count < 10) {
                Message message = consumer.receive();
                System.out.println("同步demo接受到的消息：" + ((TextMessage) message).getText());
                count++;
                System.out.println("this current count:" + count);
            }

        } catch (Exception e) {

        }

    }

    public void submit() throws JMSException {
        session.commit();
    }

    // 关闭连接
    public void close() throws JMSException {
        System.out.println("Consumer:->Closing connection");
        if (consumer != null)
            consumer.close();
        if (session != null)
            session.close();
        if (connection != null)
            connection.close();
    }
    public static void main(String [] args) throws Exception{
        ConsumerSyn consumerSyn = new ConsumerSyn("tongbu");
        consumerSyn.receive();
        System.out.println("可以开始处理其他业务啦.................");
        consumerSyn.close();
    }
}
