package it.cnr.iit.jscontact.tools.dto.comparators;

import it.cnr.iit.jscontact.tools.dto.Address;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;

@SuperBuilder
public class JSCardAddressesComparator extends AbstractAltidComparator implements Comparator<Address> {

    @Override
    public int compare(Address o1, Address o2) {

        String altid1 = o1.getAltid();
        String altid2 = o2.getAltid();
        String language1 = o1.getLanguage();
        String language2 = o2.getLanguage();

        return compare(altid1, altid2, language1, language2);
    }
}
