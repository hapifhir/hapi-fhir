package org.hl7.fhir.dstu3.hapi.ctx;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultProfileValidationSupport implements IValidationSupport {

  private static final String URL_PREFIX_VALUE_SET = "http://hl7.org/fhir/ValueSet/";
  private static final String URL_PREFIX_STRUCTURE_DEFINITION = "http://hl7.org/fhir/StructureDefinition/";
  private static final String URL_PREFIX_STRUCTURE_DEFINITION_BASE = "http://hl7.org/fhir/";

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DefaultProfileValidationSupport.class);

  private Map<String, CodeSystem> myCodeSystems;
  private Map<String, StructureDefinition> myStructureDefinitions;
  private Map<String, ValueSet> myValueSets;

  @Override
  public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
    ValueSetExpansionComponent retVal = new ValueSetExpansionComponent();

    Set<String> wantCodes = new HashSet<>();
    for (ConceptReferenceComponent next : theInclude.getConcept()) {
      wantCodes.add(next.getCode());
    }

    CodeSystem system = fetchCodeSystem(theContext, theInclude.getSystem());
    if (system != null) {
      List<ConceptDefinitionComponent> concepts = system.getConcept();
      addConcepts(theInclude, retVal, wantCodes, concepts);
    }

    for (UriType next : theInclude.getValueSet()) {
      ValueSet vs = myValueSets.get(defaultString(next.getValueAsString()));
      if (vs != null) {
        for (ConceptSetComponent nextInclude : vs.getCompose().getInclude()) {
          ValueSetExpansionComponent contents = expandValueSet(theContext, nextInclude);
          retVal.getContains().addAll(contents.getContains());
        }
      }
    }

    return retVal;
  }

  private void addConcepts(ConceptSetComponent theInclude, ValueSetExpansionComponent theRetVal, Set<String> theWantCodes, List<ConceptDefinitionComponent> theConcepts) {
    for (ConceptDefinitionComponent next : theConcepts) {
      if (theWantCodes.isEmpty() || theWantCodes.contains(next.getCode())) {
        theRetVal
          .addContains()
          .setSystem(theInclude.getSystem())
          .setCode(next.getCode())
          .setDisplay(next.getDisplay());
      }
      addConcepts(theInclude, theRetVal, theWantCodes, next.getConcept());
    }
  }

  @Override
  public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
    ArrayList<IBaseResource> retVal = new ArrayList<>();
    retVal.addAll(myCodeSystems.values());
    retVal.addAll(myStructureDefinitions.values());
    retVal.addAll(myValueSets.values());
    return retVal;
  }

  @Override
  public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
    return new ArrayList<StructureDefinition>(provideStructureDefinitionMap(theContext).values());
  }

  @Override
  public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
    return (CodeSystem) fetchCodeSystemOrValueSet(theContext, theSystem, true);
  }

  private DomainResource fetchCodeSystemOrValueSet(FhirContext theContext, String theSystem, boolean codeSystem) {
    synchronized (this) {
      Map<String, CodeSystem> codeSystems = myCodeSystems;
      Map<String, ValueSet> valueSets = myValueSets;
      if (codeSystems == null || valueSets == null) {
        codeSystems = new HashMap<String, CodeSystem>();
        valueSets = new HashMap<String, ValueSet>();

        loadCodeSystems(theContext, codeSystems, valueSets, "/org/hl7/fhir/dstu3/model/valueset/valuesets.xml");
        loadCodeSystems(theContext, codeSystems, valueSets, "/org/hl7/fhir/dstu3/model/valueset/v2-tables.xml");
        loadCodeSystems(theContext, codeSystems, valueSets, "/org/hl7/fhir/dstu3/model/valueset/v3-codesystems.xml");

        myCodeSystems = codeSystems;
        myValueSets = valueSets;
      }

      if (codeSystem) {
        return codeSystems.get(theSystem);
      } else {
        return valueSets.get(theSystem);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
    Validate.notBlank(theUri, "theUri must not be null or blank");

    if (theClass.equals(StructureDefinition.class)) {
      return (T) fetchStructureDefinition(theContext, theUri);
    }

    if (theClass.equals(ValueSet.class) || theUri.startsWith(URL_PREFIX_VALUE_SET)) {
      return (T) fetchValueSet(theContext, theUri);
    }

    return null;
  }

  @Override
  public StructureDefinition fetchStructureDefinition(FhirContext theContext, String theUrl) {
    String url = theUrl;
    if (url.startsWith(URL_PREFIX_STRUCTURE_DEFINITION)) {
      // no change
    } else if (url.indexOf('/') == -1) {
      url = URL_PREFIX_STRUCTURE_DEFINITION + url;
    } else if (StringUtils.countMatches(url, '/') == 1) {
      url = URL_PREFIX_STRUCTURE_DEFINITION_BASE + url;
    }
    Map<String, StructureDefinition> map = provideStructureDefinitionMap(theContext);
    StructureDefinition retVal = map.get(url);

    if (retVal == null && url.startsWith(URL_PREFIX_STRUCTURE_DEFINITION)) {
      String tryUrl = URL_PREFIX_STRUCTURE_DEFINITION + StringUtils.capitalize(url.substring(URL_PREFIX_STRUCTURE_DEFINITION.length()));
      retVal = map.get(tryUrl);
    }

    return retVal;
  }

  @Override
  public ValueSet fetchValueSet(FhirContext theContext, String uri) {
    return (ValueSet) fetchCodeSystemOrValueSet(theContext, uri, false);
  }

  public void flush() {
    myCodeSystems = null;
    myStructureDefinitions = null;
  }

  @Override
  public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
    CodeSystem cs = fetchCodeSystem(theContext, theSystem);
    return cs != null && cs.getContent() != CodeSystemContentMode.NOTPRESENT;
  }

  private void loadCodeSystems(FhirContext theContext, Map<String, CodeSystem> theCodeSystems, Map<String, ValueSet> theValueSets, String theClasspath) {
    ourLog.info("Loading CodeSystem/ValueSet from classpath: {}", theClasspath);
    InputStream inputStream = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
    InputStreamReader reader = null;
    if (inputStream != null) {
      try {
        reader = new InputStreamReader(inputStream, Charsets.UTF_8);

        Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
        for (BundleEntryComponent next : bundle.getEntry()) {
          if (next.getResource() instanceof CodeSystem) {
            CodeSystem nextValueSet = (CodeSystem) next.getResource();
            nextValueSet.getText().setDivAsString("");
            String system = nextValueSet.getUrl();
            if (isNotBlank(system)) {
              theCodeSystems.put(system, nextValueSet);
            }
          } else if (next.getResource() instanceof ValueSet) {
            ValueSet nextValueSet = (ValueSet) next.getResource();
            nextValueSet.getText().setDivAsString("");
            String system = nextValueSet.getUrl();
            if (isNotBlank(system)) {
              theValueSets.put(system, nextValueSet);
            }
          }
        }
      } finally {
        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(inputStream);
      }
    } else {
      ourLog.warn("Unable to load resource: {}", theClasspath);
    }
  }

  private void loadStructureDefinitions(FhirContext theContext, Map<String, StructureDefinition> theCodeSystems, String theClasspath) {
    ourLog.info("Loading structure definitions from classpath: {}", theClasspath);
    InputStream valuesetText = DefaultProfileValidationSupport.class.getResourceAsStream(theClasspath);
    if (valuesetText != null) {
      InputStreamReader reader = new InputStreamReader(valuesetText, Charsets.UTF_8);

      Bundle bundle = theContext.newXmlParser().parseResource(Bundle.class, reader);
      for (BundleEntryComponent next : bundle.getEntry()) {
        if (next.getResource() instanceof StructureDefinition) {
          StructureDefinition nextSd = (StructureDefinition) next.getResource();
          nextSd.getText().setDivAsString("");
          String system = nextSd.getUrl();
          if (isNotBlank(system)) {
            theCodeSystems.put(system, nextSd);
          }
        }
      }
    } else {
      ourLog.warn("Unable to load resource: {}", theClasspath);
    }
  }

  private Map<String, StructureDefinition> provideStructureDefinitionMap(FhirContext theContext) {
    Map<String, StructureDefinition> structureDefinitions = myStructureDefinitions;
    if (structureDefinitions == null) {
      structureDefinitions = new HashMap<String, StructureDefinition>();

      loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu3/model/profile/profiles-resources.xml");
      loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu3/model/profile/profiles-types.xml");
      loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu3/model/profile/profiles-others.xml");
      loadStructureDefinitions(theContext, structureDefinitions, "/org/hl7/fhir/dstu3/model/extension/extension-definitions.xml");

      myStructureDefinitions = structureDefinitions;
    }
    return structureDefinitions;
  }

  private CodeValidationResult testIfConceptIsInList(CodeSystem theCodeSystem, String theCode, List<ConceptDefinitionComponent> conceptList, boolean theCaseSensitive) {
    String code = theCode;
    if (theCaseSensitive == false) {
      code = code.toUpperCase();
    }

    return testIfConceptIsInListInner(theCodeSystem, conceptList, theCaseSensitive, code);
  }

  private CodeValidationResult testIfConceptIsInListInner(CodeSystem theCodeSystem, List<ConceptDefinitionComponent> conceptList, boolean theCaseSensitive, String code) {
    CodeValidationResult retVal = null;
    for (ConceptDefinitionComponent next : conceptList) {
      String nextCandidate = next.getCode();
      if (theCaseSensitive == false) {
        nextCandidate = nextCandidate.toUpperCase();
      }
      if (nextCandidate.equals(code)) {
        retVal = new CodeValidationResult(next);
        break;
      }

      // recurse
      retVal = testIfConceptIsInList(theCodeSystem, code, next.getConcept(), theCaseSensitive);
      if (retVal != null) {
        break;
      }
    }

    if (retVal != null) {
      retVal.setCodeSystemName(theCodeSystem.getName());
      retVal.setCodeSystemVersion(theCodeSystem.getVersion());
    }

    return retVal;
  }

  @Override
  public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
    CodeSystem cs = fetchCodeSystem(theContext, theCodeSystem);
    if (cs != null) {
      boolean caseSensitive = true;
      if (cs.hasCaseSensitive()) {
        caseSensitive = cs.getCaseSensitive();
      }

      CodeValidationResult retVal = testIfConceptIsInList(cs, theCode, cs.getConcept(), caseSensitive);

      if (retVal != null) {
        return retVal;
      }
    }

    return new CodeValidationResult(IssueSeverity.WARNING, "Unknown code: " + theCodeSystem + " / " + theCode);
  }

  @Override
  public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
    return validateCode(theContext, theSystem, theCode, null).asLookupCodeResult(theSystem, theCode);
  }

  @Override
  public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theName) {
    return null;
  }

}
