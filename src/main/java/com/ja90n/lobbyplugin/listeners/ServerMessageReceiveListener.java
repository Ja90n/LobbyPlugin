package com.ja90n.lobbyplugin.listeners;

import com.ja90n.lobbyplugin.MessageType;
import com.ja90n.lobbyplugin.customEvents.ServerMessageReceiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerMessageReceiveListener implements Listener {

    @EventHandler
    public void onMessage(ServerMessageReceiveEvent event){
        switch (event.getMessageType()){
            case PLAYERCOUNT:
                event.getServerCommunicationHandler().sendPlayerCountRequest(event.getSender());
                break;
            case GAMESTATE:
                event.getServerCommunicationHandler().sendGameStateResponse(event.getSender());
                break;
        }
    }
}
