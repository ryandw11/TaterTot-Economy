package me.ryandw11.tatertot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ryandw11.tatertot.Tatertot;
import me.ryandw11.tatertot.eco.TaterAPI;

public class OnJoin implements Listener{
	
	private Tatertot plugin;
	public OnJoin(Tatertot plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent e){
		TaterAPI tapi = new TaterAPI(plugin);
		if(!tapi.hasAccount(e.getPlayer().getUniqueId())){
			tapi.createAccount(e.getPlayer().getUniqueId());
		}
	}
}
