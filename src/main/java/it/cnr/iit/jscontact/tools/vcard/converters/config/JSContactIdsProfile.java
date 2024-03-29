package it.cnr.iit.jscontact.tools.vcard.converters.config;

import it.cnr.iit.jscontact.tools.dto.ResourceType;
import it.cnr.iit.jscontact.tools.dto.PersonalInfoEnum;
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
public class JSContactIdsProfile {

    public enum IdType {
        NICKNAME,
        ORGANIZATION,
        TITLE,
        EMAIL,
        ADDRESS,
        PHONE,
        LANGUAGE,
        SCHEDULING,
        RESOURCE,
        ONLINE_SERVICE,
        ANNIVERSARY,
        PERSONAL_INFO,
        PRONOUNS,
        NOTE
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class JSContactId {

        IdType idType;
        Object id;

        private static JSContactId jsContactId(IdType idType, Object id) { return JSContactId.builder().idType(idType).id(id).build(); }
        public static JSContactId nicknamesId(String id) { return jsContactId(IdType.NICKNAME, id); }
        public static JSContactId organizationsId(String id) { return jsContactId(IdType.ORGANIZATION, id); }
        public static JSContactId titlesId(String id) { return jsContactId(IdType.TITLE,id); }
        public static JSContactId emailsId(String id) { return jsContactId(IdType.EMAIL,id); }
        public static JSContactId addressesId(String id) { return jsContactId(IdType.ADDRESS,id); }
        public static JSContactId phonesId(String id) { return jsContactId(IdType.PHONE,id); }
        public static JSContactId languagesId(String id) { return jsContactId(IdType.LANGUAGE,id); }
        public static JSContactId schedulingAddressId(String id) { return jsContactId(IdType.SCHEDULING,id); }
        public static JSContactId onlineServicesId(String id) { return jsContactId(IdType.ONLINE_SERVICE,id); }
        public static JSContactId resourcesId(ResourceId id) { return jsContactId(IdType.RESOURCE,id); }
        public static JSContactId anniversariesId(String id) { return jsContactId(IdType.ANNIVERSARY,id); }
        public static JSContactId personalInfosId(PersonalInfoId id) { return jsContactId(IdType.PERSONAL_INFO,id); }
        public static JSContactId pronounsId(String id) { return jsContactId(IdType.PRONOUNS,id); }
        public static JSContactId notesId(String id) { return jsContactId(IdType.NOTE, id); }
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
        public static ResourceId directoriesId(String id) { return resourceId(ResourceType.DIRECTORY,id);}
        public static ResourceId freeBusysId(String id) { return resourceId(ResourceType.FREEBUSY,id);}
        public static ResourceId linksId(String id) { return resourceId(ResourceType.LINK,id);}
        public static ResourceId contactsId(String id) { return resourceId(ResourceType.CONTACT,id);}
        public static ResourceId calsId(String id) { return resourceId(ResourceType.CALENDAR,id);}
        public static ResourceId entriesId(String id) { return resourceId(ResourceType.ENTRY,id);}
    }


    @Builder
    @Data
    @AllArgsConstructor
    public static class PersonalInfoId {

        PersonalInfoEnum personalInfoEnum;
        String id;

        private static PersonalInfoId personalInfoId(PersonalInfoEnum type, String id) {
            return PersonalInfoId.builder().personalInfoEnum(type).id(id).build();
        }

        public static PersonalInfoId hobbiesId(String id) {
            return personalInfoId(PersonalInfoEnum.HOBBY, id);
        }

        public static PersonalInfoId interestsId(String id) {
            return personalInfoId(PersonalInfoEnum.INTEREST, id);
        }

        public static PersonalInfoId expertisesId(String id) {
            return personalInfoId(PersonalInfoEnum.EXPERTISE, id);
        }

        public static PersonalInfoId otherPersonalInfosId(String id) {
            return personalInfoId(null, id);
        }
    }

    @Singular(ignoreNullCollections = true)
    List<JSContactId> ids;

}
