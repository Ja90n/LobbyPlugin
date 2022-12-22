package com.ja90n.lobbyplugin;

import com.ja90n.lobbyplugin.customEvents.ServerMessageReceiveEvent;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

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

    /*
    To send a message to a server I use this layout:
    from server : to server : command : (args)

    for example:
    lobby:proxy:sendPlayer:(Player UUID):destinationServer
     */

    // Sends a player to a server
    public void sendPlayerToServer(UUID playerUUID, String server){
        String message = Bukkit.getServer().getName() + ":" + server + ":" + MessageType.SEND_PLAYER.getMessage() + ":" + playerUUID.toString();
        sendMessage(message);
    }

    // Sends player to lobby
    public void kickPlayer(UUID playerUUID){
        String message = Bukkit.getServer().getName() + ":" + "lobby" + ":" + MessageType.KICK_PLAYER.getMessage() + ":" + playerUUID.toString();
        sendMessage(message);
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
