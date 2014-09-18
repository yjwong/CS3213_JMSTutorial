package cs3213.jms.order;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Matric 1: A0098862L
 * Name   1: Ang Hui Loon
 * 
 * Matric 2: A0088826M
 * Name   2: Wong Yong Jie
 *
 * This file implements a pipe that transfer messages using JMS.
 */

public class JmsPipe implements IPipe {

    private QueueConnectionFactory qconFactory;
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueSender qsender;
    private QueueReceiver qreceiver;
    private Queue queue;
    private TextMessage msg;

    public JmsPipe(String simpleConnectionFactory, String simpleQueue)
            throws NamingException, JMSException {
        Context context = getInitialContext();
        qconFactory = (QueueConnectionFactory) context.lookup(simpleConnectionFactory);
        qcon = qconFactory.createQueueConnection();
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = (Queue) context.lookup(simpleQueue);
        msg = qsession.createTextMessage();
        qcon.start();
    }

    @Override
    public void write(Order order) {
        try {
            if (qsender == null) {
                qsender = qsession.createSender(queue);
            }

            msg.setText(order.toString());
            qsender.send(msg);
        } catch (JMSException e) {
            System.err.println("[JmsPipe] Failed to write order.");
        }
    }

    @Override
    public Order read() {
        try {
            if (qreceiver == null) {
                qreceiver = qsession.createReceiver(queue);
            }

            TextMessage msg = (TextMessage) qreceiver.receive();
            return Order.fromString(msg.getText());

        } catch (JMSException e) {
            System.err.println("[JmsPipe] Failed to receive order.");
        }

        return null;
    }

    @Override
    public void close() {
        try {
            if (qreceiver != null) {
                qreceiver.close();
            }

            if (qsender != null) {
                qsender.close();
            }

            qcon.close();
        } catch (JMSException e) {
            System.err.println("[JmsPipe] Failed to close pipe.");
        }
    }

    private InitialContext getInitialContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        props.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        return new InitialContext(props);
    }
}
