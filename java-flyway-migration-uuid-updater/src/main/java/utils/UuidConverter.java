package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UuidConverter {

    public static UUID createUuid(String data) {
        return UUID.nameUUIDFromBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] convertUuid(UUID uuid, byte[] bytes) {

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }
}
