package land.pvp.swingfix;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import land.pvp.swingfix.util.PluginMessageUtil;

@Plugin(id = "velocityswingfix", name = "VelocitySwingFix", version = "1.0.0-SNAPSHOT", authors = {"PvP Land Development"})
public class VelocitySwingFix {
    private final ProxyServer server;

    @Inject
    public VelocitySwingFix(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.server.getChannelRegistrar().register(MinecraftChannelIdentifier.from(PluginMessageUtil.LUNAR_PM_CHANNEL));
        this.server.getChannelRegistrar().register(MinecraftChannelIdentifier.from(PluginMessageUtil.BLC_CHANNEL));
    }

    @Subscribe
    public void onServerConnect(ServerConnectedEvent event) {
        // Lunar has to be delayed otherwise its server rule settings are reset by the vanilla server's REGISTER payload.
        this.server.getScheduler().buildTask(this, () -> {
            Player player = event.getPlayer();
            if (player == null) {
                return;
            }

            // Lunar SwingFix
            player.sendPluginMessage(() -> "REGISTER", PluginMessageUtil.LUNAR_PM_CHANNEL.getBytes(StandardCharsets.UTF_8));
            player.sendPluginMessage(() -> PluginMessageUtil.LUNAR_PM_CHANNEL, PluginMessageUtil.LUNAR_PACKET_BYTES);
            // Lunar (Apollo) SwingFix
            player.sendPluginMessage(() -> PluginMessageUtil.LUNAR_APOLLO_PM_CHANNEL, PluginMessageUtil.APOLLO_PACKET_BYTES);
            // BLC SwingFix
            player.sendPluginMessage(() -> PluginMessageUtil.BLC_CHANNEL, PluginMessageUtil.BLC_PACKET_BYTES);
        }).delay(1, TimeUnit.SECONDS).schedule();
    }
}
