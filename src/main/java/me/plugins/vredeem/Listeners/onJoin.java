package me.plugins.vredeem.Listeners;

import me.plugins.vredeem.VRedeem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(player.hasPermission("*") || player.isOp()){
            player.sendMessage(VRedeem.instance.format("&9&l[VRedeem] - Information"));
            player.sendMessage(VRedeem.instance.format("&9&l[VRedeem] - Plugin made by &0&lqViperH"));
        }




    }



}
