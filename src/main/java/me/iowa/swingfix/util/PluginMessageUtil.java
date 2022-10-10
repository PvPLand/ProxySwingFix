package me.iowa.swingfix.util;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;

public class PluginMessageUtil {
    public static final String LUNAR_PM_CHANNEL = "lunarclient:pm";
    public static final String BLC_CHANNEL = "badlion:modapi";

    public static final byte[] LUNAR_PACKET_BYTES;
    public static final byte[] BLC_PACKET_BYTES;

    static {
        // Default values required below otherwise the packet isn't constructed correctly?
        ByteBuf byteBuf = Unpooled.buffer();
        ByteBufUtil.writeVarInt(10, byteBuf);
        ByteBufUtil.writeString("legacyCombat", byteBuf);
        byteBuf.writeBoolean(true);
        byteBuf.writeInt(0); // Default Int Value
        byteBuf.writeFloat(0); // Default Float Value
        ByteBufUtil.writeString("", byteBuf); // Default String Value
        LUNAR_PACKET_BYTES = byteBuf.array();
        byteBuf.release();

        JsonObject finalJson = new JsonObject(), animations = new JsonObject(), removeHitDelay = new JsonObject(), extraData = new JsonObject();
        animations.addProperty("disabled", false);
        removeHitDelay.addProperty("forced", true);
        extraData.add("removeHitDelay", removeHitDelay);
        animations.add("extra_data", extraData);
        finalJson.add("modsDisallowed", animations);

        BLC_PACKET_BYTES = finalJson.toString().getBytes(StandardCharsets.UTF_8);
    }
}
