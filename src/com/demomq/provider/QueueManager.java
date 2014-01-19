package com.demomq.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by rishimittal on 10/1/14.
 */

public class QueueManager implements Runnable{

    protected ConcurrentLinkedQueue<String> messageQueue ;
    private BufferedReader br = null;
    private BufferedReader br2 = null;
    private PrintWriter out = null;
    private String clientName = null;
    private Socket cSocket = null;
    private Map<String, ConcurrentLinkedQueue<String> > addressMap = null;
    //private String message = null;
    protected Thread runningThread = null;

    public QueueManager(Socket cSocket, Map<String,ConcurrentLinkedQueue<String>> addressMap) {
        this.cSocket = cSocket;
        this.addressMap = addressMap;
    }

    public synchronized  void addQueue(String node, String message){
        //System.out.println(message);
        messageQueue = addressMap.get(node);

        if( messageQueue != null )
            messageQueue.add(message);
        else{
            messageQueue = new ConcurrentLinkedQueue<String>();
            messageQueue.add(message);
        }
        addressMap.put(node, messageQueue);
    }


    public synchronized void getData(){
        try {
            br = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            Thread.sleep(1000);
            String message = br.readLine();
            System.out.println("Message received by Server :" + message);
            int k = message.indexOf("?");
            String messageType = message.substring(0 , k);
            int in = message.indexOf("$");

            if (messageType.equalsIgnoreCase("send")){
                String n = message.substring(k + 1 , in);
                String m = message.substring(in + 1);
                //System.out.println("Client Name :" + n);
                //  System.out.println("com.demomq.api.Message Name :" + m);
                addQueue(n, m);
            }else {
                String cl = message.substring(k + 1);
                //System.out.println("Client Name :" + cl);
                dispatch(cl);
            }

        }catch(IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void dispatch(String client ){
        String m = null;
        messageQueue = addressMap.get(client);


        if ( messageQueue == null ) {
            m = "Message Queue Empty";
        }else {
            if(!messageQueue.isEmpty()) {
                m = (String)messageQueue.remove();
                //System.out.println(m);
            }else {
                m = "Message Queue Empty ";
            }
        }
        try {
            out = new PrintWriter(cSocket.getOutputStream());
            Thread.sleep(1000);
            out.println(m);
            out.flush();
            //System.out.println("com.demomq.api.Message Sent");
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
           getData();
    }
}
