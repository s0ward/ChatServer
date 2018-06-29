package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Dispatcher extends Thread{

    private ArrayList<Socket> clients;
    private Queue<String>  messages;

    Dispatcher(){
        clients = new ArrayList<>(10);
        messages = new LinkedList<>();
    }

    public synchronized void addClient(Socket client){
        clients.add(client);
    }

    public synchronized void removeClient(Socket client){
        if(clients.contains(client)) clients.remove(client);
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void pushMessage(String message){
        //System.out.println("in pushMessage");
        messages.add(message);
        notify();
    }

    private synchronized void sendMessage(Socket s, String message) throws IOException {
        //System.out.println("in sendMessage");
        BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        socketWriter.write(message);
        socketWriter.flush();
    }

    private synchronized String getNextMessage() throws InterruptedException {
        while(messages.size() == 0) wait();
        String message = messages.remove();
        return message;

    }

    private synchronized void sendtoAll(String message) throws IOException {
                for (Socket s : clients) sendMessage(s, message);
    }

    public void run(){

        try {
            while(true){
                String message = getNextMessage();
                sendtoAll(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
