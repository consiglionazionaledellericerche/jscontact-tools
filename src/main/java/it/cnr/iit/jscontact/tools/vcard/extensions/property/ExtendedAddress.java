package it.cnr.iit.jscontact.tools.vcard.extensions.property;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarning;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.Pid;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.HasAltId;
import ezvcard.property.VCardProperty;
import ezvcard.util.GeoUri;
import ezvcard.util.StringUtils;

public class ExtendedAddress extends VCardProperty implements HasAltId {
	private final List<String> poBoxes;
	private final List<String> extendedAddresses;
	private final List<String> streetAddresses;
	private final List<String> localities;
	private final List<String> regions;
	private final List<String> postalCodes;
	private final List<String> countries;

	private final List<String> rooms;

	private final List<String> apartments;

	private final List<String> floors;

	private final List<String> streetNumbers;

	private final List<String> streetNames;

	private final List<String> buildings;

	private final List<String> blocks;

	private final List<String> subDistricts;

	private final List<String> districts;

	private final List<String> landmarks;

	private final List<String> directions;


	public ExtendedAddress() {
		poBoxes = new ArrayList<>(1);
		extendedAddresses = new ArrayList<>(1);
		streetAddresses = new ArrayList<>(1);
		localities = new ArrayList<>(1);
		regions = new ArrayList<>(1);
		postalCodes = new ArrayList<>(1);
		countries = new ArrayList<>(1);
		rooms = new ArrayList<>(1);
		apartments = new ArrayList<>(1);
		floors = new ArrayList<>(1);
		streetNumbers = new ArrayList<>(1);
		streetNames = new ArrayList<>(1);
		buildings = new ArrayList<>(1);
		blocks = new ArrayList<>(1);
		subDistricts = new ArrayList<>(1);
		districts = new ArrayList<>(1);
		landmarks = new ArrayList<>(1);
		directions = new ArrayList<>(1);
	}

	/**
	 * Copy constructor.
	 * @param original the property to make a copy of
	 */
	public ExtendedAddress(ExtendedAddress original) {
		super(original);
		poBoxes = new ArrayList<>(original.poBoxes);
		extendedAddresses = new ArrayList<>(original.extendedAddresses);
		streetAddresses = new ArrayList<>(original.streetAddresses);
		localities = new ArrayList<>(original.localities);
		regions = new ArrayList<>(original.regions);
		postalCodes = new ArrayList<>(original.postalCodes);
		countries = new ArrayList<>(original.countries);
		rooms = new ArrayList<>(original.rooms);
		apartments = new ArrayList<>(original.apartments);
		floors = new ArrayList<>(original.floors);
		streetNumbers = new ArrayList<>(original.streetNumbers);
		streetNames = new ArrayList<>(original.streetNames);
		buildings = new ArrayList<>(original.buildings);
		blocks = new ArrayList<>(original.blocks);
		subDistricts = new ArrayList<>(original.subDistricts);
		districts = new ArrayList<>(original.districts);
		landmarks = new ArrayList<>(original.landmarks);
		directions = new ArrayList<>(original.directions);
	}

	/**
	 * Gets the P.O. (post office) box.
	 * @return the P.O. box or null if not set
	 */
	public String getPoBox() {
		return first(poBoxes);
	}

	/**
	 * Gets the list that holds the P.O. (post office) boxes that are assigned
	 * to this address. An address is unlikely to have more than one, but it's
	 * possible nonetheless.
	 * @return the P.O. boxes (this list is mutable)
	 */
	public List<String> getPoBoxes() {
		return poBoxes;
	}

	/**
	 * Sets the P.O. (post office) box.
	 * @param poBox the P.O. box or null to remove
	 */
	public void setPoBox(String poBox) {
		set(poBoxes, poBox);
	}

	/**
	 * Gets the extended address.
	 * @return the extended address (e.g. "Suite 200") or null if not set
	 */
	public String getExtendedAddress() {
		return first(extendedAddresses);
	}

	/**
	 * Gets the list that holds the extended addresses that are assigned to this
	 * address. An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the extended addresses (this list is mutable)
	 */
	public List<String> getExtendedAddresses() {
		return extendedAddresses;
	}

	/**
	 * Gets the extended address. Use this method when the ADR property of the
	 * vCard you are parsing contains unescaped comma characters.
	 * @return the extended address or null if not set
	 */
	public String getExtendedAddressFull() {
		return getAddressFull(extendedAddresses);
	}

	/**
	 * Sets the extended address.
	 * @param extendedAddress the extended address (e.g. "Suite 200") or null to
	 * remove
	 */
	public void setExtendedAddress(String extendedAddress) {
		set(extendedAddresses, extendedAddress);
	}

	/**
	 * Gets the street address
	 * @return the street address (e.g. "123 Main St")
	 */
	public String getStreetAddress() {
		return first(streetAddresses);
	}

	/**
	 * Gets the list that holds the street addresses that are assigned to this
	 * address. An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the street addresses (this list is mutable)
	 */
	public List<String> getStreetAddresses() {
		return streetAddresses;
	}

	/**
	 * Gets the street address. Use this method when the ADR property of the
	 * vCard you are parsing contains unescaped comma characters.
	 * @return the street address or null if not set
	 */
	public String getStreetAddressFull() {
		return getAddressFull(streetAddresses);
	}

	/**
	 * Sets the street address.
	 * @param streetAddress the street address (e.g. "123 Main St") or null to
	 * remove
	 */
	public void setStreetAddress(String streetAddress) {
		set(streetAddresses, streetAddress);
	}

	/**
	 * Gets the locality (city)
	 * @return the locality (e.g. "Boston") or null if not set
	 */
	public String getLocality() {
		return first(localities);
	}

	/**
	 * Gets the list that holds the localities that are assigned to this
	 * address. An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the localities (this list is mutable)
	 */
	public List<String> getLocalities() {
		return localities;
	}

	/**
	 * Sets the locality (city).
	 * @param locality the locality or null to remove
	 */
	public void setLocality(String locality) {
		set(localities, locality);
	}

	/**
	 * Gets the region (state).
	 * @return the region (e.g. "Texas") or null if not set
	 */
	public String getRegion() {
		return first(regions);
	}

	/**
	 * Gets the list that holds the regions that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the regions (this list is mutable)
	 */
	public List<String> getRegions() {
		return regions;
	}

	/**
	 * Sets the region (state).
	 * @param region the region (e.g. "Texas") or null to remove
	 */
	public void setRegion(String region) {
		set(regions, region);
	}

	/**
	 * Gets the postal code (zip code).
	 * @return the postal code (e.g. "90210") or null if not set
	 */
	public String getPostalCode() {
		return first(postalCodes);
	}

	/**
	 * Gets the list that holds the postal codes that are assigned to this
	 * address. An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the postal codes (this list is mutable)
	 */
	public List<String> getPostalCodes() {
		return postalCodes;
	}

	/**
	 * Sets the postal code (zip code).
	 * @param postalCode the postal code (e.g. "90210") or null to remove
	 */
	public void setPostalCode(String postalCode) {
		set(postalCodes, postalCode);
	}

	/**
	 * Gets the country.
	 * @return the country (e.g. "USA") or null if not set
	 */
	public String getCountry() {
		return first(countries);
	}

	/**
	 * Gets the list that holds the countries that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the countries (this list is mutable)
	 */
	public List<String> getCountries() {
		return countries;
	}

	/**
	 * Sets the country.
	 * @param country the country (e.g. "USA") or null to remove
	 */
	public void setCountry(String country) {
		set(countries, country);
	}

	/**
	 * Gets the room.
	 * @return the room or null if not set
	 */
	public String getRoom() {
		return first(rooms);
	}

	/**
	 * Gets the list that holds the rooms that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the rooms (this list is mutable)
	 */
	public List<String> getRooms() {
		return rooms;
	}

	/**
	 * Sets the room.
	 * @param room the room or null to remove
	 */
	public void setRoom(String room) {
		set(rooms, room);
	}

	/**
	 * Gets the apartment.
	 * @return the apartment or null if not set
	 */
	public String getApartment() {
		return first(apartments);
	}

	/**
	 * Gets the list that holds the apartments that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the rooms (this list is mutable)
	 */
	public List<String> getApartments() {
		return apartments;
	}

	/**
	 * Sets the apartment.
	 * @param apartment the apartment or null to remove
	 */
	public void setApartment(String apartment) {
		set(apartments, apartment);
	}

	/**
	 * Gets the floor.
	 * @return the floor or null if not set
	 */
	public String getFloor() {
		return first(floors);
	}

	/**
	 * Gets the list that holds the floors that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the floors (this list is mutable)
	 */
	public List<String> getFloors() {
		return floors;
	}

	/**
	 * Sets the floor.
	 * @param floor the floor or null to remove
	 */
	public void setFloor(String floor) {
		set(floors, floor);
	}

	/**
	 * Gets the street number.
	 * @return the street number or null if not set
	 */
	public String getStreetNumber() {
		return first(streetNumbers);
	}

	/**
	 * Gets the list that holds the street numbers that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the street numbers (this list is mutable)
	 */
	public List<String> getStreetNumbers() {
		return streetNumbers;
	}

	/**
	 * Sets the street number.
	 * @param streetNumber the floor or null to remove
	 */
	public void setStreetNumber(String streetNumber) {
		set(streetNumbers, streetNumber);
	}

	/**
	 * Gets the street name.
	 * @return the street name or null if not set
	 */
	public String getStreetName() {
		return first(streetNames);
	}

	/**
	 * Gets the list that holds the street names that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the street names (this list is mutable)
	 */
	public List<String> getStreetNames() {
		return streetNames;
	}

	/**
	 * Sets the street name.
	 * @param streetName the floor or null to remove
	 */
	public void setStreetName(String streetName) {
		set(streetNames, streetName);
	}

	/**
	 * Gets the building.
	 * @return the building or null if not set
	 */
	public String getBuilding() {
		return first(buildings);
	}

	/**
	 * Gets the list that holds the buildings that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the buildings (this list is mutable)
	 */
	public List<String> getBuildings() {
		return buildings;
	}

	/**
	 * Sets the building.
	 * @param building the floor or null to remove
	 */
	public void setBuilding(String building) {
		set(buildings, building);
	}

	/**
	 * Gets the block.
	 * @return the block or null if not set
	 */
	public String getBlock() {
		return first(blocks);
	}

	/**
	 * Gets the list that holds the blocks that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the blocks (this list is mutable)
	 */
	public List<String> getBlocks() {
		return blocks;
	}

	/**
	 * Sets the block.
	 * @param block the floor or null to remove
	 */
	public void setBlock(String block) {
		set(blocks, block);
	}

	/**
	 * Gets the sub district.
	 * @return sub district or null if not set
	 */
	public String getSubDistrict() {
		return first(subDistricts);
	}

	/**
	 * Gets the list that holds the sub districts that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the sub districts (this list is mutable)
	 */
	public List<String> getSubDistricts() {
		return subDistricts;
	}

	/**
	 * Sets the sub district.
	 * @param subDistrict the floor or null to remove
	 */
	public void setSubDistrict(String subDistrict) {
		set(subDistricts, subDistrict);
	}

	/**
	 * Gets the district.
	 * @return district or null if not set
	 */
	public String getDistrict() {
		return first(districts);
	}

	/**
	 * Gets the list that holds the districts that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the districts (this list is mutable)
	 */
	public List<String> getDistricts() {
		return districts;
	}

	/**
	 * Sets the district.
	 * @param district the floor or null to remove
	 */
	public void setDistrict(String district) {
		set(districts, district);
	}

	/**
	 * Gets the landmark.
	 * @return landmark or null if not set
	 */
	public String getLandmark() {
		return first(landmarks);
	}

	/**
	 * Gets the list that holds the landmarks that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the landmarks (this list is mutable)
	 */
	public List<String> getLandmarks() {
		return landmarks;
	}

	/**
	 * Sets the landmark.
	 * @param landmark the floor or null to remove
	 */
	public void setLandmark(String landmark) {
		set(landmarks, landmark);
	}

	/**
	 * Gets the direction.
	 * @return direction or null if not set
	 */
	public String getDirection() {
		return first(directions);
	}

	/**
	 * Gets the list that holds the directions that are assigned to this address.
	 * An address is unlikely to have more than one, but it's possible
	 * nonetheless.
	 * @return the directions (this list is mutable)
	 */
	public List<String> getDirections() {
		return directions;
	}

	/**
	 * Sets the direction.
	 * @param direction the floor or null to remove
	 */
	public void setDirection(String direction) {
		set(directions, direction);
	}

	/**
	 * Gets the list that stores this property's address types (TYPE
	 * parameters).
	 * @return the address types (e.g. "HOME", "WORK") (this list is mutable)
	 */
	public List<AddressType> getTypes() {
		return parameters.new TypeParameterList<AddressType>() {
			@Override
			protected AddressType _asObject(String value) {
				return AddressType.get(value);
			}
		};
	}

	public String getLanguage() {
		return parameters.getLanguage();
	}

	public void setLanguage(String language) {
		parameters.setLanguage(language);
	}

	/**
	 * Gets the label of the address.
	 * @return the label or null if not set
	 */
	public String getLabel() {
		return parameters.getLabel();
	}

	/**
	 * Sets the label of the address.
	 * @param label the label or null to remove
	 */
	public void setLabel(String label) {
		parameters.setLabel(label);
	}

	/**
	 * <p>
	 * Gets the global positioning coordinates that are associated with this
	 * address.
	 * </p>
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @return the geo URI or not if not found
	 * @see VCardParameters#getGeo
	 */
	public GeoUri getGeo() {
		return parameters.getGeo();
	}

	/**
	 * <p>
	 * Sets the global positioning coordinates that are associated with this
	 * address.
	 * </p>
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @param uri the geo URI or null to remove
	 * @see VCardParameters#setGeo
	 */
	public void setGeo(GeoUri uri) {
		parameters.setGeo(uri);
	}

	public List<Pid> getPids() {
		return parameters.getPids();
	}

	public Integer getPref() {
		return parameters.getPref();
	}

	public void setPref(Integer pref) {
		parameters.setPref(pref);
	}

	//@Override
	public String getAltId() {
		return parameters.getAltId();
	}

	//@Override
	public void setAltId(String altId) {
		parameters.setAltId(altId);
	}

	/**
	 * Gets the timezone that's associated with this address.
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @return the timezone (e.g. "America/New_York") or null if not set
	 */
	public String getTimezone() {
		return parameters.getTimezone();
	}

	/**
	 * Sets the timezone that's associated with this address.
	 * <p>
	 * <b>Supported versions:</b> {@code 4.0}
	 * </p>
	 * @param timezone the timezone (e.g. "America/New_York") or null to remove
	 */
	public void setTimezone(String timezone) {
		parameters.setTimezone(timezone);
	}

	@Override
	protected void _validate(List<ValidationWarning> warnings, VCardVersion version, VCard vcard) {
		for (AddressType type : getTypes()) {
			if (type == AddressType.PREF) {
				//ignore because it is converted to a PREF parameter for 4.0 vCards
				continue;
			}

			if (!type.isSupportedBy(version)) {
				warnings.add(new ValidationWarning(9, type.getValue()));
			}
		}

		/*
		 * 2.1 does not allow multi-valued components.
		 */
		if (version == VCardVersion.V2_1) {
			//@formatter:off
			if (poBoxes.size() > 1 ||
				extendedAddresses.size() > 1 ||
				streetAddresses.size() > 1 ||
				localities.size() > 1 ||
				regions.size() > 1 ||
				postalCodes.size() > 1 ||
				countries.size() > 1) {
				warnings.add(new ValidationWarning(35));
			}
			//@formatter:on
		}
	}

	@Override
	protected Map<String, Object> toStringValues() {
		Map<String, Object> values = new LinkedHashMap<>();
		values.put("poBoxes", poBoxes);
		values.put("extendedAddresses", extendedAddresses);
		values.put("streetAddresses", streetAddresses);
		values.put("localities", localities);
		values.put("regions", regions);
		values.put("postalCodes", postalCodes);
		values.put("countries", countries);
		values.put("rooms", rooms);
		values.put("apartments", apartments);
		values.put("streetNumbers", streetNumbers);
		values.put("streetNames", streetNames);
		values.put("buildings", buildings);
		values.put("blocks", blocks);
		values.put("subDistricts", subDistricts);
		values.put("districts", districts);
		values.put("landmarks", landmarks);
		values.put("directions", directions);

		return values;
	}

	@Override
	public ExtendedAddress copy() {
		return new ExtendedAddress(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + countries.hashCode();
		result = prime * result + extendedAddresses.hashCode();
		result = prime * result + localities.hashCode();
		result = prime * result + poBoxes.hashCode();
		result = prime * result + postalCodes.hashCode();
		result = prime * result + regions.hashCode();
		result = prime * result + streetAddresses.hashCode();
		result = prime * result + rooms.hashCode();
		result = prime * result + apartments.hashCode();
		result = prime * result + floors.hashCode();
		result = prime * result + streetNumbers.hashCode();
		result = prime * result + streetNames.hashCode();
		result = prime * result + buildings.hashCode();
		result = prime * result + blocks.hashCode();
		result = prime * result + subDistricts.hashCode();
		result = prime * result + districts.hashCode();
		result = prime * result + landmarks.hashCode();
		result = prime * result + directions.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		ExtendedAddress other = (ExtendedAddress) obj;
		if (!countries.equals(other.countries)) return false;
		if (!extendedAddresses.equals(other.extendedAddresses)) return false;
		if (!localities.equals(other.localities)) return false;
		if (!poBoxes.equals(other.poBoxes)) return false;
		if (!postalCodes.equals(other.postalCodes)) return false;
		if (!regions.equals(other.regions)) return false;
		if (!streetAddresses.equals(other.streetAddresses)) return false;
		if (!rooms.equals(other.rooms)) return false;
		if (!apartments.equals(other.apartments)) return false;
		if (!floors.equals(other.floors)) return false;
		if (!streetNumbers.equals(other.streetNumbers)) return false;
		if (!streetNames.equals(other.streetNames)) return false;
		if (!buildings.equals(other.buildings)) return false;
		if (!blocks.equals(other.blocks)) return false;
		if (!subDistricts.equals(other.subDistricts)) return false;
		if (!districts.equals(other.districts)) return false;
		if (!landmarks.equals(other.landmarks)) return false;
		return directions.equals(other.directions);
	}


	public boolean isExtended() {

		return !(rooms.isEmpty() &&
				apartments.isEmpty() &&
				floors.isEmpty() &&
				streetNumbers.isEmpty() &&
				streetNames.isEmpty() &&
				buildings.isEmpty() &&
				blocks.isEmpty() &&
				subDistricts.isEmpty() &&
				districts.isEmpty() &&
				landmarks.isEmpty() &&
				directions.isEmpty());
	}

	private static String first(List<String> list) {
		return list.isEmpty() ? null : list.get(0);
	}

	private static void set(List<String> list, String value) {
		list.clear();
		if (value != null) {
			list.add(value);
		}
	}

	private static String getAddressFull(List<String> list) {
		return list.isEmpty() ? null : StringUtils.join(list, ",");
	}
}