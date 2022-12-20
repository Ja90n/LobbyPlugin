package com.ja90n.lobbyplugin.listeners;

import com.ja90n.lobbyplugin.LobbyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final LobbyPlugin lobbyPlugin;

    public PlayerConnectionListener(LobbyPlugin lobbyPlugin) {
        this.lobbyPlugin = lobbyPlugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        lobbyPlugin.getNpcManager().showNPCs(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        lobbyPlugin.getNpcManager().hideNPCs(event.getPlayer());
    }
}
