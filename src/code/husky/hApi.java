package code.husky;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class hApi {
	public HashMap<String, String> contracts = new HashMap<String, String>();
	YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/hSkyrim/config.yml"));
	String header = config.getString("Pluginheader");
	public String getJob(Player p) {
		List<String> blacksmiths = config.getStringList("classes.blacksmiths");
		List<String> farmers = config.getStringList("classes.farmers");
		List<String> assassins = config.getStringList("classes.assassins");
		List<String> thieves = config.getStringList("classes.thieves");
		List<String> enchanter = config.getStringList("classes.enchanters");
		List<String> guards = config.getStringList("classes.guards");
		if(blacksmiths.contains(p.getName())) {
			return "blacksmith";
		} else if (farmers.contains(p.getName())) {
			return "farmer";
		} else if (thieves.contains(p.getName())) {
			return "thief";
		} else if (assassins.contains(p.getName())) {
			return "assassin";
		} else if (enchanter.contains(p.getName())) {
			return "enchanter";
		} else if (guards.contains(p.getName())) {
			return "guard";
		} else {
		return null;
		}
	}

	public void setJob(Player p, String job) {
		if(getJob(p) != null) {
			p.sendMessage(ChatColor.RED + header + " Error: You already have a job");
		} else {
			p.sendMessage(ChatColor.GREEN + header + " Setting you to " + job + "...");
			config.set(p.getName() + ".job", job);
			p.sendMessage(ChatColor.GREEN + header + " Done");
			try {
				config.save("plugins/hSkyrim/config.yml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void addContract(Player p, String reason, int reward) {
		String tobekilled = p.getName();
		 contracts.put(tobekilled,reason);
		 config.set("contracts", "");
		 for(Player assassanators : Bukkit.getServer().getOnlinePlayers()) {
			 if(getJob(assassanators) == "assassin") {
				 p.sendMessage(ChatColor.GREEN + "[hSkyrim] New Bounty placed on " + tobekilled + " for $" + reward + " for " + reason);
			 }
		 }
	}
	public void createNewBounty(Player target, String reason, Player sender) {
		if(alreadyHasBounty(target)) {
			sender.sendMessage(ChatColor.RED + "This person already has a bounty!");
		} else {
		config.set(target.getName()+ ".bounty.reason", reason);
		config.set(target.getName() + ".bounty.reward", config.getInt("Default-bounty-reward"));
		config.set(target.getName() + ".bounty.setter", sender);
		}
	}

	public boolean alreadyHasBounty(Player target) {
		if(config.getString(target.getName() + ".bounty.reason") != null) {
			return true;
		}
		return false;
	} 
}
