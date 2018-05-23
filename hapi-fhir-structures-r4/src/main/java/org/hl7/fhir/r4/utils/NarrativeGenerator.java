package org.hl7.fhir.r4.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.conformance.ProfileUtilities.ProfileKnowledgeProvider;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.r4.formats.FormatUtilities;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryResponseComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntrySearchComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.ResourceInteractionComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.SystemInteractionComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.SystemRestfulInteraction;
import org.hl7.fhir.r4.model.CapabilityStatement.TypeRestfulInteraction;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemFilterComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.r4.model.CodeSystem.PropertyComponent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.CompartmentDefinition;
import org.hl7.fhir.r4.model.CompartmentDefinition.CompartmentDefinitionResourceComponent;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.OtherElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ExtensionHelper;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.HumanName.NameUse;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.SampledData;
import org.hl7.fhir.r4.model.Signature;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.Timing;
import org.hl7.fhir.r4.model.Timing.EventTiming;
import org.hl7.fhir.r4.model.Timing.TimingRepeatComponent;
import org.hl7.fhir.r4.model.Timing.UnitsOfTime;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UsageContext;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceDesignationComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.r4.model.ValueSet.FilterOperator;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionParameterComponent;
import org.hl7.fhir.r4.terminologies.CodeSystemUtilities;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r4.utils.NarrativeGenerator.ConceptMapRenderInstructions;
import org.hl7.fhir.r4.utils.NarrativeGenerator.ITypeParser;
import org.hl7.fhir.r4.utils.NarrativeGenerator.ResourceContext;
import org.hl7.fhir.r4.utils.NarrativeGenerator.UsedConceptMap;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.MarkDownProcessor;
import org.hl7.fhir.utilities.MarkDownProcessor.Dialect;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.hl7.fhir.utilities.xml.XmlGenerator;
import org.w3c.dom.Element;

public class NarrativeGenerator implements INarrativeGenerator {

  public interface ITypeParser {
    Base parseType(String xml, String type) throws FHIRFormatError, IOException, FHIRException ;
  }

  public class ConceptMapRenderInstructions {
    private String name;
    private String url;
    private boolean doDescription;
    public ConceptMapRenderInstructions(String name, String url, boolean doDescription) {
      super();
      this.name = name;
      this.url = url;
      this.doDescription = doDescription;
    }
    public String getName() {
      return name;
    }
    public String getUrl() {
      return url;
    }
    public boolean isDoDescription() {
      return doDescription;
    }
    
  }

  public class UsedConceptMap {

    private ConceptMapRenderInstructions details;
    private String link;
    private ConceptMap map;
    public UsedConceptMap(ConceptMapRenderInstructions details, String link, ConceptMap map) {
      super();
      this.details = details;
      this.link = link;
      this.map = map;
    }
    public ConceptMapRenderInstructions getDetails() {
      return details;
    }
    public ConceptMap getMap() {
      return map;
    }
    public String getLink() {
      return link;
    }    
  }

  public class ResourceContext {
    Bundle bundleResource;
    
    DomainResource resourceResource;
    
    public ResourceContext(Bundle bundle, DomainResource dr) {
      super();
      this.bundleResource = bundle;
      this.resourceResource = dr;
    }

    public ResourceContext(Element bundle, Element doc) {
    }

    public ResourceContext(org.hl7.fhir.r4.elementmodel.Element bundle, org.hl7.fhir.r4.elementmodel.Element er) {
    }

    public Resource resolve(String value) {
      if (value.startsWith("#")) {
        for (Resource r : resourceResource.getContained()) {
          if (r.getId().equals(value.substring(1)))
            return r;
        }
        return null;
      }
      if (bundleResource != null) {
        for (BundleEntryComponent be : bundleResource.getEntry()) {
          if (be.getFullUrl().equals(value))
            return be.getResource();
          if (value.equals(be.getResource().fhirType()+"/"+be.getResource().getId()))
            return be.getResource();
        }
      }
      return null;
    }

  }

  private static final String ABSTRACT_CODE_HINT = "This code is not selectable ('Abstract')";

  public interface IReferenceResolver {

    ResourceWithReference resolve(String url);

  }

  private Bundle bundle;
  private String definitionsTarget;
  private String corePath;
  private String destDir;
  private ProfileKnowledgeProvider pkp;
  private MarkDownProcessor markdown = new MarkDownProcessor(Dialect.COMMON_MARK);
  private ITypeParser parser; // when generating for an element model
  
  public boolean generate(Bundle b, boolean evenIfAlreadyHasNarrative, Set<String> outputTracker) throws EOperationOutcome, FHIRException, IOException {
    boolean res = false;
    this.bundle = b;
    for (BundleEntryComponent be : b.getEntry()) {
      if (be.hasResource() && be.getResource() instanceof DomainResource) {
        DomainResource dr = (DomainResource) be.getResource();
        if (evenIfAlreadyHasNarrative || !dr.getText().hasDiv())
          res = generate(new ResourceContext(b, dr), dr, outputTracker) || res;
      }
    }
    return res;
  }

  public boolean generate(DomainResource r, Set<String> outputTracker) throws EOperationOutcome, FHIRException, IOException {
    return generate(null, r, outputTracker);
  }
  
  public boolean generate(ResourceContext rcontext, DomainResource r, Set<String> outputTracker) throws EOperationOutcome, FHIRException, IOException {
    if (rcontext == null)
      rcontext = new ResourceContext(null, r);
    
    if (r instanceof ConceptMap) {
      return generate(rcontext, (ConceptMap) r); // Maintainer = Grahame
    } else if (r instanceof ValueSet) {
      return generate(rcontext, (ValueSet) r, true); // Maintainer = Grahame
    } else if (r instanceof CodeSystem) {
      return generate(rcontext, (CodeSystem) r, true, null); // Maintainer = Grahame
    } else if (r instanceof OperationOutcome) {
      return generate(rcontext, (OperationOutcome) r); // Maintainer = Grahame
    } else if (r instanceof CapabilityStatement) {
      return generate(rcontext, (CapabilityStatement) r);   // Maintainer = Grahame
    } else if (r instanceof CompartmentDefinition) {
      return generate(rcontext, (CompartmentDefinition) r);   // Maintainer = Grahame
    } else if (r instanceof OperationDefinition) {
      return generate(rcontext, (OperationDefinition) r);   // Maintainer = Grahame
    } else if (r instanceof StructureDefinition) {
      return generate(rcontext, (StructureDefinition) r, outputTracker);   // Maintainer = Grahame
    } else if (r instanceof ImplementationGuide) {
      return generate(rcontext, (ImplementationGuide) r);   // Maintainer = Lloyd (until Grahame wants to take over . . . :))
    } else if (r instanceof DiagnosticReport) {
      inject(r, generateDiagnosticReport(new ResourceWrapperDirect(r)),  NarrativeStatus.GENERATED);   // Maintainer = Grahame
      return true;
    } else {
      StructureDefinition p = null;
      if (r.hasMeta())
        for (UriType pu : r.getMeta().getProfile())
          if (p == null)
            p = context.fetchResource(StructureDefinition.class, pu.getValue());
      if (p == null)
        p = context.fetchResource(StructureDefinition.class, r.getResourceType().toString());
      if (p == null)
        p = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+r.getResourceType().toString().toLowerCase());
      if (p != null)
        return generateByProfile(rcontext, p, true);
      else
        return false;
    }
  }

  private interface PropertyWrapper {
    public String getName();
    public boolean hasValues();
    public List<BaseWrapper> getValues();
    public String getTypeCode();
    public String getDefinition();
    public int getMinCardinality();
    public int getMaxCardinality();
    public StructureDefinition getStructure();
    public BaseWrapper value();
  }

  private interface ResourceWrapper {
    public List<ResourceWrapper> getContained();
    public String getId();
    public XhtmlNode getNarrative() throws FHIRFormatError, IOException, FHIRException;
    public String getName();
    public List<PropertyWrapper> children();
  }

  private interface BaseWrapper {
    public Base getBase() throws UnsupportedEncodingException, IOException, FHIRException;
    public List<PropertyWrapper> children();
    public PropertyWrapper getChildByName(String tail);
  }

  private class BaseWrapperElement implements BaseWrapper {
    private Element element;
    private String type;
    private StructureDefinition structure;
    private ElementDefinition definition;
    private List<ElementDefinition> children;
    private List<PropertyWrapper> list;

    public BaseWrapperElement(Element element, String type, StructureDefinition structure, ElementDefinition definition) {
      this.element = element;
      this.type = type;
      this.structure = structure;
      this.definition = definition;
    }

    @Override
    public Base getBase() throws UnsupportedEncodingException, IOException, FHIRException {
      if (type == null || type.equals("Resource") || type.equals("BackboneElement") || type.equals("Element"))
        return null;

    String xml;
		try {
			xml = new XmlGenerator().generate(element);
		} catch (org.hl7.fhir.exceptions.FHIRException e) {
			throw new FHIRException(e.getMessage(), e);
		}
      return parseType(xml, type);
    }

    @Override
    public List<PropertyWrapper> children() {
      if (list == null) {
        children = ProfileUtilities.getChildList(structure, definition);
        list = new ArrayList<NarrativeGenerator.PropertyWrapper>();
        for (ElementDefinition child : children) {
          List<Element> elements = new ArrayList<Element>();
          XMLUtil.getNamedChildrenWithWildcard(element, tail(child.getPath()), elements);
          list.add(new PropertyWrapperElement(structure, child, elements));
        }
      }
      return list;
    }

    @Override
    public PropertyWrapper getChildByName(String name) {
      for (PropertyWrapper p : children())
        if (p.getName().equals(name))
          return p;
      return null;
    }

  }

  private class PropertyWrapperElement implements PropertyWrapper {

    private StructureDefinition structure;
    private ElementDefinition definition;
    private List<Element> values;
    private List<BaseWrapper> list;

    public PropertyWrapperElement(StructureDefinition structure, ElementDefinition definition, List<Element> values) {
      this.structure = structure;
      this.definition = definition;
      this.values = values;
    }

    @Override
    public String getName() {
      return tail(definition.getPath());
    }

    @Override
    public boolean hasValues() {
      return values.size() > 0;
    }

    @Override
    public List<BaseWrapper> getValues() {
      if (list == null) {
        list = new ArrayList<NarrativeGenerator.BaseWrapper>();
        for (Element e : values)
          list.add(new BaseWrapperElement(e, determineType(e), structure, definition));
      }
      return list;
    }
    private String determineType(Element e) {
      if (definition.getType().isEmpty())
        return null;
      if (definition.getType().size() == 1) {
        if (definition.getType().get(0).getCode().equals("Element") || definition.getType().get(0).getCode().equals("BackboneElement"))
          return null;
        return definition.getType().get(0).getCode();
      }
      String t = e.getNodeName().substring(tail(definition.getPath()).length()-3);

      if (isPrimitive(Utilities.uncapitalize(t)))
        return Utilities.uncapitalize(t);
      else
        return t;
    }

    private boolean isPrimitive(String code) {
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+code);
      return sd != null && sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE;
    }

    @Override
    public String getTypeCode() {
      if (definition == null || definition.getType().size() != 1)
        throw new Error("not handled");
      return definition.getType().get(0).getCode();
    }

    @Override
    public String getDefinition() {
      if (definition == null)
        throw new Error("not handled");
      return definition.getDefinition();
    }

    @Override
    public int getMinCardinality() {
      if (definition == null)
        throw new Error("not handled");
      return definition.getMin();
    }

    @Override
    public int getMaxCardinality() {
      if (definition == null)
        throw new Error("not handled");
      return definition.getMax().equals("*") ? Integer.MAX_VALUE : Integer.parseInt(definition.getMax());
    }

    @Override
    public StructureDefinition getStructure() {
      return structure;
    }

    @Override
    public BaseWrapper value() {
      if (getValues().size() != 1)
        throw new Error("Access single value, but value count is "+getValues().size());
      return getValues().get(0);
    }

  }

  private class BaseWrapperMetaElement implements BaseWrapper {
    private org.hl7.fhir.r4.elementmodel.Element element;
    private String type;
    private StructureDefinition structure;
    private ElementDefinition definition;
    private List<ElementDefinition> children;
    private List<PropertyWrapper> list;

    public BaseWrapperMetaElement(org.hl7.fhir.r4.elementmodel.Element element, String type, StructureDefinition structure, ElementDefinition definition) {
      this.element = element;
      this.type = type;
      this.structure = structure;
      this.definition = definition;
    }

    @Override
    public Base getBase() throws UnsupportedEncodingException, IOException, FHIRException {
      if (type == null || type.equals("Resource") || type.equals("BackboneElement") || type.equals("Element"))
        return null;

      if (element.hasElementProperty())
        return null;
      ByteArrayOutputStream xml = new ByteArrayOutputStream();
      try {
        new org.hl7.fhir.r4.elementmodel.XmlParser(context).compose(element, xml, OutputStyle.PRETTY, null);
      } catch (Exception e) {
        throw new FHIRException(e.getMessage(), e);
      }
      return parseType(xml.toString(), type); 
    }

    @Override
    public List<PropertyWrapper> children() {
      if (list == null) {
        children = ProfileUtilities.getChildList(structure, definition);
        list = new ArrayList<NarrativeGenerator.PropertyWrapper>();
        for (ElementDefinition child : children) {
          List<org.hl7.fhir.r4.elementmodel.Element> elements = new ArrayList<org.hl7.fhir.r4.elementmodel.Element>();
          String name = tail(child.getPath());
          if (name.endsWith("[x]"))
            element.getNamedChildrenWithWildcard(name, elements);
          else
            element.getNamedChildren(name, elements);
          list.add(new PropertyWrapperMetaElement(structure, child, elements));
        }
      }
      return list;
    }

    @Override
    public PropertyWrapper getChildByName(String name) {
      for (PropertyWrapper p : children())
        if (p.getName().equals(name))
          return p;
      return null;
    }

  }
  public class ResourceWrapperMetaElement implements ResourceWrapper {
    private org.hl7.fhir.r4.elementmodel.Element wrapped;
    private List<ResourceWrapper> list;
    private List<PropertyWrapper> list2;
    private StructureDefinition definition;
    public ResourceWrapperMetaElement(org.hl7.fhir.r4.elementmodel.Element wrapped) {
      this.wrapped = wrapped;
      this.definition = wrapped.getProperty().getStructure();
    }

    @Override
    public List<ResourceWrapper> getContained() {
      if (list == null) {
        List<org.hl7.fhir.r4.elementmodel.Element> children = wrapped.getChildrenByName("contained");
        list = new ArrayList<NarrativeGenerator.ResourceWrapper>();
        for (org.hl7.fhir.r4.elementmodel.Element e : children) {
          list.add(new ResourceWrapperMetaElement(e));
        }
      }
      return list;
    }

    @Override
    public String getId() {
      return wrapped.getNamedChildValue("id");
    }

    @Override
    public XhtmlNode getNarrative() throws FHIRFormatError, IOException, FHIRException {
      org.hl7.fhir.r4.elementmodel.Element txt = wrapped.getNamedChild("text");
      if (txt == null)
        return null;
      org.hl7.fhir.r4.elementmodel.Element div = txt.getNamedChild("div");
      if (div == null)
        return null;
      else
        return div.getXhtml();
    }

    @Override
    public String getName() {
      return wrapped.getName();
    }

    @Override
    public List<PropertyWrapper> children() {
      if (list2 == null) {
        List<ElementDefinition> children = ProfileUtilities.getChildList(definition, definition.getSnapshot().getElement().get(0));
        list2 = new ArrayList<NarrativeGenerator.PropertyWrapper>();
        for (ElementDefinition child : children) {
          List<org.hl7.fhir.r4.elementmodel.Element> elements = new ArrayList<org.hl7.fhir.r4.elementmodel.Element>();
          if (child.getPath().endsWith("[x]"))
            wrapped.getNamedChildrenWithWildcard(tail(child.getPath()), elements);
          else
            wrapped.getNamedChildren(tail(child.getPath()), elements);
          list2.add(new PropertyWrapperMetaElement(definition, child, elements));
        }
      }
      return list2;
    }
  }

  private class PropertyWrapperMetaElement implements PropertyWrapper {

    private StructureDefinition structure;
    private ElementDefinition definition;
    private List<org.hl7.fhir.r4.elementmodel.Element> values;
    private List<BaseWrapper> list;

    public PropertyWrapperMetaElement(StructureDefinition structure, ElementDefinition definition, List<org.hl7.fhir.r4.elementmodel.Element> values) {
      this.structure = structure;
      this.definition = definition;
      this.values = values;
    }

    @Override
    public String getName() {
      return tail(definition.getPath());
    }

    @Override
    public boolean hasValues() {
      return values.size() > 0;
    }

    @Override
    public List<BaseWrapper> getValues() {
      if (list == null) {
        list = new ArrayList<NarrativeGenerator.BaseWrapper>();
        for (org.hl7.fhir.r4.elementmodel.Element e : values)
          list.add(new BaseWrapperMetaElement(e, e.fhirType(), structure, definition));
      }
      return list;
    }

    @Override
    public String getTypeCode() {
      return definition.typeSummary();
    }

    @Override
    public String getDefinition() {
      return definition.getDefinition();
    }

    @Override
    public int getMinCardinality() {
      return definition.getMin();
    }

    @Override
    public int getMaxCardinality() {
      return "*".equals(definition.getMax()) ? Integer.MAX_VALUE : Integer.valueOf(definition.getMax());
    }

    @Override
    public StructureDefinition getStructure() {
      return structure;
    }

    @Override
    public BaseWrapper value() {
      if (getValues().size() != 1)
        throw new Error("Access single value, but value count is "+getValues().size());
      return getValues().get(0);
    }

  }

  private class ResourceWrapperElement implements ResourceWrapper {

    private Element wrapped;
    private StructureDefinition definition;
    private List<ResourceWrapper> list;
    private List<PropertyWrapper> list2;

    public ResourceWrapperElement(Element wrapped, StructureDefinition definition) {
      this.wrapped = wrapped;
      this.definition = definition;
    }

    @Override
    public List<ResourceWrapper> getContained() {
      if (list == null) {
        List<Element> children = new ArrayList<Element>();
        XMLUtil.getNamedChildren(wrapped, "contained", children);
        list = new ArrayList<NarrativeGenerator.ResourceWrapper>();
        for (Element e : children) {
          Element c = XMLUtil.getFirstChild(e);
          list.add(new ResourceWrapperElement(c, context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+c.getNodeName())));
        }
      }
      return list;
    }

    @Override
    public String getId() {
      return XMLUtil.getNamedChildValue(wrapped, "id");
    }

    @Override
    public XhtmlNode getNarrative() throws FHIRFormatError, IOException, FHIRException {
      Element txt = XMLUtil.getNamedChild(wrapped, "text");
      if (txt == null)
        return null;
      Element div = XMLUtil.getNamedChild(txt, "div");
      if (div == null)
        return null;
      try {
			return new XhtmlParser().parse(new XmlGenerator().generate(div), "div");
		} catch (org.hl7.fhir.exceptions.FHIRFormatError e) {
			throw new FHIRFormatError(e.getMessage(), e);
		} catch (org.hl7.fhir.exceptions.FHIRException e) {
			throw new FHIRException(e.getMessage(), e);
		}
    }

    @Override
    public String getName() {
      return wrapped.getNodeName();
    }

    @Override
    public List<PropertyWrapper> children() {
      if (list2 == null) {
        List<ElementDefinition> children = ProfileUtilities.getChildList(definition, definition.getSnapshot().getElement().get(0));
        list2 = new ArrayList<NarrativeGenerator.PropertyWrapper>();
        for (ElementDefinition child : children) {
          List<Element> elements = new ArrayList<Element>();
          XMLUtil.getNamedChildrenWithWildcard(wrapped, tail(child.getPath()), elements);
          list2.add(new PropertyWrapperElement(definition, child, elements));
        }
      }
      return list2;
    }
  }

  private class PropertyWrapperDirect implements PropertyWrapper {
    private Property wrapped;
    private List<BaseWrapper> list;

    private PropertyWrapperDirect(Property wrapped) {
      super();
      if (wrapped == null)
        throw new Error("wrapped == null");
      this.wrapped = wrapped;
    }

    @Override
    public String getName() {
      return wrapped.getName();
    }

    @Override
    public boolean hasValues() {
      return wrapped.hasValues();
    }

    @Override
    public List<BaseWrapper> getValues() {
      if (list == null) {
        list = new ArrayList<NarrativeGenerator.BaseWrapper>();
        for (Base b : wrapped.getValues())
          list.add(b == null ? null : new BaseWrapperDirect(b));
      }
      return list;
    }

    @Override
    public String getTypeCode() {
      return wrapped.getTypeCode();
    }

    @Override
    public String getDefinition() {
      return wrapped.getDefinition();
    }

    @Override
    public int getMinCardinality() {
      return wrapped.getMinCardinality();
    }

    @Override
    public int getMaxCardinality() {
      return wrapped.getMinCardinality();
    }

    @Override
    public StructureDefinition getStructure() {
      return wrapped.getStructure();
    }

    @Override
    public BaseWrapper value() {
      if (getValues().size() != 1)
        throw new Error("Access single value, but value count is "+getValues().size());
      return getValues().get(0);
    }
  }

  private class BaseWrapperDirect implements BaseWrapper {
    private Base wrapped;
    private List<PropertyWrapper> list;

    private BaseWrapperDirect(Base wrapped) {
      super();
      if (wrapped == null)
        throw new Error("wrapped == null");
      this.wrapped = wrapped;
    }

    @Override
    public Base getBase() {
      return wrapped;
    }

    @Override
    public List<PropertyWrapper> children() {
      if (list == null) {
        list = new ArrayList<NarrativeGenerator.PropertyWrapper>();
        for (Property p : wrapped.children())
          list.add(new PropertyWrapperDirect(p));
      }
      return list;

    }

    @Override
    public PropertyWrapper getChildByName(String name) {
      Property p = wrapped.getChildByName(name);
      if (p == null)
        return null;
      else
        return new PropertyWrapperDirect(p);
    }

  }

  public class ResourceWrapperDirect implements ResourceWrapper {
    private Resource wrapped;

    public ResourceWrapperDirect(Resource wrapped) {
      super();
      if (wrapped == null)
        throw new Error("wrapped == null");
      this.wrapped = wrapped;
    }

    @Override
    public List<ResourceWrapper> getContained() {
      List<ResourceWrapper> list = new ArrayList<NarrativeGenerator.ResourceWrapper>();
      if (wrapped instanceof DomainResource) {
        DomainResource dr = (DomainResource) wrapped;
        for (Resource c : dr.getContained()) {
          list.add(new ResourceWrapperDirect(c));
        }
      }
      return list;
    }

    @Override
    public String getId() {
      return wrapped.getId();
    }

    @Override
    public XhtmlNode getNarrative() {
      if (wrapped instanceof DomainResource) {
        DomainResource dr = (DomainResource) wrapped;
        if (dr.hasText() && dr.getText().hasDiv())
          return dr.getText().getDiv();
      }
      return null;
    }

    @Override
    public String getName() {
      return wrapped.getResourceType().toString();
    }

    @Override
    public List<PropertyWrapper> children() {
      List<PropertyWrapper> list = new ArrayList<PropertyWrapper>();
      for (Property c : wrapped.children())
        list.add(new PropertyWrapperDirect(c));
      return list;
    }
  }

  public static class ResourceWithReference {

    private String reference;
    private ResourceWrapper resource;

    public ResourceWithReference(String reference, ResourceWrapper resource) {
      this.reference = reference;
      this.resource = resource;
    }

    public String getReference() {
      return reference;
    }

    public ResourceWrapper getResource() {
      return resource;
    }
  }

  private String prefix;
  private IWorkerContext context;
  private String basePath;
  private String tooCostlyNoteEmpty;
  private String tooCostlyNoteNotEmpty;
  private IReferenceResolver resolver;
  private int headerLevelContext;
  private List<ConceptMapRenderInstructions> renderingMaps = new ArrayList<ConceptMapRenderInstructions>();
  private boolean pretty;

  public NarrativeGenerator(String prefix, String basePath, IWorkerContext context) {
    super();
    this.prefix = prefix;
    this.context = context;
    this.basePath = basePath;
    init();
  }

  public Base parseType(String xml, String type) throws IOException, FHIRException {
    if (parser != null)
      return parser.parseType(xml, type);
    else
      return new XmlParser().parseAnyType(xml, type);
  }

  public NarrativeGenerator(String prefix, String basePath, IWorkerContext context, IReferenceResolver resolver) {
    super();
    this.prefix = prefix;
    this.context = context;
    this.basePath = basePath;
    this.resolver = resolver;
    init();
  }


  private void init() {
    renderingMaps.add(new ConceptMapRenderInstructions("Canonical Status", "http://hl7.org/fhir/ValueSet/resource-status", false));
  }

  public List<ConceptMapRenderInstructions> getRenderingMaps() {
    return renderingMaps;
  }

  public int getHeaderLevelContext() {
    return headerLevelContext;
  }

  public NarrativeGenerator setHeaderLevelContext(int headerLevelContext) {
    this.headerLevelContext = headerLevelContext;
    return this;
  }

  public String getTooCostlyNoteEmpty() {
    return tooCostlyNoteEmpty;
  }


  public NarrativeGenerator setTooCostlyNoteEmpty(String tooCostlyNoteEmpty) {
    this.tooCostlyNoteEmpty = tooCostlyNoteEmpty;
    return this;
  }


  public String getTooCostlyNoteNotEmpty() {
    return tooCostlyNoteNotEmpty;
  }


  public NarrativeGenerator setTooCostlyNoteNotEmpty(String tooCostlyNoteNotEmpty) {
    this.tooCostlyNoteNotEmpty = tooCostlyNoteNotEmpty;
    return this;
  }


  // dom based version, for build program
  public String generate(Element doc) throws IOException, org.hl7.fhir.exceptions.FHIRException {
    return generate(null, doc);
  }
  public String generate(ResourceContext rcontext, Element doc) throws IOException, org.hl7.fhir.exceptions.FHIRException {
    if (rcontext == null)
      rcontext = new ResourceContext(null, doc);
    String rt = "http://hl7.org/fhir/StructureDefinition/"+doc.getNodeName();
    StructureDefinition p = context.fetchResource(StructureDefinition.class, rt);
    return generateByProfile(doc, p, true);
  }

  // dom based version, for build program
  public String generate(org.hl7.fhir.r4.elementmodel.Element er, boolean showCodeDetails, ITypeParser parser) throws IOException, FHIRException {
    return generate(null, er, showCodeDetails, parser);
  }
  
  public String generate(ResourceContext rcontext, org.hl7.fhir.r4.elementmodel.Element er, boolean showCodeDetails, ITypeParser parser) throws IOException, FHIRException {
    if (rcontext == null)
      rcontext = new ResourceContext(null, er);
    this.parser = parser;
    
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.para().b().tx("Generated Narrative"+(showCodeDetails ? " with Details" : ""));
    try {
      ResourceWrapperMetaElement resw = new ResourceWrapperMetaElement(er);
      BaseWrapperMetaElement base = new BaseWrapperMetaElement(er, null, er.getProperty().getStructure(), er.getProperty().getDefinition());
      base.children();
      generateByProfile(resw, er.getProperty().getStructure(), base, er.getProperty().getStructure().getSnapshot().getElement(), er.getProperty().getDefinition(), base.children, x, er.fhirType(), showCodeDetails, 0, rcontext);

    } catch (Exception e) {
      e.printStackTrace();
      x.para().b().setAttribute("style", "color: maroon").tx("Exception generating Narrative: "+e.getMessage());
    }
    inject(er, x,  NarrativeStatus.GENERATED);
    return new XhtmlComposer(XhtmlComposer.XML, pretty).compose(x);
  }

  private boolean generateByProfile(ResourceContext rc, StructureDefinition profile, boolean showCodeDetails) {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.para().b().tx("Generated Narrative"+(showCodeDetails ? " with Details" : ""));
    try {
      generateByProfile(rc.resourceResource, profile, rc.resourceResource, profile.getSnapshot().getElement(), profile.getSnapshot().getElement().get(0), getChildrenForPath(profile.getSnapshot().getElement(), rc.resourceResource.getResourceType().toString()), x, rc.resourceResource.getResourceType().toString(), showCodeDetails, rc);
    } catch (Exception e) {
      e.printStackTrace();
      x.para().b().setAttribute("style", "color: maroon").tx("Exception generating Narrative: "+e.getMessage());
    }
    inject(rc.resourceResource, x,  NarrativeStatus.GENERATED);
    return true;
  }

  private String generateByProfile(Element er, StructureDefinition profile, boolean showCodeDetails) throws IOException, org.hl7.fhir.exceptions.FHIRException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.para().b().tx("Generated Narrative"+(showCodeDetails ? " with Details" : ""));
    try {
      generateByProfile(er, profile, er, profile.getSnapshot().getElement(), profile.getSnapshot().getElement().get(0), getChildrenForPath(profile.getSnapshot().getElement(), er.getLocalName()), x, er.getLocalName(), showCodeDetails);
    } catch (Exception e) {
      e.printStackTrace();
      x.para().b().setAttribute("style", "color: maroon").tx("Exception generating Narrative: "+e.getMessage());
    }
    inject(er, x,  NarrativeStatus.GENERATED);
    String b = new XhtmlComposer(XhtmlComposer.XML, pretty).compose(x);
    return b;
  }

  private void generateByProfile(Element eres, StructureDefinition profile, Element ee, List<ElementDefinition> allElements, ElementDefinition defn, List<ElementDefinition> children,  XhtmlNode x, String path, boolean showCodeDetails) throws FHIRException, UnsupportedEncodingException, IOException {

    ResourceWrapperElement resw = new ResourceWrapperElement(eres, profile);
    BaseWrapperElement base = new BaseWrapperElement(ee, null, profile, profile.getSnapshot().getElement().get(0));
    generateByProfile(resw, profile, base, allElements, defn, children, x, path, showCodeDetails, 0, null);
  }


  private void generateByProfile(Resource res, StructureDefinition profile, Base e, List<ElementDefinition> allElements, ElementDefinition defn, List<ElementDefinition> children,  XhtmlNode x, String path, boolean showCodeDetails, ResourceContext rc) throws FHIRException, UnsupportedEncodingException, IOException {
    generateByProfile(new ResourceWrapperDirect(res), profile, new BaseWrapperDirect(e), allElements, defn, children, x, path, showCodeDetails, 0, rc);
  }

  private void generateByProfile(ResourceWrapper res, StructureDefinition profile, BaseWrapper e, List<ElementDefinition> allElements, ElementDefinition defn, List<ElementDefinition> children,  XhtmlNode x, String path, boolean showCodeDetails, int indent, ResourceContext rc) throws FHIRException, UnsupportedEncodingException, IOException {
    if (children.isEmpty()) {
      renderLeaf(res, e, defn, x, false, showCodeDetails, readDisplayHints(defn), path, indent, rc);
    } else {
      for (PropertyWrapper p : splitExtensions(profile, e.children())) {
        if (p.hasValues()) {
          ElementDefinition child = getElementDefinition(children, path+"."+p.getName(), p);
          if (child != null) {
            Map<String, String> displayHints = readDisplayHints(child);
            if (!exemptFromRendering(child)) {
              List<ElementDefinition> grandChildren = getChildrenForPath(allElements, path+"."+p.getName());
            filterGrandChildren(grandChildren, path+"."+p.getName(), p);
              if (p.getValues().size() > 0 && child != null) {
                if (isPrimitive(child)) {
                  XhtmlNode para = x.para();
                  String name = p.getName();
                  if (name.endsWith("[x]"))
                    name = name.substring(0, name.length() - 3);
                  if (showCodeDetails || !isDefaultValue(displayHints, p.getValues())) {
                    para.b().addText(name);
                    para.tx(": ");
                    if (renderAsList(child) && p.getValues().size() > 1) {
                      XhtmlNode list = x.ul();
                      for (BaseWrapper v : p.getValues())
                        renderLeaf(res, v, child, list.li(), false, showCodeDetails, displayHints, path, indent, rc);
                    } else {
                      boolean first = true;
                      for (BaseWrapper v : p.getValues()) {
                        if (first)
                          first = false;
                        else
                          para.tx(", ");
                        renderLeaf(res, v, child, para, false, showCodeDetails, displayHints, path, indent, rc);
                      }
                    }
                  }
                } else if (canDoTable(path, p, grandChildren)) {
                  x.addTag(getHeader()).addText(Utilities.capitalize(Utilities.camelCase(Utilities.pluralizeMe(p.getName()))));
                  XhtmlNode tbl = x.table( "grid");
                  XhtmlNode tr = tbl.tr();
                  tr.td().tx("-"); // work around problem with empty table rows
                  addColumnHeadings(tr, grandChildren);
                  for (BaseWrapper v : p.getValues()) {
                    if (v != null) {
                      tr = tbl.tr();
                      tr.td().tx("*"); // work around problem with empty table rows
                      addColumnValues(res, tr, grandChildren, v, showCodeDetails, displayHints, path, indent, rc);
                    }
                  }
                } else {
                  for (BaseWrapper v : p.getValues()) {
                    if (v != null) {
                      XhtmlNode bq = x.addTag("blockquote");
                      bq.para().b().addText(p.getName());
                      generateByProfile(res, profile, v, allElements, child, grandChildren, bq, path+"."+p.getName(), showCodeDetails, indent+1, rc);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private String getHeader() {
    int i = 3;
    while (i <= headerLevelContext)
      i++;
    if (i > 6)
      i = 6;
    return "h"+Integer.toString(i);
  }

  private void filterGrandChildren(List<ElementDefinition> grandChildren,  String string, PropertyWrapper prop) {
  	List<ElementDefinition> toRemove = new ArrayList<ElementDefinition>();
  	toRemove.addAll(grandChildren);
  	for (BaseWrapper b : prop.getValues()) {
    	List<ElementDefinition> list = new ArrayList<ElementDefinition>();
  		for (ElementDefinition ed : toRemove) {
  			PropertyWrapper p = b.getChildByName(tail(ed.getPath()));
  			if (p != null && p.hasValues())
  				list.add(ed);
  		}
  		toRemove.removeAll(list);
  	}
  	grandChildren.removeAll(toRemove);
  }

  private List<PropertyWrapper> splitExtensions(StructureDefinition profile, List<PropertyWrapper> children) throws UnsupportedEncodingException, IOException, FHIRException {
    List<PropertyWrapper> results = new ArrayList<PropertyWrapper>();
    Map<String, PropertyWrapper> map = new HashMap<String, PropertyWrapper>();
    for (PropertyWrapper p : children)
      if (p.getName().equals("extension") || p.getName().equals("modifierExtension")) {
        // we're going to split these up, and create a property for each url
        if (p.hasValues()) {
          for (BaseWrapper v : p.getValues()) {
            Extension ex  = (Extension) v.getBase();
            String url = ex.getUrl();
            StructureDefinition ed = context.fetchResource(StructureDefinition.class, url);
            if (p.getName().equals("modifierExtension") && ed == null)
              throw new DefinitionException("Unknown modifier extension "+url);
            PropertyWrapper pe = map.get(p.getName()+"["+url+"]");
            if (pe == null) {
              if (ed == null) {
                if (url.startsWith("http://hl7.org/fhir") && !url.startsWith("http://hl7.org/fhir/us"))
                  throw new DefinitionException("unknown extension "+url);
                // System.out.println("unknown extension "+url);
                pe = new PropertyWrapperDirect(new Property(p.getName()+"["+url+"]", p.getTypeCode(), p.getDefinition(), p.getMinCardinality(), p.getMaxCardinality(), ex));
              } else {
                ElementDefinition def = ed.getSnapshot().getElement().get(0);
                pe = new PropertyWrapperDirect(new Property(p.getName()+"["+url+"]", "Extension", def.getDefinition(), def.getMin(), def.getMax().equals("*") ? Integer.MAX_VALUE : Integer.parseInt(def.getMax()), ex));
                ((PropertyWrapperDirect) pe).wrapped.setStructure(ed);
              }
              results.add(pe);
            } else
              pe.getValues().add(v);
          }
        }
      } else
        results.add(p);
    return results;
  }

  @SuppressWarnings("rawtypes")
  private boolean isDefaultValue(Map<String, String> displayHints, List<BaseWrapper> list) throws UnsupportedEncodingException, IOException, FHIRException {
    if (list.size() != 1)
      return false;
    if (list.get(0).getBase() instanceof PrimitiveType)
      return isDefault(displayHints, (PrimitiveType) list.get(0).getBase());
    else
      return false;
  }

  private boolean isDefault(Map<String, String> displayHints, PrimitiveType primitiveType) {
    String v = primitiveType.asStringValue();
    if (!Utilities.noString(v) && displayHints.containsKey("default") && v.equals(displayHints.get("default")))
        return true;
    return false;
  }

  private boolean exemptFromRendering(ElementDefinition child) {
    if (child == null)
      return false;
    if ("Composition.subject".equals(child.getPath()))
      return true;
    if ("Composition.section".equals(child.getPath()))
      return true;
    return false;
  }

  private boolean renderAsList(ElementDefinition child) {
    if (child.getType().size() == 1) {
      String t = child.getType().get(0).getCode();
      if (t.equals("Address") || t.equals("Reference"))
        return true;
    }
    return false;
  }

  private void addColumnHeadings(XhtmlNode tr, List<ElementDefinition> grandChildren) {
    for (ElementDefinition e : grandChildren)
      tr.td().b().addText(Utilities.capitalize(tail(e.getPath())));
  }

  private void addColumnValues(ResourceWrapper res, XhtmlNode tr, List<ElementDefinition> grandChildren, BaseWrapper v, boolean showCodeDetails, Map<String, String> displayHints, String path, int indent, ResourceContext rc) throws FHIRException, UnsupportedEncodingException, IOException {
    for (ElementDefinition e : grandChildren) {
      PropertyWrapper p = v.getChildByName(e.getPath().substring(e.getPath().lastIndexOf(".")+1));
      if (p == null || p.getValues().size() == 0 || p.getValues().get(0) == null)
        tr.td().tx(" ");
      else
        renderLeaf(res, p.getValues().get(0), e, tr.td(), false, showCodeDetails, displayHints, path, indent, rc);
    }
  }

  private String tail(String path) {
    return path.substring(path.lastIndexOf(".")+1);
  }

  private boolean canDoTable(String path, PropertyWrapper p, List<ElementDefinition> grandChildren) {
    for (ElementDefinition e : grandChildren) {
      List<PropertyWrapper> values = getValues(path, p, e);
      if (values.size() > 1 || !isPrimitive(e) || !canCollapse(e))
        return false;
    }
    return true;
  }

  private List<PropertyWrapper> getValues(String path, PropertyWrapper p, ElementDefinition e) {
    List<PropertyWrapper> res = new ArrayList<PropertyWrapper>();
    for (BaseWrapper v : p.getValues()) {
      for (PropertyWrapper g : v.children()) {
        if ((path+"."+p.getName()+"."+g.getName()).equals(e.getPath()))
          res.add(p);
      }
    }
    return res;
  }

  private boolean canCollapse(ElementDefinition e) {
    // we can collapse any data type
    return !e.getType().isEmpty();
  }

  private boolean isPrimitive(ElementDefinition e) {
    //we can tell if e is a primitive because it has types
    if (e.getType().isEmpty())
      return false;
    if (e.getType().size() == 1 && isBase(e.getType().get(0).getCode()))
      return false;
    return true;
//    return !e.getType().isEmpty()
  }

  private boolean isBase(String code) {
    return code.equals("Element") || code.equals("BackboneElement");
  }

  private ElementDefinition getElementDefinition(List<ElementDefinition> elements, String path, PropertyWrapper p) {
    for (ElementDefinition element : elements)
      if (element.getPath().equals(path))
        return element;
    if (path.endsWith("\"]") && p.getStructure() != null)
      return p.getStructure().getSnapshot().getElement().get(0);
    return null;
  }

  private void renderLeaf(ResourceWrapper res, BaseWrapper ew, ElementDefinition defn, XhtmlNode x, boolean title, boolean showCodeDetails, Map<String, String> displayHints, String path, int indent, ResourceContext rc) throws FHIRException, UnsupportedEncodingException, IOException {
    if (ew == null)
      return;


    Base e = ew.getBase();

    if (e instanceof StringType)
      x.addText(((StringType) e).getValue());
    else if (e instanceof CodeType)
      x.addText(((CodeType) e).getValue());
    else if (e instanceof IdType)
      x.addText(((IdType) e).getValue());
    else if (e instanceof Extension)
      return;
    else if (e instanceof InstantType)
      x.addText(((InstantType) e).toHumanDisplay());
    else if (e instanceof DateTimeType)
      x.addText(((DateTimeType) e).toHumanDisplay());
    else if (e instanceof Base64BinaryType)
      x.addText(new Base64().encodeAsString(((Base64BinaryType) e).getValue()));
    else if (e instanceof org.hl7.fhir.r4.model.DateType)
      x.addText(((org.hl7.fhir.r4.model.DateType) e).toHumanDisplay());
    else if (e instanceof Enumeration) {
      Object ev = ((Enumeration<?>) e).getValue();
			x.addText(ev == null ? "" : ev.toString()); // todo: look up a display name if there is one
    } else if (e instanceof BooleanType)
      x.addText(((BooleanType) e).getValue().toString());
    else if (e instanceof CodeableConcept) {
      renderCodeableConcept((CodeableConcept) e, x, showCodeDetails);
    } else if (e instanceof Coding) {
      renderCoding((Coding) e, x, showCodeDetails);
    } else if (e instanceof Annotation) {
      renderAnnotation((Annotation) e, x);
    } else if (e instanceof Identifier) {
      renderIdentifier((Identifier) e, x);
    } else if (e instanceof org.hl7.fhir.r4.model.IntegerType) {
      x.addText(Integer.toString(((org.hl7.fhir.r4.model.IntegerType) e).getValue()));
    } else if (e instanceof org.hl7.fhir.r4.model.DecimalType) {
      x.addText(((org.hl7.fhir.r4.model.DecimalType) e).getValue().toString());
    } else if (e instanceof HumanName) {
      renderHumanName((HumanName) e, x);
    } else if (e instanceof SampledData) {
      renderSampledData((SampledData) e, x);
    } else if (e instanceof Address) {
      renderAddress((Address) e, x);
    } else if (e instanceof ContactPoint) {
      renderContactPoint((ContactPoint) e, x);
    } else if (e instanceof UriType) {
      renderUri((UriType) e, x);
    } else if (e instanceof Timing) {
      renderTiming((Timing) e, x);
    } else if (e instanceof Range) {
      renderRange((Range) e, x);
    } else if (e instanceof Quantity) {
      renderQuantity((Quantity) e, x, showCodeDetails);
    } else if (e instanceof Ratio) {
      renderQuantity(((Ratio) e).getNumerator(), x, showCodeDetails);
      x.tx("/");
      renderQuantity(((Ratio) e).getDenominator(), x, showCodeDetails);
    } else if (e instanceof Period) {
      Period p = (Period) e;
      x.addText(!p.hasStart() ? "??" : p.getStartElement().toHumanDisplay());
      x.tx(" --> ");
      x.addText(!p.hasEnd() ? "(ongoing)" : p.getEndElement().toHumanDisplay());
    } else if (e instanceof Reference) {
      Reference r = (Reference) e;
      XhtmlNode c = x;
      ResourceWithReference tr = null;
      if (r.hasReferenceElement()) {
        tr = resolveReference(res, r.getReference(), rc);
        
        if (!r.getReference().startsWith("#")) {
          if (tr != null && tr.getReference() != null)
            c = x.ah(tr.getReference());
          else
            c = x.ah(r.getReference());
        }
      }
      // what to display: if text is provided, then that. if the reference was resolved, then show the generated narrative
      if (r.hasDisplayElement()) {
        c.addText(r.getDisplay());
        if (tr != null && tr.getResource() != null) {
          c.tx(". Generated Summary: ");
          generateResourceSummary(c, tr.getResource(), true, r.getReference().startsWith("#"), rc);
        }
      } else if (tr != null && tr.getResource() != null) {
        generateResourceSummary(c, tr.getResource(), r.getReference().startsWith("#"), r.getReference().startsWith("#"), rc);
      } else {
        c.addText(r.getReference());
      }
    } else if (e instanceof Resource) {
      return;
    } else if (e instanceof ElementDefinition) {
      x.tx("todo-bundle");
    } else if (e != null && !(e instanceof Attachment) && !(e instanceof Narrative) && !(e instanceof Meta)) {
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+e.fhirType());
      if (sd == null)
        throw new NotImplementedException("type "+e.getClass().getName()+" not handled yet, and no structure found");
      else
        generateByProfile(res, sd, ew, sd.getSnapshot().getElement(), sd.getSnapshot().getElementFirstRep(),
            getChildrenForPath(sd.getSnapshot().getElement(), sd.getSnapshot().getElementFirstRep().getPath()), x, path, showCodeDetails, indent + 1, rc);
    }
  }

  private boolean displayLeaf(ResourceWrapper res, BaseWrapper ew, ElementDefinition defn, XhtmlNode x, String name, boolean showCodeDetails, ResourceContext rc) throws FHIRException, UnsupportedEncodingException, IOException {
    if (ew == null)
      return false;
    Base e = ew.getBase();
    if (e == null)
      return false;

    Map<String, String> displayHints = readDisplayHints(defn);

    if (name.endsWith("[x]"))
      name = name.substring(0, name.length() - 3);

    if (!showCodeDetails && e instanceof PrimitiveType && isDefault(displayHints, ((PrimitiveType) e)))
        return false;

    if (e instanceof StringType) {
      x.addText(name+": "+((StringType) e).getValue());
      return true;
    } else if (e instanceof CodeType) {
      x.addText(name+": "+((CodeType) e).getValue());
      return true;
    } else if (e instanceof IdType) {
      x.addText(name+": "+((IdType) e).getValue());
      return true;
    } else if (e instanceof UriType) {
      x.addText(name+": "+((UriType) e).getValue());
      return true;
    } else if (e instanceof DateTimeType) {
      x.addText(name+": "+((DateTimeType) e).toHumanDisplay());
      return true;
    } else if (e instanceof InstantType) {
      x.addText(name+": "+((InstantType) e).toHumanDisplay());
      return true;
    } else if (e instanceof Extension) {
//      x.tx("Extensions: todo");
      return false;
    } else if (e instanceof org.hl7.fhir.r4.model.DateType) {
      x.addText(name+": "+((org.hl7.fhir.r4.model.DateType) e).toHumanDisplay());
      return true;
    } else if (e instanceof Enumeration) {
      x.addText(((Enumeration<?>) e).getValue().toString()); // todo: look up a display name if there is one
      return true;
    } else if (e instanceof BooleanType) {
      if (((BooleanType) e).getValue()) {
        x.addText(name);
          return true;
      }
    } else if (e instanceof CodeableConcept) {
      renderCodeableConcept((CodeableConcept) e, x, showCodeDetails);
      return true;
    } else if (e instanceof Coding) {
      renderCoding((Coding) e, x, showCodeDetails);
      return true;
    } else if (e instanceof Annotation) {
      renderAnnotation((Annotation) e, x, showCodeDetails);
      return true;
    } else if (e instanceof org.hl7.fhir.r4.model.IntegerType) {
      x.addText(Integer.toString(((org.hl7.fhir.r4.model.IntegerType) e).getValue()));
      return true;
    } else if (e instanceof org.hl7.fhir.r4.model.DecimalType) {
      x.addText(((org.hl7.fhir.r4.model.DecimalType) e).getValue().toString());
      return true;
    } else if (e instanceof Identifier) {
      renderIdentifier((Identifier) e, x);
      return true;
    } else if (e instanceof HumanName) {
      renderHumanName((HumanName) e, x);
      return true;
    } else if (e instanceof SampledData) {
      renderSampledData((SampledData) e, x);
      return true;
    } else if (e instanceof Address) {
      renderAddress((Address) e, x);
      return true;
    } else if (e instanceof ContactPoint) {
      renderContactPoint((ContactPoint) e, x);
      return true;
    } else if (e instanceof Timing) {
      renderTiming((Timing) e, x);
      return true;
    } else if (e instanceof Quantity) {
      renderQuantity((Quantity) e, x, showCodeDetails);
      return true;
    } else if (e instanceof Ratio) {
      renderQuantity(((Ratio) e).getNumerator(), x, showCodeDetails);
      x.tx("/");
      renderQuantity(((Ratio) e).getDenominator(), x, showCodeDetails);
      return true;
    } else if (e instanceof Period) {
      Period p = (Period) e;
      x.addText(name+": ");
      x.addText(!p.hasStart() ? "??" : p.getStartElement().toHumanDisplay());
      x.tx(" --> ");
      x.addText(!p.hasEnd() ? "(ongoing)" : p.getEndElement().toHumanDisplay());
      return true;
    } else if (e instanceof Reference) {
      Reference r = (Reference) e;
      if (r.hasDisplayElement())
        x.addText(r.getDisplay());
      else if (r.hasReferenceElement()) {
        ResourceWithReference tr = resolveReference(res, r.getReference(), rc);
        x.addText(tr == null ? r.getReference() : "????"); // getDisplayForReference(tr.getReference()));
      } else
        x.tx("??");
      return true;
    } else if (e instanceof Narrative) {
      return false;
    } else if (e instanceof Resource) {
      return false;
    } else if (e instanceof ContactDetail) {
      return false;
    } else if (e instanceof Range) {
      return false;
    } else if (e instanceof Meta) {
      return false;
    } else if (e instanceof Dosage) {
      return false;
    } else if (e instanceof Signature) {
      return false;
    } else if (e instanceof UsageContext) {
      return false;
    } else if (e instanceof ElementDefinition) {
      return false;
    } else if (!(e instanceof Attachment))
      throw new NotImplementedException("type "+e.getClass().getName()+" not handled yet");
    return false;
  }


  private Map<String, String> readDisplayHints(ElementDefinition defn) throws DefinitionException {
    Map<String, String> hints = new HashMap<String, String>();
    if (defn != null) {
      String displayHint = ToolingExtensions.getDisplayHint(defn);
      if (!Utilities.noString(displayHint)) {
        String[] list = displayHint.split(";");
        for (String item : list) {
          String[] parts = item.split(":");
          if (parts.length != 2)
            throw new DefinitionException("error reading display hint: '"+displayHint+"'");
          hints.put(parts[0].trim(), parts[1].trim());
        }
      }
    }
    return hints;
  }

  public static String displayPeriod(Period p) {
    String s = !p.hasStart() ? "??" : p.getStartElement().toHumanDisplay();
    s = s + " --> ";
    return s + (!p.hasEnd() ? "(ongoing)" : p.getEndElement().toHumanDisplay());
  }

  private void generateResourceSummary(XhtmlNode x, ResourceWrapper res, boolean textAlready, boolean showCodeDetails, ResourceContext rc) throws FHIRException, UnsupportedEncodingException, IOException {
    if (!textAlready) {
      XhtmlNode div = res.getNarrative();
      if (div != null) {
        if (div.allChildrenAreText())
          x.getChildNodes().addAll(div.getChildNodes());
        if (div.getChildNodes().size() == 1 && div.getChildNodes().get(0).allChildrenAreText())
          x.getChildNodes().addAll(div.getChildNodes().get(0).getChildNodes());
      }
      x.tx("Generated Summary: ");
    }
    String path = res.getName();
    StructureDefinition profile = context.fetchResource(StructureDefinition.class, path);
    if (profile == null)
      x.tx("unknown resource " +path);
    else {
      boolean firstElement = true;
      boolean last = false;
      for (PropertyWrapper p : res.children()) {
        ElementDefinition child = getElementDefinition(profile.getSnapshot().getElement(), path+"."+p.getName(), p);
        if (p.getValues().size() > 0 && p.getValues().get(0) != null && child != null && isPrimitive(child) && includeInSummary(child)) {
          if (firstElement)
            firstElement = false;
          else if (last)
            x.tx("; ");
          boolean first = true;
          last = false;
          for (BaseWrapper v : p.getValues()) {
            if (first)
              first = false;
            else if (last)
              x.tx(", ");
            last = displayLeaf(res, v, child, x, p.getName(), showCodeDetails, rc) || last;
          }
        }
      }
    }
  }


  private boolean includeInSummary(ElementDefinition child) {
    if (child.getIsModifier())
      return true;
    if (child.getMustSupport())
      return true;
    if (child.getType().size() == 1) {
      String t = child.getType().get(0).getCode();
      if (t.equals("Address") || t.equals("Contact") || t.equals("Reference") || t.equals("Uri") || t.equals("Url") || t.equals("Canonical"))
        return false;
    }
    return true;
  }

  private ResourceWithReference resolveReference(ResourceWrapper res, String url, ResourceContext rc) {
    if (url == null)
      return null;
    if (url.startsWith("#")) {
      for (ResourceWrapper r : res.getContained()) {
        if (r.getId().equals(url.substring(1)))
          return new ResourceWithReference(null, r);
      }
      return null;
    }
    
    if (rc!=null) {
      Resource bundleResource = rc.resolve(url);
      if (bundleResource!=null) {
        String bundleUrl = "#" + bundleResource.getResourceType().name().toLowerCase() + "_" + bundleResource.getId(); 
        return new ResourceWithReference(bundleUrl, new ResourceWrapperDirect(bundleResource));
      }
    }

    Resource ae = context.fetchResource(null, url);
    if (ae != null)
      return new ResourceWithReference(url, new ResourceWrapperDirect(ae));
    else if (resolver != null) {
      return resolver.resolve(url);
    } else
      return null;
  }

  private void renderCodeableConcept(CodeableConcept cc, XhtmlNode x, boolean showCodeDetails) {
    String s = cc.getText();
    if (Utilities.noString(s)) {
      for (Coding c : cc.getCoding()) {
        if (c.hasDisplayElement()) {
          s = c.getDisplay();
          break;
        }
      }
    }
    if (Utilities.noString(s)) {
      // still? ok, let's try looking it up
      for (Coding c : cc.getCoding()) {
        if (c.hasCodeElement() && c.hasSystemElement()) {
          s = lookupCode(c.getSystem(), c.getCode());
          if (!Utilities.noString(s))
            break;
        }
      }
    }

    if (Utilities.noString(s)) {
      if (cc.getCoding().isEmpty())
        s = "";
      else
        s = cc.getCoding().get(0).getCode();
    }

    if (showCodeDetails) {
      x.addText(s+" ");
      XhtmlNode sp = x.span("background: LightGoldenRodYellow", null);
      sp.tx("(Details ");
      boolean first = true;
      for (Coding c : cc.getCoding()) {
        if (first) {
          sp.tx(": ");
          first = false;
        } else
          sp.tx("; ");
        sp.tx("{"+describeSystem(c.getSystem())+" code '"+c.getCode()+"' = '"+lookupCode(c.getSystem(), c.getCode())+(c.hasDisplay() ? "', given as '"+c.getDisplay()+"'}" : ""));
      }
      sp.tx(")");
    } else {

    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (Coding c : cc.getCoding()) {
      if (c.hasCodeElement() && c.hasSystemElement()) {
        b.append("{"+c.getSystem()+" "+c.getCode()+"}");
      }
    }

    x.span(null, "Codes: "+b.toString()).addText(s);
    }
  }

  private void renderAnnotation(Annotation a, XhtmlNode x, boolean showCodeDetails) throws FHIRException {
    StringBuilder s = new StringBuilder();
    if (a.hasAuthor()) {
      s.append("Author: ");

      if (a.hasAuthorReference())
        s.append(a.getAuthorReference().getReference());
      else if (a.hasAuthorStringType())
        s.append(a.getAuthorStringType().getValue());
    }


    if (a.hasTimeElement()) {
      if (s.length() > 0)
        s.append("; ");

      s.append("Made: ").append(a.getTimeElement().toHumanDisplay());
    }

    if (a.hasText()) {
      if (s.length() > 0)
        s.append("; ");

      s.append("Annotation: ").append(a.getText());
    }

    x.addText(s.toString());
  }

  private void renderCoding(Coding c, XhtmlNode x, boolean showCodeDetails) {
    String s = "";
    if (c.hasDisplayElement())
      s = c.getDisplay();
    if (Utilities.noString(s))
      s = lookupCode(c.getSystem(), c.getCode());

    if (Utilities.noString(s))
      s = c.getCode();

    if (showCodeDetails) {
      x.addText(s+" (Details: "+describeSystem(c.getSystem())+" code "+c.getCode()+" = '"+lookupCode(c.getSystem(), c.getCode())+"', stated as '"+c.getDisplay()+"')");
    } else
      x.span(null, "{"+c.getSystem()+" "+c.getCode()+"}").addText(s);
  }

  public static String describeSystem(String system) {
    if (system == null)
      return "[not stated]";
    if (system.equals("http://loinc.org"))
      return "LOINC";
    if (system.startsWith("http://snomed.info"))
      return "SNOMED CT";
    if (system.equals("http://www.nlm.nih.gov/research/umls/rxnorm"))
      return "RxNorm";
    if (system.equals("http://hl7.org/fhir/sid/icd-9"))
      return "ICD-9";
    if (system.equals("http://dicom.nema.org/resources/ontology/DCM"))
      return "DICOM";
    if (system.equals("http://unitsofmeasure.org"))
      return "UCUM";

    return system;
  }

  private String lookupCode(String system, String code) {
    ValidationResult t = context.validateCode(system, code, null);

    if (t != null && t.getDisplay() != null)
        return t.getDisplay();
    else
      return code;

  }

  private ConceptDefinitionComponent findCode(String code, List<ConceptDefinitionComponent> list) {
    for (ConceptDefinitionComponent t : list) {
      if (code.equals(t.getCode()))
        return t;
      ConceptDefinitionComponent c = findCode(code, t.getConcept());
      if (c != null)
        return c;
    }
    return null;
  }

  public String displayCodeableConcept(CodeableConcept cc) {
    String s = cc.getText();
    if (Utilities.noString(s)) {
      for (Coding c : cc.getCoding()) {
        if (c.hasDisplayElement()) {
          s = c.getDisplay();
          break;
        }
      }
    }
    if (Utilities.noString(s)) {
      // still? ok, let's try looking it up
      for (Coding c : cc.getCoding()) {
        if (c.hasCode() && c.hasSystem()) {
          s = lookupCode(c.getSystem(), c.getCode());
          if (!Utilities.noString(s))
            break;
        }
      }
    }

    if (Utilities.noString(s)) {
      if (cc.getCoding().isEmpty())
        s = "";
      else
        s = cc.getCoding().get(0).getCode();
    }
    return s;
  }

  private void renderIdentifier(Identifier ii, XhtmlNode x) {
    x.addText(displayIdentifier(ii));
  }

  private void renderTiming(Timing s, XhtmlNode x) throws FHIRException {
    x.addText(displayTiming(s));
  }

  private void renderQuantity(Quantity q, XhtmlNode x, boolean showCodeDetails) {
    if (q.hasComparator())
      x.addText(q.getComparator().toCode());
    x.addText(q.getValue().toString());
    if (q.hasUnit())
      x.tx(" "+q.getUnit());
    else if (q.hasCode())
      x.tx(" "+q.getCode());
    if (showCodeDetails && q.hasCode()) {
      x.span("background: LightGoldenRodYellow", null).tx(" (Details: "+describeSystem(q.getSystem())+" code "+q.getCode()+" = '"+lookupCode(q.getSystem(), q.getCode())+"')");
    }
  }

  private void renderRange(Range q, XhtmlNode x) {
    if (q.hasLow())
      x.addText(q.getLow().getValue().toString());
    else
      x.tx("?");
    x.tx("-");
    if (q.hasHigh())
      x.addText(q.getHigh().getValue().toString());
    else
      x.tx("?");
    if (q.getLow().hasUnit())
      x.tx(" "+q.getLow().getUnit());
  }

  public String displayRange(Range q) {
    StringBuilder b = new StringBuilder();
    if (q.hasLow())
      b.append(q.getLow().getValue().toString());
    else
      b.append("?");
    b.append("-");
    if (q.hasHigh())
      b.append(q.getHigh().getValue().toString());
    else
      b.append("?");
    if (q.getLow().hasUnit())
      b.append(" "+q.getLow().getUnit());
    return b.toString();
  }

  private void renderHumanName(HumanName name, XhtmlNode x) {
    x.addText(displayHumanName(name));
  }

  private void renderAnnotation(Annotation annot, XhtmlNode x) {
    x.addText(annot.getText());
  }

  private void renderAddress(Address address, XhtmlNode x) {
    x.addText(displayAddress(address));
  }

  private void renderContactPoint(ContactPoint contact, XhtmlNode x) {
    x.addText(displayContactPoint(contact));
  }

  private void renderUri(UriType uri, XhtmlNode x) {
    x.ah(uri.getValue()).addText(uri.getValue());
  }

  private void renderSampledData(SampledData sampledData, XhtmlNode x) {
    x.addText(displaySampledData(sampledData));
  }

  private String displaySampledData(SampledData s) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    if (s.hasOrigin())
      b.append("Origin: "+displayQuantity(s.getOrigin()));

    if (s.hasPeriod())
      b.append("Period: "+s.getPeriod().toString());

    if (s.hasFactor())
      b.append("Factor: "+s.getFactor().toString());

    if (s.hasLowerLimit())
      b.append("Lower: "+s.getLowerLimit().toString());

    if (s.hasUpperLimit())
      b.append("Upper: "+s.getUpperLimit().toString());

    if (s.hasDimensions())
      b.append("Dimensions: "+s.getDimensions());

    if (s.hasData())
      b.append("Data: "+s.getData());

    return b.toString();
  }

  private String displayQuantity(Quantity q) {
    StringBuilder s = new StringBuilder();

    s.append("(system = '").append(describeSystem(q.getSystem()))
        .append("' code ").append(q.getCode())
        .append(" = '").append(lookupCode(q.getSystem(), q.getCode())).append("')");

    return s.toString();
  }

  private String displayTiming(Timing s) throws FHIRException {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    if (s.hasCode())
    	b.append("Code: "+displayCodeableConcept(s.getCode()));

    if (s.getEvent().size() > 0) {
      CommaSeparatedStringBuilder c = new CommaSeparatedStringBuilder();
      for (DateTimeType p : s.getEvent()) {
        c.append(p.toHumanDisplay());
      }
      b.append("Events: "+ c.toString());
    }

    if (s.hasRepeat()) {
      TimingRepeatComponent rep = s.getRepeat();
      if (rep.hasBoundsPeriod() && rep.getBoundsPeriod().hasStart())
        b.append("Starting "+rep.getBoundsPeriod().getStartElement().toHumanDisplay());
      if (rep.hasCount())
        b.append("Count "+Integer.toString(rep.getCount())+" times");
      if (rep.hasDuration())
        b.append("Duration "+rep.getDuration().toPlainString()+displayTimeUnits(rep.getPeriodUnit()));

      if (rep.hasWhen()) {
        String st = "";
        if (rep.hasOffset()) {
          st = Integer.toString(rep.getOffset())+"min ";
        }
        b.append("Do "+st);
        for (Enumeration<EventTiming> wh : rep.getWhen())
          b.append(displayEventCode(wh.getValue()));
      } else {
        String st = "";
        if (!rep.hasFrequency() || (!rep.hasFrequencyMax() && rep.getFrequency() == 1) )
          st = "Once";
        else {
          st = Integer.toString(rep.getFrequency());
          if (rep.hasFrequencyMax())
            st = st + "-"+Integer.toString(rep.getFrequency());
        }
        if (rep.hasPeriod()) {
        st = st + " per "+rep.getPeriod().toPlainString();
        if (rep.hasPeriodMax())
          st = st + "-"+rep.getPeriodMax().toPlainString();
        	st = st + " "+displayTimeUnits(rep.getPeriodUnit());
        }
        b.append("Do "+st);
      }
      if (rep.hasBoundsPeriod() && rep.getBoundsPeriod().hasEnd())
        b.append("Until "+rep.getBoundsPeriod().getEndElement().toHumanDisplay());
    }
    return b.toString();
  }

  private String displayEventCode(EventTiming when) {
    switch (when) {
    case C: return "at meals";
    case CD: return "at lunch";
    case CM: return "at breakfast";
    case CV: return "at dinner";
    case AC: return "before meals";
    case ACD: return "before lunch";
    case ACM: return "before breakfast";
    case ACV: return "before dinner";
    case HS: return "before sleeping";
    case PC: return "after meals";
    case PCD: return "after lunch";
    case PCM: return "after breakfast";
    case PCV: return "after dinner";
    case WAKE: return "after waking";
    default: return "??";
    }
  }

  private String displayTimeUnits(UnitsOfTime units) {
  	if (units == null)
  		return "??";
    switch (units) {
    case A: return "years";
    case D: return "days";
    case H: return "hours";
    case MIN: return "minutes";
    case MO: return "months";
    case S: return "seconds";
    case WK: return "weeks";
    default: return "??";
    }
  }

  public static String displayHumanName(HumanName name) {
    StringBuilder s = new StringBuilder();
    if (name.hasText())
      s.append(name.getText());
    else {
      for (StringType p : name.getGiven()) {
        s.append(p.getValue());
        s.append(" ");
      }
      if (name.hasFamily()) {
        s.append(name.getFamily());
        s.append(" ");
      }
    }
    if (name.hasUse() && name.getUse() != NameUse.USUAL)
      s.append("("+name.getUse().toString()+")");
    return s.toString();
  }

  private String displayAddress(Address address) {
    StringBuilder s = new StringBuilder();
    if (address.hasText())
      s.append(address.getText());
    else {
      for (StringType p : address.getLine()) {
        s.append(p.getValue());
        s.append(" ");
      }
      if (address.hasCity()) {
        s.append(address.getCity());
        s.append(" ");
      }
      if (address.hasState()) {
        s.append(address.getState());
        s.append(" ");
      }

      if (address.hasPostalCode()) {
        s.append(address.getPostalCode());
        s.append(" ");
      }

      if (address.hasCountry()) {
        s.append(address.getCountry());
        s.append(" ");
      }
    }
    if (address.hasUse())
      s.append("("+address.getUse().toString()+")");
    return s.toString();
  }

  public static String displayContactPoint(ContactPoint contact) {
    StringBuilder s = new StringBuilder();
    s.append(describeSystem(contact.getSystem()));
    if (Utilities.noString(contact.getValue()))
      s.append("-unknown-");
    else
      s.append(contact.getValue());
    if (contact.hasUse())
      s.append("("+contact.getUse().toString()+")");
    return s.toString();
  }

  private static String describeSystem(ContactPointSystem system) {
    if (system == null)
      return "";
    switch (system) {
    case PHONE: return "ph: ";
    case FAX: return "fax: ";
    default:
      return "";
    }
  }

  private String displayIdentifier(Identifier ii) {
    String s = Utilities.noString(ii.getValue()) ? "??" : ii.getValue();

    if (ii.hasType()) {
    	if (ii.getType().hasText())
    		s = ii.getType().getText()+" = "+s;
    	else if (ii.getType().hasCoding() && ii.getType().getCoding().get(0).hasDisplay())
    		s = ii.getType().getCoding().get(0).getDisplay()+" = "+s;
    	else if (ii.getType().hasCoding() && ii.getType().getCoding().get(0).hasCode())
    		s = lookupCode(ii.getType().getCoding().get(0).getSystem(), ii.getType().getCoding().get(0).getCode())+" = "+s;
    }

    if (ii.hasUse())
      s = s + " ("+ii.getUse().toString()+")";
    return s;
  }

  private List<ElementDefinition> getChildrenForPath(List<ElementDefinition> elements, String path) throws DefinitionException {
    // do we need to do a name reference substitution?
    for (ElementDefinition e : elements) {
      if (e.getPath().equals(path) && e.hasContentReference()) {
      	String ref = e.getContentReference();
      	ElementDefinition t = null;
      	// now, resolve the name
        for (ElementDefinition e1 : elements) {
        	if (ref.equals("#"+e1.getId()))
        		t = e1;
        }
        if (t == null)
        	throw new DefinitionException("Unable to resolve content reference "+ref+" trying to resolve "+path);
        path = t.getPath();
        break;
      }
    }

    List<ElementDefinition> results = new ArrayList<ElementDefinition>();
    for (ElementDefinition e : elements) {
      if (e.getPath().startsWith(path+".") && !e.getPath().substring(path.length()+1).contains("."))
        results.add(e);
    }
    return results;
  }


  public boolean generate(ResourceContext rcontext, ConceptMap cm) throws FHIRFormatError, DefinitionException, IOException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.h2().addText(cm.getName()+" ("+cm.getUrl()+")");

    XhtmlNode p = x.para();
    p.tx("Mapping from ");
    if (cm.hasSource())
      AddVsRef(rcontext, cm.getSource().primitiveValue(), p);
    else
      p.tx("(not specified)");
    p.tx(" to ");
    if (cm.hasTarget())
      AddVsRef(rcontext, cm.getTarget().primitiveValue(), p);
    else 
      p.tx("(not specified)");

    p = x.para();
    if (cm.getExperimental())
      p.addText(Utilities.capitalize(cm.getStatus().toString())+" (not intended for production usage). ");
    else
      p.addText(Utilities.capitalize(cm.getStatus().toString())+". ");
    p.tx("Published on "+(cm.hasDate() ? cm.getDateElement().toHumanDisplay() : "??")+" by "+cm.getPublisher());
    if (!cm.getContact().isEmpty()) {
      p.tx(" (");
      boolean firsti = true;
      for (ContactDetail ci : cm.getContact()) {
        if (firsti)
          firsti = false;
        else
          p.tx(", ");
        if (ci.hasName())
          p.addText(ci.getName()+": ");
        boolean first = true;
        for (ContactPoint c : ci.getTelecom()) {
          if (first)
            first = false;
          else
            p.tx(", ");
          addTelecom(p, c);
        }
      }
      p.tx(")");
    }
    p.tx(". ");
    p.addText(cm.getCopyright());
    if (!Utilities.noString(cm.getDescription()))
      addMarkdown(x, cm.getDescription());

    x.br();

    for (ConceptMapGroupComponent grp : cm.getGroup()) {
      String src = grp.getSource();
      boolean comment = false;
      boolean ok = true;
    Map<String, HashSet<String>> sources = new HashMap<String, HashSet<String>>();
    Map<String, HashSet<String>> targets = new HashMap<String, HashSet<String>>();
      sources.put("code", new HashSet<String>());
    targets.put("code", new HashSet<String>());
      SourceElementComponent cc = grp.getElement().get(0);
      String dst = grp.getTarget();
      sources.get("code").add(grp.getSource());
      targets.get("code").add(grp.getTarget());
      for (SourceElementComponent ccl : grp.getElement()) {
        ok = ok && ccl.getTarget().size() == 1 && ccl.getTarget().get(0).getDependsOn().isEmpty() && ccl.getTarget().get(0).getProduct().isEmpty();
      	for (TargetElementComponent ccm : ccl.getTarget()) {
      		comment = comment || !Utilities.noString(ccm.getComment());
      		for (OtherElementComponent d : ccm.getDependsOn()) {
            if (!sources.containsKey(d.getProperty()))
              sources.put(d.getProperty(), new HashSet<String>());
            sources.get(d.getProperty()).add(d.getSystem());
      		}
      		for (OtherElementComponent d : ccm.getProduct()) {
            if (!targets.containsKey(d.getProperty()))
              targets.put(d.getProperty(), new HashSet<String>());
            targets.get(d.getProperty()).add(d.getSystem());
            }

      		}
      	}

      String display;
      if (ok) {
        // simple
        XhtmlNode tbl = x.table( "grid");
        XhtmlNode tr = tbl.tr();
        tr.td().b().tx("Source Code");
        tr.td().b().tx("Equivalence");
        tr.td().b().tx("Destination Code");
        if (comment)
          tr.td().b().tx("Comment");
        for (SourceElementComponent ccl : grp.getElement()) {
          tr = tbl.tr();
          XhtmlNode td = tr.td();
          td.addText(ccl.getCode());
          display = getDisplayForConcept(grp.getSource(), ccl.getCode());
          if (display != null && !isSameCodeAndDisplay(ccl.getCode(), display))
            td.tx(" ("+display+")");
          TargetElementComponent ccm = ccl.getTarget().get(0);
          tr.td().addText(!ccm.hasEquivalence() ? "" : ccm.getEquivalence().toCode());
          td = tr.td();
          td.addText(ccm.getCode());
          display = getDisplayForConcept(grp.getTarget(), ccm.getCode());
          if (display != null && !isSameCodeAndDisplay(ccm.getCode(), display))
            td.tx(" ("+display+")");
          if (comment)
            tr.td().addText(ccm.getComment());
        }
      } else {
        XhtmlNode tbl = x.table( "grid");
        XhtmlNode tr = tbl.tr();
        XhtmlNode td;
        tr.td().colspan(Integer.toString(sources.size())).b().tx("Source Concept");
        tr.td().b().tx("Equivalence");
        tr.td().colspan(Integer.toString(targets.size())).b().tx("Destination Concept");
        if (comment)
          tr.td().b().tx("Comment");
        tr = tbl.tr();
        if (sources.get("code").size() == 1)
          tr.td().b().tx("Code "+sources.get("code").toString()+"");
        else
          tr.td().b().tx("Code");
        for (String s : sources.keySet()) {
          if (!s.equals("code")) {
            if (sources.get(s).size() == 1)
              tr.td().b().addText(getDescForConcept(s) +" "+sources.get(s).toString());
            else
              tr.td().b().addText(getDescForConcept(s));
          }
        }
        tr.td();
        if (targets.get("code").size() == 1)
          tr.td().b().tx("Code "+targets.get("code").toString());
        else
          tr.td().b().tx("Code");
        for (String s : targets.keySet()) {
          if (!s.equals("code")) {
            if (targets.get(s).size() == 1)
              tr.td().b().addText(getDescForConcept(s) +" "+targets.get(s).toString()+"");
            else
              tr.td().b().addText(getDescForConcept(s));
          }
        }
        if (comment)
          tr.td();

        for (SourceElementComponent ccl : grp.getElement()) {
          tr = tbl.tr();
          td = tr.td();
          if (sources.get("code").size() == 1)
            td.addText(ccl.getCode());
          else
            td.addText(grp.getSource()+" / "+ccl.getCode());
          display = getDisplayForConcept(grp.getSource(), ccl.getCode());
          if (display != null)
            td.tx(" ("+display+")");

          TargetElementComponent ccm = ccl.getTarget().get(0);
          for (String s : sources.keySet()) {
            if (!s.equals("code")) {
              td = tr.td();
              td.addText(getValue(ccm.getDependsOn(), s, sources.get(s).size() != 1));
              display = getDisplay(ccm.getDependsOn(), s);
              if (display != null)
                td.tx(" ("+display+")");
            }
          }
          if (!ccm.hasEquivalence())
            tr.td().tx(":"+"("+ConceptMapEquivalence.EQUIVALENT.toCode()+")");
          else
            tr.td().tx(":"+ccm.getEquivalence().toCode());
          td = tr.td();
          if (targets.get("code").size() == 1)
            td.addText(ccm.getCode());
          else
            td.addText(grp.getTarget()+" / "+ccm.getCode());
          display = getDisplayForConcept(grp.getTarget(), ccm.getCode());
          if (display != null)
            td.tx(" ("+display+")");

          for (String s : targets.keySet()) {
            if (!s.equals("code")) {
              td = tr.td();
              td.addText(getValue(ccm.getProduct(), s, targets.get(s).size() != 1));
              display = getDisplay(ccm.getProduct(), s);
              if (display != null)
                td.tx(" ("+display+")");
            }
          }
          if (comment)
            tr.td().addText(ccm.getComment());
        }
      }
    }

    inject(cm, x, NarrativeStatus.GENERATED);
    return true;
  }



  private boolean isSameCodeAndDisplay(String code, String display) {
    String c = code.replace(" ", "").replace("-", "").toLowerCase();
    String d = display.replace(" ", "").replace("-", "").toLowerCase();
    return c.equals(d);
  }

  private void inject(DomainResource r, XhtmlNode x, NarrativeStatus status) {
    if (!x.hasAttribute("xmlns"))
      x.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
    if (!r.hasText() || !r.getText().hasDiv() || r.getText().getDiv().getChildNodes().isEmpty()) {
      r.setText(new Narrative());
      r.getText().setDiv(x);
      r.getText().setStatus(status);
    } else {
      XhtmlNode n = r.getText().getDiv();
      n.hr();
      n.getChildNodes().addAll(x.getChildNodes());
    }
  }

  public Element getNarrative(Element er) {
    Element txt = XMLUtil.getNamedChild(er, "text");
    if (txt == null)
      return null;
    return XMLUtil.getNamedChild(txt, "div");
  }


  private void inject(Element er, XhtmlNode x, NarrativeStatus status) {
    if (!x.hasAttribute("xmlns"))
      x.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
    Element txt = XMLUtil.getNamedChild(er, "text");
    if (txt == null) {
      txt = er.getOwnerDocument().createElementNS(FormatUtilities.FHIR_NS, "text");
      Element n = XMLUtil.getFirstChild(er);
      while (n != null && (n.getNodeName().equals("id") || n.getNodeName().equals("meta") || n.getNodeName().equals("implicitRules") || n.getNodeName().equals("language")))
        n = XMLUtil.getNextSibling(n);
      if (n == null)
        er.appendChild(txt);
      else
        er.insertBefore(txt, n);
    }
    Element st = XMLUtil.getNamedChild(txt, "status");
    if (st == null) {
      st = er.getOwnerDocument().createElementNS(FormatUtilities.FHIR_NS, "status");
      Element n = XMLUtil.getFirstChild(txt);
      if (n == null)
        txt.appendChild(st);
      else
        txt.insertBefore(st, n);
    }
    st.setAttribute("value", status.toCode());
    Element div = XMLUtil.getNamedChild(txt, "div");
    if (div == null) {
      div = er.getOwnerDocument().createElementNS(FormatUtilities.XHTML_NS, "div");
      div.setAttribute("xmlns", FormatUtilities.XHTML_NS);
      txt.appendChild(div);
    }
    if (div.hasChildNodes())
      div.appendChild(er.getOwnerDocument().createElementNS(FormatUtilities.XHTML_NS, "hr"));
    new XhtmlComposer(XhtmlComposer.XML, pretty).compose(div, x);
  }

  private void inject(org.hl7.fhir.r4.elementmodel.Element er, XhtmlNode x, NarrativeStatus status) throws IOException, FHIRException {
    if (!x.hasAttribute("xmlns"))
      x.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
    org.hl7.fhir.r4.elementmodel.Element txt = er.getNamedChild("text");
    if (txt == null) {
      txt = new org.hl7.fhir.r4.elementmodel.Element("text", er.getProperty().getChild(null, "text"));
      int i = 0;
      while (i < er.getChildren().size() && (er.getChildren().get(i).getName().equals("id") || er.getChildren().get(i).getName().equals("meta") || er.getChildren().get(i).getName().equals("implicitRules") || er.getChildren().get(i).getName().equals("language")))
        i++;
      if (i >= er.getChildren().size())
        er.getChildren().add(txt);
      else
        er.getChildren().add(i, txt);
    }
    org.hl7.fhir.r4.elementmodel.Element st = txt.getNamedChild("status");
    if (st == null) {
      st = new org.hl7.fhir.r4.elementmodel.Element("status", txt.getProperty().getChild(null, "status"));
      txt.getChildren().add(0, st);
    }
    st.setValue(status.toCode());
    org.hl7.fhir.r4.elementmodel.Element div = txt.getNamedChild("div");
    if (div == null) {
      div = new org.hl7.fhir.r4.elementmodel.Element("div", txt.getProperty().getChild(null, "div"));
      txt.getChildren().add(div);
      div.setValue(new XhtmlComposer(XhtmlComposer.XML, pretty).compose(x));
    }
    div.setXhtml(x);
  }

  private String getDisplay(List<OtherElementComponent> list, String s) {
    for (OtherElementComponent c : list) {
      if (s.equals(c.getProperty()))
        return getDisplayForConcept(c.getSystem(), c.getValue());
    }
    return null;
  }

  private String getDisplayForConcept(String system, String value) {
    if (value == null || system == null)
      return null;
    ValidationResult cl = context.validateCode(system, value, null);
    return cl == null ? null : cl.getDisplay();
  }



  private String getDescForConcept(String s) {
    if (s.startsWith("http://hl7.org/fhir/v2/element/"))
        return "v2 "+s.substring("http://hl7.org/fhir/v2/element/".length());
    return s;
  }

  private String getValue(List<OtherElementComponent> list, String s, boolean withSystem) {
    for (OtherElementComponent c : list) {
      if (s.equals(c.getProperty()))
        if (withSystem)
          return c.getSystem()+" / "+c.getValue();
        else
          return c.getValue();
    }
    return null;
  }

  private void addTelecom(XhtmlNode p, ContactPoint c) {
    if (c.getSystem() == ContactPointSystem.PHONE) {
      p.tx("Phone: "+c.getValue());
    } else if (c.getSystem() == ContactPointSystem.FAX) {
      p.tx("Fax: "+c.getValue());
    } else if (c.getSystem() == ContactPointSystem.EMAIL) {
      p.ah( "mailto:"+c.getValue()).addText(c.getValue());
    } else if (c.getSystem() == ContactPointSystem.URL) {
      if (c.getValue().length() > 30)
        p.ah(c.getValue()).addText(c.getValue().substring(0, 30)+"...");
      else
        p.ah(c.getValue()).addText(c.getValue());
    }
  }

  /**
   * This generate is optimised for the FHIR build process itself in as much as it
   * generates hyperlinks in the narrative that are only going to be correct for
   * the purposes of the build. This is to be reviewed in the future.
   *
   * @param vs
   * @param codeSystems
   * @throws IOException
   * @throws DefinitionException
   * @throws FHIRFormatError
   * @throws Exception
   */
  public boolean generate(ResourceContext rcontext, CodeSystem cs, boolean header, String lang) throws FHIRFormatError, DefinitionException, IOException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    boolean hasExtensions = false;
    hasExtensions = generateDefinition(x, cs, header, lang);
    inject(cs, x, hasExtensions ? NarrativeStatus.EXTENSIONS :  NarrativeStatus.GENERATED);
    return true;
  }

  private boolean generateDefinition(XhtmlNode x, CodeSystem cs, boolean header, String lang) throws FHIRFormatError, DefinitionException, IOException {
    boolean hasExtensions = false;

    if (header) {
      XhtmlNode h = x.h2();
      h.addText(cs.hasTitle() ? cs.getTitle() : cs.getName());
      addMarkdown(x, cs.getDescription());
      if (cs.hasCopyright())
        generateCopyright(x, cs, lang);
    }

    generateProperties(x, cs, lang);
    generateFilters(x, cs, lang);
    List<UsedConceptMap> maps = new ArrayList<UsedConceptMap>();
    hasExtensions = generateCodeSystemContent(x, cs, hasExtensions, maps, lang);

    return hasExtensions;
  }

  private void generateFilters(XhtmlNode x, CodeSystem cs, String lang) {
    if (cs.hasFilter()) {
      x.para().b().tx(context.translator().translate("xhtml-gen-cs", "Filters", lang));
      XhtmlNode tbl = x.table("grid");
      XhtmlNode tr = tbl.tr();
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Code", lang));
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Description", lang));
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "operator", lang));
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Value", lang));
      for (CodeSystemFilterComponent f : cs.getFilter()) {
        tr = tbl.tr();
        tr.td().tx(f.getCode());
        tr.td().tx(f.getDescription());
        XhtmlNode td = tr.td();
        for (Enumeration<org.hl7.fhir.r4.model.CodeSystem.FilterOperator> t : f.getOperator())
          td.tx(t.asStringValue()+" ");
        tr.td().tx(f.getValue());
      }
    }
  }

  private void generateProperties(XhtmlNode x, CodeSystem cs, String lang) {
    if (cs.hasProperty()) {
      x.para().b().tx(context.translator().translate("xhtml-gen-cs", "Properties", lang));
      XhtmlNode tbl = x.table("grid");
      XhtmlNode tr = tbl.tr();
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Code", lang));
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "URL", lang));
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Description", lang));
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Type", lang));
      for (PropertyComponent p : cs.getProperty()) {
        tr = tbl.tr();
        tr.td().tx(p.getCode());
        tr.td().tx(p.getUri());
        tr.td().tx(p.getDescription());
        tr.td().tx(p.hasType() ? p.getType().toCode() : "");
      }
    }
  }

  private boolean generateCodeSystemContent(XhtmlNode x, CodeSystem cs, boolean hasExtensions, List<UsedConceptMap> maps, String lang) throws FHIRFormatError, DefinitionException, IOException {
    XhtmlNode p = x.para();
    if (cs.getContent() == CodeSystemContentMode.COMPLETE)
      p.tx(context.translator().translateAndFormat("xhtml-gen-cs", lang, "This code system %s defines the following codes", cs.getUrl())+":");
    else if (cs.getContent() == CodeSystemContentMode.EXAMPLE)
      p.tx(context.translator().translateAndFormat("xhtml-gen-cs", lang, "This code system %s defines many codes, of which the following are some examples", cs.getUrl())+":");
    else if (cs.getContent() == CodeSystemContentMode.FRAGMENT )
      p.tx(context.translator().translateAndFormat("xhtml-gen-cs", lang, "This code system %s defines many codes, of which the following are a subset", cs.getUrl())+":");
    else if (cs.getContent() == CodeSystemContentMode.NOTPRESENT ) {
      p.tx(context.translator().translateAndFormat("xhtml-gen-cs", lang, "This code system %s defines many codes, but they are not represented here", cs.getUrl()));
      return false;
    }
    XhtmlNode t = x.table( "codes");
    boolean commentS = false;
    boolean deprecated = false;
    boolean display = false;
    boolean hierarchy = false;
    boolean version = false;
    for (ConceptDefinitionComponent c : cs.getConcept()) {
      commentS = commentS || conceptsHaveComments(c);
      deprecated = deprecated || conceptsHaveDeprecated(cs, c);
      display = display || conceptsHaveDisplay(c);
      version = version || conceptsHaveVersion(c);
      hierarchy = hierarchy || c.hasConcept();
    }
    addMapHeaders(addTableHeaderRowStandard(t, hierarchy, display, true, commentS, version, deprecated, lang), maps);
    for (ConceptDefinitionComponent c : cs.getConcept()) {
      hasExtensions = addDefineRowToTable(t, c, 0, hierarchy, display, commentS, version, deprecated, maps, cs.getUrl(), cs, lang) || hasExtensions;
    }
//    if (langs.size() > 0) {
//      Collections.sort(langs);
//      x.para().b().tx("Additional Language Displays");
//      t = x.table( "codes");
//      XhtmlNode tr = t.tr();
//      tr.td().b().tx("Code");
//      for (String lang : langs)
//        tr.td().b().addText(describeLang(lang));
//      for (ConceptDefinitionComponent c : cs.getConcept()) {
//        addLanguageRow(c, t, langs);
//      }
//    }
    return hasExtensions;
  }

  private int countConcepts(List<ConceptDefinitionComponent> list) {
    int count = list.size();
    for (ConceptDefinitionComponent c : list)
      if (c.hasConcept())
        count = count + countConcepts(c.getConcept());
    return count;
  }

  private void generateCopyright(XhtmlNode x, CodeSystem cs, String lang) {
    XhtmlNode p = x.para();
    p.b().tx(context.translator().translate("xhtml-gen-cs", "Copyright Statement:", lang));
    smartAddText(p, " " + cs.getCopyright());
  }


  /**
   * This generate is optimised for the FHIR build process itself in as much as it
   * generates hyperlinks in the narrative that are only going to be correct for
   * the purposes of the build. This is to be reviewed in the future.
   *
   * @param vs
   * @param codeSystems
   * @throws FHIRException
   * @throws IOException
   * @throws Exception
   */
  public boolean generate(ResourceContext rcontext, ValueSet vs, boolean header) throws FHIRException, IOException {
    generate(rcontext, vs, null, header);
    return true;
  }

  public void generate(ResourceContext rcontext, ValueSet vs, ValueSet src, boolean header) throws FHIRException, IOException {
    List<UsedConceptMap> maps = findReleventMaps(vs);
    
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    boolean hasExtensions;
    if (vs.hasExpansion()) {
      // for now, we just accept an expansion if there is one
      hasExtensions = generateExpansion(x, vs, src, header, maps);
    } else {
      hasExtensions = generateComposition(rcontext, x, vs, header, maps);
    }
    inject(vs, x, hasExtensions ? NarrativeStatus.EXTENSIONS :  NarrativeStatus.GENERATED);
  }

  private List<UsedConceptMap> findReleventMaps(ValueSet vs) throws FHIRException {
    List<UsedConceptMap> res = new ArrayList<UsedConceptMap>();
    for (MetadataResource md : context.allConformanceResources()) {
      if (md instanceof ConceptMap) {
        ConceptMap cm = (ConceptMap) md;
        if (isSource(vs, cm.getSource())) {
          ConceptMapRenderInstructions re = findByTarget(cm.getTarget());
          if (re != null) {
            ValueSet vst = cm.hasTarget() ? context.fetchResource(ValueSet.class, cm.hasTargetCanonicalType() ? cm.getTargetCanonicalType().getValue() : cm.getTargetUriType().asStringValue()) : null;
            res.add(new UsedConceptMap(re, vst == null ? cm.getUserString("path") : vst.getUserString("path"), cm));
          }
        }
      }
    }
    return res;
//    Map<ConceptMap, String> mymaps = new HashMap<ConceptMap, String>();
//  for (ConceptMap a : context.findMapsForSource(vs.getUrl())) {
//    String url = "";
//    ValueSet vsr = context.fetchResource(ValueSet.class, ((Reference) a.getTarget()).getReference());
//    if (vsr != null)
//      url = (String) vsr.getUserData("filename");
//    mymaps.put(a, url);
//  }
//    Map<ConceptMap, String> mymaps = new HashMap<ConceptMap, String>();
//  for (ConceptMap a : context.findMapsForSource(cs.getValueSet())) {
//    String url = "";
//    ValueSet vsr = context.fetchResource(ValueSet.class, ((Reference) a.getTarget()).getReference());
//    if (vsr != null)
//      url = (String) vsr.getUserData("filename");
//    mymaps.put(a, url);
//  }
    // also, look in the contained resources for a concept map
//    for (Resource r : cs.getContained()) {
//      if (r instanceof ConceptMap) {
//        ConceptMap cm = (ConceptMap) r;
//        if (((Reference) cm.getSource()).getReference().equals(cs.getValueSet())) {
//          String url = "";
//          ValueSet vsr = context.fetchResource(ValueSet.class, ((Reference) cm.getTarget()).getReference());
//          if (vsr != null)
//              url = (String) vsr.getUserData("filename");
//        mymaps.put(cm, url);
//        }
//      }
//    }
  }

  private ConceptMapRenderInstructions findByTarget(Type source) {
    String src = source.primitiveValue();
    if (src != null)
      for (ConceptMapRenderInstructions t : renderingMaps) {
        if (src.equals(t.url))
          return t;
      }
    return null;
  }

  private boolean isSource(ValueSet vs, Type source) {
    return vs.getUrl().equals(source.primitiveValue());
  }

  private Integer countMembership(ValueSet vs) {
    int count = 0;
    if (vs.hasExpansion())
      count = count + conceptCount(vs.getExpansion().getContains());
    else {
      if (vs.hasCompose()) {
        if (vs.getCompose().hasExclude()) {
          try {
            ValueSetExpansionOutcome vse = context.expandVS(vs, true, false);
            count = 0;
            count += conceptCount(vse.getValueset().getExpansion().getContains());
            return count;
          } catch (Exception e) {
            return null;
          }
        }
        for (ConceptSetComponent inc : vs.getCompose().getInclude()) {
          if (inc.hasFilter())
            return null;
          if (!inc.hasConcept())
            return null;
          count = count + inc.getConcept().size();
        }
      }
    }
    return count;
  }

  private int conceptCount(List<ValueSetExpansionContainsComponent> list) {
    int count = 0;
    for (ValueSetExpansionContainsComponent c : list) {
      if (!c.getAbstract())
        count++;
      count = count + conceptCount(c.getContains());
    }
    return count;
  }

  private boolean generateExpansion(XhtmlNode x, ValueSet vs, ValueSet src, boolean header, List<UsedConceptMap> maps) throws FHIRFormatError, DefinitionException, IOException {
    boolean hasExtensions = false;
    List<String> langs = new ArrayList<String>();


    if (header) {
      XhtmlNode h = x.addTag(getHeader());
      h.tx("Value Set Contents");
      if (IsNotFixedExpansion(vs))
        addMarkdown(x, vs.getDescription());
      if (vs.hasCopyright())
        generateCopyright(x, vs);
    }
    if (ToolingExtensions.hasExtension(vs.getExpansion(), "http://hl7.org/fhir/StructureDefinition/valueset-toocostly"))
      x.para().setAttribute("style", "border: maroon 1px solid; background-color: #FFCCCC; font-weight: bold; padding: 8px").addText(vs.getExpansion().getContains().isEmpty() ? tooCostlyNoteEmpty : tooCostlyNoteNotEmpty );
    else {
      Integer count = countMembership(vs);
      if (count == null)
        x.para().tx("This value set does not contain a fixed number of concepts");
      else
        x.para().tx("This value set contains "+count.toString()+" concepts");
    }

    generateVersionNotice(x, vs.getExpansion());

    CodeSystem allCS = null;
    boolean doLevel = false;
    for (ValueSetExpansionContainsComponent cc : vs.getExpansion().getContains()) {
      if (cc.hasContains()) {
        doLevel = true;
        break;
      }
    }
    
    boolean doSystem = true; // checkDoSystem(vs, src);
    boolean doDefinition = checkDoDefinition(vs.getExpansion().getContains());
    if (doSystem && allFromOneSystem(vs)) {
      doSystem = false;
      XhtmlNode p = x.para();
      p.tx("All codes from system ");
      allCS = context.fetchCodeSystem(vs.getExpansion().getContains().get(0).getSystem());
      String ref = null;
      if (allCS != null)
        ref = getCsRef(allCS);
      if (ref == null)
        p.code(vs.getExpansion().getContains().get(0).getSystem());
      else
        p.ah(prefix+ref).code(vs.getExpansion().getContains().get(0).getSystem());
    }
    XhtmlNode t = x.table( "codes");
    XhtmlNode tr = t.tr();
    if (doLevel)
      tr.td().b().tx("Lvl");
    tr.td().attribute("style", "white-space:nowrap").b().tx("Code");
    if (doSystem)
      tr.td().b().tx("System");
    tr.td().b().tx("Display");
    if (doDefinition)
      tr.td().b().tx("Definition");

    addMapHeaders(tr, maps);
    for (ValueSetExpansionContainsComponent c : vs.getExpansion().getContains()) {
      addExpansionRowToTable(t, c, 0, doLevel, doSystem, doDefinition, maps, allCS, langs);
    }

    // now, build observed languages

    if (langs.size() > 0) {
      Collections.sort(langs);
      x.para().b().tx("Additional Language Displays");
      t = x.table( "codes");
      tr = t.tr();
      tr.td().b().tx("Code");
      for (String lang : langs)
        tr.td().b().addText(describeLang(lang));
      for (ValueSetExpansionContainsComponent c : vs.getExpansion().getContains()) {
        addLanguageRow(c, t, langs);
      }
    }

    return hasExtensions;
  }

  @SuppressWarnings("rawtypes")
  private void generateVersionNotice(XhtmlNode x, ValueSetExpansionComponent expansion) {
    Map<String, String> versions = new HashMap<String, String>();
    for (ValueSetExpansionParameterComponent p : expansion.getParameter()) {
      if (p.getName().equals("version")) {
        String[] parts = ((PrimitiveType) p.getValue()).asStringValue().split("\\|");
        if (parts.length == 2)
          versions.put(parts[0], parts[1]);
      }
    }
    if (!versions.isEmpty()) {
      StringBuilder b = new StringBuilder();
      b.append("Expansion based on ");
      boolean first = true;
      for (String s : versions.keySet()) {
        if (first)
          first = false;
        else
          b.append(", ");
        if (!s.equals("http://snomed.info/sct"))
          b.append(describeSystem(s)+" version "+versions.get(s));
        else {
          String[] parts = versions.get(s).split("\\/");
          if (parts.length >= 5) {
            String m = describeModule(parts[4]);
            if (parts.length == 7)
              b.append("SNOMED CT "+m+" edition "+formatSCTDate(parts[6]));
            else
              b.append("SNOMED CT "+m+" edition");
          } else
            b.append(describeSystem(s)+" version "+versions.get(s));
        }
      }

      x.para().setAttribute("style", "border: black 1px dotted; background-color: #EEEEEE; padding: 8px").addText(b.toString());
    }
  }

  private String formatSCTDate(String ds) {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    Date date;
    try {
      date = format.parse(ds);
    } catch (ParseException e) {
      return ds;
    }
    return new SimpleDateFormat("dd-MMM yyyy").format(date);
  }

  private String describeModule(String module) {
    if ("900000000000207008".equals(module))
      return "International";
    if ("731000124108".equals(module))
      return "United States";
    if ("32506021000036107".equals(module))
      return "Australian";
    if ("449081005".equals(module))
      return "Spanish";
    if ("554471000005108".equals(module))
      return "Danish";
    if ("11000146104".equals(module))
      return "Dutch";
    if ("45991000052106".equals(module))
      return "Swedish";
    if ("999000041000000102".equals(module))
      return "United Kingdon";
    return module;
  }

  private boolean hasVersionParameter(ValueSetExpansionComponent expansion) {
    for (ValueSetExpansionParameterComponent p : expansion.getParameter()) {
      if (p.getName().equals("version"))
        return true;
    }
    return false;
  }

  private void addLanguageRow(ValueSetExpansionContainsComponent c, XhtmlNode t, List<String> langs) {
    XhtmlNode tr = t.tr();
    tr.td().addText(c.getCode());
    for (String lang : langs) {
      String d = null;
      for (Extension ext : c.getExtension()) {
        if (ToolingExtensions.EXT_TRANSLATION.equals(ext.getUrl())) {
          String l = ToolingExtensions.readStringExtension(ext, "lang");
          if (lang.equals(l))
            d = ToolingExtensions.readStringExtension(ext, "content");;
        }
      }
      tr.td().addText(d == null ? "" : d);
    }
    for (ValueSetExpansionContainsComponent cc : c.getContains()) {
      addLanguageRow(cc, t, langs);
    }
  }


  private String describeLang(String lang) {
    ValueSet v = context.fetchResource(ValueSet.class, "http://hl7.org/fhir/ValueSet/languages");
    if (v != null) {
      ConceptReferenceComponent l = null;
      for (ConceptReferenceComponent cc : v.getCompose().getIncludeFirstRep().getConcept()) {
        if (cc.getCode().equals(lang))
          l = cc;
      }
      if (l == null) {
        if (lang.contains("-"))
          lang = lang.substring(0, lang.indexOf("-"));
        for (ConceptReferenceComponent cc : v.getCompose().getIncludeFirstRep().getConcept()) {
          if (cc.getCode().equals(lang) || cc.getCode().startsWith(lang+"-"))
            l = cc;
        }
      }
      if (l != null) {
        if (lang.contains("-"))
          lang = lang.substring(0, lang.indexOf("-"));
        String en = l.getDisplay();
        String nativelang = null;
        for (ConceptReferenceDesignationComponent cd : l.getDesignation()) {
          if (cd.getLanguage().equals(lang))
            nativelang = cd.getValue();
        }
        if (nativelang == null)
          return en+" ("+lang+")";
        else
          return nativelang+" ("+en+", "+lang+")";
      }
    }
    return lang;
  }


  private boolean checkDoDefinition(List<ValueSetExpansionContainsComponent> contains) {
    for (ValueSetExpansionContainsComponent c : contains) {
      CodeSystem cs = context.fetchCodeSystem(c.getSystem());
      if (cs != null)
        return true;
      if (checkDoDefinition(c.getContains()))
        return true;
    }
    return false;
  }


  private boolean allFromOneSystem(ValueSet vs) {
    if (vs.getExpansion().getContains().isEmpty())
      return false;
    String system = vs.getExpansion().getContains().get(0).getSystem();
    for (ValueSetExpansionContainsComponent cc : vs.getExpansion().getContains()) {
      if (!checkSystemMatches(system, cc))
        return false;
    }
    return true;
  }


  private boolean checkSystemMatches(String system, ValueSetExpansionContainsComponent cc) {
    if (!system.equals(cc.getSystem()))
      return false;
    for (ValueSetExpansionContainsComponent cc1 : cc.getContains()) {
      if (!checkSystemMatches(system, cc1))
        return false;
    }
     return true;
  }


  private boolean checkDoSystem(ValueSet vs, ValueSet src) {
    if (src != null)
      vs = src;
    if (vs.hasCompose())
      return true;
    return false;
  }

  private boolean IsNotFixedExpansion(ValueSet vs) {
    if (vs.hasCompose())
      return false;


    // it's not fixed if it has any includes that are not version fixed
    for (ConceptSetComponent cc : vs.getCompose().getInclude()) {
      if (cc.hasValueSet())
        return true;
      if (!cc.hasVersion())
        return true;
    }
    return false;
  }


  private void addLanguageRow(ConceptDefinitionComponent c, XhtmlNode t, List<String> langs) {
    XhtmlNode tr = t.tr();
    tr.td().addText(c.getCode());
    for (String lang : langs) {
      ConceptDefinitionDesignationComponent d = null;
      for (ConceptDefinitionDesignationComponent designation : c.getDesignation()) {
        if (designation.hasLanguage()) {
          if (lang.equals(designation.getLanguage()))
            d = designation;
        }
      }
      tr.td().addText(d == null ? "" : d.getValue());
    }
  }

//  private void scanLangs(ConceptDefinitionComponent c, List<String> langs) {
//    for (ConceptDefinitionDesignationComponent designation : c.getDesignation()) {
//      if (designation.hasLanguage()) {
//        String lang = designation.getLanguage();
//        if (langs != null && !langs.contains(lang) && c.hasDisplay() && !c.getDisplay().equalsIgnoreCase(designation.getValue()))
//          langs.add(lang);
//      }
//    }
//    for (ConceptDefinitionComponent g : c.getConcept())
//      scanLangs(g, langs);
//  }

  private void addMapHeaders(XhtmlNode tr, List<UsedConceptMap> maps) throws FHIRFormatError, DefinitionException, IOException {
	  for (UsedConceptMap m : maps) {
	  	XhtmlNode td = tr.td();
	  	XhtmlNode b = td.b();
	  	XhtmlNode a = b.ah(prefix+m.getLink());
      a.addText(m.getDetails().getName());
      if (m.getDetails().isDoDescription() && m.getMap().hasDescription())
        addMarkdown(td, m.getMap().getDescription());
	  }
  }

	private void smartAddText(XhtmlNode p, String text) {
	  if (text == null)
	    return;

    String[] lines = text.split("\\r\\n");
    for (int i = 0; i < lines.length; i++) {
      if (i > 0)
        p.br();
      p.addText(lines[i]);
    }
  }

  private boolean conceptsHaveComments(ConceptDefinitionComponent c) {
    if (ToolingExtensions.hasCSComment(c))
      return true;
    for (ConceptDefinitionComponent g : c.getConcept())
      if (conceptsHaveComments(g))
        return true;
    return false;
  }

  private boolean conceptsHaveDisplay(ConceptDefinitionComponent c) {
    if (c.hasDisplay())
      return true;
    for (ConceptDefinitionComponent g : c.getConcept())
      if (conceptsHaveDisplay(g))
        return true;
    return false;
  }

  private boolean conceptsHaveVersion(ConceptDefinitionComponent c) {
    if (c.hasUserData("cs.version.notes"))
      return true;
    for (ConceptDefinitionComponent g : c.getConcept())
      if (conceptsHaveVersion(g))
        return true;
    return false;
  }

  private boolean conceptsHaveDeprecated(CodeSystem cs, ConceptDefinitionComponent c) {
    if (CodeSystemUtilities.isDeprecated(cs, c))
      return true;
    for (ConceptDefinitionComponent g : c.getConcept())
      if (conceptsHaveDeprecated(cs, g))
        return true;
    return false;
  }

  private void generateCopyright(XhtmlNode x, ValueSet vs) {
    XhtmlNode p = x.para();
    p.b().tx("Copyright Statement:");
    smartAddText(p, " " + vs.getCopyright());
  }


  private XhtmlNode addTableHeaderRowStandard(XhtmlNode t, boolean hasHierarchy, boolean hasDisplay, boolean definitions, boolean comments, boolean version, boolean deprecated, String lang) {
    XhtmlNode tr = t.tr();
    if (hasHierarchy)
      tr.td().b().tx("Lvl");
    tr.td().attribute("style", "white-space:nowrap").b().tx(context.translator().translate("xhtml-gen-cs", "Code", lang));
    if (hasDisplay)
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Display", lang));
    if (definitions)
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Definition", lang));
    if (deprecated)
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Deprecated", lang));
    if (comments)
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Comments", lang));
    if (version)
      tr.td().b().tx(context.translator().translate("xhtml-gen-cs", "Version", lang));
    return tr;
  }

  private void addExpansionRowToTable(XhtmlNode t, ValueSetExpansionContainsComponent c, int i, boolean doLevel, boolean doSystem, boolean doDefinition, List<UsedConceptMap> maps, CodeSystem allCS, List<String> langs) {
    XhtmlNode tr = t.tr();
    XhtmlNode td = tr.td();

    String tgt = makeAnchor(c.getSystem(), c.getCode());
    td.an(tgt);

    if (doLevel) {
      td.addText(Integer.toString(i));
      td = tr.td();
    }
    String s = Utilities.padLeft("", '\u00A0', i*2);
    td.attribute("style", "white-space:nowrap").addText(s);
    addCodeToTable(c.getAbstract(), c.getSystem(), c.getCode(), c.getDisplay(), td);
    if (doSystem) {
      td = tr.td();
      td.addText(c.getSystem());
    }
    td = tr.td();
    if (c.hasDisplayElement())
      td.addText(c.getDisplay());

    if (doDefinition) {
      CodeSystem cs = allCS;
      if (cs == null)
        cs = context.fetchCodeSystem(c.getSystem());
      td = tr.td();
      if (cs != null)
        td.addText(CodeSystemUtilities.getCodeDefinition(cs, c.getCode()));
    }
    for (UsedConceptMap m : maps) {
      td = tr.td();
      List<TargetElementComponentWrapper> mappings = findMappingsForCode(c.getCode(), m.getMap());
      boolean first = true;
      for (TargetElementComponentWrapper mapping : mappings) {
        if (!first)
            td.br();
        first = false;
        XhtmlNode span = td.span(null, mapping.comp.getEquivalence().toString());
        span.addText(getCharForEquivalence(mapping.comp));
        addRefToCode(td, mapping.group.getTarget(), m.getLink(), mapping.comp.getCode()); 
        if (!Utilities.noString(mapping.comp.getComment()))
          td.i().tx("("+mapping.comp.getComment()+")");
      }
    }
    for (Extension ext : c.getExtension()) {
      if (ToolingExtensions.EXT_TRANSLATION.equals(ext.getUrl())) {
        String lang = ToolingExtensions.readStringExtension(ext,  "lang");
        if (!Utilities.noString(lang) && !langs.contains(lang))
          langs.add(lang);
      }
    }
    for (ValueSetExpansionContainsComponent cc : c.getContains()) {
      addExpansionRowToTable(t, cc, i+1, doLevel, doSystem, doDefinition, maps, allCS, langs);
    }
  }

  private void addCodeToTable(boolean isAbstract, String system, String code, String display, XhtmlNode td) {
    CodeSystem e = context.fetchCodeSystem(system);
    if (e == null || e.getContent() != org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode.COMPLETE) {
      if (isAbstract)
        td.i().setAttribute("title", ABSTRACT_CODE_HINT).addText(code);
      else if ("http://snomed.info/sct".equals(system)) {
        td.ah("http://browser.ihtsdotools.org/?perspective=full&conceptId1="+code).addText(code);
      } else if ("http://loinc.org".equals(system)) {
          td.ah("http://details.loinc.org/LOINC/"+code+".html").addText(code);
      } else        
        td.addText(code);
    } else {
      String href = prefix+getCsRef(e);
      if (href.contains("#"))
        href = href + "-"+Utilities.nmtokenize(code);
      else
        href = href + "#"+e.getId()+"-"+Utilities.nmtokenize(code);
      if (isAbstract)
        td.ah(href).setAttribute("title", ABSTRACT_CODE_HINT).i().addText(code);
      else
        td.ah(href).addText(code);
    }
  }

  private class TargetElementComponentWrapper {
    private ConceptMapGroupComponent group;
    private TargetElementComponent comp;
    public TargetElementComponentWrapper(ConceptMapGroupComponent group, TargetElementComponent comp) {
      super();
      this.group = group;
      this.comp = comp;
    }

  }

  private String langDisplay(String l, boolean isShort) {
    ValueSet vs = context.fetchResource(ValueSet.class, "http://hl7.org/fhir/ValueSet/languages");
    for (ConceptReferenceComponent vc : vs.getCompose().getInclude().get(0).getConcept()) {
      if (vc.getCode().equals(l)) {
        for (ConceptReferenceDesignationComponent cd : vc.getDesignation()) {
          if (cd.getLanguage().equals(l))
            return cd.getValue()+(isShort ? "" : " ("+vc.getDisplay()+")");
        }
        return vc.getDisplay();
      }
    }
    return "??Lang";
  }
 
  private boolean addDefineRowToTable(XhtmlNode t, ConceptDefinitionComponent c, int i, boolean hasHierarchy, boolean hasDisplay, boolean comment, boolean version, boolean deprecated, List<UsedConceptMap> maps, String system, CodeSystem cs, String lang) {
    boolean hasExtensions = false;
    XhtmlNode tr = t.tr();
    XhtmlNode td = tr.td();
    if (hasHierarchy) {
      td.addText(Integer.toString(i+1));
      td = tr.td();
      String s = Utilities.padLeft("", '\u00A0', i*2);
      td.addText(s);
    }
    td.attribute("style", "white-space:nowrap").addText(c.getCode());
    XhtmlNode a;
    if (c.hasCodeElement()) {
      td.an(cs.getId()+"-" + Utilities.nmtokenize(c.getCode()));
    }

    if (hasDisplay) {
      td = tr.td();
      if (c.hasDisplayElement()) {
        if (lang == null) {
          td.addText(c.getDisplay());
        } else if (lang.equals("*")) {
          boolean sl = false;
          for (ConceptDefinitionDesignationComponent cd : c.getDesignation()) 
            if (cd.getUse().is("http://hl7.org/fhir/CodeSystem/designation-usage", "display") && cd.hasLanguage() && !c.getDisplay().equalsIgnoreCase(cd.getValue())) 
              sl = true;
          td.addText((sl ? cs.getLanguage("en")+": " : "")+c.getDisplay());
          for (ConceptDefinitionDesignationComponent cd : c.getDesignation()) {
            if (cd.getUse().is("http://hl7.org/fhir/CodeSystem/designation-usage", "display") && cd.hasLanguage() && !c.getDisplay().equalsIgnoreCase(cd.getValue())) {
              td.br();
              td.addText(cd.getLanguage()+": "+cd.getValue());
            }
          }
       } else if (lang.equals(cs.getLanguage()) || (lang.equals("en") && !cs.hasLanguage())) {
         td.addText(c.getDisplay());
       } else {
         for (ConceptDefinitionDesignationComponent cd : c.getDesignation()) {
           if (cd.getUse().is("http://hl7.org/fhir/CodeSystem/designation-usage", "display") && cd.hasLanguage() && cd.getLanguage().equals(lang)) {
             td.addText(cd.getValue());
           }
         }
       }
      }
    }
    td = tr.td();
    if (c != null && 
        c.hasDefinitionElement()) {
      if (lang == null) {
        td.addText(c.getDefinition());
      } else if (lang.equals("*")) {
        boolean sl = false;
        for (ConceptDefinitionDesignationComponent cd : c.getDesignation()) 
          if (cd.getUse().is("http://hl7.org/fhir/CodeSystem/designation-usage", "definition") && cd.hasLanguage() && !c.getDefinition().equalsIgnoreCase(cd.getValue())) 
            sl = true;
        td.addText((sl ? cs.getLanguage("en")+": " : "")+c.getDefinition());
        for (ConceptDefinitionDesignationComponent cd : c.getDesignation()) {
          if (cd.getUse().is("http://hl7.org/fhir/CodeSystem/designation-usage", "definition") && cd.hasLanguage() && !c.getDefinition().equalsIgnoreCase(cd.getValue())) {
            td.br();
            td.addText(cd.getLanguage()+": "+cd.getValue());
          }
        }
     } else if (lang.equals(cs.getLanguage()) || (lang.equals("en") && !cs.hasLanguage())) {
       td.addText(c.getDefinition());
     } else {
       for (ConceptDefinitionDesignationComponent cd : c.getDesignation()) {
         if (cd.getUse().is("http://hl7.org/fhir/CodeSystem/designation-usage", "definition") && cd.hasLanguage() && cd.getLanguage().equals(lang)) {
           td.addText(cd.getValue());
         }
       }
     }
    }
    if (deprecated) {
      td = tr.td();
      Boolean b = CodeSystemUtilities.isDeprecated(cs, c);
      if (b !=  null && b) {
        smartAddText(td, context.translator().translate("xhtml-gen-cs", "Deprecated", lang));
        hasExtensions = true;
        if (ToolingExtensions.hasExtension(c, ToolingExtensions.EXT_REPLACED_BY)) {
          Coding cc = (Coding) ToolingExtensions.getExtension(c, ToolingExtensions.EXT_REPLACED_BY).getValue();
          td.tx(" (replaced by ");
          String url = getCodingReference(cc, system);
          if (url != null) {
            td.ah(url).addText(cc.getCode());
            td.tx(": "+cc.getDisplay()+")");
          } else
            td.addText(cc.getCode()+" '"+cc.getDisplay()+"' in "+cc.getSystem()+")");
        }
      }
    }
    if (comment) {
      td = tr.td();
      Extension ext = c.getExtensionByUrl(ToolingExtensions.EXT_CS_COMMENT);
      if (ext != null) {
        hasExtensions = true;
        String bc = ext.hasValue() ? ext.getValue().primitiveValue() : null;
        Map<String, String> translations = ToolingExtensions.getLanguageTranslations(ext.getValue());

        if (lang == null) {
          if (bc != null)
            td.addText(bc);
        } else if (lang.equals("*")) {
          boolean sl = false;
          for (String l : translations.keySet()) 
            if (bc == null || !bc.equalsIgnoreCase(translations.get(l))) 
              sl = true;
          if (bc != null) {
            td.addText((sl ? cs.getLanguage("en") : "")+bc);
          }
          for (String l : translations.keySet()) {
            if (bc == null || !bc.equalsIgnoreCase(translations.get(l))) {
              if (!td.getChildNodes().isEmpty()) 
                td.br();
              td.addText(l+": "+translations.get(l));
            }
          }
        } else if (lang.equals(cs.getLanguage()) || (lang.equals("en") && !cs.hasLanguage())) {
          if (bc != null)
            td.addText(bc);
        } else {
          if (bc != null)
            translations.put(cs.getLanguage("en"), bc);
          for (String l : translations.keySet()) { 
            if (l.equals(lang)) {
              td.addText(translations.get(l));
            }
          }
        }
      }      
    }
    if (version) {
      td = tr.td();
      if (c.hasUserData("cs.version.notes"))
        td.addText(c.getUserString("cs.version.notes"));
    }
    for (UsedConceptMap m : maps) {
      td = tr.td();
      List<TargetElementComponentWrapper> mappings = findMappingsForCode(c.getCode(), m.getMap());
      boolean first = true;
      for (TargetElementComponentWrapper mapping : mappings) {
      	if (!first)
      		  td.br();
      	first = false;
      	XhtmlNode span = td.span(null, mapping.comp.hasEquivalence() ?  mapping.comp.getEquivalence().toCode() : "");
        span.addText(getCharForEquivalence(mapping.comp));
      	a = td.ah(prefix+m.getLink()+"#"+makeAnchor(mapping.group.getTarget(), mapping.comp.getCode()));
        a.addText(mapping.comp.getCode());
        if (!Utilities.noString(mapping.comp.getComment()))
          td.i().tx("("+mapping.comp.getComment()+")");
      }
    }
    for (CodeType e : ToolingExtensions.getSubsumes(c)) {
      hasExtensions = true;
      tr = t.tr();
      td = tr.td();
      String s = Utilities.padLeft("", '.', i*2);
      td.addText(s);
      a = td.ah("#"+Utilities.nmtokenize(e.getValue()));
      a.addText(c.getCode());
    }
    for (ConceptDefinitionComponent cc : c.getConcept()) {
      hasExtensions = addDefineRowToTable(t, cc, i+1, hasHierarchy, hasDisplay, comment, version, deprecated, maps, system, cs, lang) || hasExtensions;
    }
    return hasExtensions;
  }


  private String makeAnchor(String codeSystem, String code) {
    String s = codeSystem+'-'+code;
    StringBuilder b = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '.')
        b.append(c);
      else
        b.append('-');
    }
    return b.toString();
  }

  private String getCodingReference(Coding cc, String system) {
    if (cc.getSystem().equals(system))
      return "#"+cc.getCode();
    if (cc.getSystem().equals("http://snomed.info/sct"))
      return "http://snomed.info/sct/"+cc.getCode();
    if (cc.getSystem().equals("http://loinc.org"))
      return "http://s.details.loinc.org/LOINC/"+cc.getCode()+".html";
    return null;
  }

  private String getCharForEquivalence(TargetElementComponent mapping) {
    if (!mapping.hasEquivalence())
      return "";
	  switch (mapping.getEquivalence()) {
	  case EQUAL : return "=";
	  case EQUIVALENT : return "~";
	  case WIDER : return "<";
	  case NARROWER : return ">";
	  case INEXACT : return "><";
	  case UNMATCHED : return "-";
	  case DISJOINT : return "!=";
    default: return "?";
	  }
  }

  private List<TargetElementComponentWrapper> findMappingsForCode(String code, ConceptMap map) {
    List<TargetElementComponentWrapper> mappings = new ArrayList<TargetElementComponentWrapper>();

    for (ConceptMapGroupComponent g : map.getGroup()) {
      for (SourceElementComponent c : g.getElement()) {
	  	if (c.getCode().equals(code))
          for (TargetElementComponent cc : c.getTarget())
            mappings.add(new TargetElementComponentWrapper(g, cc));
      }
	  }
	  return mappings;
  }

  private boolean generateComposition(ResourceContext rcontext, XhtmlNode x, ValueSet vs, boolean header, List<UsedConceptMap> maps) throws FHIRException, IOException {
	  boolean hasExtensions = false;
    List<String> langs = new ArrayList<String>();

    if (header) {
      XhtmlNode h = x.h2();
      h.addText(vs.getName());
      addMarkdown(x, vs.getDescription());
      if (vs.hasCopyrightElement())
        generateCopyright(x, vs);
    }
    XhtmlNode p = x.para();
    p.tx("This value set includes codes from the following code systems:");

    XhtmlNode ul = x.ul();
    XhtmlNode li;
    for (ConceptSetComponent inc : vs.getCompose().getInclude()) {
      hasExtensions = genInclude(rcontext, ul, inc, "Include", langs, maps) || hasExtensions;
    }
    for (ConceptSetComponent exc : vs.getCompose().getExclude()) {
      hasExtensions = genInclude(rcontext, ul, exc, "Exclude", langs, maps) || hasExtensions;
    }

    // now, build observed languages

    if (langs.size() > 0) {
      Collections.sort(langs);
      x.para().b().tx("Additional Language Displays");
      XhtmlNode t = x.table( "codes");
      XhtmlNode tr = t.tr();
      tr.td().b().tx("Code");
      for (String lang : langs)
        tr.td().b().addText(describeLang(lang));
      for (ConceptSetComponent c : vs.getCompose().getInclude()) {
        for (ConceptReferenceComponent cc : c.getConcept()) {
          addLanguageRow(cc, t, langs);
        }
      }
    }

    return hasExtensions;
  }

    private void addLanguageRow(ConceptReferenceComponent c, XhtmlNode t, List<String> langs) {
      XhtmlNode tr = t.tr();
      tr.td().addText(c.getCode());
      for (String lang : langs) {
        String d = null;
        for (ConceptReferenceDesignationComponent cd : c.getDesignation()) {
          String l = cd.getLanguage();
          if (lang.equals(l))
            d = cd.getValue();
        }
        tr.td().addText(d == null ? "" : d);
      }
    }

  private void AddVsRef(ResourceContext rcontext, String value, XhtmlNode li) {
    Resource res = rcontext == null ? null : rcontext.resolve(value); 
    if (res != null && !(res instanceof MetadataResource)) {
      li.addText(value);
      return;      
    }      
    MetadataResource vs = (MetadataResource) res;
    if (vs == null)
    		vs = context.fetchResource(ValueSet.class, value);
    if (vs == null)
    		vs = context.fetchResource(StructureDefinition.class, value);
//    if (vs == null)
    	//      vs = context.fetchResource(DataElement.class, value);
    if (vs == null)
    		vs = context.fetchResource(Questionnaire.class, value);
    if (vs != null) {
      String ref = (String) vs.getUserData("path");
      
      ref = adjustForPath(ref);
      XhtmlNode a = li.ah(ref == null ? "??" : ref.replace("\\", "/"));
      a.addText(value);
    } else {
    	CodeSystem cs = context.fetchCodeSystem(value);
    	if (cs != null) {
        String ref = (String) cs.getUserData("path");
        ref = adjustForPath(ref);
        XhtmlNode a = li.ah(ref == null ? "??" : ref.replace("\\", "/"));
        a.addText(value);
	    } else if (value.equals("http://snomed.info/sct") || value.equals("http://snomed.info/id")) {
	      XhtmlNode a = li.ah(value);
	      a.tx("SNOMED-CT");
	    }
	    else {
	      if (value.startsWith("http://hl7.org") && !Utilities.existsInList(value, "http://hl7.org/fhir/sid/icd-10-us"))
	        System.out.println("Unable to resolve value set "+value);
	      li.addText(value);
    }
  }
	}

  private String adjustForPath(String ref) {
    if (prefix == null)
      return ref;
    else
      return prefix+ref;
  }

  private boolean genInclude(ResourceContext rcontext, XhtmlNode ul, ConceptSetComponent inc, String type, List<String> langs, List<UsedConceptMap> maps) throws FHIRException, IOException {
    boolean hasExtensions = false;
    XhtmlNode li;
    li = ul.li();
    CodeSystem e = context.fetchCodeSystem(inc.getSystem());

    if (inc.hasSystem()) {
      if (inc.getConcept().size() == 0 && inc.getFilter().size() == 0) {
        li.addText(type+" all codes defined in ");
        addCsRef(inc, li, e);
      } else {
        if (inc.getConcept().size() > 0) {
          li.addText(type+" these codes as defined in ");
          addCsRef(inc, li, e);

          XhtmlNode t = li.table("none");
          boolean hasComments = false;
          boolean hasDefinition = false;
          for (ConceptReferenceComponent c : inc.getConcept()) {
            hasComments = hasComments || ExtensionHelper.hasExtension(c, ToolingExtensions.EXT_VS_COMMENT);
            hasDefinition = hasDefinition || ExtensionHelper.hasExtension(c, ToolingExtensions.EXT_DEFINITION);
          }
          if (hasComments || hasDefinition)
            hasExtensions = true;
          addMapHeaders(addTableHeaderRowStandard(t, false, true, hasDefinition, hasComments, false, false, null), maps);
          for (ConceptReferenceComponent c : inc.getConcept()) {
            XhtmlNode tr = t.tr();
            XhtmlNode td = tr.td();
            ConceptDefinitionComponent cc = getConceptForCode(e, c.getCode(), inc);
            addCodeToTable(false, inc.getSystem(), c.getCode(), c.hasDisplay()? c.getDisplay() : cc != null ? cc.getDisplay() : "", td);

            td = tr.td();
            if (!Utilities.noString(c.getDisplay()))
              td.addText(c.getDisplay());
            else if (cc != null && !Utilities.noString(cc.getDisplay()))
              td.addText(cc.getDisplay());

            td = tr.td();
            if (ExtensionHelper.hasExtension(c, ToolingExtensions.EXT_DEFINITION))
              smartAddText(td, ToolingExtensions.readStringExtension(c, ToolingExtensions.EXT_DEFINITION));
            else if (cc != null && !Utilities.noString(cc.getDefinition()))
              smartAddText(td, cc.getDefinition());

            if (ExtensionHelper.hasExtension(c, ToolingExtensions.EXT_VS_COMMENT)) {
              smartAddText(tr.td(), "Note: "+ToolingExtensions.readStringExtension(c, ToolingExtensions.EXT_VS_COMMENT));
            }
            for (ConceptReferenceDesignationComponent cd : c.getDesignation()) {
              if (cd.hasLanguage() && !langs.contains(cd.getLanguage()))
                langs.add(cd.getLanguage());
            }
          }
        }
        boolean first = true;
        for (ConceptSetFilterComponent f : inc.getFilter()) {
          if (first) {
            li.addText(type+" codes from ");
            first = false;
          } else
            li.tx(" and ");
          addCsRef(inc, li, e);
          li.tx(" where "+f.getProperty()+" "+describe(f.getOp())+" ");
          if (e != null && codeExistsInValueSet(e, f.getValue())) {
            String href = prefix+getCsRef(e);
            if (href.contains("#"))
              href = href + "-"+Utilities.nmtokenize(f.getValue());
            else
              href = href + "#"+e.getId()+"-"+Utilities.nmtokenize(f.getValue());
            li.ah(href).addText(f.getValue());
          } else if ("concept".equals(f.getProperty()) && inc.hasSystem()) {
            li.addText(f.getValue());
            ValidationResult vr = context.validateCode(inc.getSystem(), f.getValue(), null);
            if (vr.isOk()) {
              li.tx(" ("+vr.getDisplay()+")");
            }
          }
          else
            li.addText(f.getValue());
          String disp = ToolingExtensions.getDisplayHint(f);
          if (disp != null)
            li.tx(" ("+disp+")");
        }
      }
      if (inc.hasValueSet()) {
        li.tx(", where the codes are contained in ");
        boolean first = true;
        for (UriType vs : inc.getValueSet()) {
          if (first)
            first = false;
          else
            li.tx(", ");
          AddVsRef(rcontext, vs.asStringValue(), li);
        }
      }
    } else {
      li = ul.li();
      li.tx("Import all the codes that are contained in ");
      boolean first = true;
      for (UriType vs : inc.getValueSet()) {
        if (first)
          first = false;
        else
          li.tx(", ");
        AddVsRef(rcontext, vs.asStringValue(), li);
      }
    }
    return hasExtensions;
  }

  private String describe(FilterOperator op) {
    switch (op) {
    case EQUAL: return " = ";
    case ISA: return " is-a ";
    case ISNOTA: return " is-not-a ";
    case REGEX: return " matches (by regex) ";
		case NULL: return " ?? ";
		case IN: return " in ";
		case NOTIN: return " not in ";
    case DESCENDENTOF: return " descends from ";
    case EXISTS: return " exists ";
    case GENERALIZES: return " generalizes ";
    }
    return null;
  }

  private ConceptDefinitionComponent getConceptForCode(CodeSystem e, String code, ConceptSetComponent inc) {
    // first, look in the code systems
    if (e == null)
    e = context.fetchCodeSystem(inc.getSystem());
    if (e != null) {
      ConceptDefinitionComponent v = getConceptForCode(e.getConcept(), code);
      if (v != null)
        return v;
    }

    if (!context.hasCache()) {
      ValueSetExpansionComponent vse;
      try {
        vse = context.expandVS(inc, false);
      } catch (TerminologyServiceException e1) {
        return null;
      }
      if (vse != null) {
        ConceptDefinitionComponent v = getConceptForCodeFromExpansion(vse.getContains(), code);
      if (v != null)
        return v;
    }
    }

    return context.validateCode(inc.getSystem(), code, null).asConceptDefinition();
  }



  private ConceptDefinitionComponent getConceptForCode(List<ConceptDefinitionComponent> list, String code) {
    for (ConceptDefinitionComponent c : list) {
    if (code.equals(c.getCode()))
      return c;
      ConceptDefinitionComponent v = getConceptForCode(c.getConcept(), code);
      if (v != null)
        return v;
    }
    return null;
  }

  private ConceptDefinitionComponent getConceptForCodeFromExpansion(List<ValueSetExpansionContainsComponent> list, String code) {
    for (ValueSetExpansionContainsComponent c : list) {
      if (code.equals(c.getCode())) {
        ConceptDefinitionComponent res = new ConceptDefinitionComponent();
        res.setCode(c.getCode());
        res.setDisplay(c.getDisplay());
        return res;
      }
      ConceptDefinitionComponent v = getConceptForCodeFromExpansion(c.getContains(), code);
      if (v != null)
        return v;
    }
    return null;
  }

  private void addRefToCode(XhtmlNode td, String target, String vslink, String code) {
    CodeSystem cs = context.fetchCodeSystem(target);
    String cslink = getCsRef(cs);
    XhtmlNode a = null;
    if (cslink != null) 
      a = td.ah(prefix+cslink+"#"+cs.getId()+"-"+code);
    else
      a = td.ah(prefix+vslink+"#"+code);
    a.addText(code);
  }

  private  <T extends Resource> void addCsRef(ConceptSetComponent inc, XhtmlNode li, T cs) {
    String ref = null;
    boolean addHtml = true;
    if (cs != null) {
      ref = (String) cs.getUserData("external.url");
      if (Utilities.noString(ref))
        ref = (String) cs.getUserData("filename");
      else
        addHtml = false;
      if (Utilities.noString(ref))
        ref = (String) cs.getUserData("path");
    }
    String spec = getSpecialReference(inc.getSystem());
    if (spec != null) {
      XhtmlNode a = li.ah(spec);
      a.code(inc.getSystem());
    } else if (cs != null && ref != null) {
      if (!Utilities.noString(prefix) && ref.startsWith("http://hl7.org/fhir/"))
        ref = ref.substring(20)+"/index.html";
      else if (addHtml && !ref.contains(".html"))
        ref = ref + ".html";
      XhtmlNode a = li.ah(prefix+ref.replace("\\", "/"));
      a.code(inc.getSystem());
    } else {
      li.code(inc.getSystem());
    }
  }

  private String getSpecialReference(String system) {
    if ("http://snomed.info/sct".equals(system))
      return "http://www.snomed.org/";
    if (Utilities.existsInList(system, "http://loinc.org", "http://unitsofmeasure.org", "http://www.nlm.nih.gov/research/umls/rxnorm", "http://ncimeta.nci.nih.gov", "http://fdasis.nlm.nih.gov", 
         "http://www.radlex.org", "http://www.whocc.no/atc", "http://dicom.nema.org/resources/ontology/DCM", "http://www.genenames.org", "http://www.ensembl.org", "http://www.ncbi.nlm.nih.gov/nuccore", 
         "http://www.ncbi.nlm.nih.gov/clinvar", "http://sequenceontology.org", "http://www.hgvs.org/mutnomen", "http://www.ncbi.nlm.nih.gov/projects/SNP", "http://cancer.sanger.ac.uk/cancergenome/projects/cosmic", 
         "http://www.lrg-sequence.org", "http://www.omim.org", "http://www.ncbi.nlm.nih.gov/pubmed", "http://www.pharmgkb.org", "http://clinicaltrials.gov", "http://www.ebi.ac.uk/ipd/imgt/hla/")) 
      return system;
      
    return null;
  }

  private String getCsRef(String system) {
    CodeSystem cs = context.fetchCodeSystem(system);
    return getCsRef(cs);
  }

  private  <T extends Resource> String getCsRef(T cs) {
    String ref = (String) cs.getUserData("filename");
    if (ref == null)
      ref = (String) cs.getUserData("path");
    if (ref == null)
      return "??.html";
    if (!ref.contains(".html"))
      ref = ref + ".html";
    return ref.replace("\\", "/");
  }

  private boolean codeExistsInValueSet(CodeSystem cs, String code) {
    for (ConceptDefinitionComponent c : cs.getConcept()) {
      if (inConcept(code, c))
        return true;
    }
    return false;
  }

  private boolean inConcept(String code, ConceptDefinitionComponent c) {
    if (c.hasCodeElement() && c.getCode().equals(code))
      return true;
    for (ConceptDefinitionComponent g : c.getConcept()) {
      if (inConcept(code, g))
        return true;
    }
    return false;
  }

  /**
   * This generate is optimised for the build tool in that it tracks the source extension.
   * But it can be used for any other use.
   *
   * @param vs
   * @param codeSystems
   * @throws DefinitionException
   * @throws Exception
   */
  public boolean generate(ResourceContext rcontext, OperationOutcome op) throws DefinitionException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    boolean hasSource = false;
    boolean success = true;
    for (OperationOutcomeIssueComponent i : op.getIssue()) {
    	success = success && i.getSeverity() == IssueSeverity.INFORMATION;
    	hasSource = hasSource || ExtensionHelper.hasExtension(i, ToolingExtensions.EXT_ISSUE_SOURCE);
    }
    if (success)
    	x.para().tx("All OK");
    if (op.getIssue().size() > 0) {
    		XhtmlNode tbl = x.table("grid"); // on the basis that we'll most likely be rendered using the standard fhir css, but it doesn't really matter
    		XhtmlNode tr = tbl.tr();
    		tr.td().b().tx("Severity");
    		tr.td().b().tx("Location");
        tr.td().b().tx("Code");
        tr.td().b().tx("Details");
        tr.td().b().tx("Diagnostics");
    		if (hasSource)
    			tr.td().b().tx("Source");
    		for (OperationOutcomeIssueComponent i : op.getIssue()) {
    			tr = tbl.tr();
    			tr.td().addText(i.getSeverity().toString());
    			XhtmlNode td = tr.td();
    			boolean d = false;
    			for (StringType s : i.getLocation()) {
    				if (d)
    					td.tx(", ");
    				else
    					d = true;
    				td.addText(s.getValue());
    			}
          tr.td().addText(i.getCode().getDisplay());
          tr.td().addText(gen(i.getDetails()));
          smartAddText(tr.td(), i.getDiagnostics());
    			if (hasSource) {
    				Extension ext = ExtensionHelper.getExtension(i, ToolingExtensions.EXT_ISSUE_SOURCE);
            tr.td().addText(ext == null ? "" : gen(ext));
    			}
    		}
    	}
    inject(op, x, hasSource ? NarrativeStatus.EXTENSIONS :  NarrativeStatus.GENERATED);
    return true;
  }


  public String genType(Type type) throws DefinitionException {
    if (type instanceof Coding)
      return gen((Coding) type);
    if (type instanceof CodeableConcept)
      return displayCodeableConcept((CodeableConcept) type);
    if (type instanceof Quantity)
      return displayQuantity((Quantity) type);
    if (type instanceof Range)
      return displayRange((Range) type);
    return null;
  }
	private String gen(Extension extension) throws DefinitionException {
		if (extension.getValue() instanceof CodeType)
			return ((CodeType) extension.getValue()).getValue();
		if (extension.getValue() instanceof Coding)
			return gen((Coding) extension.getValue());

	  throw new DefinitionException("Unhandled type "+extension.getValue().getClass().getName());
  }

	public String gen(CodeableConcept code) {
		if (code == null)
	  	return null;
		if (code.hasText())
			return code.getText();
		if (code.hasCoding())
			return gen(code.getCoding().get(0));
		return null;
	}

	public String gen(Coding code) {
	  if (code == null)
	  	return null;
	  if (code.hasDisplayElement())
	  	return code.getDisplay();
	  if (code.hasCodeElement())
	  	return code.getCode();
	  return null;
  }

  public boolean generate(ResourceContext rcontext, ImplementationGuide ig) throws EOperationOutcome, FHIRException, IOException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.h2().addText(ig.getName());
    x.para().tx("The official URL for this implementation guide is: ");
    x.pre().tx(ig.getUrl());
    addMarkdown(x, ig.getDescription());
    inject(ig, x, NarrativeStatus.GENERATED);
    return true;
  }
	public boolean generate(ResourceContext rcontext, OperationDefinition opd) throws EOperationOutcome, FHIRException, IOException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.h2().addText(opd.getName());
    x.para().addText(Utilities.capitalize(opd.getKind().toString())+": "+opd.getName());
    x.para().tx("The official URL for this operation definition is: ");
    x.pre().tx(opd.getUrl());
    addMarkdown(x, opd.getDescription());

    if (opd.getSystem())
      x.para().tx("URL: [base]/$"+opd.getCode());
    for (CodeType c : opd.getResource()) {
      if (opd.getType())
        x.para().tx("URL: [base]/"+c.getValue()+"/$"+opd.getCode());
      if (opd.getInstance())
        x.para().tx("URL: [base]/"+c.getValue()+"/[id]/$"+opd.getCode());
    }

    x.para().tx("Parameters");
    XhtmlNode tbl = x.table( "grid");
    XhtmlNode tr = tbl.tr();
    tr.td().b().tx("Use");
    tr.td().b().tx("Name");
    tr.td().b().tx("Cardinality");
    tr.td().b().tx("Type");
    tr.td().b().tx("Binding");
    tr.td().b().tx("Documentation");
    for (OperationDefinitionParameterComponent p : opd.getParameter()) {
      genOpParam(rcontext, tbl, "", p);
    }
    addMarkdown(x, opd.getComment());
    inject(opd, x, NarrativeStatus.GENERATED);
    return true;
	}

	private void genOpParam(ResourceContext rcontext, XhtmlNode tbl, String path, OperationDefinitionParameterComponent p) throws EOperationOutcome, FHIRException, IOException {
		XhtmlNode tr;
      tr = tbl.tr();
      tr.td().addText(p.getUse().toString());
		tr.td().addText(path+p.getName());
      tr.td().addText(Integer.toString(p.getMin())+".."+p.getMax());
      tr.td().addText(p.hasType() ? p.getType() : "");
      XhtmlNode td = tr.td();
      if (p.hasBinding() && p.getBinding().hasValueSet()) {
        if (p.getBinding().getValueSet() instanceof CanonicalType)
          AddVsRef(rcontext, p.getBinding().getValueSetCanonicalType().getValue(), td);
        else
          td.ah(p.getBinding().getValueSetUriType().getValue()).tx("External Reference");
        td.tx(" ("+p.getBinding().getStrength().getDisplay()+")");
      }
      addMarkdown(tr.td(), p.getDocumentation());
      if (!p.hasType()) {
			for (OperationDefinitionParameterComponent pp : p.getPart()) {
				genOpParam(rcontext, tbl, path+p.getName()+".", pp);
        }
      }
    }

	private void addMarkdown(XhtmlNode x, String text) throws FHIRFormatError, IOException, DefinitionException {
	  if (text != null) {
	    // 1. custom FHIR extensions
	    while (text.contains("[[[")) {
	      String left = text.substring(0, text.indexOf("[[["));
	      String link = text.substring(text.indexOf("[[[")+3, text.indexOf("]]]"));
	      String right = text.substring(text.indexOf("]]]")+3);
	      String url = link;
	      String[] parts = link.split("\\#");
	      StructureDefinition p = context.fetchResource(StructureDefinition.class, parts[0]);
	      if (p == null)
	        p = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+parts[0]);
	      if (p == null)
	        p = context.fetchResource(StructureDefinition.class, link);
	      if (p != null) {
	        url = p.getUserString("path");
	        if (url == null)
	          url = p.getUserString("filename");
	      } else
	        throw new DefinitionException("Unable to resolve markdown link "+link);

	      text = left+"["+link+"]("+url+")"+right;
	    }

	    // 2. markdown
	    String s = markdown.process(Utilities.escapeXml(text), "narrative generator");
	    XhtmlParser p = new XhtmlParser();
	    XhtmlNode m;
		try {
			m = p.parse("<div>"+s+"</div>", "div");
		} catch (org.hl7.fhir.exceptions.FHIRFormatError e) {
			throw new FHIRFormatError(e.getMessage(), e);
		}
	    x.getChildNodes().addAll(m.getChildNodes());
	  }
  }

  public boolean generate(ResourceContext rcontext, CompartmentDefinition cpd) {
    StringBuilder in = new StringBuilder();
    StringBuilder out = new StringBuilder();
    for (CompartmentDefinitionResourceComponent cc: cpd.getResource()) {
      CommaSeparatedStringBuilder rules = new CommaSeparatedStringBuilder();
      if (!cc.hasParam()) {
        out.append(" <li><a href=\"").append(cc.getCode().toLowerCase()).append(".html\">").append(cc.getCode()).append("</a></li>\r\n");
      } else if (!rules.equals("{def}")) {
        for (StringType p : cc.getParam())
          rules.append(p.asStringValue());
        in.append(" <tr><td><a href=\"").append(cc.getCode().toLowerCase()).append(".html\">").append(cc.getCode()).append("</a></td><td>").append(rules.toString()).append("</td></tr>\r\n");
      }
    }
    XhtmlNode x;
    try {
      x = new XhtmlParser().parseFragment("<div><p>\r\nThe following resources may be in this compartment:\r\n</p>\r\n" +
          "<table class=\"grid\">\r\n"+
          " <tr><td><b>Resource</b></td><td><b>Inclusion Criteria</b></td></tr>\r\n"+
          in.toString()+
          "</table>\r\n"+
          "<p>\r\nA resource is in this compartment if the nominated search parameter (or chain) refers to the patient resource that defines the compartment.\r\n</p>\r\n" +
          "<p>\r\n\r\n</p>\r\n" +
          "<p>\r\nThe following resources are never in this compartment:\r\n</p>\r\n" +
          "<ul>\r\n"+
          out.toString()+
          "</ul></div>\r\n");
      inject(cpd, x, NarrativeStatus.GENERATED);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean generate(ResourceContext rcontext, CapabilityStatement conf) throws FHIRFormatError, DefinitionException, IOException {
    XhtmlNode x = new XhtmlNode(NodeType.Element, "div");
    x.h2().addText(conf.getName());
    addMarkdown(x, conf.getDescription());
    if (conf.getRest().size() > 0) {
      CapabilityStatementRestComponent rest = conf.getRest().get(0);
      XhtmlNode t = x.table(null);
      addTableRow(t, "Mode", rest.getMode().toString());
      addTableRow(t, "Description", rest.getDocumentation());

      addTableRow(t, "Transaction", showOp(rest, SystemRestfulInteraction.TRANSACTION));
      addTableRow(t, "System History", showOp(rest, SystemRestfulInteraction.HISTORYSYSTEM));
      addTableRow(t, "System Search", showOp(rest, SystemRestfulInteraction.SEARCHSYSTEM));

      t = x.table(null);
      XhtmlNode tr = t.tr();
      tr.th().b().tx("Resource Type");
      tr.th().b().tx("Profile");
      tr.th().b().tx("Read");
      tr.th().b().tx("V-Read");
      tr.th().b().tx("Search");
      tr.th().b().tx("Update");
      tr.th().b().tx("Updates");
      tr.th().b().tx("Create");
      tr.th().b().tx("Delete");
      tr.th().b().tx("History");

      for (CapabilityStatementRestResourceComponent r : rest.getResource()) {
        tr = t.tr();
        tr.td().addText(r.getType());
        if (r.hasProfile()) {
          tr.td().ah(prefix+r.getProfile()).addText(r.getProfile());
        }
        tr.td().addText(showOp(r, TypeRestfulInteraction.READ));
        tr.td().addText(showOp(r, TypeRestfulInteraction.VREAD));
        tr.td().addText(showOp(r, TypeRestfulInteraction.SEARCHTYPE));
        tr.td().addText(showOp(r, TypeRestfulInteraction.UPDATE));
        tr.td().addText(showOp(r, TypeRestfulInteraction.HISTORYINSTANCE));
        tr.td().addText(showOp(r, TypeRestfulInteraction.CREATE));
        tr.td().addText(showOp(r, TypeRestfulInteraction.DELETE));
        tr.td().addText(showOp(r, TypeRestfulInteraction.HISTORYTYPE));
      }
    }

    inject(conf, x, NarrativeStatus.GENERATED);
    return true;
  }

  private String showOp(CapabilityStatementRestResourceComponent r, TypeRestfulInteraction on) {
    for (ResourceInteractionComponent op : r.getInteraction()) {
      if (op.getCode() == on)
        return "y";
    }
    return "";
  }

  private String showOp(CapabilityStatementRestComponent r, SystemRestfulInteraction on) {
    for (SystemInteractionComponent op : r.getInteraction()) {
      if (op.getCode() == on)
        return "y";
    }
    return "";
  }

  private void addTableRow(XhtmlNode t, String name, String value) {
    XhtmlNode tr = t.tr();
    tr.td().addText(name);
    tr.td().addText(value);
  }

  public XhtmlNode generateDocumentNarrative(Bundle feed) {
    /*
     When the document is presented for human consumption, applications must present the collated narrative portions of the following resources in order:
     * The Composition resource
     * The Subject resource
     * Resources referenced in the section.content
     */
    XhtmlNode root = new XhtmlNode(NodeType.Element, "div");
    Composition comp = (Composition) feed.getEntry().get(0).getResource();
    root.getChildNodes().add(comp.getText().getDiv());
    Resource subject = ResourceUtilities.getById(feed, null, comp.getSubject().getReference());
    if (subject != null && subject instanceof DomainResource) {
      root.hr();
      root.getChildNodes().add(((DomainResource)subject).getText().getDiv());
    }
    List<SectionComponent> sections = comp.getSection();
    renderSections(feed, root, sections, 1);
    return root;
  }

  private void renderSections(Bundle feed, XhtmlNode node, List<SectionComponent> sections, int level) {
    for (SectionComponent section : sections) {
      node.hr();
      if (section.hasTitleElement())
        node.addTag("h"+Integer.toString(level)).addText(section.getTitle());
//      else if (section.hasCode())
//        node.addTag("h"+Integer.toString(level)).addText(displayCodeableConcept(section.getCode()));

//      if (section.hasText()) {
//        node.getChildNodes().add(section.getText().getDiv());
//      }
//
//      if (!section.getSection().isEmpty()) {
//        renderSections(feed, node.addTag("blockquote"), section.getSection(), level+1);
//      }
    }
  }


  public class ObservationNode {
    private String ref;
    private ResourceWrapper obs;
    private List<ObservationNode> contained = new ArrayList<NarrativeGenerator.ObservationNode>();
  }

  public XhtmlNode generateDiagnosticReport(ResourceWrapper dr) {
    XhtmlNode root = new XhtmlNode(NodeType.Element, "div");
    XhtmlNode h2 = root.h2();
    displayCodeableConcept(h2, getProperty(dr, "code").value());
    h2.tx(" ");
    PropertyWrapper pw = getProperty(dr, "category");
    if (valued(pw)) {
      h2.tx("(");
      displayCodeableConcept(h2, pw.value());
      h2.tx(") ");
    }
    displayDate(h2, getProperty(dr, "issued").value());

    XhtmlNode tbl = root.table( "grid");
    XhtmlNode tr = tbl.tr();
    XhtmlNode tdl = tr.td();
    XhtmlNode tdr = tr.td();
    populateSubjectSummary(tdl, getProperty(dr, "subject").value());
    tdr.b().tx("Report Details");
    tdr.br();
    pw = getProperty(dr, "perfomer");
    if (valued(pw)) {
      tdr.addText(pluralise("Performer", pw.getValues().size())+":");
      for (BaseWrapper v : pw.getValues()) {
        tdr.tx(" ");
        displayReference(tdr, v);
      }
      tdr.br();
    }
    pw = getProperty(dr, "identifier");
    if (valued(pw)) {
      tdr.addText(pluralise("Identifier", pw.getValues().size())+":");
      for (BaseWrapper v : pw.getValues()) {
        tdr.tx(" ");
        displayIdentifier(tdr, v);
      }
      tdr.br();
    }
    pw = getProperty(dr, "request");
    if (valued(pw)) {
      tdr.addText(pluralise("Request", pw.getValues().size())+":");
      for (BaseWrapper v : pw.getValues()) {
        tdr.tx(" ");
        displayReferenceId(tdr, v);
      }
      tdr.br();
    }

    pw = getProperty(dr, "result");
    if (valued(pw)) {
      List<ObservationNode> observations = fetchObservations(pw.getValues());
      buildObservationsTable(root, observations);
    }

    pw = getProperty(dr, "conclusion");
    if (valued(pw))
      displayText(root.para(), pw.value());

    pw = getProperty(dr, "result");
    if (valued(pw)) {
      XhtmlNode p = root.para();
      p.b().tx("Coded Diagnoses :");
      for (BaseWrapper v : pw.getValues()) {
        tdr.tx(" ");
        displayCodeableConcept(tdr, v);
      }
    }
    return root;
  }

  private void buildObservationsTable(XhtmlNode root, List<ObservationNode> observations) {
    XhtmlNode tbl = root.table( "none");
    for (ObservationNode o : observations) {
      addObservationToTable(tbl, o, 0);
    }
  }

  private void addObservationToTable(XhtmlNode tbl, ObservationNode o, int i) {
    XhtmlNode tr = tbl.tr();
    if (o.obs == null) {
      XhtmlNode td = tr.td().colspan("6");
      td.i().tx("This Observation could not be resolved");
    } else {
      addObservationToTable(tr, o.obs, i);
      // todo: contained observations
    }
    for (ObservationNode c : o.contained) {
      addObservationToTable(tbl, c, i+1);
    }
  }

  private void addObservationToTable(XhtmlNode tr, ResourceWrapper obs, int i) {
    // TODO Auto-generated method stub

    // code (+bodysite)
    XhtmlNode td = tr.td();
    PropertyWrapper pw = getProperty(obs, "result");
    if (valued(pw)) {
      displayCodeableConcept(td, pw.value());
    }
    pw = getProperty(obs, "bodySite");
    if (valued(pw)) {
      td.tx(" (");
      displayCodeableConcept(td, pw.value());
      td.tx(")");
    }

    // value / dataAbsentReason (in red)
    td = tr.td();
    pw = getProperty(obs, "value[x]");
    if (valued(pw)) {
      if (pw.getTypeCode().equals("CodeableConcept"))
        displayCodeableConcept(td, pw.value());
      else if (pw.getTypeCode().equals("string"))
        displayText(td, pw.value());
      else
        td.addText(pw.getTypeCode()+" not rendered yet");
    }

    // units
    td = tr.td();
    td.tx("to do");

    // reference range
    td = tr.td();
    td.tx("to do");

    // flags (status other than F, interpretation, )
    td = tr.td();
    td.tx("to do");

    // issued if different to DR
    td = tr.td();
    td.tx("to do");
  }

  private boolean valued(PropertyWrapper pw) {
    return pw != null && pw.hasValues();
  }

  private void displayText(XhtmlNode c, BaseWrapper v) {
    c.addText(v.toString());
  }

  private String pluralise(String name, int size) {
    return size == 1 ? name : name+"s";
  }

  private void displayIdentifier(XhtmlNode c, BaseWrapper v) {
    String hint = "";
    PropertyWrapper pw = v.getChildByName("type");
    if (valued(pw)) {
      hint = genCC(pw.value());
    } else {
      pw = v.getChildByName("system");
      if (valued(pw)) {
        hint = pw.value().toString();
      }
    }
    displayText(c.span(null, hint), v.getChildByName("value").value());
  }

  private String genCoding(BaseWrapper value) {
    PropertyWrapper pw = value.getChildByName("display");
    if (valued(pw))
      return pw.value().toString();
    pw = value.getChildByName("code");
    if (valued(pw))
      return pw.value().toString();
    return "";
  }

  private String genCC(BaseWrapper value) {
    PropertyWrapper pw = value.getChildByName("text");
    if (valued(pw))
      return pw.value().toString();
    pw = value.getChildByName("coding");
    if (valued(pw))
      return genCoding(pw.getValues().get(0));
    return "";
  }

  private void displayReference(XhtmlNode c, BaseWrapper v) {
    c.tx("to do");
  }


  private void displayDate(XhtmlNode c, BaseWrapper baseWrapper) {
    c.tx("to do");
  }

  private void displayCodeableConcept(XhtmlNode c, BaseWrapper property) {
    c.tx("to do");
  }

  private void displayReferenceId(XhtmlNode c, BaseWrapper v) {
    c.tx("to do");
  }

  private PropertyWrapper getProperty(ResourceWrapper res, String name) {
    for (PropertyWrapper t : res.children()) {
      if (t.getName().equals(name))
        return t;
    }
    return null;
  }

  private void populateSubjectSummary(XhtmlNode container, BaseWrapper subject) {
    ResourceWrapper r = fetchResource(subject);
    if (r == null)
      container.tx("Unable to get Patient Details");
    else if (r.getName().equals("Patient"))
      generatePatientSummary(container, r);
    else
      container.tx("Not done yet");
  }

  private void generatePatientSummary(XhtmlNode c, ResourceWrapper r) {
    c.tx("to do");
  }

  private ResourceWrapper fetchResource(BaseWrapper subject) {
    if (resolver == null)
      return null;
    String url = subject.getChildByName("reference").value().toString();
    ResourceWithReference rr = resolver.resolve(url);
    return rr == null ? null : rr.resource;
  }

  private List<ObservationNode> fetchObservations(List<BaseWrapper> list) {
    return new ArrayList<NarrativeGenerator.ObservationNode>();
  }

  public XhtmlNode renderBundle(Bundle b) throws FHIRException {
    if (b.getType() == BundleType.DOCUMENT) {
      if (!b.hasEntry() || !(b.getEntryFirstRep().hasResource() && b.getEntryFirstRep().getResource() instanceof Composition))
        throw new FHIRException("Invalid document - first entry is not a Composition");
      Composition dr = (Composition) b.getEntryFirstRep().getResource();
      return dr.getText().getDiv();
    } else  {
      XhtmlNode root = new XhtmlNode(NodeType.Element, "div");
      root.para().addText("Bundle "+b.getId()+" of type "+b.getType().toCode());
      int i = 0;
      for (BundleEntryComponent be : b.getEntry()) {
        i++;
        if (be.hasResource() && be.getResource().hasId())
          root.an(be.getResource().getResourceType().name().toLowerCase() + "_" + be.getResource().getId());
        root.hr();
        root.para().addText("Entry "+Integer.toString(i)+(be.hasFullUrl() ? " - Full URL = " + be.getFullUrl() : ""));
        if (be.hasRequest())
          renderRequest(root, be.getRequest());
        if (be.hasSearch())
          renderSearch(root, be.getSearch());
        if (be.hasResponse())
          renderResponse(root, be.getResponse());
        if (be.hasResource()) {
          root.para().addText("Resource "+be.getResource().fhirType()+":");
          if (be.hasResource() && be.getResource() instanceof DomainResource) {
            DomainResource dr = (DomainResource) be.getResource();
            if ( dr.getText().hasDiv())
              root.blockquote().getChildNodes().addAll(dr.getText().getDiv().getChildNodes());
          }
        }
      }
      return root;
    }
  }

  private void renderSearch(XhtmlNode root, BundleEntrySearchComponent search) {
    StringBuilder b = new StringBuilder();
    b.append("Search: ");
    if (search.hasMode())
      b.append("mode = "+search.getMode().toCode());
    if (search.hasScore()) {
      if (search.hasMode())
        b.append(",");
      b.append("score = "+search.getScore());
    }
    root.para().addText(b.toString());    
  }

  private void renderResponse(XhtmlNode root, BundleEntryResponseComponent response) {
    root.para().addText("Request:");
    StringBuilder b = new StringBuilder();
    b.append(response.getStatus()+"\r\n");
    if (response.hasLocation())
      b.append("Location: "+response.getLocation()+"\r\n");
    if (response.hasEtag())
      b.append("E-Tag: "+response.getEtag()+"\r\n");
    if (response.hasLastModified())
      b.append("LastModified: "+response.getEtag()+"\r\n");
    root.pre().addText(b.toString());    
  }

  private void renderRequest(XhtmlNode root, BundleEntryRequestComponent request) {
    root.para().addText("Response:");
    StringBuilder b = new StringBuilder();
    b.append(request.getMethod()+" "+request.getUrl()+"\r\n");
    if (request.hasIfNoneMatch())
      b.append("If-None-Match: "+request.getIfNoneMatch()+"\r\n");
    if (request.hasIfModifiedSince())
      b.append("If-Modified-Since: "+request.getIfModifiedSince()+"\r\n");
    if (request.hasIfMatch())
      b.append("If-Match: "+request.getIfMatch()+"\r\n");
    if (request.hasIfNoneExist())
      b.append("If-None-Exist: "+request.getIfNoneExist()+"\r\n");
    root.pre().addText(b.toString());    
  }

  public XhtmlNode renderBundle(org.hl7.fhir.r4.elementmodel.Element element) throws FHIRException {
    XhtmlNode root = new XhtmlNode(NodeType.Element, "div");
    for (Base b : element.listChildrenByName("entry")) {
      org.hl7.fhir.r4.elementmodel.Element r = ((org.hl7.fhir.r4.elementmodel.Element) b).getNamedChild("resource");
      if (r!=null) {
        XhtmlNode c = getHtmlForResource(r);
        if (c != null)
          root.getChildNodes().addAll(c.getChildNodes());
        root.hr();
      }
    }
    return root;
  }

  private XhtmlNode getHtmlForResource(org.hl7.fhir.r4.elementmodel.Element element) {
    org.hl7.fhir.r4.elementmodel.Element text = element.getNamedChild("text");
    if (text == null)
      return null;
    org.hl7.fhir.r4.elementmodel.Element div = text.getNamedChild("div");
    if (div == null)
      return null;
    else
      return div.getXhtml();
  }

  public String getDefinitionsTarget() {
    return definitionsTarget;
  }

  public void setDefinitionsTarget(String definitionsTarget) {
    this.definitionsTarget = definitionsTarget;
  }

  public String getCorePath() {
    return corePath;
  }

  public void setCorePath(String corePath) {
    this.corePath = corePath;
  }

  public String getDestDir() {
    return destDir;
  }

  public void setDestDir(String destDir) {
    this.destDir = destDir;
  }

  public ProfileKnowledgeProvider getPkp() {
    return pkp;
  }

  public void setPkp(ProfileKnowledgeProvider pkp) {
    this.pkp = pkp;
  }

  public boolean isPretty() {
    return pretty;
  }

  public void setPretty(boolean pretty) {
    this.pretty = pretty;
  }

  
}
