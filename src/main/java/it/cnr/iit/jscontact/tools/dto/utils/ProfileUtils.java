package it.cnr.iit.jscontact.tools.dto.utils;

import it.cnr.iit.jscontact.tools.constraints.groups.profiles.Profile_RDAP;
import it.cnr.iit.jscontact.tools.dto.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileUtils {

    private static final Map<String, List<String>> rdapProfileProperties = new HashMap<>() {{
        put(Card.class.getName(), List.of("language","kind","name","organizations","addresses","phones","emails","links","localizations"));
        put(Name.class.getName(), List.of("full","components"));
        put(NameComponent.class.getName(), List.of("kind","value"));
        put(Organization.class.getName(), List.of("name"));
        put(Address.class.getName(), List.of("full","components","countryCode"));
        put(AddressComponent.class.getName(), List.of("kind","value"));
        put(EmailAddress.class.getName(), List.of("address"));
        put(Phone.class.getName(), List.of("number","features"));
        put(Link.class.getName(), List.of("kind","uri"));
    }};

    @Getter
    private static final Map<String, Map> profileProperties = new HashMap<>()
    {{
        put(Profile_RDAP.class.getName(),rdapProfileProperties);
    }};

    @Getter
    private static final Map<String, String> profileNames = new HashMap<>()
    {{
        put(Profile_RDAP.class.getName(),"rdap");
    }};

    @Getter @Setter
    private static String profileName;

    public static void unsetProfileName() {
        profileName = null;
    }

}
