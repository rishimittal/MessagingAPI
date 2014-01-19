package com.demomq.api;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by rishimittal on 10/1/14.
 */
public class ConnectionFactory {

    public static Connection getConnection(){
        return new MessagingAPI();
    }

}
