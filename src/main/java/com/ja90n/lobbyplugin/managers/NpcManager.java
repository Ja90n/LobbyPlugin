package com.ja90n.lobbyplugin.managers;

import com.ja90n.lobbyplugin.enums.Skin;
import com.ja90n.lobbyplugin.instances.Npc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class NpcManager {

    private final ArrayList<Npc> npcs;
    private int id;

    public NpcManager(){
        npcs = new ArrayList<>();
        createNPCs();
    }

    private void createNPCs(){
        // Create all NPCs on startup
        createNPC(
                Skin.KABOUTERTEUN08,
                new Location(Bukkit.getWorld("world"), 0,-60,0),
                ChatColor.GREEN + "KabouterTeun08");
    }

    private void createNPC(Skin skin, Location location, String name){
        npcs.add(new Npc(skin,location,name,id));
        id++;
    }

    // Show or hide NPCs
    public void showNPCs(Player player){
        for (Npc npc : npcs){
            npc.showNPC(player);
        }
    }

    public void hideNPCs(Player player){
        for (Npc npc : getNpcs(player.getUniqueId())){
            npc.hideNPC(player);
        }
    }

    // Get NPCs
    private ArrayList<Npc> getNpcs() {
        return npcs;
    }

    private ArrayList<Npc> getNpcs(UUID playerUUID){
        ArrayList<Npc> arrayList = new ArrayList<>();
        for (Npc npc : npcs){
            if (npc.getPlayerUUIDs().contains(playerUUID)){
                arrayList.add(npc);
            }
        }
        return arrayList;
    }

    private ArrayList<Npc> getNpcs(Skin skin){
        ArrayList<Npc> arrayList = new ArrayList<>();
        for (Npc npc : npcs){
            if (npc.getSkin().equals(skin)){
                arrayList.add(npc);
            }
        }
        return arrayList;
    }

    private Npc getNpc(int id){
        for (Npc npc : npcs){
            if (npc.getId() == id){
                return npc;
            }
        }
        return null;
    }
}
