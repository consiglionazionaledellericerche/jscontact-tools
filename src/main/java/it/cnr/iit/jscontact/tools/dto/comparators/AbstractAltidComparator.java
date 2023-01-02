package it.cnr.iit.jscontact.tools.dto.comparators;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAltidComparator {

    String defaultLanguage;

    protected int compare(String altid1, String altid2, String language1, String language2) {

        if (altid1 == null && altid2 == null)
            return 0;
        else if (altid1 == null && altid2 != null)
            return -1;
        else if (altid2 == null)
            return 1;
        else {
            int altidInt1 = Integer.parseInt(altid1);
            int altidInt2 = Integer.parseInt(altid2);

            if (altidInt1 != altidInt2)
                return Integer.compare(altidInt1, altidInt2);
            else {

                if (language1 == null && language2 == null)
                    return 0;
                else if (language1 == null && language2 != null)
                    return -1;
                else if (language2 == null)
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
