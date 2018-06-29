package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ChatClientThread extends Thread {

    private int CLIENT_REQUEST_TIMEOUT = 15*60*1000;
    private Socket sock = null;
    private Dispatcher dispatcher = null;
    private BufferedReader socketReader = null;
    private BufferedWriter socketWriter = null;


    ChatClientThread(Socket sock, Dispatcher dispatcher) throws IOException {
        this.sock = sock;
        this.dispatcher = dispatcher;
        sock.setSoTimeout(CLIENT_REQUEST_TIMEOUT);
        socketReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        socketWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    public void run(){

        System.out.println(new Date().toString() + " : " +
                "Accepted client : " + sock.getInetAddress() +
                ":" + sock.getPort());


        try {
            socketWriter.write("Welcome to the chat room!\n");
            socketWriter.write("Enter your username:\n");
            socketWriter.flush();
            String name = socketReader.readLine();
            System.out.println(name+" entered.");

            dispatcher.addClient(sock);

            while (!isInterrupted()) {
                //socketWriter.write(name+": ");
                //socketWriter.flush();
                String message = socketReader.readLine();
                if (message == null) break; // Client closed the socket
                dispatcher.pushMessage(name+": "+message+"\n");


            }
            dispatcher.removeClient(sock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
