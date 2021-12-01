package it.cnr.iit.jscontact.tools.vcard.converters.config;

import it.cnr.iit.jscontact.tools.dto.OnlineLabelKey;
import it.cnr.iit.jscontact.tools.dto.PersonalInformationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

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
        ONLINE,
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
        public static JSContactId onlinesId(ResourceId id) { return jsContactId(IdType.PHOTO,id); }
        public static JSContactId anniversariesId(String id) { return jsContactId(IdType.ANNIVERSARY,id); }
        public static JSContactId personalInfosId(PersonalInfoId id) { return jsContactId(IdType.PERSONAL_INFO,id); }
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class ResourceId {

        OnlineLabelKey labelKey;
        String id;

        private static ResourceId resourceId(OnlineLabelKey labelKey, String id) { return ResourceId.builder().labelKey(labelKey).id(id).build(); }
        public static ResourceId keysId(String id) { return resourceId(OnlineLabelKey.KEY,id);}
        public static ResourceId logosId(String id) { return resourceId(OnlineLabelKey.LOGO,id);}
        public static ResourceId soundsId(String id) { return resourceId(OnlineLabelKey.SOUND,id);}
        public static ResourceId orgDirectoriesId(String id) { return resourceId(OnlineLabelKey.ORG_DIRECTORY,id);}
        public static ResourceId imppsId(String id) { return resourceId(OnlineLabelKey.IMPP,id);}
        public static ResourceId fbUrlsId(String id) { return resourceId(OnlineLabelKey.FBURL,id);}
        public static ResourceId urlsId(String id) { return resourceId(OnlineLabelKey.URL,id);}
        public static ResourceId contactUrisId(String id) { return resourceId(OnlineLabelKey.CONTACT_URI,id);}
        public static ResourceId caladrUrisId(String id) { return resourceId(OnlineLabelKey.CALADRURI,id);}
        public static ResourceId calurisId(String id) { return resourceId(OnlineLabelKey.CALURI,id);}
        public static ResourceId sourcesId(String id) { return resourceId(OnlineLabelKey.SOURCE,id);}
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
