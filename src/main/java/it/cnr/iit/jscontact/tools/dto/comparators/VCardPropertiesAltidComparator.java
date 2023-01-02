package it.cnr.iit.jscontact.tools.dto.comparators;

import ezvcard.property.VCardProperty;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;

@SuperBuilder
public class VCardPropertiesAltidComparator extends AbstractAltidComparator implements Comparator<VCardProperty> {

    @Override
    public int compare(VCardProperty o1, VCardProperty o2) {

        String altid1 = o1.getParameter(VCardParamEnum.ALTID.getValue());
        String altid2 = o2.getParameter(VCardParamEnum.ALTID.getValue());
        String language1 = o1.getParameter(VCardParamEnum.LANGUAGE.getValue());
        String language2 = o2.getParameter(VCardParamEnum.LANGUAGE.getValue());

        return compare(altid1, altid2, language1, language2);
    }
}
