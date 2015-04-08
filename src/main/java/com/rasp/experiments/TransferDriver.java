package com.rasp.experiments;

import com.rasp.experiments.protobuf.ProtoClient;
import com.rasp.experiments.protobuf.ProtoServer;
import com.rasp.utils.autodiscovery.Service;

import java.util.Scanner;

/**
 * Author : rahulmadhavan
 * File   :
 * Email  : rahulk@ccs.neu.edu
 * Created: 4/2/15
 * Edited :
 */
public class TransferDriver {
    private static int PORT_NO = 9293;
    private static TransferDriverContext transferDriverContext = new TransferDriverContext(PORT_NO);
    private static ProtoClient protoClient = new ProtoClient();

    public static void main(String[] args) {

        //start server

        ProtoServer.initializeServer(transferDriverContext.getLocalServiceAddress(),PORT_NO);

        while (true){

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] words = input.split(" ");

            switch (words[0]){
                case "discover": printServicesOnNetwork() ;break;
                case "send":
                    if(isSendInputValid(words)){
                        send(words[1],words[2]);
                    }else{
                        System.out.println("Usage : send <ip> <message>");
                    }
                    ;break;
                case "help": printHelpMessage(); break;
                case "exit": System.exit(0); break;
                default: printDefaultMessage();
            }

        }

    }

    private static void printDefaultMessage(){
        String message = "unrecognized input: type help to see the list of commands";
        System.out.println(message);
    }

    private static void printServicesOnNetwork(){
        String message = "\n\n";
        for(Service service : transferDriverContext.discover()){
            message += service.getIp();
            message += "\n";
        }
        System.out.println(message);
    }

    private static void printHelpMessage(){
        String message =  "Input : "+
                "discover                 : to discover all nodes in the network \n"+
                "send <ip> <message>      : to send a message to another node\n" +
                "help                     : to print the help menu\n" +
                "exit                     : to exit the program \n" ;
        System.out.println(message);
    }

    private static boolean isSendInputValid(String[] input){
        if(input.length < 3){
            return false;
        }else{
            return true;
        }
    }

    private static void send(String ip,String message){
        protoClient.send(ip,PORT_NO,message);
    }

}
