package it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe;


import static ezvcard.util.StringUtils.join;

import java.util.List;

import com.github.mangstadt.vinnie.io.VObjectPropertyValues.SemiStructuredValueBuilder;
import com.github.mangstadt.vinnie.io.VObjectPropertyValues.SemiStructuredValueIterator;
import com.github.mangstadt.vinnie.io.VObjectPropertyValues.StructuredValueBuilder;
import com.github.mangstadt.vinnie.io.VObjectPropertyValues.StructuredValueIterator;

import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.io.ParseContext;
import ezvcard.io.html.HCardElement;
import ezvcard.io.json.JCardValue;
import ezvcard.io.scribe.VCardPropertyScribe;
import ezvcard.io.text.WriteContext;
import ezvcard.io.xml.XCardElement;
import ezvcard.parameter.VCardParameters;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedAddress;

public class ExtendedAddressScribe extends VCardPropertyScribe<ExtendedAddress> {
	public ExtendedAddressScribe() {
		super(ExtendedAddress.class, "ADR");
	}

	@Override
	protected VCardDataType _defaultDataType(VCardVersion version) {
		return VCardDataType.TEXT;
	}

	@Override
	protected void _prepareParameters(ExtendedAddress property, VCardParameters copy, VCardVersion version, VCard vcard) {
		handlePrefParam(property, copy, version, vcard);

		if (version == VCardVersion.V2_1 || version == VCardVersion.V3_0) {
			/*
			 * Remove the LABEL parameter. By the time this line of code is
			 * reached, VCardWriter will have created a LABEL property from this
			 * property's LABEL parameter
			 */
			copy.setLabel(null);
		}
	}

	@Override
	protected String _writeText(ExtendedAddress property, WriteContext context) {
		/*
		 * StructuredValueBuilder cannot be used with 2.1 because it escapes
		 * comma characters. For example, if someone's street address is
		 * "Foo,bar Lane", the comma character must NOT be escaped when written
		 * to a 2.1 vCard.
		 * 
		 * The reason commas are not escaped in 2.1 is because 2.1 does not
		 * allow multi-valued components like 3.0 and 4.0 do (for example,
		 * multiple street addresses).
		 * 
		 * If an Address object has multi-valued components, and it is being
		 * written to a 2.1 vCard, then ez-vcard will comma-delimit them to
		 * prevent data loss. But this is not part of the 2.1 syntax.
		 */
		if (context.getVersion() == VCardVersion.V2_1) {
			SemiStructuredValueBuilder builder = new SemiStructuredValueBuilder();
			builder.append(join(property.getPoBoxes(), ","));
			builder.append(join(property.getExtendedAddresses(), ","));
			builder.append(join(property.getStreetAddresses(), ","));
			builder.append(join(property.getLocalities(), ","));
			builder.append(join(property.getRegions(), ","));
			builder.append(join(property.getPostalCodes(), ","));
			builder.append(join(property.getCountries(), ","));
			return builder.build(false, context.isIncludeTrailingSemicolons());
		} else {
			StructuredValueBuilder builder = new StructuredValueBuilder();
			builder.append(property.getPoBoxes());
			builder.append(property.getExtendedAddresses());
			builder.append(property.getStreetAddresses());
			builder.append(property.getLocalities());
			builder.append(property.getRegions());
			builder.append(property.getPostalCodes());
			builder.append(property.getCountries());
			builder.append(property.getRooms());
			builder.append(property.getApartments());
			builder.append(property.getFloors());
			builder.append(property.getStreetNumbers());
			builder.append(property.getStreetNames());
			builder.append(property.getBuildings());
			builder.append(property.getBlocks());
			builder.append(property.getSubDistricts());
			builder.append(property.getDistricts());
			builder.append(property.getLandmarks());
			builder.append(property.getDirections());
			return builder.build(context.isIncludeTrailingSemicolons());
		}
	}

	@Override
	protected ExtendedAddress _parseText(String value, VCardDataType dataType, VCardParameters parameters, ParseContext context) {
		if (context.getVersion() == VCardVersion.V2_1) {
			/*
			 * 2.1 does not recognize multi-valued components.
			 */
			SemiStructuredValueIterator it = new SemiStructuredValueIterator(value);
			return parseSemiStructuredValue(it);
		} else {
			StructuredValueIterator it = new StructuredValueIterator(value);
			return parseStructuredValue(it);
		}
	}

	@Override
	protected void _writeXml(ExtendedAddress property, XCardElement parent) {
		parent.append("pobox", property.getPoBoxes()); //Note: The XML element must always be added, even if the value is null
		parent.append("ext", property.getExtendedAddresses());
		parent.append("street", property.getStreetAddresses());
		parent.append("locality", property.getLocalities());
		parent.append("region", property.getRegions());
		parent.append("code", property.getPostalCodes());
		parent.append("country", property.getCountries());
		parent.append("room", property.getRooms());
		parent.append("apartment", property.getApartments());
		parent.append("floor", property.getFloors());
		parent.append("street-number", property.getStreetNumbers());
		parent.append("street-name", property.getStreetNames());
		parent.append("building", property.getBuildings());
		parent.append("block", property.getBlocks());
		parent.append("sub-district", property.getSubDistricts());
		parent.append("district", property.getDistricts());
		parent.append("landmark", property.getLandmarks());
		parent.append("direction", property.getDirections());
	}

	@Override
	protected ExtendedAddress _parseXml(XCardElement element, VCardParameters parameters, ParseContext context) {
		ExtendedAddress property = new ExtendedAddress();
		property.getPoBoxes().addAll(sanitizeXml(element, "pobox"));
		property.getExtendedAddresses().addAll(sanitizeXml(element, "ext"));
		property.getStreetAddresses().addAll(sanitizeXml(element, "street"));
		property.getLocalities().addAll(sanitizeXml(element, "locality"));
		property.getRegions().addAll(sanitizeXml(element, "region"));
		property.getPostalCodes().addAll(sanitizeXml(element, "code"));
		property.getCountries().addAll(sanitizeXml(element, "country"));
		property.getRooms().addAll(sanitizeXml(element, "room"));
		property.getApartments().addAll(sanitizeXml(element, "apartment"));
		property.getFloors().addAll(sanitizeXml(element, "floor"));
		property.getStreetNumbers().addAll(sanitizeXml(element, "street-number"));
		property.getStreetNames().addAll(sanitizeXml(element, "street-name"));
		property.getBuildings().addAll(sanitizeXml(element, "building"));
		property.getBlocks().addAll(sanitizeXml(element, "block"));
		property.getSubDistricts().addAll(sanitizeXml(element, "sub-district"));
		property.getDistricts().addAll(sanitizeXml(element, "district"));
		property.getLandmarks().addAll(sanitizeXml(element, "landmark"));
		property.getDirections().addAll(sanitizeXml(element, "direction"));
		return property;
	}

	private List<String> sanitizeXml(XCardElement element, String name) {
		return element.all(name);
	}

	@Override
	protected ExtendedAddress _parseHtml(HCardElement element, ParseContext context) {
		ExtendedAddress property = new ExtendedAddress();
		property.getPoBoxes().addAll(element.allValues("post-office-box"));
		property.getExtendedAddresses().addAll(element.allValues("extended-address"));
		property.getStreetAddresses().addAll(element.allValues("street-address"));
		property.getLocalities().addAll(element.allValues("locality"));
		property.getRegions().addAll(element.allValues("region"));
		property.getPostalCodes().addAll(element.allValues("postal-code"));
		property.getCountries().addAll(element.allValues("country-name"));
		property.getRooms().addAll(element.allValues("room"));
		property.getApartments().addAll(element.allValues("apartment"));
		property.getFloors().addAll(element.allValues("floor"));
		property.getStreetNumbers().addAll(element.allValues("street-number"));
		property.getStreetNames().addAll(element.allValues("street-name"));
		property.getBuildings().addAll(element.allValues("building"));
		property.getBlocks().addAll(element.allValues("block"));
		property.getSubDistricts().addAll(element.allValues("sub-district"));
		property.getDistricts().addAll(element.allValues("district"));
		property.getLandmarks().addAll(element.allValues("landmark"));
		property.getDirections().addAll(element.allValues("direction"));

		List<String> types = element.types();
		property.getParameters().putAll(VCardParameters.TYPE, types);

		return property;
	}

	@Override
	protected JCardValue _writeJson(ExtendedAddress property) {
		//@formatter:off
		return JCardValue.structured(
			property.getPoBoxes(),
			property.getExtendedAddresses(),
			property.getStreetAddresses(),
			property.getLocalities(),
			property.getRegions(),
			property.getPostalCodes(),
			property.getCountries(),
			property.getRooms(),
			property.getApartments(),
			property.getFloors(),
			property.getStreetNumbers(),
			property.getStreetNames(),
			property.getBuildings(),
			property.getBlocks(),
			property.getSubDistricts(),
			property.getDistricts(),
			property.getLandmarks(),
			property.getDirections()
		);
		//@formatter:on
	}

	@Override
	protected ExtendedAddress _parseJson(JCardValue value, VCardDataType dataType, VCardParameters parameters, ParseContext context) {
		StructuredValueIterator it = new StructuredValueIterator(value.asStructured());
		return parseStructuredValue(it);
	}

	private static ExtendedAddress parseStructuredValue(StructuredValueIterator it) {
		ExtendedAddress property = new ExtendedAddress();

		property.getPoBoxes().addAll(it.nextComponent());
		property.getExtendedAddresses().addAll(it.nextComponent());
		property.getStreetAddresses().addAll(it.nextComponent());
		property.getLocalities().addAll(it.nextComponent());
		property.getRegions().addAll(it.nextComponent());
		property.getPostalCodes().addAll(it.nextComponent());
		property.getCountries().addAll(it.nextComponent());
		property.getRooms().addAll(it.nextComponent());
		property.getApartments().addAll(it.nextComponent());
		property.getFloors().addAll(it.nextComponent());
		property.getStreetNumbers().addAll(it.nextComponent());
		property.getStreetNames().addAll(it.nextComponent());
		property.getBuildings().addAll(it.nextComponent());
		property.getBlocks().addAll(it.nextComponent());
		property.getSubDistricts().addAll(it.nextComponent());
		property.getDistricts().addAll(it.nextComponent());
		property.getLandmarks().addAll(it.nextComponent());
		property.getDirections().addAll(it.nextComponent());

		return property;
	}

	private static ExtendedAddress parseSemiStructuredValue(SemiStructuredValueIterator it) {
		ExtendedAddress property = new ExtendedAddress();

		String next = it.next();
		if (next != null) {
			property.getPoBoxes().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getExtendedAddresses().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getStreetAddresses().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getLocalities().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getRegions().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getPostalCodes().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getCountries().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getRooms().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getApartments().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getFloors().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getStreetNumbers().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getStreetNames().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getBuildings().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getBlocks().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getSubDistricts().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getDistricts().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getLandmarks().add(next);
		}

		next = it.next();
		if (next != null) {
			property.getDirections().add(next);
		}

		return property;
	}
}
