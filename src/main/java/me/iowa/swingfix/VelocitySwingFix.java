package me.iowa.swingfix;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.iowa.swingfix.util.PluginMessageUtil;

@Plugin(id = "velocityswingfix", name = "VelocitySwingFix", version = "1.0.0-SNAPSHOT", authors = {"PvP Land Development"})
public class VelocitySwingFix {
    private final ProxyServer server;

    @Inject
    public VelocitySwingFix(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Do some operation demanding access to the Velocity API here.
        // For instance, we could register an event:
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from(PluginMessageUtil.LUNAR_PM_CHANNEL));
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from(PluginMessageUtil.BLC_CHANNEL));
    }

    @Subscribe
    public void onServerConnect(ServerConnectedEvent event) {
        // Lunar has to be delayed otherwise its server rule settings are reset by the vanilla server's REGISTER payload.
        server.getScheduler().buildTask(this, () -> {
            if (event.getPlayer() == null) {
                return;
            }

            // Lunar SwingFix
            event.getPlayer().sendPluginMessage(() -> "REGISTER", PluginMessageUtil.LUNAR_PM_CHANNEL.getBytes(StandardCharsets.UTF_8));
            event.getPlayer().sendPluginMessage(() -> PluginMessageUtil.LUNAR_PM_CHANNEL, PluginMessageUtil.LUNAR_PACKET_BYTES);

            // BLC SwingFix
            event.getPlayer().sendPluginMessage(() -> PluginMessageUtil.BLC_CHANNEL, PluginMessageUtil.BLC_PACKET_BYTES);
        }).delay(1, TimeUnit.SECONDS).schedule();
    }
}
