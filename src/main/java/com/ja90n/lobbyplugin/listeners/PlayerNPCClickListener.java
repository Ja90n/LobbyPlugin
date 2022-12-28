package com.ja90n.lobbyplugin.listeners;

import com.ja90n.lobbyplugin.LobbyPlugin;
import com.ja90n.lobbyplugin.customEvents.PlayerNPCClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNPCClickListener implements Listener {

    private LobbyPlugin lobbyPlugin;

    public PlayerNPCClickListener(LobbyPlugin lobbyPlugin) {
        this.lobbyPlugin = lobbyPlugin;
    }

    @EventHandler
    public void onNPCClick(PlayerNPCClickEvent event){
        event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Sending you to Bout's server!");
        lobbyPlugin.getServerCommunicationHandler().sendPlayerToServer(event.getPlayer().getUniqueId(),"bout");
    }
}