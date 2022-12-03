package it.cnr.iit.jscontact.tools.dto.utils;

import it.cnr.iit.jscontact.tools.dto.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Comparator;

@Builder
@AllArgsConstructor
public class JSCardAddressesComparator implements Comparator<Address> {

    private String defaultLanguage;

    @Override
    public int compare(Address o1, Address o2) {

        String altid1 = o1.getAltid();
        String altid2 = o2.getAltid();

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

                String language1 = o1.getLanguage();
                String language2 = o2.getLanguage();

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
