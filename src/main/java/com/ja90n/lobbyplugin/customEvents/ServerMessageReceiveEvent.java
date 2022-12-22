package com.ja90n.lobbyplugin.customEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerMessageReceiveEvent extends Event {

    private final String message;

    public ServerMessageReceiveEvent(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
