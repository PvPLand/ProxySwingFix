package land.pvp.swingfix;

import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import land.pvp.swingfix.util.PluginMessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;

public class PaperSwingFix extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginMessageUtil.LUNAR_CHANNEL);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginMessageUtil.LUNAR_APOLLO_CHANNEL);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginMessageUtil.ANIMATIUM_CHANNEL);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginMessageUtil.BLC_CHANNEL);

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.getServer().getScheduler().runTaskLater(this, () -> {
            Player player = event.getPlayer();
            if (!player.isOnline()) {
                return;
            }

            // Lunar SwingFix
            player.sendPluginMessage(this, "REGISTER", PluginMessageUtil.LUNAR_CHANNEL.getBytes(StandardCharsets.UTF_8));
            player.sendPluginMessage(this, PluginMessageUtil.LUNAR_CHANNEL, PluginMessageUtil.LUNAR_PACKET_BYTES);
            // Lunar (Apollo) SwingFix
            player.sendPluginMessage(this, PluginMessageUtil.LUNAR_APOLLO_CHANNEL, PluginMessageUtil.APOLLO_PACKET_BYTES);
            // BLC SwingFix
            player.sendPluginMessage(this, PluginMessageUtil.BLC_CHANNEL, PluginMessageUtil.BLC_PACKET_BYTES);
            // Animatium
            player.sendPluginMessage(this, PluginMessageUtil.ANIMATIUM_CHANNEL, PluginMessageUtil.ANIMATIUM_PACKET_BYTES);
        }, 1L);
    }
}
