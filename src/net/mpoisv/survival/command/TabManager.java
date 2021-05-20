package net.mpoisv.survival.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabManager implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ZombieSurvival")) {
			return ZombieSurvivalCommand.onTabComplete(sender, cmd, label, args);
		}else if(cmd.getName().equalsIgnoreCase("vote")) {
			return VoteCommand.onTabComplete(sender, cmd, label, args);
		}
		
		return null;
	}
}
