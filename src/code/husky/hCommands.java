package code.husky;

import java.io.File;
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class hCommands implements CommandExecutor {

    hApi api = new hApi();
    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/hSkyrim/config.yml"));

    public boolean onCommand(CommandSender sender, Command command, String c, String[] args) {

        Player p = (Player) sender;
        ChatColor red = ChatColor.RED;

        ChatColor aqua = ChatColor.AQUA;

        if (c.equalsIgnoreCase("rp")) {
            if (args.length == 0) {
                p.sendMessage(red + "Please use '/rp help' for more info");
            } else {

                if (args[0].equalsIgnoreCase("help")) {
                    api.displayHelp(p);

                } else if (args[0].equalsIgnoreCase("join")) {
                    String reqJob = args[1];
                    if (reqJob.equalsIgnoreCase("blacksmith") || reqJob.equalsIgnoreCase("assassin") || reqJob.equalsIgnoreCase("thief") || reqJob.equalsIgnoreCase("enchanter") || reqJob.equalsIgnoreCase("farmer")) {
                        api.setJob(p, reqJob);
                    }

                } else if (args[0].equalsIgnoreCase("jobhelp")) {
                    api.displayJobHelp(p);

                } else if (args[0].equalsIgnoreCase("racehelp")) {
                    api.displayRaceHelp(p);

                } else if (args[0].equalsIgnoreCase("contract")) {
                    if (api.getJob(p) != "assassin" && api.getJob(p) != "guard") {
                        if (args[1] != null && args[2] != null) {
                            Economy e = hSkyrim.getEconomy();
                            if (e.has(p.getName(), config.getInt("Default-bounty-limit"))) {
                                api.addContract(Bukkit.getPlayer(args[1]), args[2], config.getInt("Deafult-bounty-limit"));
                            } else {
                                p.sendMessage(ChatColor.GREEN + "[hSkyrim] You don't have enough money to create this contract!");
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
