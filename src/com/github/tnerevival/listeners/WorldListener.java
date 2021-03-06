package com.github.tnerevival.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class WorldListener implements Listener {
	
	TNE plugin;
	
	public WorldListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		String world = player.getWorld().getName();
		
		if(TNE.configurations.getBoolean("Core.World.EnableChangeFee")) {
			if(!player.hasPermission("tne.bypass.world")) {
				if(AccountUtils.hasFunds(player.getUniqueId(), AccountUtils.getWorldCost(world))) {
					AccountUtils.removeFunds(player.getUniqueId(), AccountUtils.getWorldCost(world));
					AccountUtils.initializeWorldData(player.getUniqueId(), world);
					player.sendMessage(ChatColor.DARK_RED + "You have been charged " + ChatColor.GOLD + MISCUtils.formatBalance(world, AccountUtils.getWorldCost(world)) + ChatColor.DARK_RED + " for changing worlds.");
				} else {
					player.teleport(event.getFrom().getSpawnLocation());
					player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you need at least " + ChatColor.GOLD + MISCUtils.formatBalance(world, AccountUtils.getWorldCost(world)) + ChatColor.DARK_RED + " to change worlds.");
				}
			} else {
				AccountUtils.initializeWorldData(player.getUniqueId(), world);
			}
		} else {
			AccountUtils.initializeWorldData(player.getUniqueId(), world);
		}
	}
}