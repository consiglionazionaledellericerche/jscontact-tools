package it.cnr.iit.jscontact.tools.vcard.extensions.property;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarning;
import ezvcard.property.HasAltId;
import ezvcard.property.SortString;
import ezvcard.property.StructuredName;
import ezvcard.property.VCardProperty;

public class ExtendedStructuredName extends VCardProperty implements HasAltId {
	private final List<String> familyNames;
	private String given;
	private final List<String> additional;
	private final List<String> prefixes;
	private final List<String> suffixes;
	private final List<String> surname2;
	private final List<String> generation;


	public ExtendedStructuredName() {
		familyNames = new ArrayList<>();
		additional = new ArrayList<>();
		prefixes = new ArrayList<>();
		suffixes = new ArrayList<>();
		surname2 = new ArrayList<>();
		generation = new ArrayList<>();
	}

	/**
	 * Copy constructor.
	 * @param original the property to make a copy of
	 */
	public ExtendedStructuredName(ExtendedStructuredName original) {
		super(original);
		familyNames = new ArrayList<>(original.familyNames);
		given = original.given;
		additional = new ArrayList<>(original.additional);
		prefixes = new ArrayList<>(original.prefixes);
		suffixes = new ArrayList<>(original.suffixes);
		surname2 = new ArrayList<>(original.surname2);
		generation = new ArrayList<>(original.generation);
	}

	/**
	 * Gets the family name (aka "surname" or "last name" and "secondary surname").
	 * @return the family name or null if not set
	 */
	public List<String> getFamilyNames() {
		return familyNames;
	}

	/**
	 * Gets the family name (aka "surname" or "last name" and "secondary surname").
	 * @return the family name or null if not set
	 */
	public String getFamily() {
		return (familyNames.isEmpty()) ? null : familyNames.get(0);
	}


	/**
	 * Sets the family name (aka "surname" or "last name" and "secondary surname").
	 * @param familyNames the family names or null to remove
	 */
	public void setFamilyNames(List<String> familyNames) {
		this.familyNames.addAll(familyNames);
	}

	/**
	 * Sets the family names (aka "surname" or "last name" and "secondary surname").
	 * @param family the family name or null to remove
	 */
	public void setFamily(String family) {
		this.familyNames.add(family);
	}

	/**
	 * Gets the given name (aka "first name").
	 * @return the given name or null if not set
	 */
	public String getGiven() {
		return given;
	}

	/**
	 * Sets the given name (aka "first name").
	 * @param given the given name or null to remove
	 */
	public void setGiven(String given) {
		this.given = given;
	}

	/**
	 * Gets the list that stores additional names the person goes by (for
	 * example, a middle name).
	 * @return the additional names (this list is mutable)
	 */
	public List<String> getAdditionalNames() {
		return additional;
	}

	/**
	 * Gets the list that stores the person's honorific prefixes.
	 * @return the prefixes (e.g. "Dr.", "Mr.") (this list is mutable)
	 */
	public List<String> getPrefixes() {
		return prefixes;
	}

	/**
	 * Gets the list that stores the person's honorary suffixes.
	 * @return the suffixes (e.g. "M.D.", "Jr.") (this list is mutable)
	 */
	public List<String> getSuffixes() {
		return suffixes;
	}

	/**
	 * Gets the list that stores the person's secondary surnames.
	 * @return the secondary surname (this list is mutable)
	 */
	public List<String> getSurname2() {
		return surname2;
	}

	/**
	 * Gets the list that stores the person's generations.
	 * @return the generation (this list is mutable)
	 */
	public List<String> getGeneration() {
		return generation;
	}

	/**
	 * <p>
	 * Gets the list that holds string(s) which define how to sort the vCard.
	 * </p>
	 * <p>
	 * 2.1 and 3.0 vCards should use the {@link SortString} property instead.
	 * </p>
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @return the sort string(s) (this list is mutable). For example, if the
	 * family name is "d'Aboville" and the given name is "Christine", the sort
	 * strings might be ["Aboville", "Christine"].
	 */
	public List<String> getSortAs() {
		return parameters.getSortAs();
	}

	/**
	 * <p>
	 * Defines a sortable version of the person's name.
	 * </p>
	 * <p>
	 * 2.1 and 3.0 vCards should use the {@link SortString} property instead.
	 * </p>
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @param family the sortable version of the family name (for example,
	 * "Adboville" if the family name is "d'Aboville") or null to remove
	 */
	public void setSortAs(String family) {
		parameters.setSortAs(family);
	}

	/**
	 * <p>
	 * Defines a sortable version of the person's name.
	 * </p>
	 * <p>
	 * 2.1 and 3.0 vCards should use the {@link SortString} property instead.
	 * </p>
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @param family the sortable version of the family name (for example,
	 * "Adboville" if the family name is "d'Aboville")
	 * @param given the sortable version of the given name
	 */
	public void setSortAs(String family, String given) {
		parameters.setSortAs(family, given);
	}

	public String getLanguage() {
		return parameters.getLanguage();
	}

	public void setLanguage(String language) {
		parameters.setLanguage(language);
	}

	//@Override
	public String getAltId() {
		return parameters.getAltId();
	}

	//@Override
	public void setAltId(String altId) {
		parameters.setAltId(altId);
	}

	@Override
	protected Map<String, Object> toStringValues() {
		Map<String, Object> values = new LinkedHashMap<>();
		values.put("family", familyNames);
		values.put("given", given);
		values.put("additional", additional);
		values.put("prefixes", prefixes);
		values.put("suffixes", suffixes);
		values.put("surname", surname2);
		values.put("generation", generation);
		return values;
	}

	@Override
	protected void _validate(List<ValidationWarning> warnings, VCardVersion version, VCard vcard) {
		/*
		 * 2.1 does not allow multi-valued components.
		 */
		if (version == VCardVersion.V2_1) {
			//@formatter:off
			if (additional.size() > 1 ||
				prefixes.size() > 1 ||
				suffixes.size() > 1 ||
			    surname2.size() > 1 ||
			    generation.size() > 1) {
				warnings.add(new ValidationWarning(34));
			}
			//@formatter:on
		}
	}

	@Override
	public ExtendedStructuredName copy() {
		return new ExtendedStructuredName(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + additional.hashCode();
		result = prime * result + familyNames.hashCode();
		result = prime * result + ((given == null) ? 0 : given.hashCode());
		result = prime * result + prefixes.hashCode();
		result = prime * result + suffixes.hashCode();
		result = prime * result + surname2.hashCode();
		result = prime * result + generation.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		ExtendedStructuredName other = (ExtendedStructuredName) obj;
		if (!additional.equals(other.additional)) return false;
		if (!familyNames.equals(other.familyNames)) return false;
		if (given == null) {
			if (other.given != null) return false;
		} else if (!given.equals(other.given)) return false;
		if (!prefixes.equals(other.prefixes)) return false;
		if (!suffixes.equals(other.suffixes)) return false;
		if (!surname2.equals(other.surname2)) return false;
		return generation.equals(other.generation);
	}

	public boolean equalsStructuredName(StructuredName sn) {

		if (sn == null) return false;

		if (!additional.equals(sn.getAdditionalNames())) return false;
		if (familyNames.size() != 1 || !familyNames.get(0).equals(sn.getFamily())) return false;
		if (given == null) {
			if (sn.getGiven() != null) return false;
		} else if (!given.equals(sn.getGiven())) return false;
		if (!prefixes.equals(sn.getPrefixes())) return false;
		return suffixes.equals(sn.getSuffixes());
	}
}