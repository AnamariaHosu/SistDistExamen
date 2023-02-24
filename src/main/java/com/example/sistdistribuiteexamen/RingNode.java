package com.example.sistdistribuiteexamen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RingNode {
    private int id;
    private int port;
    private int successorPort;
    private int predecessorPort;

    public RingNode(int id, int port, int successorPort, int predecessorPort) {
        this.id = id;
        this.port = port;
        this.successorPort = successorPort;
        this.predecessorPort = predecessorPort;
    }

    public void start() {
        try {
            // Create a server socket to listen for incoming messages from predecessor
            ServerSocket serverSocket = new ServerSocket(port);

            // Create a socket to send messages to successor
            Socket successorSocket = new Socket("localhost", successorPort);

            // Create a reader to read messages from predecessor
            BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.accept().getInputStream()));

            // Create a writer to send messages to successor
            PrintWriter out = new PrintWriter(new OutputStreamWriter(successorSocket.getOutputStream()), true);

            // Send initial message to successor
            out.println("Message from node " + id);

            int messagesReceived = 0;
            while (messagesReceived < 2) {
                // Wait for message from predecessor
                String message = in.readLine();

                // If the message is not from itself, relay it to successor
                if (!message.contains(String.valueOf(id))) {
                    out.println(message + " forwarded by node " + id);
                } else {
                    // Increment message count when message is received from itself
                    messagesReceived++;
                }
            }

            // Close sockets and server socket
            successorSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java RingNode <id> <port> <successorPort> <predecessorPort>");
            return;
        }

        // Parse command line arguments
        int id = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        int successorPort = Integer.parseInt(args[2]);
        int predecessorPort = Integer.parseInt(args[3]);

        // Create and start the node
        RingNode node = new RingNode(id, port, successorPort, predecessorPort);
        node.start();
    }
}
