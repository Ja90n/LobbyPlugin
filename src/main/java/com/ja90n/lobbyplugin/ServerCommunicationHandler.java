package com.ja90n.lobbyplugin;

import com.ja90n.lobbyplugin.customEvents.ServerMessageReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.UUID;

public class ServerCommunicationHandler {

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 9090;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final LobbyPlugin lobbyPlugin;
    private final String name;

    public ServerCommunicationHandler(LobbyPlugin lobbyPlugin) throws IOException {
        socket = new Socket(SERVER_IP, PORT);
        this.lobbyPlugin = lobbyPlugin;

        name = "lobby";

        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(),true);

        initialConnect();
    }

    public void initialConnect(){
        Bukkit.getScheduler().runTaskAsynchronously(lobbyPlugin, () -> {
            int random = randomNumber();
            sendMessage(name + ":proxy:initialConnect:" + random);
            while (true){
                try {
                    String message = input.readLine();
                    String[] args = message.split(":");
                    if (String.valueOf(random).equals(args[3])){
                        lobbyPlugin.getLogger().warning("Initial connection success!");
                        receiveMessage();
                        break;
                    }
                } catch (IOException e) {}
            }
        });
    }

    public void receiveMessage() throws IOException {
        Bukkit.getScheduler().runTaskAsynchronously(lobbyPlugin, () -> {
            while (true){
                try {
                    String message = input.readLine();
                    if (message.startsWith(Bukkit.getServer().getName())){
                        ServerMessageReceiveEvent event = new ServerMessageReceiveEvent(message);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } catch (IOException e) {
                    reconnect();
                }
            }
        });
    }

    public void reconnect(){
        lobbyPlugin.getLogger().warning("Disconnected from server");
        lobbyPlugin.getLogger().warning("Trying to reconnecting...");
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(LobbyPlugin.getInstance(), () -> {
            lobbyPlugin.getLogger().warning("Still trying to reconnect");
        },0,80);

        Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
            while (true){
                try {
                    socket = new Socket(SERVER_IP,PORT);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output = new PrintWriter(socket.getOutputStream(),true);
                    task.cancel();
                    lobbyPlugin.getLogger().info("Connection restored!");
                    break;
                } catch (IOException e) {}
            }
        });
    }

    private void sendMessage(String message){
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

    private int randomNumber(){
        Random rand = new Random();
        int upperbound = 10000; // Last number not included
        return rand.nextInt(upperbound);
    }
}
