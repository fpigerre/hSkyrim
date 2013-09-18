package code.husky;

import java.io.File;
import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class hListener implements Listener {

    ArrayList<String> victimpvp = new ArrayList<String>();
    hApi api = new hApi();
    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/hSkyrim/config.yml"));

    @EventHandler
    public void chat(PlayerChatEvent event) {
        String format = config.getString("ChatFormat");
        Player p = event.getPlayer();
        if (api.getJob(p) != null && api.getRace(p) != null) {
            format = "[" + ChatColor.AQUA + api.getRace(p) + " " + api.getJob(p) + ChatColor.WHITE + "] " + event.getPlayer().getName() + " : " + event.getMessage();
        } else {
            format = "[Commoner] " + ChatColor.GREEN + event.getPlayer().getName() + ChatColor.WHITE + " : " + event.getMessage();
        }
        event.setFormat(format);
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();
        if (damager instanceof Player && damagee instanceof Player) {

            Player victim = (Player) damagee;
            Player attacker = (Player) damager;
            String v = victim.getName();

            if (api.alreadyHasBounty(victim)) {
                if (victim.getHealthScale() == 0) {
                    Economy e = hSkyrim.getEconomy();
                    e.bankWithdraw(config.getString(v + ".bounty.setter"), config.getDouble("Default-bounty-limit"));
                    e.bankDeposit(attacker.getName(), config.getDouble("Default-bounty.limit"));
                    if (api.getJob(attacker) == "assassin") {
                        api.createGuardBounty(attacker);
                    }
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (api.getJob(onlinePlayer) == "assassin" || api.getJob(onlinePlayer) == "guard") {
                            onlinePlayer.sendMessage(ChatColor.GREEN + attacker.getName() + " has just killed " + v + " and recieved " + config.getDouble("Default-bounty-limit"));
                        }
                    }
                }
            } else {
                if (api.getJob(attacker) != "assassin" && api.getJob(attacker) != "guard") {
                    event.setCancelled(true);
                    attacker.sendMessage(ChatColor.GREEN + "[hSkyrim] You are not allowed to PVP as you aren't an assassin or guard");
                    Economy econ = hSkyrim.getEconomy();
                    // take money --> econ.withdrawPlayer(playername, amount);
                    // give money --> econ.depositPlayer(playername, amount);
                }
                if (api.getJob(attacker) == "guard") {
                    if (victim.getHealthScale() == 0) {
                        if (api.alreadyHasGuardBounty(victim)) {
                            api.removeGuardBounty(victim);

                            Economy e = hSkyrim.getEconomy();
                            e.bankDeposit(attacker.getName(), config.getInt("Default-bounty-reward"));
                            attacker.sendMessage(ChatColor.GREEN + "[hSkyrim] You received a bounty of $" + config.getInt("Default-bounty-reward") + " for killing " + victim.getName() + "!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (api.alreadyHasGuardBounty(p) && api.getJob(p) != "guard") {
            for (Player guards : Bukkit.getOnlinePlayers()) {
                if (api.getJob(guards) == "guard") {
                    guards.sendMessage(ChatColor.GREEN + "[hSkyrim] " + p.getDisplayName() + " has a guard bounty! Kill them to receive the reward!");
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().getBlock().equals(Material.WATER)) {
            if (api.getRace(event.getPlayer()).equals("argonian")) {
                event.getPlayer().setRemainingAir(20);
            }
        }
    }

}