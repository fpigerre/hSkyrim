package code.husky;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class hCommands implements CommandExecutor {
	hApi api = new hApi();
	YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/hSkyrim/config.yml"));
	public boolean onCommand(CommandSender sender, Command command, String c, String[] args)  { 
		Player p = (Player) sender;
		ChatColor red = ChatColor.RED;
		List<String> blacksmiths = config.getStringList("classes.blacksmiths");
		List<String> farmers = config.getStringList("classes.farmers");
		List<String> assassins = config.getStringList("classes.assassins");
		List<String> thieves = config.getStringList("classes.thieves");
		List<String> enchanters = config.getStringList("classes.enchanters");
		List<String> guards = config.getStringList("classes.guards");
		ChatColor aqua = ChatColor.AQUA;
		if(c.equalsIgnoreCase("rp")) {
			if(args.length == 0) {
				p.sendMessage(red + "Please use '/rp help' for more info");
			} else {
				if(args[0].equalsIgnoreCase("help")) {
					p.sendMessage(aqua + "/rp join <job> - sets you to that job");
					p.sendMessage(aqua + "/rp leave <job> - leaves the current job");
					p.sendMessage(aqua + "/rp jobhelp - gives help on your current job");
					p.sendMessage(aqua + "/killreq <player> - places a bounty on someone (cost = " + config.getInt("Default-bounty-limit" + ")"));
				} else if(args[0].equalsIgnoreCase("join")) {
					String job = args[1];
					if(job.equalsIgnoreCase("blacksmith") || job.equalsIgnoreCase("assassin") || job.equalsIgnoreCase("thief") || job.equalsIgnoreCase("enchanter") || job.equalsIgnoreCase("farmer")) {
						api.setJob(p,job);
					}
				} else if(args[0].equalsIgnoreCase("jobhelp")) {
					if(api.getJob(p) == "guard") {
						
					} else if(api.getJob(p) == "blacksmith") {
						
					} else if(api.getJob(p) == "enchanter") {
						
					} else if(api.getJob(p) == "thieves") {
						
					} else if(api.getJob(p) == "guard") {
						
					} 
				}
			}
			return true;
		}
		return false;
	}
}
