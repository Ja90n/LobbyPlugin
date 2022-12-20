package com.ja90n.lobbyplugin.listeners;

import com.ja90n.lobbyplugin.LobbyPlugin;
import com.ja90n.lobbyplugin.utils.CustomPacketEventUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final LobbyPlugin lobbyPlugin;
    private final CustomPacketEventUtil customPacketEventUtil;

    public PlayerConnectionListener(LobbyPlugin lobbyPlugin) {
        this.lobbyPlugin = lobbyPlugin;
        customPacketEventUtil = new CustomPacketEventUtil(lobbyPlugin.getNpcManager());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        lobbyPlugin.getNpcManager().showNPCs(event.getPlayer());
        customPacketEventUtil.inject(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        lobbyPlugin.getNpcManager().hideNPCs(event.getPlayer());
        customPacketEventUtil.stop(event.getPlayer());
    }


}
