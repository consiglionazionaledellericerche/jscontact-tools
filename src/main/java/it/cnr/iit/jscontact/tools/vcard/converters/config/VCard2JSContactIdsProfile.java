package it.cnr.iit.jscontact.tools.vcard.converters.config;

import it.cnr.iit.jscontact.tools.dto.ResourceType;
import it.cnr.iit.jscontact.tools.dto.PersonalInformationType;
import lombok.*;

import java.util.List;

/**
 * Abstract class for configuring the JSContact Ids profile.
 *
 * @author Mario Loffredo
 */
@Data
@Builder
@AllArgsConstructor
public class VCard2JSContactIdsProfile {

    public enum IdType {
        ORGANIZATION,
        TITLE,
        EMAIL,
        ADDRESS,
        PHONE,
        PHOTO,
        SCHEDULING,
        RESOURCE,
        ONLINE_SERVICE,
        ANNIVERSARY,
        PERSONAL_INFO
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class JSContactId {

        IdType idType;
        Object id;

        private static JSContactId jsContactId(IdType idType, Object id) { return JSContactId.builder().idType(idType).id(id).build(); }
        public static JSContactId organizationsId(String id) { return jsContactId(IdType.ORGANIZATION, id); }
        public static JSContactId titlesId(String id) { return jsContactId(IdType.TITLE,id); }
        public static JSContactId emailsId(String id) { return jsContactId(IdType.EMAIL,id); }
        public static JSContactId addressesId(String id) { return jsContactId(IdType.ADDRESS,id); }
        public static JSContactId phonesId(String id) { return jsContactId(IdType.PHONE,id); }
        public static JSContactId photosId(String id) { return jsContactId(IdType.PHOTO,id); }
        public static JSContactId schedulingId(String id) { return jsContactId(IdType.SCHEDULING,id); }
        public static JSContactId onlineServicesId(String id) { return jsContactId(IdType.ONLINE_SERVICE,id); }
        public static JSContactId resourcesId(ResourceId id) { return jsContactId(IdType.RESOURCE,id); }
        public static JSContactId anniversariesId(String id) { return jsContactId(IdType.ANNIVERSARY,id); }
        public static JSContactId personalInfosId(PersonalInfoId id) { return jsContactId(IdType.PERSONAL_INFO,id); }
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class ResourceId {

        ResourceType type;
        String id;

        private static ResourceId resourceId(ResourceType type, String id) { return ResourceId.builder().type(type).id(id).build(); }
        public static ResourceId keysId(String id) { return resourceId(ResourceType.KEY,id);}
        public static ResourceId logosId(String id) { return resourceId(ResourceType.LOGO,id);}
        public static ResourceId soundsId(String id) { return resourceId(ResourceType.SOUND,id);}
        public static ResourceId orgDirectoriesId(String id) { return resourceId(ResourceType.ORG_DIRECTORY,id);}
        public static ResourceId fbUrlsId(String id) { return resourceId(ResourceType.FBURL,id);}
        public static ResourceId urlsId(String id) { return resourceId(ResourceType.URI,id);}
        public static ResourceId contactUrisId(String id) { return resourceId(ResourceType.CONTACT_URI,id);}
        public static ResourceId calurisId(String id) { return resourceId(ResourceType.CALURI,id);}
        public static ResourceId sourcesId(String id) { return resourceId(ResourceType.SOURCE,id);}
    }


    @Builder
    @Data
    @AllArgsConstructor
    public static class PersonalInfoId {

        PersonalInformationType type;
        String id;

        private static PersonalInfoId personalInfoId(PersonalInformationType type, String id) { return PersonalInfoId.builder().type(type).id(id).build(); }
        public static PersonalInfoId hobbiesId(String id) { return personalInfoId(PersonalInformationType.HOBBY,id);}
        public static PersonalInfoId interestsId(String id) { return personalInfoId(PersonalInformationType.INTEREST,id);}
        public static PersonalInfoId expertisesId(String id) { return personalInfoId(PersonalInformationType.EXPERTISE,id);}
        public static PersonalInfoId otherPersonalInfosId(String id) { return personalInfoId(null,id);}
    }


    public static final VCard2JSContactIdsProfile RDAP_PROFILE = VCard2JSContactIdsProfile.builder()
                                                                                    .id(JSContactId.organizationsId("org"))
                                                                                    .id(JSContactId.emailsId("email"))
                                                                                    .id(JSContactId.phonesId("voice"))  // 1st jCard phone number
                                                                                    .id(JSContactId.phonesId("fax"))    // 2nd jCard phone number
                                                                                    .id(JSContactId.addressesId("int")) // 1st jCard address
                                                                                    .id(JSContactId.addressesId("loc")) // 2nd jCard address
                                                                                    .build();

    @Singular(ignoreNullCollections = true)
    List<JSContactId> ids;

}
