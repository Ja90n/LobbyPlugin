package com.ja90n.lobbyplugin.customEvents;

import com.ja90n.lobbyplugin.instances.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerNPCClickEvent extends Event {

    private final int entityId;
    private final Npc npc;
    private final Player player;

    public PlayerNPCClickEvent(int entityId, Npc npc, Player player){
        this.entityId = entityId;
        this.npc = npc;
        this.player = player;
    }

    public int getEntityId() {
        return entityId;
    }

    public Npc getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
