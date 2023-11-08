package ServerPackage;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Serveur {
    public static void main(String[] args) {
        int port = 1234;
        ArrayList<InetSocketAddress> clients = new ArrayList<>();

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("Serveur en attente de connexions sur le port " + port);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetSocketAddress clientAddress = (InetSocketAddress) receivePacket.getSocketAddress();

                if (!clients.contains(clientAddress)) {
                    clients.add(clientAddress);
                    System.out.println("Nouveau client connecté depuis " + clientAddress.getAddress() + ":" + clientAddress.getPort());
                }

                // Diffuser le message à tous les clients, sauf à l'expéditeur
                for (InetSocketAddress client : clients) {
                    if (!client.equals(clientAddress)) {
                        byte[] sendData = message.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client);
                        serverSocket.send(sendPacket);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
