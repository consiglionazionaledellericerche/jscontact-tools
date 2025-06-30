package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

public class VersionUtils {

    @AllArgsConstructor
    public enum VersionEnum {

        VERSION_1_0("1.0"),
        VERSION_2_0("2.0");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static VersionEnum getEnum(String value) throws IllegalArgumentException {
            return (value == null) ? null : EnumUtils.getEnum(VersionEnum.class, value);
        }

        @Override
        public String toString() {
            return value;
        }

    }

    private static VersionEnum defaultVersion = VersionEnum.VERSION_2_0;

    private static VersionEnum defaultVersionForRdap = VersionEnum.VERSION_2_0;

    /**
     * Returns the default JSContact version.
     * @return default JSContact version as string
     */
    public static String getDefaultVersion() {
        return defaultVersion.getValue();
    }

    /**
     * Returns the default JSContact version.
     * @return default JSContact version as enum
     */
    public static VersionEnum getDefaultVersionEnum() {
        return defaultVersion;
    }

    /**
     * Sets the default JSContact version.
     * @param version the JSContact version enum to use as default
     */
    public static void setDefaultVersion(VersionEnum version) {
        defaultVersion = version;
    }

    /**
     * Returns the default JSContact version for RDAP
     * @return default JSContact version for RDAP as string
     */
    public static String getDefaultVersionForRdap() {
        return defaultVersionForRdap.getValue();
    }

    /**
     * Returns the default JSContact version for RDAP
     * @return default JSContact version for RDAP as enum
     */
    public static VersionEnum getDefaultVersionEnumForRdap() {
        return defaultVersionForRdap;
    }

    /**
     * Sets the default JSContact version for RDAP.
     * @param version the JSContact version enum to use for RDAP as default
     */
    public static void setDefaultVersionForRdap(VersionEnum version) {
        defaultVersionForRdap = version;
    }

}
