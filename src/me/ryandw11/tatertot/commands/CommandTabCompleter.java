package me.ryandw11.tatertot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTabCompleter implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tater")){
			ArrayList<String> completions = new ArrayList<>();
			if(args.length == 1){
				completions = new ArrayList<String>();
				if(sender.hasPermission("tatereco.set")) completions.add("set");
				if(sender.hasPermission("tatereco.add")) completions.add("add");
				if(sender.hasPermission("tatereco.remove")) completions.add("remove");
				if(sender.hasPermission("tatereco.balance")) completions.add("balance");
				if(sender.hasPermission("tatereco.withdraw")) completions.add("withdraw");
				if(sender.hasPermission("tatereco.deposit")) completions.add("deposit");
				completions = getAppliableTabCompleters(args.length == 1 ? args[0] : "", completions);
			}else{
				return null;
			}
			Collections.sort(completions);
			return completions;
		}
		
		return null;
	}
	
	public ArrayList<String> getAppliableTabCompleters(String arg, ArrayList<String> completions) {
	       if (arg == null || arg.equalsIgnoreCase("")) {
	           return completions;
	       }
	       ArrayList<String> valid = new ArrayList<>();
	       for (String posib : completions) {
	           if (posib.startsWith(arg)) {
	               valid.add(posib);
	           }
	       }
	       return valid;
	   }

}
