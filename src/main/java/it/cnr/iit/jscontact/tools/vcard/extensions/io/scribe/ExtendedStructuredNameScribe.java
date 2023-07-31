package it.cnr.iit.jscontact.tools.vcard.extensions.io.scribe;

import static ezvcard.util.StringUtils.join;

import com.github.mangstadt.vinnie.io.VObjectPropertyValues.SemiStructuredValueBuilder;
import com.github.mangstadt.vinnie.io.VObjectPropertyValues.SemiStructuredValueIterator;
import com.github.mangstadt.vinnie.io.VObjectPropertyValues.StructuredValueBuilder;
import com.github.mangstadt.vinnie.io.VObjectPropertyValues.StructuredValueIterator;

import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.io.ParseContext;
import ezvcard.io.html.HCardElement;
import ezvcard.io.json.JCardValue;
import ezvcard.io.scribe.VCardPropertyScribe;
import ezvcard.io.text.WriteContext;
import ezvcard.io.xml.XCardElement;
import ezvcard.parameter.VCardParameters;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;

public class ExtendedStructuredNameScribe extends VCardPropertyScribe<ExtendedStructuredName> {
	public ExtendedStructuredNameScribe() {
		super(ExtendedStructuredName.class, "N");
	}

	@Override
	protected VCardDataType _defaultDataType(VCardVersion version) {
		return VCardDataType.TEXT;
	}

	@Override
	protected String _writeText(ExtendedStructuredName property, WriteContext context) {
		/*
		 * StructuredValueBuilder cannot be used with 2.1 because it escapes
		 * comma characters. For example, if someone's last name is "Foo,bar",
		 * the comma character must NOT be escaped when written to a 2.1 vCard.
		 * 
		 * The reason commas are not escaped in 2.1 is because 2.1 does not
		 * allow multi-valued components like 3.0 and 4.0 do (for example,
		 * multiple suffixes).
		 * 
		 * If a StructuredName object has multi-valued components, and it is
		 * being written to a 2.1 vCard, then ez-vcard will comma-delimit them
		 * to prevent data loss. But this is not part of the 2.1 syntax.
		 */
		if (context.getVersion() == VCardVersion.V2_1) {
			SemiStructuredValueBuilder builder = new SemiStructuredValueBuilder();
			builder.append(property.getFamily());
			builder.append(property.getGiven());
			builder.append(join(property.getAdditionalNames(), ","));
			builder.append(join(property.getPrefixes(), ","));
			builder.append(join(property.getSuffixes(), ","));
			return builder.build(false, context.isIncludeTrailingSemicolons());
		} else {
			StructuredValueBuilder builder = new StructuredValueBuilder();
			builder.append(property.getFamily());
			builder.append(property.getGiven());
			builder.append(property.getAdditionalNames());
			builder.append(property.getPrefixes());
			builder.append(property.getSuffixes());
			builder.append(property.getSurname2());
			builder.append(property.getGeneration());
			return builder.build(context.isIncludeTrailingSemicolons());
		}
	}

	@Override
	protected ExtendedStructuredName _parseText(String value, VCardDataType dataType, VCardParameters parameters, ParseContext context) {
		ExtendedStructuredName property = new ExtendedStructuredName();

		if (context.getVersion() == VCardVersion.V2_1) {
			/*
			 * 2.1 does not recognize multi-valued components.
			 */
			SemiStructuredValueIterator it = new SemiStructuredValueIterator(value);
			property.setFamily(it.next());
			property.setGiven(it.next());

			String next = it.next();
			if (next != null) {
				property.getAdditionalNames().add(next);
			}

			next = it.next();
			if (next != null) {
				property.getPrefixes().add(next);
			}

			next = it.next();
			if (next != null) {
				property.getSuffixes().add(next);
			}
		} else {
			StructuredValueIterator it = new StructuredValueIterator(value);
			property.setFamily(it.nextValue());
			property.setGiven(it.nextValue());
			property.getAdditionalNames().addAll(it.nextComponent());
			property.getPrefixes().addAll(it.nextComponent());
			property.getSuffixes().addAll(it.nextComponent());
			property.getSurname2().addAll(it.nextComponent());
			property.getGeneration().addAll(it.nextComponent());
		}

		return property;
	}

	@Override
	protected void _writeXml(ExtendedStructuredName property, XCardElement parent) {
		parent.append("surname", property.getFamily()); //the XML element still needs to be printed if value == null
		parent.append("given", property.getGiven());
		parent.append("additional", property.getAdditionalNames());
		parent.append("prefix", property.getPrefixes());
		parent.append("suffix", property.getSuffixes());
		parent.append("surname2", property.getSurname2());
		parent.append("feberation", property.getGeneration());
	}

	@Override
	protected ExtendedStructuredName _parseXml(XCardElement element, VCardParameters parameters, ParseContext context) {
		ExtendedStructuredName property = new ExtendedStructuredName();

		property.setFamily(s(element.first("surname")));
		property.setGiven(s(element.first("given")));
		property.getAdditionalNames().addAll(element.all("additional"));
		property.getPrefixes().addAll(element.all("prefix"));
		property.getSuffixes().addAll(element.all("suffix"));
		property.getSurname2().addAll(element.all("surname2"));
		property.getGeneration().addAll(element.all("generation"));

		return property;
	}

	private static String s(String value) {
		return (value == null || value.isEmpty()) ? null : value;
	}

	@Override
	protected ExtendedStructuredName _parseHtml(HCardElement element, ParseContext context) {
		ExtendedStructuredName property = new ExtendedStructuredName();

		property.setFamily(s(element.firstValue("family-name")));
		property.setGiven(s(element.firstValue("given-name")));
		property.getAdditionalNames().addAll(element.allValues("additional-name"));
		property.getPrefixes().addAll(element.allValues("honorific-prefix"));
		property.getSuffixes().addAll(element.allValues("honorific-suffix"));
		property.getSurname2().addAll(element.allValues("surname2"));
		property.getGeneration().addAll(element.allValues("generation"));

		return property;
	}

	@Override
	protected JCardValue _writeJson(ExtendedStructuredName property) {
		return JCardValue.structured(property.getFamily(), property.getGiven(), property.getAdditionalNames(), property.getPrefixes(), property.getSuffixes(), property.getSurname2(), property.getGeneration());
	}

	@Override
	protected ExtendedStructuredName _parseJson(JCardValue value, VCardDataType dataType, VCardParameters parameters, ParseContext context) {
		ExtendedStructuredName property = new ExtendedStructuredName();
		StructuredValueIterator it = new StructuredValueIterator(value.asStructured());

		property.setFamily(it.nextValue());
		property.setGiven(it.nextValue());
		property.getAdditionalNames().addAll(it.nextComponent());
		property.getPrefixes().addAll(it.nextComponent());
		property.getSuffixes().addAll(it.nextComponent());
		property.getSurname2().addAll(it.nextComponent());
		property.getGeneration().addAll(it.nextComponent());

		return property;
	}
}
