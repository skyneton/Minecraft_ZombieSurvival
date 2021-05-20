package net.mpoisv.survival.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VoteCommand {
	public static void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		if(args.length == 1) {
			List<String> temp;
			if(label.equalsIgnoreCase("vote")) {
				temp = SubCommand.main;
			}else temp = SubCommand.ko;
			for(String sub : temp) {
				if(sub.toLowerCase().startsWith(args[0].toLowerCase())) arr.add(sub);
			}
		}
		
		Collections.sort(arr);
		return arr;
	}
	
	public static class SubCommand {
		public static List<String> main = Arrays.asList("agree", "opposition");
		public static List<String> ko = Arrays.asList("찬성", "반대");
	}
}
