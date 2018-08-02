package me.ryandw11.tatertot.eco;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ryandw11.tatertot.Tatertot;
import me.ryandw11.tatertot.api.NoAccountException;
import net.md_5.bungee.api.ChatColor;

public class TaterAPI {
	
	private Tatertot plugin;
	public TaterAPI(Tatertot plugin){
		this.plugin = plugin;
	}
	/**
	 * Check if a player has an account.
	 * @param id The user id.
	 * @return
	 */
	public boolean hasAccount(UUID id){
		if(plugin.data.contains(id.toString() + ".tots")){
			return true;
		}
		return false;
	}
	
	/**
	 * Create an account if one does not exist.
	 * @param id
	 */
	public void createAccount(UUID id){
		if(!hasAccount(id)){
			plugin.data.set(id + ".tots", 0);
			plugin.saveFile();
		}
	}
	
	/**
	 * Get the balance of a player.
	 * @param id
	 * @return
	 * @throws NoAccountException If an account does not exist.
	 */
	public int getBalance(UUID id) throws NoAccountException{
		if(!hasAccount(id))
			throw new NoAccountException();
		return plugin.data.getInt(id + ".tots");
	}
	
	/**
	 * Set the balance of the player.
	 * Note: Doubles are converted to integers.
	 * @param id
	 * @param amount
	 */
	public void setBalance(UUID id, double amount){
		if(!hasAccount(id))
			createAccount(id);
		plugin.data.set(id + ".tots", Math.round(amount));
		plugin.saveFile();
	}
	
	/**
	 * 
	 * @param id
	 * @param amount
	 */
	public void addBalance(UUID id, double amount){
		try {
			plugin.data.set(id + ".tots", getBalance(id) + Math.round(amount));
			plugin.saveFile();
		} catch (NoAccountException e) {
			
		}
		plugin.saveFile();
	}
	
	/**
	 * 
	 * @param id
	 * @param amount
	 */
	public void subtractBalance(UUID id, double amount){
		try {
			if((getBalance(id) - Math.round(amount)) >= 0){
				plugin.data.set(id + ".tots", getBalance(id) - Math.round(amount));
				plugin.saveFile();
			}
			else
				setBalance(id, 0);
		} catch (NoAccountException e) {
			
		}
		plugin.saveFile();
	}
	
	/**
	 * Get the tatertot items stack.
	 * @return item stack.
	 */
	public ItemStack getTatertot(int amount){
		ItemStack item = new ItemStack(Material.BAKED_POTATO, amount);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GOLD + "Tatertot");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "This is a tatertot!");
		lore.add(ChatColor.YELLOW + "Use it as currency!");
		itemMeta.setLore(lore);
		itemMeta.addEnchant(Enchantment.LUCK, 1, false);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(itemMeta);
		return item;
	}
}
