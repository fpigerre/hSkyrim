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

    /**
     * Gets a player's job.
     *
     * @param p A Player
     * @return String The player's job
     */
    public String getJob(Player p) {

        List<String> blacksmiths = config.getStringList("classes.blacksmiths");
        List<String> farmers = config.getStringList("classes.farmers");
        List<String> assassins = config.getStringList("classes.assassins");
        List<String> thieves = config.getStringList("classes.thieves");
        List<String> enchanter = config.getStringList("classes.enchanters");
        List<String> guards = config.getStringList("classes.guards");

        if (blacksmiths.contains(p.getName())) {
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

    /**
     * Sets a player's job
     *
     * @param p   A Player
     * @param job The job to be set
     */
    public void setJob(Player p, String job) {
        if (getJob(p) != null) {
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

    /**
     * Adds a bounty contract to a player
     *
     * @param p      A Player
     * @param reason The reason for the contract
     * @param reward The reward once the contract is completed
     */
    public void addContract(Player p, String reason, int reward) {
        String tobekilled = p.getName();
        contracts.put(tobekilled, reason);
        config.set("contracts", "");
        for (Player assassins : Bukkit.getServer().getOnlinePlayers()) {
            if (getJob(assassins) == "assassin") {
                p.sendMessage(ChatColor.GREEN + "[hSkyrim] New Bounty placed on " + tobekilled + " for $" + reward + " for " + reason);
            }
        }
    }

    /**
     * Adds a bounty to a player
     *
     * @param target A Player
     * @param reason The reason for the bounty to be placed
     * @param sender The player who created the bounty
     */
    public void createNewBounty(Player target, String reason, Player sender) {
        if (alreadyHasBounty(target)) {
            sender.sendMessage(ChatColor.RED + "This person already has a bounty!");
        } else {
            config.set(target.getName() + ".bounty.reason", reason);
            config.set(target.getName() + ".bounty.reward", config.getInt("Default-bounty-reward"));
            config.set(target.getName() + ".bounty.setter", sender);
        }
    }

    /**
     * Creates a guard bounty
     *
     * @param target A Player, generally an assassin or thief
     */
    public void createGuardBounty(Player target) {
        config.set(target.getName() + ".guard.reward", config.getInt("Default-bounty-reward"));
        for (Player guards : Bukkit.getServer().getOnlinePlayers()) {
            if (getJob(guards) == "guard") {
                guards.sendMessage(ChatColor.GREEN + "[hSkyrim] New Guard Bounty placed on " + target.getName() + "!");
            }
        }
    }

    /**
     * Checks if a player has an active guard bounty
     *
     * @param target A Player
     * @return Boolean If the player has an active guard bounty
     */
    public boolean alreadyHasGuardBounty(Player target) {
        if (config.getString(target.getName() + ".guard.reward") != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an active guard bounty
     *
     * @param target A Player
     */
    public void removeGuardBounty(Player target) {
        if (alreadyHasBounty(target)) {
            config.set(target.getName() + ".guard.reward", null);
        }
    }

    /**
     * Checks if a player already has a bounty
     *
     * @param target A Player
     * @return Boolean If the player already has an active bounty
     */
    public boolean alreadyHasBounty(Player target) {
        if (config.getString(target.getName() + ".bounty.reason") != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Displays general help to a certain player
     *
     * @param p A Player
     */
    public void displayHelp(Player p) {
        p.sendMessage(ChatColor.AQUA + "/rp join <job> - sets you to that job");
        p.sendMessage(ChatColor.AQUA + "/rp leave <job> - leaves the current job");
        p.sendMessage(ChatColor.AQUA + "/rp jobhelp - gives help on your current job");
        p.sendMessage(ChatColor.AQUA + "/rp contract <player> <reason> - Requests an assassin to take out a contract on somebody! (cost = " + config.getInt("Default-bounty-limit") + ")");
    }

    /**
     * Displays job specific help to a certain player
     *
     * @param p A Player
     */
    public void displayJobHelp(Player p) {

        switch (getJob(p)) {

            case ("guard"):
                p.sendMessage(ChatColor.GREEN + "Guards have a duty to protect the people.");
                p.sendMessage(ChatColor.GREEN + "If you see somebody attacking an innocent player, you have a duty to attack them back.");
                p.sendMessage(ChatColor.GREEN + "You can also collect bounties by killing players that have a bounty against them!");
                break;

            case ("blacksmith"):
                p.sendMessage(ChatColor.GREEN + "Blacksmiths earn money by crafting weapons, tools and armor.");
                p.sendMessage(ChatColor.GREEN + "The amount of money a blacksmith earns depends on the type and quantity of the artifacts you craft.");
                break;

            case ("enchanter"):
                p.sendMessage(ChatColor.GREEN + "Enchanters gain money by enchanting weapons, armor and tools.");
                p.sendMessage(ChatColor.GREEN + "The amount of money an enchanter earns depends on the type and quantity of the artifacts you craft.");
                break;

            case ("thieves"):
                p.sendMessage(ChatColor.GREEN + "Thieves can earn money by stealing goods and participating in organised crime.");
                p.sendMessage(ChatColor.GREEN + "It's a risky business, as a thief needs to sell stolen goods, which isn't always easy.");
                p.sendMessage(ChatColor.GREEN + "There's also the danger of being caught by a guard, or having a contract taken out against you by an assassin.");
                break;

            case ("assassin"):
                p.sendMessage(ChatColor.GREEN + "Assassins carry out contracts to kill people.");
                p.sendMessage(ChatColor.GREEN + "Assassins can be hired by members of the public to take out people they dislike.");
                p.sendMessage(ChatColor.GREEN + "Although being an assassin can pay well, it's a very risky business!");
                break;

            default:
                p.sendMessage(ChatColor.RED + "An error has occured! Please contact the developers of the hSkyrim plugin!");
                break;
        }
    }
}
