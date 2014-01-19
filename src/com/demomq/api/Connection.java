package com.demomq.api;

/**
 * Created by rishimittal on 10/1/14.
 */
public interface Connection {

    public int connect(String host_name, int server_port);
    public boolean send(String destinationName, String message);
    public String receive(String queueName);

}