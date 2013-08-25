package code.husky;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class hSkyrim extends JavaPlugin {
	public static Economy economy = null;
	YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/hSkyrim/config.yml"));
	List<String> blacksmiths = new ArrayList();
	List<String> assassins = new ArrayList();
	List<String> enchanters = new ArrayList();
	List<String> farmers = new ArrayList();
	List<String> thieves = new ArrayList();
	List<String> guards = new ArrayList();
	hCommands commands = new hCommands();
	hListener listener = new hListener();
	private hSkyrim instance;
	
	private hSkyrim() {
		
	}
	
	public static hSkyrim getInstance() {
		return instance;
	}
	
	public void onEnable() {
		createConfig();
		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
		}
		getCommand("rp").setExecutor(commands);
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	public void onDisable() {
		try {
			config.save("plugins/hSkyrim/config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void createConfig() {
		 boolean exists = new File("plugins/hSkyrim/config.yml").exists();
		 if(!exists) {
		 new File("plugins/hSkyrim").mkdir();
		 	config.options().header("hSkyrim, made by Husky!");
		 	config.set("Pluginheader", "[hSkyrim]");
		 	config.set("ChatFormat", "#job - #playername : #message");
		 	config.set("Default-bounty-limit", 250);
		 	config.set("classes.blacksmiths", blacksmiths);
		 	config.set("classes.guards", guards);
		 	config.set("classes.thieves", thieves);
		 	config.set("classes.enchanters", enchanters);
		 	config.set("classes.assassins", assassins);
		 	config.set("classes.farmers", farmers);
		    try {
				config.save("plugins/hSkyrim/config.yml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	}
	 private boolean setupEconomy()
	    {
	        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            economy = economyProvider.getProvider();
	        }
	        return (economy != null);
	    }
	 public static Economy getEconomy() {
		 return economy;
	 }
   
}
