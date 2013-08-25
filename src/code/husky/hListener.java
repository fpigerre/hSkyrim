package code.husky;

import java.io.File;
import java.util.ArrayList;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class hListener implements Listener {
    ArrayList<String> victimpvp = new ArrayList<String>();
    hApi api = new hApi();
    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/hSkyrim/config.yml"));
    @EventHandler
    public void chat (PlayerChatEvent e) {
        String format = config.getString("ChatFormat");
        Player p = e.getPlayer();
        if(api.getJob(p) != null) {
            format =  "[" +ChatColor.AQUA+ api.getJob(p)+ChatColor.WHITE + "] " + e.getPlayer().getName() + " : " + e.getMessage();
        } else {
            format =   "[] "+ ChatColor.GREEN  + e.getPlayer().getName()+ ChatColor.WHITE + " : " + e.getMessage();
        }
        e.setFormat(format);
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent evt) {
        Entity damager = evt.getDamager();
        Entity damagee = evt.getEntity();
        if (damager instanceof Player && damagee instanceof Player) {
            Player victim = (Player)evt.getEntity();
            Player damage = (Player) damager;
            String v = victim.getName();
            if(damage.getHealth() == 0) {
                if(api.alreadyHasBounty(victim)) {
                    Economy e = hSkyrim.getEconomy();
                    e.bankWithdraw(config.getString(v + ".bounty.setter"), config.getDouble("Default-bounty-limit"));
                    e.bankDeposit(damage.getName(), config.getDouble("Default-bounty.limit"));
                    for(Player ss : Bukkit.getOnlinePlayers()) {
                        if(api.getJob(ss) == "assassin") {
                            ss.sendMessage(ChatColor.GREEN + damage.getName() + " has just killed " + v + " and recieved " + config.getDouble("Default-bounty-limit"));
                        }
                    }
                }	
            }

            if(!victimpvp.contains(v)) {
                victimpvp.add(v); 
            } else {

            }
            Player assassin = (Player) damager;
            /* code here, Husk :)
             */
            if(api.getJob(assassin) != "assassin") {
                if(victimpvp.contains(v)) {
                    evt.setCancelled(false);
                } else {
                    evt.setCancelled(true);
                    assassin.sendMessage(ChatColor.GREEN + "[hSkyrim] You are not allowed to PVP as you aren't an assassin");
                    Economy econ = hSkyrim.getEconomy();
                    // take money --> econ.withdrawPlayer(playername, amount);
                    // give money --> econ.depositPlayer(playername, amount);	
                }
                // :O You can't pvp other players, unless you are an assassin
            } else {
                if (!(evt.isCancelled())) {
                    evt.setCancelled(false); // <-- not needed, i know :D
                }
            }
        }
    }
}