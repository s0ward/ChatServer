package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer {

    private static int LISTENING_PORT = 9000;



    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(LISTENING_PORT);
        System.out.println("Server started. Listening on port "+LISTENING_PORT+"...");

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.start();

        while(true){
            Socket socket = serverSocket.accept();
            ChatClientThread chatClientThread = new ChatClientThread(socket, dispatcher);
            chatClientThread.start();
        }
    }

}
