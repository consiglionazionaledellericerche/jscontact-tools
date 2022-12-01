package it.cnr.iit.jscontact.tools.dto.utils;

import ezvcard.property.VCardProperty;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.util.Comparator;

@Builder
@AllArgsConstructor
public class VCardPropertiesComparator implements Comparator<VCardProperty> {

    private String defaultLanguage;

    @Override
    public int compare(VCardProperty o1, VCardProperty o2) {

        String altid1 = o1.getParameter(VCardParamEnum.ALTID.getValue());
        String altid2 = o2.getParameter(VCardParamEnum.ALTID.getValue());

        if (altid1 == null && altid2 == null)
            return 0;
        else if (altid1 == null && altid2 != null)
            return -1;
        else if (altid1 != null && altid2 == null)
            return 1;
        else {
            int altidInt1 = Integer.parseInt(altid1);
            int altidInt2 = Integer.parseInt(altid2);

            if (altidInt1 != altidInt2)
                return Integer.compare(altidInt1,altidInt2);
            else {

                String language1 = o1.getParameter(VCardParamEnum.LANGUAGE.getValue());
                String language2 = o2.getParameter(VCardParamEnum.LANGUAGE.getValue());

                if (language1 == null && language2 == null)
                    return 0;
                else if (language1 == null && language2 != null)
                    return -1;
                else if (language1 != null && language2 == null)
                    return 1;
                else if (defaultLanguage == null)
                    return 0;
                else {
                    if (language1.equalsIgnoreCase(defaultLanguage))
                        return -1;
                    else if (language2.equalsIgnoreCase(defaultLanguage))
                        return 1;
                    else
                        return 0;
                }
            }
        }
    }
}
