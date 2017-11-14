package v2;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zsh on 2017/11/4.
 * 异步接收消息
 */
public class ConsumerAsyn implements MessageListener{
    private ConnectionFactory connectionFactory;
    private Connection connection = null;
    private Session session = null;
    private Destination destination;
    private MessageConsumer consumer;

    public void init() throws Exception{
        connectionFactory = new ActiveMQConnectionFactory("tcp://172.104.160.225:61616");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("test_que");
        consumer = session.createConsumer(destination);
        connection.start();
    }

    public void receive() throws Exception{
        try{
            init();
            System.out.println("......注册监听");
            consumer.setMessageListener(this);

        }catch(Exception e){
            e.printStackTrace();
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

    @Override
    public void onMessage(Message message) {
        System.out.println("进入监听器的函数回调方法:");
        try{
            if(message instanceof TextMessage){
                System.out.println("接受消息"+((TextMessage)message).getText());
            }
            Thread.sleep(1000);
        }catch(Exception e){

        }
    }

    public static void main(String [] args) throws Exception{
        ConsumerAsyn consumer = new ConsumerAsyn();
        consumer.receive();
        System.out.println("异步接收完毕");
//        Thread.sleep(100000);
    }
}
