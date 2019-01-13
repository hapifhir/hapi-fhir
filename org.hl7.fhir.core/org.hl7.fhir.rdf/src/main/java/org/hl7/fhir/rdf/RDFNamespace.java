package org.hl7.fhir.rdf;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;


/**
 * An RDF Namespace
 */
public class RDFNamespace {

    private String prefix;
    private String URI;

    public RDFNamespace(String prefix, String uri) {
        this.prefix = prefix;
        this.URI = uri;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getURI() {
        return URI;
    }

    public void addNsPrefix(Model model) {
        model.setNsPrefix(prefix, URI);
    }

    public String uriFor(String name) {
        return URI + name;
    }

    public Resource resourceRef(String name) {
        return ResourceFactory.createResource(uriFor(name));
    }

    public Property property(String name) {
        return ResourceFactory.createProperty(uriFor(name));
    }


    /*
     *  FHIR specific namespaces
     */
    public static final RDFNamespace FHIR = new RDFNamespace("fhir", "http://hl7.org/fhir/");
    public static final RDFNamespace FHIR_VS = new RDFNamespace("fhir-vs", "http://hl7.org/fhir/ValueSet/");
    public static final RDFNamespace EX = new RDFNamespace("ex", "http://hl7.org/fhir/StructureDefinition/");
    public static final RDFNamespace RIM = new RDFNamespace("rim", "http://hl7.org/owl/rim/");
    public static final RDFNamespace CS = new RDFNamespace("cs", "http://hl7.org/orim/codesystem/");
    public static final RDFNamespace VS = new RDFNamespace("vs", "http://hl7.org/orim/valueset/");
    public static final RDFNamespace DT = new RDFNamespace("dt", "http://hl7.org/orim/datatype/");
    public static final RDFNamespace LOINC = new RDFNamespace("loinc", "http://loinc.org/rdf#");
    public static final RDFNamespace W5 = new RDFNamespace("w5", "http://hl7.org/fhir/w5#");

    // For some reason these aren't included in the XSD and RDF namespaces -- do we need to update Jena library?
    public static final Property XSDpattern;
    public static final Resource RDFXMLLiteral;
    static {
        XSDpattern = ResourceFactory.createProperty(XSD.getURI() + "pattern");
        RDFXMLLiteral = ResourceFactory.createResource(RDF.getURI() + "XMLLiteral");
    }

    /**
     * Add the FHIR namespaces to the RDF model
     * @param model model to add namespaces to
     */
    public static void addFHIRNamespaces(Model model) {
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("rdfs", RDFS.getURI());
        FHIR.addNsPrefix(model);
        W5.addNsPrefix(model);
        FHIR_VS.addNsPrefix(model);
        EX.addNsPrefix(model);
        model.setNsPrefix("xsd", XSD.getURI());
        model.setNsPrefix("owl", OWL2.getURI());
        model.setNsPrefix("dc", DC_11.getURI());
        model.setNsPrefix("dcterms", DCTerms.getURI());
        RIM.addNsPrefix(model);
        CS.addNsPrefix(model);
        VS.addNsPrefix(model);
        DT.addNsPrefix(model);
        LOINC.addNsPrefix(model);
    }
}
