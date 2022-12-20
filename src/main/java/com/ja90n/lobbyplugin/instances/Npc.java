package com.ja90n.lobbyplugin.instances;

import com.ja90n.lobbyplugin.LobbyPlugin;
import com.ja90n.lobbyplugin.enums.Skin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Npc {

    private ArrayList<UUID> playerUUIDs;
    private final ServerPlayer npc;
    private final int id;
    private final Skin skin;
    private final Location location;

    public Npc(Skin skin, Location location, String name, int id) {
        this.id = id;
        this.skin = skin;
        this.location = location;
        playerUUIDs = new ArrayList<>();

        MinecraftServer minecraftServer = MinecraftServer.getServer();
        ServerLevel serverLevel = ((CraftWorld) Bukkit.getWorld("world")).getHandle();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

        npc = new ServerPlayer(minecraftServer, serverLevel, gameProfile, null);
        npc.setPos(location.getX(), location.getY(), location.getZ());
    }

    public void showNPC(Player player){
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
        connection.send(new ClientboundAddPlayerPacket(npc));

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((yaw % 360) * 256 / 360)));
        connection.send(new ClientboundMoveEntityPacket.Rot(npc.getBukkitEntity().getEntityId(), (byte) ((yaw % 360) * 256 / 360), (byte) ((pitch % 360) * 256 / 360), true));

        SynchedEntityData data = npc.getEntityData();
        byte bitmask = (byte) (0x01 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40);
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), bitmask);
        connection.send(new ClientboundSetEntityDataPacket(npc.getId(), data, true));

        playerUUIDs.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(LobbyPlugin.getInstance(), () ->
                connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc)), 10);
    }

    public void hideNPC(Player player){
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
    }

    public int getId() {
        return id;
    }

    public Skin getSkin() {
        return skin;
    }

    public ArrayList<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }
}