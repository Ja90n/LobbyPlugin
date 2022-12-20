package com.ja90n.lobbyplugin.listeners;

import com.ja90n.lobbyplugin.LobbyPlugin;
import com.ja90n.lobbyplugin.customEvents.PlayerNPCClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNPCClickListener implements Listener {

    private LobbyPlugin lobbyPlugin;

    public PlayerNPCClickListener(LobbyPlugin lobbyPlugin) {
        this.lobbyPlugin = lobbyPlugin;
    }

    @EventHandler
    public void onNPCClick(PlayerNPCClickEvent event){
        event.getNpc().hideNPC(event.getPlayer());
    }
}
