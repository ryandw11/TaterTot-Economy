package me.ryandw11.tatertot;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.ryandw11.tatertot.commands.CommandTabCompleter;
import me.ryandw11.tatertot.commands.TaterGive;
import me.ryandw11.tatertot.eco.TaterEconomy;
import me.ryandw11.tatertot.listeners.OnJoin;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

public class Tatertot extends JavaPlugin{
	
	public File datafile = new File(getDataFolder() + "/data/economy.yml");
	public FileConfiguration data = YamlConfiguration.loadConfiguration(datafile);
	@Override
	public void onEnable(){
		Vault v = (Vault) Bukkit.getServer().getPluginManager().getPlugin("Vault");
		Bukkit.getServicesManager().register(Economy.class, new TaterEconomy(this), v, ServicePriority.Normal);
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
		getCommand("tater").setExecutor(new TaterGive(this));
		getCommand("tater").setTabCompleter(new CommandTabCompleter());
		loadFile();
	}
	
	@Override
	public void onDisable(){
		saveFile();
	}
	
	 /**
     * Save the data file.
     */
    public void saveFile(){
		
		try{
			data.save(datafile);
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
	}
	/**
	 * load the data file
	 */
	public void loadFile(){
		if(datafile.exists()){
			try {
				data.load(datafile);
				
			} catch (IOException | InvalidConfigurationException e) {

				e.printStackTrace();
			}
		}
		else{
			try {
				data.save(datafile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
