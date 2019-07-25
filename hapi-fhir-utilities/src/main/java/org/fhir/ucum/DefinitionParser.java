/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *******************************************************************************/

package org.fhir.ucum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


/**
 * Parses the file ucum-essense.xml
 * 
 * @author Grahame Grieve
 *
 */

public class DefinitionParser {

	public UcumModel parse(String filename) throws UcumException, XmlPullParserException, IOException, ParseException  {
		return parse(new FileInputStream(new File(filename)));
	}

	public UcumModel parse(InputStream stream) throws XmlPullParserException, IOException, ParseException, UcumException  {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
				System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput(stream, null);

		int eventType = xpp.next();
		if (eventType != XmlPullParser.START_TAG)
			throw new XmlPullParserException("Unable to process XML document");
		if (!xpp.getName().equals("root")) 
			throw new XmlPullParserException("Unable to process XML document: expected 'root' but found '"+xpp.getName()+"'");
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss' 'Z");
		Date date = fmt.parse(xpp.getAttributeValue(null, "revision-date").substring(7, 32));        
		UcumModel root = new UcumModel(xpp.getAttributeValue(null, "version"), xpp.getAttributeValue(null, "revision"), date);
		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				if (Utilities.isWhitespace(xpp.getText()))
					xpp.next();
				else
					throw new XmlPullParserException("Unexpected text "+xpp.getText());
			} else if (xpp.getName().equals("prefix")) 
				root.getPrefixes().add(parsePrefix(xpp));
			else if (xpp.getName().equals("base-unit")) 
				root.getBaseUnits().add(parseBaseUnit(xpp));
			else if (xpp.getName().equals("unit")) 
				root.getDefinedUnits().add(parseUnit(xpp));
			else 
				throw new XmlPullParserException("unknown element name "+xpp.getName());
		}
		return root;
	}

	private DefinedUnit parseUnit(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		DefinedUnit unit = new DefinedUnit(xpp.getAttributeValue(null, "Code"), xpp.getAttributeValue(null, "CODE"));
		unit.setMetric("yes".equals(xpp.getAttributeValue(null, "isMetric")));
		unit.setSpecial("yes".equals(xpp.getAttributeValue(null, "isSpecial")));
		unit.setClass_(xpp.getAttributeValue(null, "class"));
		xpp.next();
		skipWhitespace(xpp);
		while (xpp.getEventType() == XmlPullParser.START_TAG && "name".equals(xpp.getName()))
			unit.getNames().add(readElement(xpp, "name", "unit "+unit.getCode(), false));
		if (xpp.getEventType() == XmlPullParser.START_TAG && "printSymbol".equals(xpp.getName()))
			unit.setPrintSymbol(readElement(xpp, "printSymbol", "unit "+unit.getCode(), true));
		unit.setProperty(readElement(xpp, "property", "unit "+unit.getCode(), false));
		unit.setValue(parseValue(xpp, "unit "+unit.getCode()));
		xpp.next();
		skipWhitespace(xpp);
		return unit;
	}

	private Value parseValue(XmlPullParser xpp, String context) throws XmlPullParserException, UcumException, IOException  {
		checkAtElement(xpp, "value", context);
		Decimal val = null;
		if (xpp.getAttributeValue(null, "value") != null) 
			try {
				if (xpp.getAttributeValue(null, "value").contains("."))
					val = new Decimal(xpp.getAttributeValue(null, "value"), 24); // unlimited precision for these
				else
					val = new Decimal(xpp.getAttributeValue(null, "value"));
			} catch (NumberFormatException e) {
				throw new XmlPullParserException("Error reading "+context+": "+e.getMessage());
			}
		Value value = new Value(xpp.getAttributeValue(null, "Unit"), xpp.getAttributeValue(null, "UNIT"), val);
		value.setText(readElement(xpp, "value", context, true));
		return value;
	}

	private BaseUnit parseBaseUnit(XmlPullParser xpp) throws XmlPullParserException, IOException {
		BaseUnit base = new BaseUnit(xpp.getAttributeValue(null, "Code"), xpp.getAttributeValue(null, "CODE"));
		base.setDim(xpp.getAttributeValue(null, "dim").charAt(0));
		xpp.next();
		skipWhitespace(xpp);
		base.getNames().add(readElement(xpp, "name", "base-unit "+base.getCode(), false));
		base.setPrintSymbol(readElement(xpp, "printSymbol", "base-unit "+base.getCode(), false));
		base.setProperty(readElement(xpp, "property", "base-unit "+base.getCode(), false));
		xpp.next();
		skipWhitespace(xpp);
		return base;
	}

	private Prefix parsePrefix(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		Prefix prefix = new Prefix(xpp.getAttributeValue(null, "Code"), xpp.getAttributeValue(null, "CODE"));
		xpp.next();
		skipWhitespace(xpp);
		prefix.getNames().add(readElement(xpp, "name", "prefix "+prefix.getCode(), false));
		prefix.setPrintSymbol(readElement(xpp, "printSymbol", "prefix "+prefix.getCode(), false));
		checkAtElement(xpp, "value", "prefix "+prefix.getCode());
		prefix.setValue(new Decimal(xpp.getAttributeValue(null, "value"), 24));
		readElement(xpp, "value", "prefix "+prefix.getCode(), true);
		xpp.next();
		skipWhitespace(xpp);
		return prefix;
	}

	private String readElement(XmlPullParser xpp, String name, String context, boolean complex) throws XmlPullParserException, IOException {
		checkAtElement(xpp, name, context);
		xpp.next();
		skipWhitespace(xpp);
		String val = null;
		if (complex) {
			val = readText(xpp);
		} else if (xpp.getEventType() == XmlPullParser.TEXT) {
			val = xpp.getText();
			xpp.next();
			skipWhitespace(xpp);
		}
		if (xpp.getEventType() != XmlPullParser.END_TAG) {
			throw new XmlPullParserException("Unexpected content reading "+context);
		}
		xpp.next();
		skipWhitespace(xpp);
		return val;
	}

	private String readText(XmlPullParser xpp) throws XmlPullParserException, IOException {
		StringBuilder bldr = new StringBuilder();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				bldr.append(xpp.getText());
				xpp.next();
			} else {
				xpp.next();
				bldr.append(readText(xpp));
				xpp.next();
				skipWhitespace(xpp);
			}
		}
		return bldr.toString();
	}

	private void skipWhitespace(XmlPullParser xpp) throws XmlPullParserException, IOException {
		while (xpp.getEventType() == XmlPullParser.TEXT && Utilities.isWhitespace(xpp.getText())) 
			xpp.next();		
	}

	private void checkAtElement(XmlPullParser xpp, String name, String context) throws XmlPullParserException {
		if (xpp.getEventType() != XmlPullParser.START_TAG)
			throw new XmlPullParserException("Unexpected state looking for "+name+": at "+Integer.toString(xpp.getEventType())+"  reading "+context);
		if (!xpp.getName().equals(name))
			throw new XmlPullParserException("Unexpected element looking for "+name+": found "+xpp.getName()+"  reading "+context);		
	}
}
