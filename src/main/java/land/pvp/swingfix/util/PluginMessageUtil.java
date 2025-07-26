package land.pvp.swingfix.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;

public class PluginMessageUtil {
    public static final String LUNAR_CHANNEL = "lunarclient:pm";
    public static final String LUNAR_APOLLO_CHANNEL = "apollo:json";
    public static final String BLC_CHANNEL = "badlion:modapi";
    public static final String ANIMATIUM_CHANNEL = "animatium:set_features";

    public static final byte[] LUNAR_PACKET_BYTES;
    public static final byte[] BLC_PACKET_BYTES;
    public static final byte[] APOLLO_PACKET_BYTES;
    public static final byte[] ANIMATIUM_PACKET_BYTES;

    static {
        // Default values required below otherwise the packet isn't constructed correctly?
        ByteBuf byteBuf = Unpooled.buffer();
        ByteBufUtil.writeVarInt(10, byteBuf);
        ByteBufUtil.writeString("legacyCombat", byteBuf);
        byteBuf.writeBoolean(true);
        byteBuf.writeInt(0); // Default Int Value
        byteBuf.writeFloat(0); // Default Float Value
        ByteBufUtil.writeString("", byteBuf); // Default String Value
        LUNAR_PACKET_BYTES = ByteBufUtil.copy(byteBuf);
        byteBuf.clear();

        ByteBufUtil.writeVarInt(1, byteBuf);
        Utf8String.write(byteBuf, "miss_penalty", "miss_penalty".length());
        ANIMATIUM_PACKET_BYTES = ByteBufUtil.copy(byteBuf);
        byteBuf.release();

        JsonObject finalJson = new JsonObject();
        JsonObject modsDisallowed = new JsonObject();
        JsonObject animations = new JsonObject();
        JsonObject removeHitDelay = new JsonObject();
        JsonObject extraData = new JsonObject();
        animations.addProperty("disabled", false);
        removeHitDelay.addProperty("forced", true);
        extraData.add("removeHitDelay", removeHitDelay);
        animations.add("extra_data", extraData);
        modsDisallowed.add("Animations", animations);
        finalJson.add("modsDisallowed", modsDisallowed);

        BLC_PACKET_BYTES = finalJson.toString().getBytes(StandardCharsets.UTF_8);

        JsonArray settings = new JsonArray();

        JsonObject combat = new JsonObject();
        combat.addProperty("apollo_module", "combat");
        combat.addProperty("enable", true);

        JsonObject properties = new JsonObject();
        properties.add("disable-miss-penalty", new JsonPrimitive(true));
        combat.add("properties", properties);

        settings.add(combat);

        JsonObject message = new JsonObject();
        message.addProperty("@type", "type.googleapis.com/lunarclient.apollo.configurable.v1.OverrideConfigurableSettingsMessage");
        message.add("configurable_settings", settings);

        APOLLO_PACKET_BYTES = message.toString().getBytes(StandardCharsets.UTF_8);
    }
}
