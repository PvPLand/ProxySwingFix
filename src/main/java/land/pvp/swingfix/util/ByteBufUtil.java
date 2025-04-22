package land.pvp.swingfix.util;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

// net.md_5.bungee.protocol.DefinedPacket
public class ByteBufUtil {
    public static void writeVarInt(int value, ByteBuf output) {
        int part;
        do {
            part = value & 0x7F;

            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }

            output.writeByte(part);

        } while (value != 0);
    }

    public static void writeString(String s, ByteBuf buf) {
        ByteBufUtil.writeString(s, buf, Short.MAX_VALUE);
    }

    public static void writeString(String s, ByteBuf buf, int maxLength) {
        if (s.length() > maxLength) {
            throw new RuntimeException("Cannot send string longer than " + maxLength + " (got " + s.length() + " characters)");
        }

        byte[] b = s.getBytes(Charsets.UTF_8);
        if (b.length > maxLength * 3) {
            throw new RuntimeException("Cannot send string longer than " + (maxLength * 3) + " (got " + b.length + " bytes)");
        }

        ByteBufUtil.writeVarInt(b.length, buf);
        buf.writeBytes(b);
    }

    public static byte[] copy(ByteBuf buf) {
        byte[] copy = new byte[buf.readableBytes()];
        buf.markReaderIndex();
        buf.readBytes(copy);
        buf.resetReaderIndex();
        return copy;
    }
}
