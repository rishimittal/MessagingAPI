package com.demomq.provider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by rishimittal on 8/1/14.
 */

public class JMSServer implements Runnable{

    private int socketPort = 2000;
    private static ServerSocket serverSocket = null;
    protected Thread runningThread = null;
    private Map<String, ConcurrentLinkedQueue<String> > addressMap = null;

    public void startServer(){
        try {
        this.serverSocket = new ServerSocket(this.socketPort);
            System.out.println("Server began Listening ...!");
        }catch(IOException iox){
            iox.printStackTrace();
        }
     }


    public void createMapAddress(){
        addressMap = java.util.Collections.synchronizedMap(new HashMap<String, ConcurrentLinkedQueue<String>>());
    }


    @Override
    public void run() {

        synchronized(this){
            this.runningThread = Thread.currentThread();
        }

        startServer();

        while( true ){

            Socket cSocket = null;

            try{
                cSocket = serverSocket.accept();
                //System.out.println("new Con");
                new Thread(new QueueManager(cSocket, addressMap)).start();
            }catch(IOException ex){
                throw new RuntimeException("Error in accepting connection", ex);
            }

        }

    }

    public static void main(String arr[]){
            JMSServer server = new JMSServer();
            server.createMapAddress();
            new Thread(server).start();
    }

}
