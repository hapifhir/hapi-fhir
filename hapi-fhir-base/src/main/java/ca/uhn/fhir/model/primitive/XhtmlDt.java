package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.XmlDetectionUtil;
import ca.uhn.fhir.util.XmlUtil;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Note that as of HAPI FHIR 3.1.0, this method no longer uses
 * the StAX XMLEvent type as the XML representation, and uses a
 * String instead. If you need to work with XML as StAX events, you
 * can use the {@link XmlUtil#parse(String)} and {@link XmlUtil#encode(List)}
 * methods to do so.
 */
@DatatypeDef(name = "xhtml")
public class XhtmlDt extends BasePrimitive<String> {

	private static final String DECL_XMLNS = " xmlns=\"http://www.w3.org/1999/xhtml\"";
	public static final String DIV_OPEN_FIRST = "<div" + DECL_XMLNS + ">";
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public XhtmlDt() {
		// nothing
	}

	/**
	 * Constructor which accepts a string code
	 *
	 * @see #setValueAsString(String) for a description of how this value is applied
	 */
	@SimpleSetter()
	public XhtmlDt(@SimpleSetter.Parameter(name = "theTextDiv") String theTextDiv) {
		setValueAsString(theTextDiv);
	}

	@Override
	protected String encode(String theValue) {
		return theValue;
	}

	public boolean hasContent() {
		return isNotBlank(getValue());
	}

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && (getValue() == null || getValue().isEmpty());
	}

	@Override
	protected String parse(String theValue) {
		if (XmlDetectionUtil.isStaxPresent()) {
			// for validation
			XmlUtil.parse(theValue);
		}
		return theValue;
	}


	/**
	 * Note that as of HAPI FHIR 3.1.0, this method no longer uses
	 * the StAX XMLEvent type as the XML representation, and uses a
	 * String instead. If you need to work with XML as StAX events, you
	 * can use the {@link XmlUtil#parse(String)} and {@link XmlUtil#encode(List)}
	 * methods to do so.
	 */
	@Override
	public String getValue() {
		return super.getValue();
	}

	/**
	 * Note that as of HAPI FHIR 3.1.0, this method no longer uses
	 * the StAX XMLEvent type as the XML representation, and uses a
	 * String instead. If you need to work with XML as StAX events, you
	 * can use the {@link XmlUtil#parse(String)} and {@link XmlUtil#encode(List)}
	 * methods to do so.
	 */
	@Override
	public BasePrimitive<String> setValue(String theValue) throws DataFormatException {
		return super.setValue(theValue);
	}

	/**
	 * Accepts a textual DIV and parses it into XHTML events which are stored internally.
	 * <p>
	 * <b>Formatting note:</b> The text will be trimmed {@link String#trim()}. If the text does not start with an HTML tag (generally this would be a div tag), a div tag will be automatically placed
	 * surrounding the text.
	 * </p>
	 * <p>
	 * Also note that if the parsed text contains any entities (&amp;foo;) which are not a part of the entities defined in core XML (e.g. &amp;sect; which is valid in XHTML 1.0 but not in XML 1.0) they
	 * will be parsed and converted to their equivalent unicode character.
	 * </p>
	 */
	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null || theValue.isEmpty()) {
			super.setValueAsString(null);
		} else {
			String value = theValue.trim();
			value = preprocessXhtmlNamespaceDeclaration(value);

			super.setValueAsString(value);
		}
	}

	public static String preprocessXhtmlNamespaceDeclaration(String value) {
		if (value.charAt(0) != '<') {
			value = DIV_OPEN_FIRST + value + "</div>";
		}

		boolean hasProcessingInstruction = value.startsWith("<?");
		int firstTagIndex = value.indexOf("<", hasProcessingInstruction ? 1 : 0);
		if (firstTagIndex != -1) {
			int firstTagEnd = value.indexOf(">", firstTagIndex);
			int firstSlash = value.indexOf("/", firstTagIndex);
			if (firstTagEnd != -1) {
				if (firstSlash > firstTagEnd) {
					String firstTag = value.substring(firstTagIndex, firstTagEnd);
					if (!firstTag.contains(" xmlns")) {
						value = value.substring(0, firstTagEnd) + DECL_XMLNS + value.substring(firstTagEnd);
					}
				}
			}
		}
		return value;
	}

}
