package land.pvp.swingfix;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import land.pvp.swingfix.util.PluginMessageUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public final class BungeeSwingFix extends Plugin implements Listener {
    @Override
    public void onEnable() {
        this.getProxy().registerChannel(PluginMessageUtil.LUNAR_PM_CHANNEL);
        this.getProxy().registerChannel(PluginMessageUtil.BLC_CHANNEL);

        this.getProxy().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        this.getProxy().unregisterChannel(PluginMessageUtil.LUNAR_PM_CHANNEL);
        this.getProxy().unregisterChannel(PluginMessageUtil.BLC_CHANNEL);
    }

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        // Lunar has to be delayed otherwise its server rule settings are reset by the vanilla server's REGISTER payload.
        this.getProxy().getScheduler().schedule(this, () -> {
            ProxiedPlayer player = event.getPlayer();
            if (player == null) {
                return;
            }

            // Lunar SwingFix
            player.sendData("REGISTER", PluginMessageUtil.LUNAR_PM_CHANNEL.getBytes(StandardCharsets.UTF_8));
            player.sendData(PluginMessageUtil.LUNAR_PM_CHANNEL, PluginMessageUtil.LUNAR_PACKET_BYTES);
            // BLC SwingFix
            player.sendData(PluginMessageUtil.BLC_CHANNEL, PluginMessageUtil.BLC_PACKET_BYTES);
        }, 1, TimeUnit.SECONDS);
    }
}
