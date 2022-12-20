package com.ja90n.lobbyplugin.utils;

import com.ja90n.lobbyplugin.LobbyPlugin;
import com.ja90n.lobbyplugin.customEvents.PlayerNPCClickEvent;
import com.ja90n.lobbyplugin.instances.Npc;
import com.ja90n.lobbyplugin.managers.NpcManager;
import io.netty.channel.*;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class CustomPacketEventUtil {

    private NpcManager npcManager;

    public CustomPacketEventUtil(NpcManager npcManager) {
        this.npcManager = npcManager;
    }

    public void inject(Player player){
        ChannelDuplexHandler channelHandler = new ChannelDuplexHandler(){
            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                super.write(ctx, packet, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object rawPacket) throws Exception {
                if (rawPacket instanceof ServerboundInteractPacket){
                    ServerboundInteractPacket packet = (ServerboundInteractPacket) rawPacket;

                    Field type = packet.getClass().getDeclaredField("b");
                    type.setAccessible(true);
                    Object typeData = type.get(packet);

                    if (typeData.toString().split("\\$")[1].charAt(0) == 'e') return;

                    try {
                        Field hand = typeData.getClass().getDeclaredField("a");
                        hand.setAccessible(true);
                        if (!hand.get(typeData).toString().equals("MAIN_HAND")) return;

                    } catch (NoSuchFieldException e){}

                    Field id = packet.getClass().getDeclaredField("a");
                    id.setAccessible(true);
                    int entityId = id.getInt(packet);

                    Npc npc = npcManager.getNPC(entityId);
                    if (npc != null){
                        Bukkit.getScheduler().runTask(LobbyPlugin.getInstance(), () -> {
                            PlayerNPCClickEvent playerNPCClickEvent = new PlayerNPCClickEvent(entityId,npc, player);
                            Bukkit.getPluginManager().callEvent(playerNPCClickEvent);
                        });
                    }
                }
                super.channelRead(ctx, rawPacket);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.getConnection().channel.pipeline();
        pipeline.addBefore("packet_handler",player.getName(),channelHandler);
    }

    public void stop(Player player){
        Channel channel = ((CraftPlayer) player).getHandle().connection.getConnection().channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }
}
