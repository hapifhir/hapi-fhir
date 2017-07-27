package org.hl7.fhir.utilities.ucum.tests;

/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing & Health Intersections P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Health Intersections P/L - port to java, ongoing maintenance
 *    
 ******************************************************************************/

import java.io.FileInputStream;
import java.io.IOException;

import org.hl7.fhir.exceptions.UcumException;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.ucum.Decimal;
import org.hl7.fhir.utilities.ucum.Pair;
import org.hl7.fhir.utilities.ucum.UcumEssenceService;
import org.hl7.fhir.utilities.ucum.UcumService;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class UcumTester {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 * @throws UcumException 
	 * @ 
	 */
	public static void main(String[] args) throws XmlPullParserException, IOException, UcumException  {
		if (args.length == 0) {
			System.out.println("UCUM Tester - parameters:");
			System.out.println("  -definitions [filename] - filename for the UCUM definitions");
			System.out.println("  -tests [fileanme] - filename for the UCUM tests");
			System.out.println("See http://unitsofmeasure.org/trac/ for source files for both ucum definitions");
			System.out.println("(ucum-essence.xml) and tests (http://unitsofmeasure.org/trac/wiki/FunctionalTests)");
		} else {
			UcumTester worker = new UcumTester();
			int i = 0;
			while (i < args.length) {
				String a = args[i];
				i++;
				if (a.equalsIgnoreCase("-definitions"))
					worker.setDefinitions(args[i]);
				else if (a.equalsIgnoreCase("-tests"))
					worker.setTests(args[i]);
				else 
					System.out.println("Unknown parameter: '"+a+"'");
				i++;
			}
			worker.execute();
		}
	}

	private String definitions;
	private String tests;
	public String getDefinitions() {
		return definitions;
	}
	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}
	public String getTests() {
		return tests;
	}
	public void setTests(String tests) {
		this.tests = tests;
	}
	

	private UcumService ucumSvc;
	private int errCount;
	
	private void execute() throws XmlPullParserException, IOException, UcumException  {

		testDecimal();
		
		ucumSvc = new UcumEssenceService(definitions);
	  errCount = 0;
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput(new FileInputStream(tests), null);

		int eventType = xpp.next();
		if (eventType != XmlPullParser.START_TAG)
			throw new XmlPullParserException("Unable to process XML document");
		if (!xpp.getName().equals("ucumTests")) 
			throw new XmlPullParserException("Unable to process XML document: expected 'ucumTests' but found '"+xpp.getName()+"'");

		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				if (Utilities.isWhitespace(xpp.getText()))
					xpp.next();
				else
					throw new XmlPullParserException("Unexpected text "+xpp.getText());
			} else if (xpp.getName().equals("history")) 
				skipElement(xpp);
			else if (xpp.getName().equals("validation")) 
				runValidationTests(xpp);
			else if (xpp.getName().equals("displayNameGeneration")) 
				runDisplayNameGeneration(xpp);
			else if (xpp.getName().equals("conversion")) 
				runConversion(xpp);
			else if (xpp.getName().equals("multiplication")) 
				runMultiplication(xpp);
			else 
				throw new XmlPullParserException("unknown element name "+xpp.getName());
		}
		xpp.next();
		if (errCount > 0)
			System.err.println(Integer.toString(errCount)+" errors");
	}
	
	private void runMultiplication(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				if (Utilities.isWhitespace(xpp.getText()))
					xpp.next();
				else
					throw new XmlPullParserException("Unexpected text "+xpp.getText());
			} else if (xpp.getName().equals("case")) 
				runMultiplicationCase(xpp);
			else 
				throw new XmlPullParserException("unknown element name "+xpp.getName());
		}
		xpp.next();	  
  }
	
	private void runMultiplicationCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		
	  String id = xpp.getAttributeValue(null, "id");
	  String v1 = xpp.getAttributeValue(null, "v1");
	  String u1 = xpp.getAttributeValue(null, "u1");
	  String v2 = xpp.getAttributeValue(null, "v2");
	  String u2 = xpp.getAttributeValue(null, "u2");
	  String vRes = xpp.getAttributeValue(null, "vRes");
	  String uRes = xpp.getAttributeValue(null, "uRes");
	  
	  Pair o1 = new Pair(new Decimal(v1), u1);
	  Pair o2 = new Pair(new Decimal(v2), u2);
	  Pair o3 = ucumSvc.multiply(o1, o2);

		debug("Multiplication Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

	  // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
		if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
	  	errCount++;
	  	System.err.println("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
	  }
 		while (xpp.getEventType() != XmlPullParser.END_TAG) 
	    xpp.next();
 		xpp.next();
 		
  }
	
	private void testDecimal() throws UcumException  {
    testAsInteger();
    testStringSupport();
    testCompares();
    testAddition();
    testMultiplication();
  }
	
	private void testMultiplication() throws UcumException  {
	  testMultiply("2", "2", "4");
	  testMultiply("2", "0.5", "1");
	  testMultiply("0", "0", "0");
	  testMultiply("0", "1", "0");
	  testMultiply("4", "4", "16");
	  testMultiply("20", "20", "400");
	  testMultiply("200", "20", "4000");
	  testMultiply("400", "400", "160000");
	  testMultiply("2.0", "2.0", "4.0");
	  testMultiply("2.00", "2.0", "4.0");
	  testMultiply("2.0", "0.2", "0.4");
	  testMultiply("2.0", "0.20", "0.40");
	  testMultiply("13", "13", "169");
	  testMultiply("12", "89", "1068");
	  testMultiply("1234", "6789", "8377626");

	  testMultiply("10000", "0.0001", "1");
	  testMultiply("10000", "0.00010", "1.0");
	  testMultiply("10000", "0.000100", "1.00");
	  testMultiply("10000", "0.0001000", "1.000");
	  testMultiply("10000", "0.00010000", "1.0000");
	  testMultiply("10000", "0.000100000", "1.00000");
	  testMultiply("10000.0", "0.000100000", "1.00000");
	  testMultiply("10000.0", "0.0001000000", "1.00000");
	  testMultiply("10000.0", "0.00010000000", "1.00000");

	  testMultiply("2", "-2", "-4");
	  testMultiply("-2", "2", "-4");
	  testMultiply("-2", "-2", "4");

	  testMultiply("35328734682734", "2349834295876423", "83016672387407213199375780482");
	  testMultiply("35328734682734000000000", "2349834295876423000000000", "83016672387407213199375780482000000000000000000");
	  testMultiply("3532873468.2734", "23498342958.76423", "83016672387407213199.375780482");

	  testDivide("500", "4", "125");
	  testDivide("1260257", "37", "34061");

	  testDivide("127", "4", "31.75");
	  testDivide("10", "10", "1");
	  testDivide("1", "1", "1");
	  testDivide("10", "3", "3.3");
	  testDivide("10.0", "3", "3.33");
	  testDivide("10.00", "3", "3.333");
	  testDivide("10.00", "3.0", "3.3");
	  testDivide("100", "1", "100");
	  testDivide("1000", "10", "100");
	  testDivide("100001", "10", "10000.1");
	  testDivide("100", "10", "10");
	  testDivide("1", "10", "0.1");
	  testDivide("1", "15", "0.067");
	  testDivide("1.0", "15", "0.067");
	  testDivide("1.00", "15.0", "0.0667");
	  testDivide("1", "0.1", "10");
	  testDivide("1", "0.10", "10");
	  testDivide("1", "0.010", "100");
	  testDivide("1", "1.5", "0.67");
	  testDivide("1.0", "1.5", "0.67");
	  testDivide("10", "1.5", "6.7");

	  testDivide("-1", "1", "-1");
	  testDivide("1", "-1", "-1");
	  testDivide("-1", "-1", "1");

	  testDivide("2", "2", "1");
	  testDivide("20", "2", "10");
	  testDivide("22", "2", "11");

	  testDivide("83016672387407213199375780482", "2349834295876423", "35328734682734");
	  testDivide("83016672387407213199375780482000000000000000000", "2349834295876423000000000", "35328734682734000000000");
	  testDivide("83016672387407213199.375780482", "23498342958.76423", "3532873468.2734");

	  testDivInt("500", "4", "125");
	  testDivInt("1260257", "37", "34061");
	  testDivInt("127", "4", "31");
	  testDivInt("10", "10", "1");
	  testDivInt("1", "1", "1");
	  testDivInt("100", "1", "100");
	  testDivInt("1000", "10", "100");
	  testDivInt("100001", "10", "10000");
	  testDivInt("1", "1.5", "0");
	  testDivInt("10", "1.5", "6");

	  testModulo("10", "1", "0");
	  testModulo("7", "4", "3");

	  testMultiply("2", "2", "4");
	  testMultiply("2.0", "2.0", "4.0");
	  testMultiply("2.00", "2.0", "4.0");

	  testDivide("10",  "3", "3.3");
	  testDivide("10.0",  "3", "3.33");
	  testDivide("10.00",  "3", "3.333");
	  testDivide("10.00",  "3.0", "3.3");
	  testDivide("10",  "3.0", "3.3");

	  
  }

	private void testModulo(String s1, String s2, String s3) throws UcumException   {
	  Decimal v1 = new Decimal(s1);
	  Decimal v2 = new Decimal(s2);
	  Decimal v3 = v1.modulo(v2);
    check(v3.asDecimal().equals(s3), s1+" % "+s2+" = "+s3+", but the library returned "+v3.asDecimal());
  }

	private void testDivInt(String s1, String s2, String s3) throws UcumException   {
	  Decimal v1 = new Decimal(s1);
	  Decimal v2 = new Decimal(s2);
	  Decimal v3 = v1.divInt(v2);
    check(v3.asDecimal().equals(s3), s1+" /(int) "+s2+" = "+s3+", but the library returned "+v3.asDecimal());
  }

	private void testDivide(String s1, String s2, String s3)  throws UcumException  {
	  Decimal v1 = new Decimal(s1);
	  Decimal v2 = new Decimal(s2);
	  Decimal v3 = v1.divide(v2);
    check(v3.asDecimal().equals(s3), s1+" / "+s2+" = "+s3+", but the library returned "+v3.asDecimal());
  }

	private void testMultiply(String s1, String s2, String s3) throws UcumException  {
	  Decimal v1 = new Decimal(s1);
	  Decimal v2 = new Decimal(s2);
	  Decimal v3 = v1.multiply(v2);
    check(v3.asDecimal().equals(s3), s1+" * "+s2+" = "+s3+", but the library returned "+v3.asDecimal());
  }

	private void testAddition() throws UcumException  {
	  testAdd("1", "1", "2");
	  testAdd("0", "1", "1");
	  testAdd("0", "0", "0");
	  testAdd("5", "5", "10");
	  testAdd("10", "1", "11");
	  testAdd("11", "12", "23");
	  testAdd("15", "16", "31");
	  testAdd("150", "160", "310");
	  testAdd("153", "168", "321");
	  testAdd("15300000000000000000000000000000000001", "1680", "15300000000000000000000000000000001681");
	  testAdd("1", ".1", "1.1");
	  testAdd("1", ".001", "1.001");
	  testAdd(".1", ".1", "0.2");
	  testAdd(".1", ".01", "0.11");

	  testSubtract("2", "1", "1");
	  testSubtract("2", "0", "2");
	  testSubtract("0", "0", "0");
	  testSubtract("0", "2", "-2");
	  testSubtract("2", "2", "0");
	  testSubtract("1", "2", "-1");
	  testSubtract("20", "1", "19");
	  testSubtract("2", ".1", "1.9");
	  testSubtract("2", ".000001", "1.999999");
	  testSubtract("2", "2.000001", "-0.000001");
	  testSubtract("3.5", "35.5", "-32.0");

	  testAdd("5", "6", "11");
	  testAdd("5", "-6", "-1");
	  testAdd("-5", "6", "1");
	  testAdd("-5", "-6", "-11");

	  testSubtract("5", "6", "-1");
	  testSubtract("6", "5", "1");
	  testSubtract("5", "-6", "11");
	  testSubtract("6", "-5", "11");
	  testSubtract("-5", "6", "-11");
	  testSubtract("-6", "5", "-11");
	  testSubtract("-5", "-6", "1");
	  testSubtract("-6", "-5", "-1");

	  testAdd("2", "0.001", "2.001");
	  testAdd("2.0", "0.001", "2.001");
	  
  }
	
	private void testSubtract(String s1, String s2, String s3) throws UcumException  {
	  Decimal v1 = new Decimal(s1);
	  Decimal v2 = new Decimal(s2);
	  Decimal v3 = v1.subtract(v2);
    check(v3.asDecimal().equals(s3), s1+" - "+s2+" = "+s3+", but the library returned "+v3.asDecimal());
  }
	
	private void testAdd(String s1, String s2, String s3) throws UcumException  {
	  Decimal v1 = new Decimal(s1);
	  Decimal v2 = new Decimal(s2);
	  Decimal v3 = v1.add(v2);
    check(v3.asDecimal().equals(s3), s1+" + "+s2+" = "+s3+", but the library returned "+v3.asDecimal());
  }
	
	private void testCompares() throws UcumException  {
		testCompares("1", "1", 0);
		testCompares("0", "0", 0);
		testCompares("0", "1", -1);
		testCompares("1", "0", 1);
		
		testCompares("10", "10", 0);
		testCompares("100", "100", 0);
		testCompares("0.1", "0.1", 0);
		testCompares("0.01", "0.01", 0);
		testCompares("0.01", "0.0100", 0);
		testCompares("1", "1.00000000", 0);
		testCompares("1.111111", "1.111111", 0);
	}
	
	private void testCompares(String v1, String v2, int outcome) throws UcumException  {
	  Decimal d1 = new Decimal(v1);
	  Decimal d2 = new Decimal(v2);
	  int result = d1.comparesTo(d2);
	  check(result == outcome, "Compare fail: "+v1+".compares("+v2+") should be "+Integer.toString(outcome)+" but was "+Integer.toString(result));	  
  }
	
	private void testStringSupport() throws UcumException  {
	  testString("1", "1", "1e0");
	  testString("0", "0", "0e0");
	  testString("10", "10", "1.0e1");
	  testString("99", "99", "9.9e1");
	  testString("-1", "-1", "-1e0");
	  testString("-0", "0", "0e0");
	  testString("-10", "-10", "-1.0e1");
	  testString("-99", "-99", "-9.9e1");

	  testString("1.1", "1.1", "1.1e0");
	  testString("-1.1", "-1.1", "-1.1e0");
	  testString("11.1", "11.1", "1.11e1");
	  testString("1.11", "1.11", "1.11e0");
	  testString("1.111", "1.111", "1.111e0");
	  testString("0.1", "0.1", "1e-1");
	  testString("00.1", "0.1", "1e-1");
	  testString(".1", "0.1", "1e-1");
	  testString("1.0", "1.0", "1.0e0");
	  testString("1.00", "1.00", "1.00e0");
	  testString("1.000000000000000000000000000000000000000", "1.000000000000000000000000000000000000000", "1.000000000000000000000000000000000000000e0");

	  testString("-11.1", "-11.1", "-1.11e1");
	  testString("-1.11", "-1.11", "-1.11e0");
	  testString("-1.111", "-1.111", "-1.111e0");
	  testString("-0.1", "-0.1", "-1e-1");
	  testString("-00.1", "-0.1", "-1e-1");
	  testString("-.1", "-0.1", "-1e-1");
	  testString("-1.0", "-1.0", "-1.0e0");
	  testString("-1.00", "-1.00", "-1.00e0");
	  testString("-1.000000000000000000000000000000000000000", "-1.000000000000000000000000000000000000000", "-1.000000000000000000000000000000000000000e0");

	  testString("0.0", "0.0", "0.0e0");
	  testString("0.0000", "0.0000", "0.0000e0");
	  testString("0.1", "0.1", "1e-1");
	  testString("00.1", "0.1", "1e-1");
	  testString("0.100", "0.100", "1.00e-1");
	  testString("100", "100", "1.00e2");
	  testString("1.0", "1.0", "1.0e0");
	  testString("1.1", "1.1", "1.1e0");
	  testString("-0.1", "-0.1", "-1e-1");
	  testString("0.01", "0.01", "1e-2");
	  testString("0.001", "0.001", "1e-3");
	  testString("0.0001", "0.0001", "1e-4");
	  testString("00.0001", "0.0001", "1e-4");
	  testString("000.0001", "0.0001", "1e-4");
	  testString("-0.01", "-0.01", "-1e-2");
	  testString("10.01", "10.01", "1.001e1");
	  testString("0.0001", "0.0001", "1e-4");
	  testString("0.00001", "0.00001", "1e-5");
	  testString("0.000001", "0.000001", "1e-6");
	  testString("0.0000001", "0.0000001", "1e-7");
	  testString("0.000000001", "0.000000001", "1e-9");
	  testString("0.00000000001", "0.00000000001", "1e-11");
	  testString("0.0000000000001", "0.0000000000001", "1e-13");
	  testString("0.000000000000001", "0.000000000000001", "1e-15");
	  testString("0.00000000000000001", "0.00000000000000001", "1e-17");
	  testString("10.1", "10.1", "1.01e1");
	  testString("100.1", "100.1", "1.001e2");
	  testString("1000.1", "1000.1", "1.0001e3");
	  testString("10000.1", "10000.1", "1.00001e4");
	  testString("100000.1", "100000.1", "1.000001e5");
	  testString("1000000.1", "1000000.1", "1.0000001e6");
	  testString("10000000.1", "10000000.1", "1.00000001e7");
	  testString("100000000.1", "100000000.1", "1.000000001e8");
	  testString("1000000000.1", "1000000000.1", "1.0000000001e9");
	  testString("10000000000.1", "10000000000.1", "1.00000000001e10");
	  testString("100000000000.1", "100000000000.1", "1.000000000001e11");
	  testString("1000000000000.1", "1000000000000.1", "1.0000000000001e12");
	  testString("10000000000000.1", "10000000000000.1", "1.00000000000001e13");
	  testString("100000000000000.1", "100000000000000.1", "1.000000000000001e14");
//	  testString("1e-3", "1e-3");   , "1e-3");  e0  }

	  testTrunc("1", "1");
	  testTrunc("1.01", "1");
	  testTrunc("-1.01", "-1");
	  testTrunc("0.01", "0");
	  testTrunc("-0.01", "0");
	  testTrunc("0.1", "0");
	  testTrunc("0.0001", "0");
	  testTrunc("100.000000000000000000000000000000000000000001", "100");
  }
	
	private void testTrunc(String s1, String s2) throws UcumException  {
	  Decimal o1 = new Decimal(s1);
	  Decimal o2 = o1.trunc();
    check(o2.asDecimal().equals(s2), "wrong trunc - expected "+s2+" but got "+o2.asDecimal());
  }
	
	private void testString(String s, String st, String std) throws UcumException  {
	  Decimal dec = new Decimal(s);
	  String s1 = dec.toString();
	  String s2 = dec.asScientific();

	  check(s1.equals(st), "decimal: expected "+st+" but got "+s1);
	  check(s2.equals(std), "scientific: expected "+std+" but got "+s2);

	  dec = new Decimal(std);
    s1 = dec.asDecimal();
	  check(s1.equals(st), "decimal(2): expected "+st+" but got "+s1);	  
  }
	
	private void testAsInteger() throws UcumException  {
	  testInteger(0);
	  testInteger(1);
	  testInteger(2);
	  testInteger(64);
	  testInteger(Integer.MAX_VALUE);
	  testInteger(-1);
	  testInteger(-2);
	  testInteger(-64);
	  testInteger(Integer.MIN_VALUE);	  
  }
	
	private void testInteger(int i) throws UcumException  {
		Decimal d = new Decimal(i);
		check(d.asInteger() == i, "Failed to round trip the integer "+Integer.toString(i));
	}
	
	private void check(boolean b, String msg) {
	  if (!b)
	  	throw new Error(msg);	  
  }
	
	private void runConversion(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				if (Utilities.isWhitespace(xpp.getText()))
					xpp.next();
				else
					throw new XmlPullParserException("Unexpected text "+xpp.getText());
			} else if (xpp.getName().equals("case")) 
				runConversionCase(xpp);
			else 
				throw new XmlPullParserException("unknown element name "+xpp.getName());
		}
		xpp.next();
  }

	private void runConversionCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		
	  String id = xpp.getAttributeValue(null, "id");
	  String value = xpp.getAttributeValue(null, "value");
	  String srcUnit = xpp.getAttributeValue(null, "srcUnit");
	  String dstUnit = xpp.getAttributeValue(null, "dstUnit");
	  String outcome = xpp.getAttributeValue(null, "outcome");
	  System.out.println("case "+id+": "+value+" "+srcUnit+" -> "+outcome+" "+dstUnit);
	  Decimal res = ucumSvc.convert(new Decimal(value), srcUnit, dstUnit);
		debug("Convert Test "+id+": the value '"+value+" "+srcUnit+"' ==> "+res.toString()+" "+dstUnit);

	  // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
		if (res.comparesTo(new Decimal(outcome)) != 0) {
	  	errCount++;
	  	System.err.println("Test "+id+": The value '"+outcome+"' was expected the result was "+res.toString());
	  }
 		while (xpp.getEventType() != XmlPullParser.END_TAG) 
	    xpp.next();
 		xpp.next();
  }
	
	
	private void debug(String string) {
		System.out.println(string);
	  
  }
	private void runDisplayNameGeneration(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				if (Utilities.isWhitespace(xpp.getText()))
					xpp.next();
				else
					throw new XmlPullParserException("Unexpected text "+xpp.getText());
			} else if (xpp.getName().equals("case")) 
				runDisplayNameGenerationCase(xpp);
			else 
				throw new XmlPullParserException("unknown element name "+xpp.getName());
		}
		xpp.next();
  }

	private void runDisplayNameGenerationCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
	  String id = xpp.getAttributeValue(null, "id");
	  String unit = xpp.getAttributeValue(null, "unit");
	  String display = xpp.getAttributeValue(null, "display");
	  
	  String res = ucumSvc.analyse(unit);
		debug("Analyse Test "+id+": the unit '"+unit+"' ==> "+res);

	  if (!res.equals(display)) {
	  	errCount++;
	  	System.err.println("Test "+id+": The unit '"+unit+"' was expected to be displayed as '"+display+"', but was displayed as "+res);
	  }
 		while (xpp.getEventType() != XmlPullParser.END_TAG) 
	    xpp.next();
 		xpp.next();
  }
	
	private void runValidationTests(XmlPullParser xpp) throws XmlPullParserException, IOException  {
		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.TEXT) {
				if (Utilities.isWhitespace(xpp.getText()))
					xpp.next();
				else
					throw new XmlPullParserException("Unexpected text "+xpp.getText());
			} else if (xpp.getName().equals("case")) 
				runValidationCase(xpp);
			else 
				throw new XmlPullParserException("unknown element name "+xpp.getName());
		}
		xpp.next();
  }
	
	private void runValidationCase(XmlPullParser xpp) throws XmlPullParserException, IOException  {
	  String id = xpp.getAttributeValue(null, "id");
	  String unit = xpp.getAttributeValue(null, "unit");
	  boolean valid = "true".equals(xpp.getAttributeValue(null, "valid"));
	  String reason = xpp.getAttributeValue(null, "reason");
	  
	  String res = ucumSvc.validate(unit);
		boolean result = res == null;
		if (result)
		  debug("Validation Test "+id+": the unit '"+unit+"' is valid");
		else
		  debug("Validation Test "+id+": the unit '"+unit+"' is not valid because "+res);

	  if (valid != result) {
	  	errCount++;
	  	if (valid)
	  		System.err.println("Test "+id+": The unit '"+unit+"' was expected to be valid, but wasn't accepted");
	  	else
	  		System.err.println("Test "+id+": The unit '"+unit+"' was expected to be invalid because '"+reason+"', but was accepted");
	  }
 		while (xpp.getEventType() != XmlPullParser.END_TAG) 
	    xpp.next();
 		xpp.next();
  }
	
	private void skipElement(XmlPullParser xpp) throws XmlPullParserException, IOException  {
		xpp.next();
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() == XmlPullParser.START_TAG)
				skipElement(xpp);
			else 
				xpp.next();
		}
		xpp.next();
  }


}
