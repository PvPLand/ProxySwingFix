package land.pvp.swingfix;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.DefinedPacket;

public final class BungeeSwingFix extends Plugin implements Listener {
    public static final String LUNAR_PM_CHANNEL = "lunarclient:pm";
    public static final String BLC_CHANNEL = "badlion:modapi";

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getProxy().registerChannel(BungeeSwingFix.LUNAR_PM_CHANNEL);
        this.getProxy().registerChannel(BungeeSwingFix.BLC_CHANNEL);

        this.getProxy().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        getProxy().getScheduler().schedule(this, () -> {
            // Lunar SwingFix
            ByteBuf byteBuf = Unpooled.buffer();
            DefinedPacket.writeVarInt(10, byteBuf); // Packet ID
            DefinedPacket.writeString("legacyCombat", byteBuf); // Server Rule
            byteBuf.writeBoolean(true); // Value
            byteBuf.writeInt(0); // Default Int Value
            byteBuf.writeFloat(0); // Default Float Value
            DefinedPacket.writeString("", byteBuf); // Default String Value
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            event.getPlayer().sendData(BungeeSwingFix.LUNAR_PM_CHANNEL, bytes);
            byteBuf.release();

            // Badlion SwingFix (Thanks vaperion for letting me skid)
            JsonObject finalJson = new JsonObject(), animations = new JsonObject(), removeHitDelay = new JsonObject(), extraData = new JsonObject();
            animations.addProperty("disabled", false);
            removeHitDelay.addProperty("forced", true);
            extraData.add("removeHitDelay", removeHitDelay);
            animations.add("extra_data", extraData);
            finalJson.add("modsDisallowed", animations);

            event.getPlayer().sendData(BungeeSwingFix.BLC_CHANNEL, finalJson.toString().getBytes(StandardCharsets.UTF_8));
        }, 1, TimeUnit.SECONDS);
    }
}
