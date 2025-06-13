package it.cnr.iit.jscontact.tools.dto.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import it.cnr.iit.jscontact.tools.dto.AnniversaryEnum;
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


    public static String getDefaultVersion() {
        return defaultVersion.getValue();
    }

    public static void setDefaultVersion(VersionEnum version) {
        defaultVersion = version;
    }

    public static String getDefaultVersionForRdap() {
        return defaultVersionForRdap.getValue();
    }
    public static void setDefaultVersionForRdap(VersionEnum version) {
        defaultVersionForRdap = version;
    }

}
