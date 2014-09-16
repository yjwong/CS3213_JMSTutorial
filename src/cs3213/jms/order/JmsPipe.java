package cs3213.jms.order;

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

    public JmsPipe(String simpleConnectionFactory, String simpleQueue) {
    }

    @Override
    public void write(Order s) {

    }

    @Override
    public Order read() {
        return null;
    }

    @Override
    public void close() {

    }
}
