package it.cnr.iit.jscontact.tools.dto.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Utility class for generating UUIDs (V4, V5 and Nil UUIDs) [RFC9562]
 *
 * @see <a href="https://tools.ietf.org/html/rfc9562">RFC9562</a>
 * @author Mario Loffredo
 */
public class UuidUtils {

    private static final String UUID_NAMESPACE_PREFIX = "urn:uuid:";

    private static long getLeastAndMostSignificantBitsVersion5(final byte[] src, final int offset) {
        long ans = 0;
        for (int i = offset + 7; i >= offset; i -= 1) {
            ans <<= 8;
            ans |= src[i] & 0xffL;
        }
        return ans;
    }

    /**
     * Returns a name based V5 UUID as a string value prefixed by the "urn:uuid" namespace.
     * @param name the name the UUID is based upon
     * @return the name based V5 UUID
     * @throws NoSuchAlgorithmException
     */
    public static UUID getNameBasedV5Uuid(String name) throws NoSuchAlgorithmException {

        byte[] bytes = name.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        byte[] hash = md.digest(bytes);

        long msb = getLeastAndMostSignificantBitsVersion5(hash, 0);
        long lsb = getLeastAndMostSignificantBitsVersion5(hash, 8);
        // Set the version field
        msb &= ~(0xfL << 12);
        msb |= 5L << 12;
        // Set the variant field to 2
        lsb &= ~(0x3L << 62);
        lsb |= 2L << 62;
        return new UUID(msb, lsb);

    }

    /**
     * Returns a name based V5 UUID as a string value prefixed by the "urn:uuid" namespace.
     * @param name the name the UUID is based upon
     * @return the name based V5 UUID as a string value
     */
    public static String getNameBasedV5UuidPrefixedByNamespace(String name) {
        return UUID_NAMESPACE_PREFIX + getNameBasedV5UuidPrefixedByNamespace(name);
    }

    /**
     * Returns a random V4 UUID.
     * @return the random V4 UUID
     */
    public static UUID getRandomV4Uuid() {
        return UUID.randomUUID();
    }

    /**
     * Returns a random V4 UUID as a string value prefixed by the "urn:uuid" namespace.
     * @return the random V4 UUID as a string value
     */
    public static String getRandomV4UuidPrefixedByNamespace() {
        return UUID_NAMESPACE_PREFIX + getRandomV4Uuid();
    }

    /**
     * Returns a nil UUID.
     * @return the nil UUID
     */
    public static UUID getNilUuid() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }


    /**
     * Returns a nil UUID as a string value prefixed by the "urn:uuid" namespace.
     * @return the nil UUID as a string value
     */
    public static String getNilUuidPrefixedByNamespace() {
        return UUID_NAMESPACE_PREFIX + getNilUuid();
    }
}
