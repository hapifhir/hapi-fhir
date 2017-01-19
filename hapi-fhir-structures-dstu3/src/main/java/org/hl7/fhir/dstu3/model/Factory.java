package org.hl7.fhir.dstu3.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.UUID;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;

/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/



public class Factory {

  public static IdType newId(String value) {
    if (value == null)
      return null;
    IdType res = new IdType();
    res.setValue(value);
    return res;
	}

  public static StringType newString_(String value) {
    if (value == null)
      return null;
    StringType res = new StringType();
    res.setValue(value);
    return res;
  }

  public static UriType newUri(String value) throws URISyntaxException {
    if (value == null)
      return null;
    UriType res = new UriType();
    res.setValue(value);
    return res;
  }

  public static DateTimeType newDateTime(String value) throws ParseException {
    if (value == null)
      return null;
    return new DateTimeType(value);
  }

  public static DateType newDate(String value) throws ParseException {
    if (value == null)
      return null;
    return new DateType(value);
  }

  public static CodeType newCode(String value) {
    if (value == null)
      return null;
    CodeType res = new CodeType();
    res.setValue(value);
    return res;
  }

  public static IntegerType newInteger(int value) {
    IntegerType res = new IntegerType();
    res.setValue(value);
    return res;
  }
  
  public static IntegerType newInteger(java.lang.Integer value) {
    if (value == null)
      return null;
    IntegerType res = new IntegerType();
    res.setValue(value);
    return res;
  }
  
  public static BooleanType newBoolean(boolean value) {
    BooleanType res = new BooleanType();
    res.setValue(value);
    return res;
  }
  
  public static ContactPoint newContactPoint(ContactPointSystem system, String value) {
  	ContactPoint res = new ContactPoint();
	res.setSystem(system);
	res.setValue(value);
	return res;
  }

	public static Extension newExtension(String uri, Type value, boolean evenIfNull) {
		if (!evenIfNull && (value == null || value.isEmpty()))
			return null;
		Extension e = new Extension();
		e.setUrl(uri);
		e.setValue(value);
	  return e;
  }

	public static CodeableConcept newCodeableConcept(String code, String system, String display) {
		CodeableConcept cc = new CodeableConcept();
		Coding c = new Coding();
		c.setCode(code);
		c.setSystem(system);
		c.setDisplay(display);
		cc.getCoding().add(c);
	  return cc;
  }

	public static Reference makeReference(String url) {
	  Reference rr = new Reference();
	  rr.setReference(url);
	  return rr;
	}

	public static Narrative newNarrative(NarrativeStatus status, String html) throws IOException, FHIRException {
		Narrative n = new Narrative();
		n.setStatus(status);
		try {
			n.setDiv(new XhtmlParser().parseFragment("<div>"+Utilities.escapeXml(html)+"</div>"));
		} catch (org.hl7.fhir.exceptions.FHIRException e) {
			throw new FHIRException(e.getMessage(), e);
		}
		return n;
	}

	public static Coding makeCoding(String code) throws FHIRException {
		String[] parts = code.split("\\|");
		Coding c = new Coding();
		if (parts.length == 2) {
			c.setSystem(parts[0]);
			c.setCode(parts[1]);
		} else if (parts.length == 3) {
			c.setSystem(parts[0]);
			c.setCode(parts[1]);
			c.setDisplay(parts[2]);
		} else 
			throw new FHIRException("Unable to understand the code '"+code+"'. Use the format system|code(|display)");
		return c;
	}

	public static Reference makeReference(String url, String text) {
		Reference rr = new Reference();
		rr.setReference(url);
		if (!Utilities.noString(text))
			rr.setDisplay(text);
		return rr;
	}

  public static String createUUID() {
    return "urn:uuid:"+UUID.randomUUID().toString().toLowerCase();
  }

  public Type create(String name) throws FHIRException {
    if (name.equals("boolean"))
      return new BooleanType();
    else if (name.equals("integer"))
      return new IntegerType();
    else if (name.equals("decimal"))
      return new DecimalType();
    else if (name.equals("base64Binary"))
      return new Base64BinaryType();
    else if (name.equals("instant"))
      return new InstantType();
    else if (name.equals("string"))
      return new StringType();
    else if (name.equals("uri"))
      return new UriType();
    else if (name.equals("date"))
      return new DateType();
    else if (name.equals("dateTime"))
      return new DateTimeType();
    else if (name.equals("time"))
      return new TimeType();
    else if (name.equals("code"))
      return new CodeType();
    else if (name.equals("oid"))
      return new OidType();
    else if (name.equals("id"))
      return new IdType();
    else if (name.equals("unsignedInt"))
      return new UnsignedIntType();
    else if (name.equals("positiveInt"))
      return new PositiveIntType();
    else if (name.equals("markdown"))
      return new MarkdownType();
    else if (name.equals("Annotation"))
      return new Annotation();
    else if (name.equals("Attachment"))
      return new Attachment();
    else if (name.equals("Identifier"))
      return new Identifier();
    else if (name.equals("CodeableConcept"))
      return new CodeableConcept();
    else if (name.equals("Coding"))
      return new Coding();
    else if (name.equals("Quantity"))
      return new Quantity();
    else if (name.equals("Range"))
      return new Range();
    else if (name.equals("Period"))
      return new Period();
    else if (name.equals("Ratio"))
      return new Ratio();
    else if (name.equals("SampledData"))
      return new SampledData();
    else if (name.equals("Signature"))
      return new Signature();
    else if (name.equals("HumanName"))
      return new HumanName();
    else if (name.equals("Address"))
      return new Address();
    else if (name.equals("ContactPoint"))
      return new ContactPoint();
    else if (name.equals("Timing"))
      return new Timing();
    else if (name.equals("Reference"))
      return new Reference();
    else if (name.equals("Meta"))
      return new Meta();
    else
      throw new FHIRException("Unknown data type name "+name);
  }
}
