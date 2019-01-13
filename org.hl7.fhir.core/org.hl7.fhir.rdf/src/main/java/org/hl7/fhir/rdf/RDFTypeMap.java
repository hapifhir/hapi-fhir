package org.hl7.fhir.rdf;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.XSD;


public class RDFTypeMap {
    /**
     * FHIR to XSD data type map.
     */
    static public final Map<String, Resource> ptMap = new HashMap<String, Resource>();
    static public final Map<Resource, Resource> owlTypeMap = new HashMap<Resource, Resource>();

    static {
        ptMap.put("base64Binary", XSD.base64Binary);
        ptMap.put("boolean", XSD.xboolean);
	    ptMap.put("code", XSD.xstring);
        ptMap.put("date", XSD.date);
        ptMap.put("dateTime", XSD.dateTime);
        ptMap.put("gYear", XSD.gYear);
        ptMap.put("gYearMonth", XSD.gYearMonth);
        ptMap.put("decimal", XSD.decimal);
        ptMap.put("instant", XSD.dateTime);
	    ptMap.put("id", XSD.xstring);
        ptMap.put("int", XSD.integer);
        ptMap.put("integer", XSD.integer);
        ptMap.put("markdown", XSD.xstring);
        ptMap.put("oid", XSD.anyURI);
        ptMap.put("positiveInt", XSD.positiveInteger);
        ptMap.put("string", XSD.xstring);
        ptMap.put("time", XSD.time);
        ptMap.put("unsignedInt", XSD.nonNegativeInteger);
        ptMap.put("uri", XSD.anyURI);
        ptMap.put("url", XSD.anyURI);
        ptMap.put("canonical", XSD.anyURI);
        ptMap.put("uuid", XSD.anyURI);
        ptMap.put("anyURI", XSD.anyURI);
        ptMap.put("token", RDFNamespace.FHIR.resourceRef("token"));
        ptMap.put("nonNegativeInteger", XSD.nonNegativeInteger);
        ptMap.put("positiveInteger", XSD.positiveInteger);

        owlTypeMap.put(XSD.gYear, XSD.dateTime);
        owlTypeMap.put(XSD.gYearMonth, XSD.dateTime);
        owlTypeMap.put(XSD.date, XSD.dateTime);
        owlTypeMap.put(XSD.time, XSD.xstring);
        owlTypeMap.put(RDFNamespace.FHIR.resourceRef("xhtml"), XSD.xstring);
    }

    public static Resource xsd_type_for(String type, boolean owl_types_required) {
        // TODO: find why namespaces are part of some of these types...
        String key = type.startsWith("xs:")? type.substring(3) : type;
        Resource rval = ptMap.containsKey(key) ? ptMap.get(key) : RDFNamespace.FHIR.resourceRef(key);
        if(owl_types_required && owlTypeMap.containsKey(rval))
            rval = owlTypeMap.get(rval);
        return rval;
    }
}
