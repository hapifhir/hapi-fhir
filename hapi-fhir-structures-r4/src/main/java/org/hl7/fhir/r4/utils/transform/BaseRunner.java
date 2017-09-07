package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.Property;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.*;

public abstract class BaseRunner {

  private IWorkerContext worker;
  private StructureMap structureMap;
  private Map<String, Integer> ids = new HashMap<String, Integer>();
  private ITransformerServices services;
  private FHIRPathEngine fhirPathEngine;
  private Map<String, StructureMap> library;

  public IWorkerContext getWorker() {
    return worker;
  }

  public void setWorker(IWorkerContext worker) {
    this.worker = worker;
  }

  public StructureMap getStructureMap() {
    return structureMap;
  }

  public void setStructureMap(StructureMap structureMap) {
    this.structureMap = structureMap;
  }

  public Map<String, Integer> getIds() {
    return ids;
  }

  public void setIds(Map<String, Integer> ids) {
    this.ids = ids;
  }

  public ITransformerServices getServices() {
    return services;
  }

  public void setServices(ITransformerServices services) {
    this.services = services;
  }

  public FHIRPathEngine getFhirPathEngine() {
    return fhirPathEngine;
  }

  public void setFhirPathEngine(FHIRPathEngine fhirPathEngine) {
    this.fhirPathEngine = fhirPathEngine;
  }

  public Map<String, StructureMap> getLibrary() {
    return library;
  }

  public void setLibrary(Map<String, StructureMap> library) {
    this.library = library;
  }


  public void initializeLibrary() {
    library = new HashMap<String, StructureMap>();
  }

  protected void log(String cnt) {
    if (getServices() != null)
      getServices().log(cnt);
  }

  protected void noteInput(VariablesForProfiling vars, StructureMap.StructureMapGroupInputComponent inp, VariableMode mode, XhtmlNode xs) {
    VariableForProfiling v = vars.get(mode, inp.getName());
    if (v != null)
      xs.addText("Input: " + v.getProperty().getPath());
  }

  protected PropertyWithType createProfile(StructureMap map, List<StructureDefinition> profiles, PropertyWithType prop, String sliceName, Base ctxt) throws DefinitionException {
    if (prop.getBaseProperty().getDefinition().getPath().contains("."))
      throw new DefinitionException("Unable to process entry point");

    String type = prop.getBaseProperty().getDefinition().getPath();
    String suffix = "";
    if (ids.containsKey(type)) {
      int id = ids.get(type);
      id++;
      ids.put(type, id);
      suffix = "-" + Integer.toString(id);
    } else
      ids.put(type, 0);

    StructureDefinition profile = new StructureDefinition();
    profiles.add(profile);
    profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
    profile.setType(type);
    profile.setBaseDefinition(prop.getBaseProperty().getStructure().getUrl());
    profile.setName("Profile for " + profile.getType() + " for " + sliceName);
    profile.setUrl(map.getUrl().replace("StructureMap", "StructureDefinition") + "-" + profile.getType() + suffix);
    ctxt.setUserData("profile", profile.getUrl()); // then we can easily assign this profile url for validation later when we actually transform
    profile.setId(map.getId() + "-" + profile.getType() + suffix);
    profile.setStatus(map.getStatus());
    profile.setExperimental(map.getExperimental());
    profile.setDescription("Generated automatically from the mapping by the Java Reference Implementation");
    for (ContactDetail c : map.getContact()) {
      ContactDetail p = profile.addContact();
      p.setName(c.getName());
      for (ContactPoint cc : c.getTelecom())
        p.addTelecom(cc);
    }
    profile.setDate(map.getDate());
    profile.setCopyright(map.getCopyright());
    profile.setFhirVersion(Constants.VERSION);
    profile.setKind(prop.getBaseProperty().getStructure().getKind());
    profile.setAbstract(false);
    ElementDefinition ed = profile.getDifferential().addElement();
    ed.setPath(profile.getType());
    prop.setProfileProperty(new Property(worker, ed, profile));
    return prop;
  }

  protected Coding buildCoding(String uri, String code) throws FHIRException {
    // if we can get this as a valueSet, we will
    String system = null;
    String display = null;
    ValueSet vs = Utilities.noString(uri) ? null : getWorker().fetchResourceWithException(ValueSet.class, uri);
    if (vs != null) {
      ValueSetExpander.ValueSetExpansionOutcome vse = getWorker().expandVS(vs, true, false);
      if (vse.getError() != null)
        throw new FHIRException(vse.getError());
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (ValueSet.ValueSetExpansionContainsComponent t : vse.getValueset().getExpansion().getContains()) {
        if (t.hasCode())
          b.append(t.getCode());
        if (code.equals(t.getCode()) && t.hasSystem()) {
          system = t.getSystem();
          display = t.getDisplay();
          break;
        }
        if (code.equalsIgnoreCase(t.getDisplay()) && t.hasSystem()) {
          system = t.getSystem();
          display = t.getDisplay();
          break;
        }
      }
      if (system == null)
        throw new FHIRException("The code '" + code + "' is not in the value set '" + uri + "' (valid codes: " + b.toString() + "; also checked displays)");
    } else
      system = uri;
    IWorkerContext.ValidationResult vr = getWorker().validateCode(system, code, null);
    if (vr != null && vr.getDisplay() != null)
      display = vr.getDisplay();
    return new Coding().setSystem(system).setCode(code).setDisplay(display);
  }

  @SuppressWarnings("rawtypes")
  protected Coding buildCoding(Type value1, Type value2) {
    return new Coding().setSystem(((PrimitiveType) value1).asStringValue()).setCode(((PrimitiveType) value2).asStringValue());
  }

  protected ResolvedGroup resolveGroupReference(StructureMap map, StructureMap.StructureMapGroupComponent source, String name) throws FHIRException {
    String kn = "ref^" + name;
    if (source.hasUserData(kn))
      return (ResolvedGroup) source.getUserData(kn);

    ResolvedGroup res = new ResolvedGroup();
    res.targetMap = null;
    res.target = null;
    for (StructureMap.StructureMapGroupComponent grp : map.getGroup()) {
      if (grp.getName().equals(name)) {
        if (res.targetMap == null) {
          res.targetMap = map;
          res.target = grp;
        } else
          throw new FHIRException("Multiple possible matches for rule '" + name + "'");
      }
    }
    if (res.targetMap != null) {
      source.setUserData(kn, res);
      return res;
    }

    for (UriType imp : map.getImport()) {
      List<StructureMap> impMapList = findMatchingMaps(imp.getValue());
      if (impMapList.size() == 0)
        throw new FHIRException("Unable to find map(s) for " + imp.getValue());
      for (StructureMap impMap : impMapList) {
        if (!impMap.getUrl().equals(map.getUrl())) {
          for (StructureMap.StructureMapGroupComponent grp : impMap.getGroup()) {
            if (grp.getName().equals(name)) {
              if (res.targetMap == null) {
                res.targetMap = impMap;
                res.target = grp;
              } else
                throw new FHIRException("Multiple possible matches for rule '" + name + "' in " + res.targetMap.getUrl() + " and " + impMap.getUrl());
            }
          }
        }
      }
    }
    if (res.target == null)
      throw new FHIRException("No matches found for rule '" + name + "'. Reference found in " + map.getUrl());
    source.setUserData(kn, res);
    return res;
  }

  protected List<StructureMap> findMatchingMaps(String value) {
    List<StructureMap> res = new ArrayList<StructureMap>();
    if (value.contains("*")) {
      for (StructureMap sm : library.values()) {
        if (urlMatches(value, sm.getUrl())) {
          res.add(sm);
        }
      }
    } else {
      StructureMap sm = library.get(value);
      if (sm != null)
        res.add(sm);
    }
    Set<String> check = new HashSet<String>();
    for (StructureMap sm : res) {
      if (check.contains(sm.getUrl()))
        throw new Error("duplicate");
      else
        check.add(sm.getUrl());
    }
    return res;
  }

  public boolean urlMatches(String mask, String url) {
    return url.length() > mask.length() && url.startsWith(mask.substring(0, mask.indexOf("*"))) && url.endsWith(mask.substring(mask.indexOf("*") + 1));
  }

  protected String getParamStringNoNull(Variables vars, StructureMap.StructureMapGroupRuleTargetParameterComponent parameter, String message) throws FHIRException {
    Base b = getParam(vars, parameter);
    if (b == null)
      throw new FHIRException("Unable to find a value for " + parameter.toString() + ". Context: " + message);
    if (!b.hasPrimitiveValue())
      throw new FHIRException("Found a value for " + parameter.toString() + ", but it has a type of " + b.fhirType() + " and cannot be treated as a string. Context: " + message);
    return b.primitiveValue();
  }

  protected String getParamString(Variables vars, StructureMap.StructureMapGroupRuleTargetParameterComponent parameter) throws DefinitionException {
    Base b = getParam(vars, parameter);
    if (b == null || !b.hasPrimitiveValue())
      return null;
    return b.primitiveValue();
  }


  protected Base getParam(Variables vars, StructureMap.StructureMapGroupRuleTargetParameterComponent parameter) throws DefinitionException {
    Type p = parameter.getValue();
    if (!(p instanceof IdType))
      return p;
    else {
      String n = ((IdType) p).asStringValue();
      Base b = vars.get(VariableMode.INPUT, n);
      if (b == null)
        b = vars.get(VariableMode.OUTPUT, n);
      if (b == null)
        throw new DefinitionException("Variable " + n + " not found (" + vars.summary() + ")");
      return b;
    }
  }

}
