package org.hl7.fhir.r4.terminologies;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r4.utils.EOperationOutcome;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

public class ValueSetCheckerSimple implements ValueSetChecker {

  private ValueSet valueset;
  private IWorkerContext context;

  public ValueSetCheckerSimple(ValueSet source, IWorkerContext context) {
    this.valueset = source;
    this.context = context;
  }

  public ValidationResult validateCode(CodeableConcept code) throws FHIRException {
    // first, we validate the codings themselves
    List<String> errors = new ArrayList<String>();
    List<String> warnings = new ArrayList<String>();
    for (Coding c : code.getCoding()) {
      if (!c.hasSystem())
        warnings.add("Coding has no system");
      CodeSystem cs = context.fetchCodeSystem(c.getSystem());
      if (cs == null)
        throw new FHIRException("Unsupported system "+c.getSystem()+" - system is not specified or implicit");
      if (cs.getContent() != CodeSystemContentMode.COMPLETE)
        throw new FHIRException("Unable to resolve system "+c.getSystem()+" - system is not complete");
      ValidationResult res = validateCode(c, cs);
      if (!res.isOk())
        errors.add(res.getMessage());
      else if (res.getMessage() != null)
        warnings.add(res.getMessage());
    }
    if (valueset != null) {
      boolean ok = false;
      for (Coding c : code.getCoding()) {
        ok = ok || codeInValueSet(c.getSystem(), c.getCode());
      }
      if (!ok)
        errors.add(0, "None of the provided codes are in the value set "+valueset.getUrl());
    }
    if (errors.size() > 0)
      return new ValidationResult(IssueSeverity.ERROR, errors.toString());
    else if (warnings.size() > 0)
      return new ValidationResult(IssueSeverity.WARNING, warnings.toString());
    else 
      return new ValidationResult(IssueSeverity.INFORMATION, null);
  }

  public ValidationResult validateCode(Coding code) throws FHIRException {
    // first, we validate the concept itself
    String system = code.hasSystem() ? code.getSystem() : getValueSetSystem();
    if (system == null && !code.hasDisplay()) { // dealing with just a plain code (enum)
      system = systemForCodeInValueSet(code.getCode());
    }
    CodeSystem cs = context.fetchCodeSystem(system);
    if (cs == null)
      throw new FHIRException("Unable to resolve system "+system+" - system is not specified or implicit");
    if (cs.getContent() != CodeSystemContentMode.COMPLETE)
      throw new FHIRException("Unable to resolve system "+system+" - system is not complete");
    ValidationResult res = validateCode(code, cs);
      
    // then, if we have a value set, we check it's in the value set
    if (res.isOk() && valueset != null && !codeInValueSet(system, code.getCode()))
      res.setMessage("Not in value set "+valueset.getUrl()).setSeverity(IssueSeverity.ERROR); 
    return res;
  }


  private ValidationResult validateCode(Coding code, CodeSystem cs) {
    ConceptDefinitionComponent cc = findCodeInConcept(cs.getConcept(), code.getCode());
    if (cc == null)
      return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+cs.getUrl());
    if (code.getDisplay() == null)
      return new ValidationResult(cc);
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    if (cc.hasDisplay()) {
      b.append(cc.getDisplay());
      if (code.getDisplay().equalsIgnoreCase(cc.getDisplay()))
        return new ValidationResult(cc);
    }
    for (ConceptDefinitionDesignationComponent ds : cc.getDesignation()) {
      b.append(ds.getValue());
      if (code.getDisplay().equalsIgnoreCase(ds.getValue()))
        return new ValidationResult(cc);
    }
    return new ValidationResult(IssueSeverity.WARNING, "Display Name for "+code+" must be one of '"+b.toString()+"'", cc);
  }

  private String getValueSetSystem() throws FHIRException {
    if (valueset == null)
      throw new FHIRException("Unable to resolve system - no value set");
    if (valueset.getCompose().hasExclude())
      throw new FHIRException("Unable to resolve system - value set has excludes");
    if (valueset.getCompose().getInclude().size() == 0)
      throw new FHIRException("Unable to resolve system - value set has no includes");
    for (ConceptSetComponent inc : valueset.getCompose().getInclude()) {
      if (inc.hasValueSet())
        throw new FHIRException("Unable to resolve system - value set has imports");
      if (!inc.hasSystem())
        throw new FHIRException("Unable to resolve system - value set has include with no system");
    }
    if (valueset.getCompose().getInclude().size() == 1)
      return valueset.getCompose().getInclude().get(0).getSystem();
    
    return null;
  }

  private ConceptDefinitionComponent findCodeInConcept(List<ConceptDefinitionComponent> concept, String code) {
    for (ConceptDefinitionComponent cc : concept) {
      if (code.equals(cc.getCode()))
        return cc;
      ConceptDefinitionComponent c = findCodeInConcept(cc.getConcept(), code);
      if (c != null)
        return c;
    }
    return null;
  }

  
  private String systemForCodeInValueSet(String code) {
    String sys = null;
    if (valueset.hasCompose()) {
      if (valueset.getCompose().hasExclude())
        return null;
      for (ConceptSetComponent vsi : valueset.getCompose().getInclude()) {
        if (vsi.hasValueSet())
          return null;
        if (!vsi.hasSystem()) 
          return null;
        if (vsi.hasFilter())
          return null;
        CodeSystem cs = context.fetchCodeSystem(vsi.getSystem());
        if (cs == null)
          return null;
        if (vsi.hasConcept()) {
          for (ConceptReferenceComponent cc : vsi.getConcept()) {
            boolean match = cs.getCaseSensitive() ? cc.getCode().equals(code) : cc.getCode().equalsIgnoreCase(code);
            if (match) {
              if (sys == null)
                sys = vsi.getSystem();
              else if (!sys.equals(vsi.getSystem()))
                return null;
            }
          }
        } else {
          ConceptDefinitionComponent cc = findCodeInConcept(cs.getConcept(), code);
          if (cc != null) {
            if (sys == null)
              sys = vsi.getSystem();
            else if (!sys.equals(vsi.getSystem()))
              return null;
          }
        }
      }
    }
    
    return sys;  
  }
  
  @Override
  public boolean codeInValueSet(String system, String code) throws FHIRException {
    if (valueset.hasCompose()) {
      boolean ok = false;
      for (ConceptSetComponent vsi : valueset.getCompose().getInclude()) {
        ok = ok || inComponent(vsi, system, code, valueset.getCompose().getInclude().size() == 1);
      }
      for (ConceptSetComponent vsi : valueset.getCompose().getExclude()) {
        ok = ok && !inComponent(vsi, system, code, valueset.getCompose().getInclude().size() == 1);
      }
      return ok;
    }
    
    return false;
  }

  private boolean inComponent(ConceptSetComponent vsi, String system, String code, boolean only) throws FHIRException {
    for (UriType uri : vsi.getValueSet()) {
      if (inImport(uri.getValue(), system, code))
        return true;
    }

    if (!vsi.hasSystem())
      return false;
    
    if (only && system == null) {
      // whether we know the system or not, we'll accept the stated codes at face value
      for (ConceptReferenceComponent cc : vsi.getConcept())
        if (cc.getCode().equals(code)) 
          return true;
    }
    
    if (!system.equals(vsi.getSystem()))
      return false;
    if (vsi.hasFilter())
      throw new FHIRException("Filters - not done yet");
    
    CodeSystem def = context.fetchCodeSystem(system);
    if (def.getContent() != CodeSystemContentMode.COMPLETE) 
      throw new FHIRException("Unable to resolve system "+vsi.getSystem()+" - system is not complete");
    
    List<ConceptDefinitionComponent> list = def.getConcept();
    return validateCodeInConceptList(code, def, list);
  }

  public boolean validateCodeInConceptList(String code, CodeSystem def, List<ConceptDefinitionComponent> list) {
    if (def.getCaseSensitive()) {
      for (ConceptDefinitionComponent cc : list) {
        if (cc.getCode().equals(code)) 
          return true;
        if (cc.hasConcept() && validateCodeInConceptList(code, def, cc.getConcept()))
          return true;
      }
    } else {
      for (ConceptDefinitionComponent cc : list) {
        if (cc.getCode().equalsIgnoreCase(code)) 
          return true;
        if (cc.hasConcept() && validateCodeInConceptList(code, def, cc.getConcept()))
          return true;
      }
    }
    return false;
  }
  
  private boolean inImport(String uri, String system, String code) throws FHIRException {
    ValueSet vs = context.fetchResource(ValueSet.class, uri);
    ValueSetCheckerSimple vsc = new ValueSetCheckerSimple(vs, context);
    return vsc.codeInValueSet(system, code);
  }

}
