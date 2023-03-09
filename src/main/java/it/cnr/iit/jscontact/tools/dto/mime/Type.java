package it.cnr.iit.jscontact.tools.dto.mime;

import lombok.Builder;

@Builder
public class Type {

    @Builder.Default
    String typeParam = "card";
    @Builder.Default
    String versionParam = "1.0";

    @Override
    public String toString() {
        return String.format("application/jscontact+json;type=%s;version=%s", typeParam, versionParam);
    }

    public String toStringNoParams() {
        return "application/jscontact+json";
    }

}
