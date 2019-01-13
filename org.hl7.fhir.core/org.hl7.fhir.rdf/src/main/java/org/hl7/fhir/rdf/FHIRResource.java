package org.hl7.fhir.rdf;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.hl7.fhir.utilities.Utilities;


/**
 * FHIR wrapper class for RDF Resource
 */
public class FHIRResource {
    public Resource resource;

    /**
     * Construct a named resource in the FHIR namespace
     * @param model RDF model that contains the resource
     * @param name Resource name
     * @param label Resource label
     */
    FHIRResource(Model model, String name, String label) {
        resource = model.createResource(RDFNamespace.FHIR.uriFor(name));
        addLabel(label);
    }

    /**
     * Create an anonymous resource (aka. BNode)
     */
    FHIRResource(Model model) {
        resource = model.createResource();
    }

    /**
     * Create a list resource
     * @param members Members of the list
     */
    FHIRResource(Model model, List<Resource> members) {
        resource = model.createList(members.iterator());
    }

    public FHIRResource addObjectProperty(Property p, Resource o) {
        resource.addProperty(p, o);
        return this;
    }

    public FHIRResource addObjectProperty(Property p, FHIRResource o) {
        return addObjectProperty(p, o.resource);
    }

    public FHIRResource addDataProperty(Property p, String o) {
        resource.addLiteral(p, o);
        return this;
    }

    public FHIRResource addDataProperty(Property p, String o, XSDDatatype dt) {
        resource.addLiteral(p, ResourceFactory.createTypedLiteral(o, dt));
        return this;
    }

    public FHIRResource addType(Resource type) {
        return addObjectProperty(RDF.type, type);
    }

    private FHIRResource addLabel(String name) {
        return addDataProperty(RDFS.label, name);
    }

    public FHIRResource addTitle(String title) {
        if (!Utilities.noString(title))
            addDataProperty(DC.title, title);
        return this;
    }

    public FHIRResource addDefinition(String definition) {
        if (!Utilities.noString(definition) && (!resource.hasProperty(DC.title) ||
                !resource.getProperty(DC.title).toString().equals(definition))) {
            addDataProperty(RDFS.comment, definition);
        }
        return this;
    }

    public FHIRResource domain(FHIRResource d) {
        return addObjectProperty(RDFS.domain, d);
    }

    public FHIRResource range(Resource r) {
        return addObjectProperty(RDFS.range, r);
    }

    public FHIRResource restriction(Resource restriction) {
        return addObjectProperty(RDFS.subClassOf, restriction);
    }

}

