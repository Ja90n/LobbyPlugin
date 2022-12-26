package com.ja90n.lobbyplugin.customEvents;

import com.ja90n.lobbyplugin.MessageType;
import com.ja90n.lobbyplugin.ServerCommunicationHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class ServerMessageReceiveEvent extends Event {

    private final String rawMessage;
    private final String sender;
    private final MessageType messageType;
    private final ArrayList<String> args;
    private final ServerCommunicationHandler serverCommunicationHandler;

    public ServerMessageReceiveEvent(String rawMessage, String sender, MessageType messageType, ArrayList<String> args, ServerCommunicationHandler serverCommunicationHandler){
        this.rawMessage = rawMessage;
        this.sender = sender;
        this.messageType = messageType;
        this.args = args;
        this.serverCommunicationHandler = serverCommunicationHandler;
    }

    public ServerCommunicationHandler getServerCommunicationHandler() {
        return serverCommunicationHandler;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public String getSender() {
        return sender;
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
