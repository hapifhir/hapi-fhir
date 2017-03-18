package org.hl7.fhir.convertors;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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



/*
  Copyright (c) 2011+, HL7, Inc.
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


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Factory;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Range;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Timing;
import org.hl7.fhir.dstu3.model.Timing.EventTiming;
import org.hl7.fhir.dstu3.model.Timing.TimingRepeatComponent;
import org.hl7.fhir.dstu3.model.Timing.UnitsOfTime;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.OIDUtils;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.ucum.UcumService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Convert {

	private CDAUtilities cda;
	private UcumService ucumSvc;
	private Set<String> oids = new HashSet<String>();
	private String defaultTimezone;
	private boolean generateMissingExtensions;
	
	public Convert(CDAUtilities cda, UcumService ucumSvc, String defaultTimezone) {
		super();
		this.cda = cda;
		this.ucumSvc = ucumSvc;
		this.defaultTimezone = defaultTimezone;
	}
	
	public Identifier makeIdentifierFromII(Element e) throws Exception {
		Identifier id = new Identifier();
		String r = e.getAttribute("root");
		String ex;
		if (e.hasAttribute("extension") && Utilities.noString(e.getAttribute("extension"))) {
			if (generateMissingExtensions) 
				ex = UUID.randomUUID().toString();
			else
				throw new Exception("Broken identifier - extension is blank");
		} else 
			ex = e.getAttribute("extension");
      
		if (Utilities.noString(ex)) {
			id.setSystem("urn:ietf:rfc:3986");
			if (isGuid(r)) 
				id.setValue("urn:uuid:"+r);
			else if (UriForOid(r) != null)
				id.setValue(UriForOid(r));
			else 
				id.setValue(UriForOid(r));
		} else {
			if (isGuid(r)) 
				id.setSystem("urn:uuid:"+r);
			else if (UriForOid(r) != null)
				id.setSystem(UriForOid(r));
			else 
				id.setSystem("urn:oid:"+r);
			id.setValue(ex);
		}
		return id;
	}

	public String makeURIfromII(Element e) {
		String r = e.getAttribute("root");
		if (Utilities.noString(e.getAttribute("extension"))) {
			if (isGuid(r)) 
				return "urn:uuid:"+r;
			else if (UriForOid(r) != null)
				return UriForOid(r);
			else 
				return UriForOid(r);
		} else {
			if (isGuid(r)) 
				return "urn:uuid:"+r+"::"+e.getAttribute("extension");
			else if (UriForOid(r) != null)
				return UriForOid(r)+"::"+e.getAttribute("extension");
			else 
				return "urn:oid:"+r+"::"+e.getAttribute("extension");
		}
  }
	
	private String UriForOid(String r) {
		String uri = OIDUtils.getUriForOid(r);
		if (uri != null)
			return uri;
		else {
			oids.add(r);
			return "urn:oid:"+r;
		}
	}

	public boolean isGuid(String r) {
		return r.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
	}

	public InstantType makeInstantFromTS(Element child) throws Exception {
	  InstantType i = InstantType.parseV3(child.getAttribute("value"));
	  return i;
  }

	public CodeableConcept makeCodeableConceptFromCD(Element cv) throws Exception {
		if (cv == null)
			return null;
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding(makeCodingFromCV(cv));
		for (Element e : cda.getChildren(cv, "translation"))
			cc.addCoding(makeCodingFromCV(e));
		if (cda.getChild(cv, "originalText") != null) {
			Element ote = cda.getChild(cv, "originalText");
//			if (cda.getChild(ote, "reference") != null) {
//				if (cda.getChild(ote, "reference").getAttribute("value").startsWith("#")) {
//					Element t = cda.getByXmlId(cda.getChild(ote, "reference").getAttribute("value").substring(1));
//					String ot = t.getTextContent().trim();
//					cc.setText(Utilities.noString(ot) ? null : ot);
//				} else
//					throw new Exception("external references not handled yet "+cda.getChild(ote, "reference").getAttribute("value"));
//			} else {	  		
				String ot = ote.getTextContent().trim();
				cc.setText(Utilities.noString(ot) ? null : ot);
	  	//}  
	  }
	  return cc;
  }

	public Coding makeCodingFromCV(Element cd) throws Exception {
		if (cd == null || Utilities.noString(cd.getAttribute("code")))
			return null;
	  Coding c = new Coding();
	  c.setCode(cd.getAttribute("code"));
	  c.setDisplay(cd.getAttribute("displayName"));
	  String r = cd.getAttribute("codeSystem");
	  String uri = getUriForOID(r);
	  if (uri != null)
	  	c.setSystem(uri);
	  else if (isGuid(r)) 
			c.setSystem("urn:uuid:"+r);
		else if (UriForOid(r) != null)
			c.setSystem(UriForOid(r));
		else 
			c.setSystem("urn:oid:"+r);
	  return c;
  }

	private String getUriForOID(String r) {
		if (r.equals("2.16.840.1.113883.6.1"))
			return "http://loinc.org";
		if (r.equals("2.16.840.1.113883.6.96"))
			return "http://snomed.info/sct";
	  return null;
  }

	public Address makeAddressFromAD(Element e) {
		if (e == null)
			return null;
	  Address a = new Address();
  	String use = e.getAttribute("use");
	  if (use != null) {
	  	if (use.equals("H") || use.equals("HP") || use.equals("HV"))
	  		a.setUse(AddressUse.HOME);
	  	else if (use.equals("WP") || use.equals("DIR") || use.equals("PUB"))
	  		a.setUse(AddressUse.WORK);
	  	else if (use.equals("TMP"))
	  		a.setUse(AddressUse.TEMP);
	  	else if (use.equals("BAD"))
	  		a.setUse(AddressUse.OLD);
	  }
	  Node n = e.getFirstChild();
	  while (n != null) {
	  	if (n.getNodeType() == Node.ELEMENT_NODE) {
	  		String v = n.getTextContent();
	  		if (n.getLocalName().equals("additionalLocator"))
	  			a.getLine().add(makeString(v));
//	  		else if (e.getLocalName().equals("unitID"))
//	  			else if (e.getLocalName().equals("unitType"))
	  			else if (n.getLocalName().equals("deliveryAddressLine"))
		  			a.getLine().add(makeString(v));
//	  			else if (e.getLocalName().equals("deliveryInstallationType"))
//	  			else if (e.getLocalName().equals("deliveryInstallationArea"))
//	  			else if (e.getLocalName().equals("deliveryInstallationQualifier"))
//	  			else if (e.getLocalName().equals("deliveryMode"))
//	  			else if (e.getLocalName().equals("deliveryModeIdentifier"))
	  			else if (n.getLocalName().equals("streetAddressLine"))
		  			a.getLine().add(makeString(v));
//	  			else if (e.getLocalName().equals("houseNumber"))
//	  			else if (e.getLocalName().equals("buildingNumberSuffix"))
//	  			else if (e.getLocalName().equals("postBox"))
//	  			else if (e.getLocalName().equals("houseNumberNumeric"))
//	  			else if (e.getLocalName().equals("streetName"))
//	  			else if (e.getLocalName().equals("streetNameBase"))
//	  			else if (e.getLocalName().equals("streetNameType"))
	  			else if (n.getLocalName().equals("direction"))
		  			a.getLine().add(makeString(v));
	  			else if (n.getLocalName().equals("careOf"))
		  			a.getLine().add(makeString(v));
//	  			else if (e.getLocalName().equals("censusTract"))
	  			else if (n.getLocalName().equals("country"))
		  			a.setCountry(v);
	  			//else if (e.getLocalName().equals("county"))
	  			else if (n.getLocalName().equals("city"))
		  			a.setCity(v);
//	  			else if (e.getLocalName().equals("delimiter"))
//	  			else if (e.getLocalName().equals("precinct"))
	  			else if (n.getLocalName().equals("state"))
		  			a.setState(v);
	  			else if (n.getLocalName().equals("postalCode"))
		  			a.setPostalCode(v);
	  	}  		
	  	n = n.getNextSibling();
	  }
	  return a;
  }

	public StringType makeString(String v) {
	  StringType s = new StringType();
	  s.setValue(v);
	  return s;
  }

	public ContactPoint makeContactFromTEL(Element e) throws Exception {
		if (e == null)
			return null;
		if (e.hasAttribute("nullFlavor"))
			return null;
	  ContactPoint c = new ContactPoint();
  	String use = e.getAttribute("use");
	  if (use != null) {
	  	if (use.equals("H") || use.equals("HP") || use.equals("HV"))
	  		c.setUse(ContactPointUse.HOME);
	  	else if (use.equals("WP") || use.equals("DIR") || use.equals("PUB"))
	  		c.setUse(ContactPointUse.WORK);
	  	else if (use.equals("TMP"))
	  		c.setUse(ContactPointUse.TEMP);
	  	else if (use.equals("BAD"))
	  		c.setUse(ContactPointUse.OLD);
	  }
	  if (e.getAttribute("value") != null) {
	  	String[] url = e.getAttribute("value").split(":");
	  	if (url.length == 1) {
	  		c.setValue(url[0].trim());
	  		c.setSystem(ContactPointSystem.PHONE);
	  	} else {
	  		if (url[0].equals("tel"))
	  			c.setSystem(ContactPointSystem.PHONE);
	  		else if (url[0].equals("mailto"))
	  			c.setSystem(ContactPointSystem.EMAIL);
	  		else if (e.getAttribute("value").contains(":"))
	  			c.setSystem(ContactPointSystem.OTHER);
	  		else 
	  			c.setSystem(ContactPointSystem.PHONE);
	  		c.setValue(url[1].trim());
	  	}
	  }
	  return c;
	  
  }

	public HumanName makeNameFromEN(Element e) {
		if (e == null)
			return null;
	  HumanName hn = new HumanName();
  	String use = e.getAttribute("use");
	  if (use != null) {
	  	if (use.equals("L"))
	  		hn.setUse(NameUse.USUAL);
	  	else if (use.equals("C"))
	  		hn.setUse(NameUse.OFFICIAL);
	  	else if (use.equals("P") || use.equals("A"))
	  		hn.setUse(NameUse.ANONYMOUS);
	  	else if (use.equals("TMP"))
	  		hn.setUse(NameUse.TEMP);
	  	else if (use.equals("BAD"))
	  		hn.setUse(NameUse.OLD);
	  }
	   
	  Node n = e.getFirstChild();
	  while (n != null) {
	  	if (n.getNodeType() == Node.ELEMENT_NODE) {
	  		String v = n.getTextContent();
	  		if (n.getLocalName().equals("family"))
	  			hn.setFamilyElement(makeString(v));
   			else if (n.getLocalName().equals("given"))
   				hn.getGiven().add(makeString(v));
  			else if (n.getLocalName().equals("prefix"))
  				hn.getPrefix().add(makeString(v));
  			else if (n.getLocalName().equals("suffix"))
  				hn.getSuffix().add(makeString(v));
	  	}  		
	  	n = n.getNextSibling();
	  }
	  return hn;
  }

	public DateTimeType makeDateTimeFromTS(Element ts) throws Exception {
		if (ts == null)
			return null;
		
    String v = ts.getAttribute("value");
    if (Utilities.noString(v))
    	return null;
    
    if (v.length() > 8 && !hasTimezone(v))
    	v += defaultTimezone;
    DateTimeType d = DateTimeType.parseV3(v);
    return d;
  }

	private boolean hasTimezone(String v) {
	  return v.contains("+") || v.contains("-") || v.endsWith("Z");
  }


	public DateType makeDateFromTS(Element ts) throws Exception {
		if (ts == null)
			return null;
		
    String v = ts.getAttribute("value");
    if (Utilities.noString(v))
    	return null;
    DateType d = DateType.parseV3(v);
    return d;
  }

	public Period makePeriodFromIVL(Element ivl) throws Exception {
	  if (ivl == null)
	  	return null;
	  Period p = new Period();
	  Element low = cda.getChild(ivl, "low");
		if (low != null)
	  	p.setStartElement(makeDateTimeFromTS(low));
	  Element high = cda.getChild(ivl, "high");
		if (high != null)
	  	p.setEndElement(makeDateTimeFromTS(high));
	  
		if (p.getStartElement() != null || p.getEndElement() != null)
	    return p;
		else
			return null;
  }

	// this is a weird one - where CDA has an IVL, and FHIR has a date
	public DateTimeType makeDateTimeFromIVL(Element ivl) throws Exception {
	  if (ivl == null)
	  	return null;
	  if (ivl.hasAttribute("value")) 
	  	return makeDateTimeFromTS(ivl);
	  Element high =  cda.getChild(ivl, "high");
	  if (high != null)
	  	return makeDateTimeFromTS(high);
	  Element low =  cda.getChild(ivl, "low");
	  if (low != null)
	  	return makeDateTimeFromTS(low);
	  return null;
  }

	public Type makeStringFromED(Element e) throws Exception {
		if (e == null)
			return null;
		if (cda.getChild(e, "reference") != null) {
			if (cda.getChild(e, "reference").getAttribute("value").startsWith("#")) {
				Element t = cda.getByXmlId(cda.getChild(e, "reference").getAttribute("value").substring(1));
				String ot = t.getTextContent().trim();
				return Utilities.noString(ot) ? null : Factory.newString_(ot);
			} else
				throw new Exception("external references not handled yet "+cda.getChild(e, "reference").getAttribute("value"));
		}
		return Factory.newString_(e.getTextContent());
  }

	public Type makeTypeFromANY(Element e) throws Exception {
		if (e == null)
			return null;
	  String t = e.getAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "type");
	  if (Utilities.noString(t))
	  	throw new Exception("Missing type on RIM attribute with type any");
	  if (t.equals("CD") || t.equals("CE"))
	  	return makeCodeableConceptFromCD(e);
	  else if (t.equals("ST"))
	  	return makeStringFromED(e);
	  else
	  	throw new Exception("Not done yet (type = "+t+")");
  }

	public Type makeMatchingTypeFromIVL(Element ivl) throws Exception {
		if (ivl == null)
			return null;
	  if (ivl.getAttribute("value") != null)
	  	return makeDateTimeFromIVL(ivl);
	  if (cda.getChild(ivl, "low") != null || cda.getChild(ivl, "high") != null )
	    return makePeriodFromIVL(ivl);
	  throw new Exception("not handled yet");
  }

	public Type makeCodeableConceptFromNullFlavor(String nf) throws Exception {
	  // Some nullFlavors have explicit values in value sets. This can only be called where there aren't. 
	  if (nf == null || "".equals(nf))
	  	return null;
	  if ("NI".equals(nf))
	  	return null; // there's no code for this
	  if ("NA".equals(nf))
	  	return Factory.newCodeableConcept("unsupported", "http://hl7.org/fhir/data-absent-reason", "Unsupported"); // todo: is this reasonable? Why else would you use N/A?
	  if ("UNK".equals(nf))
	  	return Factory.newCodeableConcept("unknown", "http://hl7.org/fhir/data-absent-reason", "Unknown"); 
	  if ("ASKU".equals(nf))
	  	return Factory.newCodeableConcept("asked", "http://hl7.org/fhir/data-absent-reason", "Asked/Unknown"); 
	  if ("NAV".equals(nf))
	  	return Factory.newCodeableConcept("temp", "http://hl7.org/fhir/data-absent-reason", "Temporarily Unavailable"); 
	  if ("NASK".equals(nf))
	  	return Factory.newCodeableConcept("notasked", "http://hl7.org/fhir/data-absent-reason", "Not Asked"); 
	  if ("MSK".equals(nf))
	  	return Factory.newCodeableConcept("masked", "http://hl7.org/fhir/data-absent-reason", "Masked"); 
	  if ("OTH".equals(nf))
	  	return null; // well, what should be done? 
  	return null; // well, what should be done? 
	  	
  }

	public Range makeRangeFromIVLPQ(Element ivlpq) throws Exception {
		if (ivlpq == null)
	    return null;
		Element low = cda.getChild(ivlpq, "low");
		Element high = cda.getChild(ivlpq, "high");
		if (low == null && high == null)
			return null;
		Range r = new Range();
		r.setLow(makeSimpleQuantityFromPQ(low, ivlpq.getAttribute("unit")));
		r.setHigh(makeSimpleQuantityFromPQ(high, ivlpq.getAttribute("unit")));
		return r;
	}
	
	public Quantity makeQuantityFromPQ(Element pq) throws Exception {
		return makeQuantityFromPQ(pq, null);
  }

	public Quantity makeQuantityFromPQ(Element pq, String units) throws Exception {
		if (pq == null)
	    return null;
		Quantity qty = new Quantity();
		String n = pq.getAttribute("value").replace(",", "").trim();
		try {
		  qty.setValue(new BigDecimal(n));
		} catch (Exception e) {
			throw new Exception("Unable to process value '"+n+"'", e);
		}			
		units = Utilities.noString(pq.getAttribute("unit")) ? units : pq.getAttribute("unit");
		if (!Utilities.noString(units)) {
			if (ucumSvc == null || ucumSvc.validate(units) != null)
				qty.setUnit(units);
			else {
				qty.setCode(units);
				qty.setSystem("http://unitsofmeasure.org");
				qty.setUnit(ucumSvc.getCommonDisplay(units));
			}
		}
		return qty;		
  }

	public SimpleQuantity makeSimpleQuantityFromPQ(Element pq, String units) throws Exception {
		if (pq == null)
	    return null;
		SimpleQuantity qty = new SimpleQuantity();
		String n = pq.getAttribute("value").replace(",", "").trim();
		try {
		  qty.setValue(new BigDecimal(n));
		} catch (Exception e) {
			throw new Exception("Unable to process value '"+n+"'", e);
		}			
		units = Utilities.noString(pq.getAttribute("unit")) ? units : pq.getAttribute("unit");
		if (!Utilities.noString(units)) {
			if (ucumSvc == null || ucumSvc.validate(units) != null)
				qty.setUnit(units);
			else {
				qty.setCode(units);
				qty.setSystem("http://unitsofmeasure.org");
				qty.setUnit(ucumSvc.getCommonDisplay(units));
			}
		}
		return qty;		
  }

	public AdministrativeGender makeGenderFromCD(Element cd) throws Exception {
	  String code = cd.getAttribute("code");
	  String system = cd.getAttribute("codeSystem");
	  if ("2.16.840.1.113883.5.1".equals(system)) {
	  	if ("F".equals(code))
	  		return AdministrativeGender.FEMALE;
	  	if ("M".equals(code))
	  		return AdministrativeGender.MALE;
	  }
	  throw new Exception("Unable to read Gender "+system+"::"+code);
  }

	/*
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:EIVL_TS]: 389
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:EIVL_TS]/event: 389
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:IVL_TS]: 33470
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:IVL_TS]/high: 20566
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:IVL_TS]/high[nullFlavor:NA]: 9581
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:IVL_TS]/low: 32501
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:IVL_TS]/low[nullFlavor:UNK]: 969
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:PIVL_TS]: 17911
  /entry[COMP]/substanceAdministration[SBADM,EVN]/effectiveTime[type:PIVL_TS]/period: 17911
   */
	public Type makeSomethingFromGTS(List<Element> children) throws Exception {
		if (children.isEmpty())
			return null;
		if (children.size() == 1) {
			String type = children.get(0).getAttribute("xsi:type");
			if (type.equals("IVL_TS"))
				return makePeriodFromIVL(children.get(0));
			else
				throw new Exception("Unknown GTS type '"+type+"'");
		}
		CommaSeparatedStringBuilder t = new CommaSeparatedStringBuilder();
		for (Element c : children)
			t.append(c.getAttribute("xsi:type"));
		if (t.toString().equals("IVL_TS, PIVL_TS"))
			return makeTimingFromBoundedPIVL(children.get(0), children.get(1));
		if (t.toString().equals("IVL_TS, EIVL_TS"))
			return makeTimingFromBoundedEIVL(children.get(0), children.get(1));
  	throw new Exception("Unknown GTS pattern '"+t.toString()+"'");
  }

	private Type makeTimingFromBoundedEIVL(Element ivl, Element eivl) throws Exception {
	  Timing t = new Timing();
	  t.setRepeat(new TimingRepeatComponent());
	  Element e = cda.getChild(eivl, "event");
	  t.getRepeat().setBounds(makePeriodFromIVL(ivl));
	  t.getRepeat().addWhen(convertEventTiming(e.getAttribute("code")));
	  return t;
  }

	private EventTiming convertEventTiming(String e) throws Exception {
	  if ("HS".equals(e))
	  	return EventTiming.HS;
	  throw new Exception("Unknown event "+e);
  }

	private Timing makeTimingFromBoundedPIVL(Element ivl, Element pivl) throws Exception {
	  Timing t = new Timing();
	  t.setRepeat(new TimingRepeatComponent());
	  Element p = cda.getChild(pivl, "period");
	  t.getRepeat().setBounds(makePeriodFromIVL(ivl));
	  t.getRepeat().setPeriod(new BigDecimal(p.getAttribute("value")));
	  t.getRepeat().setPeriodUnit(convertTimeUnit(p.getAttribute("unit")));
	  return t;
  }

	private UnitsOfTime convertTimeUnit(String u) throws Exception {
	  if ("h".equals(u))
	  	return UnitsOfTime.H;
	  if ("d".equals(u))
	  	return UnitsOfTime.D;
	  if ("w".equals(u))
	  	return UnitsOfTime.WK;
	  throw new Exception("Unknown unit of time "+u);
  }

	public Set<String> getOids() {
		return oids;
	}

	public boolean isGenerateMissingExtensions() {
		return generateMissingExtensions;
	}

	public void setGenerateMissingExtensions(boolean generateMissingExtensions) {
		this.generateMissingExtensions = generateMissingExtensions;
	}

	
}
