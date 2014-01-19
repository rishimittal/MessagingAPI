package com.demomq.api;

import com.demomq.api.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;

/**
 * Created by rishimittal on 8/1/14.
 */
public class MessagingAPI implements Connection {

    private Socket clientSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    String receivedMessage = null;

    public int connect(String host_name, int server_port) {

        try {
            clientSocket = new Socket(host_name, server_port);
            Thread.sleep(1000);

        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (clientSocket !=  null)
              return 1;
        return 0;
    }

    public boolean send(String client, String s) {

        String mf = "send";
        String message = mf.concat("?").concat(client).concat("$").concat(s);
        boolean rValue = false;
        //System.out.println(message);
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            Thread.sleep(1000);
            out.println(message);
            out.flush();
            rValue = true;
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rValue;
    }

    public String receive(String client){
        String mf = "receive";
        String message = mf.concat("?").concat(client);
        //System.out.println(message);
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //Thread.sleep(1000);
            //System.out.println("Message Received :");
            out.println(message);
            out.flush();
            //System.out.println("com.demomq.api.Message Sent");
            Thread.sleep(1000);
            receivedMessage = in.readLine();
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }
}
