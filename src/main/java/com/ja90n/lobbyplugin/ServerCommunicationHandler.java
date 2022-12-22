package com.ja90n.lobbyplugin;

import com.ja90n.lobbyplugin.customEvents.ServerMessageReceiveEvent;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerCommunicationHandler {

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 9090;

    private final BufferedReader input;
    private final PrintWriter output;

    public ServerCommunicationHandler() throws IOException {
        Socket socket = new Socket(SERVER_IP, PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(),true);
    }

    public void sendMessage(String message){
        output.println(message);
    }

    public void receiveMessage() throws IOException {
        Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
            while (true){
                try {
                    String message = input.readLine();
                    if (message.startsWith(Bukkit.getServer().getName())){
                        ServerMessageReceiveEvent event = new ServerMessageReceiveEvent(message);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } catch (IOException e) {
                    System.out.println("ik kan niet zo veel");
                }
            }
        });
    }
}
