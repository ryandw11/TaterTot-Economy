package me.ryandw11.tatertot.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.ryandw11.tatertot.Tatertot;
import me.ryandw11.tatertot.api.NoAccountException;
import me.ryandw11.tatertot.eco.TaterAPI;
import net.md_5.bungee.api.ChatColor;

public class TaterGive implements CommandExecutor {
	private TaterAPI tapi;
	public TaterGive(Tatertot plugin){
		tapi = new TaterAPI(plugin);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(!sender.hasPermission("tatereco.tater")){
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return true;
		}
		if(args.length == 0){
			sender.sendMessage(ChatColor.GREEN + "=================[" + ChatColor.GOLD + "Tatertot Economy" + ChatColor.GREEN + "]=================");
			if(sender.hasPermission("tatereco.set")) sender.sendMessage(ChatColor.GOLD + "/tater set (player) (amount)" + ChatColor.GREEN + " - Set a player's balance.");
			if(sender.hasPermission("tatereco.add")) sender.sendMessage(ChatColor.GOLD + "/tater add (player) (amount)" + ChatColor.GREEN + " - Add to a player's balance.");
			if(sender.hasPermission("tatereco.remove")) sender.sendMessage(ChatColor.GOLD + "/tater remove (player) (amount)" + ChatColor.GREEN + " - Remove taters from a player's balance.");
			if(sender.hasPermission("tatereco.withdraw")) sender.sendMessage(ChatColor.GOLD + "/tater withdraw (amount)" + ChatColor.GREEN + " - Grab your taters into physical form.");
			if(sender.hasPermission("tatereco.deposit")) sender.sendMessage(ChatColor.GOLD + "/tater deposit (amount)" + ChatColor.GREEN + " - Put your taters into your account.");
			if(sender.hasPermission("tatereco.balance")) sender.sendMessage(ChatColor.GOLD + "/tater balance" + ChatColor.GREEN + " - Grab your tater balance.");
			return true;
		}
		else if(args.length == 1 && (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal"))){
			if(!sender.hasPermission("tatereco.balance")){
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			try {
				p.sendMessage(ChatColor.GREEN + "You have " + ChatColor.GOLD + "" + tapi.getBalance(p.getUniqueId()) + ChatColor.GREEN + " tatertots.");
			} catch (NoAccountException e) {
				p.sendMessage(ChatColor.RED + "You do not have an account. Please contact an admin.");
			}
			return true;
		}
		else if(args.length == 2 && args[0].equalsIgnoreCase("withdraw")){
			if(!sender.hasPermission("tatereco.withdraw")){
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			int amount;
			int balance;
			try{
			amount = Integer.valueOf(args[1]);
			} catch (NumberFormatException e){
				p.sendMessage(ChatColor.RED + "That is not a valid number to withdraw!");
				amount = 0;
				return true;
			}
			try {
				balance = tapi.getBalance(p.getUniqueId());
			} catch (NoAccountException e) {
				balance = 0;
			}
			
			if(amount > balance){
				p.sendMessage(ChatColor.RED + "You do not have enough tatertots to withdraw that amount!");
				return true;
			}
			if(p.getInventory().firstEmpty() == -1){
				p.sendMessage(ChatColor.RED + "You do not have enough room in your inventory!");
				return true;
			}
			tapi.subtractBalance(p.getUniqueId(), amount);
			p.getInventory().addItem(tapi.getTatertot(amount));
			p.sendMessage(ChatColor.GREEN + "You withdrew those tatertots!");
			return true;
		}
		else if(args.length == 2 && args[0].equalsIgnoreCase("deposit")){
			if(!sender.hasPermission("tatereco.deposit")){
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "This command is for players only!");
				return true;
			}
			Player p = (Player) sender;
			
			int amount;
			int tatersFound = 0;
			
			try{
			amount = Integer.valueOf(args[1]);
			} catch (NumberFormatException e){
				p.sendMessage(ChatColor.RED + "That is not a valid number to deposit!");
				amount = 0;
				return true;
			}
			for(ItemStack i : p.getInventory().getContents()){
				if(i != null && i.equals(tapi.getTatertot(i.getAmount()))){
					if((tatersFound + i.getAmount()) < amount){
						tatersFound += i.getAmount();
						//i.setType(null);
						p.getInventory().remove(i);
					}
					else if(tatersFound >= amount){
						p.sendMessage(ChatColor.RED + "You do not have that many tatertots! Depositing the ones you do have!");
						break;
					}
					else{
						int takeaway = amount - tatersFound;
						i.setAmount(i.getAmount() - takeaway);
						tatersFound += takeaway;
						break;
					}
				}
			}
			tapi.addBalance(p.getUniqueId(), tatersFound);
			p.sendMessage(ChatColor.GREEN + "" + tatersFound + " taters were successfully deposited.");
			return true;
		}
		else if(args.length == 3 && args[0].equalsIgnoreCase("set")){
			if(!sender.hasPermission("tatereco.set")){
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
			if(op == null){
				sender.sendMessage(ChatColor.RED + "That player does not exist!");
				return true;
			}
			int amount;
			try{
				amount = Integer.valueOf(args[2]);
			} catch (NumberFormatException e){
					sender.sendMessage(ChatColor.RED + "That is not a valid number to set!");
					amount = 0;
					return true;
			}
			
			tapi.setBalance(op.getUniqueId(), Math.abs(amount));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + op.getName() + "'s &aaccount was set to &6 " + amount + " &ataters!"));
			return true;
		}
		else if(args.length == 3 && args[0].equalsIgnoreCase("add")){
			if(!sender.hasPermission("tatereco.add")){
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
			if(op == null){
				sender.sendMessage(ChatColor.RED + "That player does not exist!");
				return true;
			}
			int amount;
			try{
				amount = Integer.valueOf(args[2]);
			} catch (NumberFormatException e){
					sender.sendMessage(ChatColor.RED + "That is not a valid number to add!");
					amount = 0;
					return true;
			}
			
			tapi.addBalance(op.getUniqueId(), Math.abs(amount));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + op.getName() + "'s &aaccount was changed by &6 " + amount + " &ataters!"));
			return true;
		}
		else if(args.length == 3 && args[0].equalsIgnoreCase("remove")){
			if(!sender.hasPermission("tatereco.remove")){
				sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
			if(op == null){
				sender.sendMessage(ChatColor.RED + "That player does not exist!");
				return true;
			}
			int amount;
			int balance;
			try{
				amount = Integer.valueOf(args[2]);
			} catch (NumberFormatException e){
					sender.sendMessage(ChatColor.RED + "That is not a valid number to remove!");
					amount = 0;
					return true;
			}
			try {
				balance = tapi.getBalance(op.getUniqueId());
			} catch (NoAccountException e) {
				sender.sendMessage(ChatColor.RED + "That person does not have an account!");
				return true;
			}
			if(amount > balance){
				sender.sendMessage(ChatColor.RED + "That person does not have the amount of money specified!");
				return true;
			}
			
			tapi.subtractBalance(op.getUniqueId(), Math.abs(amount));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + op.getName() + "'s &aaccount was set to &6 " + (balance - amount) + " &ataters!"));
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Invalid command. Do /tater for assistance.");
		return false;
	}

}
