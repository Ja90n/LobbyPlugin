package com.ja90n.lobbyplugin;

import com.ja90n.lobbyplugin.commands.MainCommand;
import com.ja90n.lobbyplugin.listeners.PlayerConnectionListener;
import com.ja90n.lobbyplugin.listeners.PlayerNPCClickListener;
import com.ja90n.lobbyplugin.managers.NpcManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class LobbyPlugin extends JavaPlugin {

    private static LobbyPlugin lobbyPlugin;
    private NpcManager npcManager;
    private ServerCommunicationHandler serverCommunicationHandler;

    @Override
    public void onEnable() {
        lobbyPlugin = this;
        npcManager = new NpcManager();
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this),this);
        getServer().getPluginManager().registerEvents(new PlayerNPCClickListener(this),this);
        getCommand("npc").setExecutor(new MainCommand(this));

        try {
            serverCommunicationHandler = new ServerCommunicationHandler(this);
        } catch (IOException e) {
            getLogger().warning("Connection refused");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()){
            npcManager.hideNPCs(player);
        }
    }

    public static LobbyPlugin getInstance(){
        return lobbyPlugin;
    }

    public NpcManager getNpcManager() {
        return npcManager;
    }

    public ServerCommunicationHandler getServerCommunicationHandler() {
        return serverCommunicationHandler;
    }
}
