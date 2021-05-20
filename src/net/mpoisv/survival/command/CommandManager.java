package net.mpoisv.survival.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ZombieSurvival")) {
			ZombieSurvivalCommand.onCommand(sender, cmd, label, args);
		}else if(cmd.getName().equalsIgnoreCase("vote")) {
			VoteCommand.onCommand(sender, cmd, label, args);
		}
		return true;
	}
}
