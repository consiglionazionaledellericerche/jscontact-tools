package it.cnr.iit.jscontact.tools.dto.utils;

import java.util.Base64;

public class Base64Utils {

    public static String decode(String s) {

        if (s == null) return null;

        byte[] decodedBytes = Base64.getDecoder().decode(s);
        return new String(decodedBytes);
    }

    public static String encode(String s) {

        if (s == null) return null;

        return Base64.getEncoder().encodeToString(s.getBytes());
    }

}
