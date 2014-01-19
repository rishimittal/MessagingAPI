import com.demomq.api.MessagingAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Created by rishimittal on 8/1/14.
 */
public class ClientC {

    private static String host_name = "localhost";
    private static int server_port = 2000;
    private static BufferedReader brt = null;

    public static void main(String arr[]){

        MessagingAPI msgapi = new MessagingAPI();
        boolean rval;

     /*
            int con = msgapi.connect(host_name, server_port);
            if (con == 1)
                System.out.println("Client C connected");
            */
            while(true){
                try{
                System.out.println("Select from three following Choices : - ");
                    System.out.println("1. Send Message");
                    System.out.println("2. Receive Message");
                    System.out.println("3. Exit");

                brt = new BufferedReader( new InputStreamReader(System.in));
                int choice = 0;
                    try {
                    choice = Integer.parseInt(brt.readLine());
                    }catch (NumberFormatException ne){
                        System.out.println("Improper Format");
                    }
                switch(choice){
                    case 1:
                        int con = msgapi.connect(host_name, server_port);
                        if (con == 1)
                            System.out.println("Client C connected");
                        System.out.print("To : ");
                        String cName = brt.readLine();
                        System.out.print("Message : ");
                        String message  = brt.readLine();
                        rval = msgapi.send(cName, message);
                        if(rval) {
                            System.out.println("Message Sent Sucessfully");
                        }
                        break;
                    case 2:
                        int con1 = msgapi.connect(host_name, server_port);
                        if (con1 == 1)
                            System.out.println("Client C connected");
                        System.out.println("Message Received : " +msgapi.receive("ClientC"));
                        break;
                    case 3:
                        exit(0);
                        break;
                    default:
                        System.out.println("Wrong Option");
                        break;
                }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }



    }
}
