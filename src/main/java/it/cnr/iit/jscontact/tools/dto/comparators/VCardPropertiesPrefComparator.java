package it.cnr.iit.jscontact.tools.dto.comparators;

import ezvcard.property.VCardProperty;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Comparator;

@Builder
@AllArgsConstructor
public class VCardPropertiesPrefComparator implements Comparator<VCardProperty> {

    @Override
    public int compare(VCardProperty o1, VCardProperty o2) {

        String pref1 = o1.getParameter(VCardParamEnum.PREF.getValue());
        String pref2 = o2.getParameter(VCardParamEnum.PREF.getValue());

        if (pref1 == null && pref2 == null)
            return 0;
        else if (pref1 == null && pref2 != null)
            return 1;
        else if (pref2 == null)
            return -1;
        else
            return Integer.compare(Integer.parseInt(pref1), Integer.parseInt(pref2));
    }
}
