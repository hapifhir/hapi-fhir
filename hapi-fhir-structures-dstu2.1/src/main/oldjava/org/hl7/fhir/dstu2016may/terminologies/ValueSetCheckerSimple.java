package org.hl7.fhir.dstu2016may.terminologies;

import java.util.List;

import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.UriType;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu2016may.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu2016may.utils.EOperationOutcome;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext.ValidationResult;

public class ValueSetCheckerSimple implements ValueSetChecker {

  private ValueSet valueset;
  private ValueSetExpanderFactory factory;
  private IWorkerContext context;

  public ValueSetCheckerSimple(ValueSet source, ValueSetExpanderFactory factory, IWorkerContext context) {
    this.valueset = source;
    this.factory = factory;
    this.context = context;
  }

  @Override
  public boolean codeInValueSet(String system, String code) throws EOperationOutcome, Exception {

    if (valueset.hasCompose()) {
      boolean ok = false;
      for (UriType uri : valueset.getCompose().getImport()) {
        ok = ok || inImport(uri.getValue(), system, code);
      }
      for (ConceptSetComponent vsi : valueset.getCompose().getInclude()) {
        ok = ok || inComponent(vsi, system, code);
      }
      for (ConceptSetComponent vsi : valueset.getCompose().getExclude()) {
        ok = ok && !inComponent(vsi, system, code);
      }
    }
    
    return false;
  }

  private boolean inImport(String uri, String system, String code) throws EOperationOutcome, Exception {
    ValueSet vs = context.fetchResource(ValueSet.class, uri);
    if (vs == null) 
      return false ; // we can't tell
    return codeInExpansion(factory.getExpander().expand(vs), system, code);
  }

  private boolean codeInExpansion(ValueSetExpansionOutcome vso, String system, String code) throws EOperationOutcome, Exception {
    if (vso.getService() != null) {
      return vso.getService().codeInValueSet(system, code);
    } else {
      for (ValueSetExpansionContainsComponent c : vso.getValueset().getExpansion().getContains()) {
        if (code.equals(c.getCode()) && (system == null || system.equals(c.getSystem())))
          return true;
        if (codeinExpansion(c, system, code)) 
          return true;
      }
    }
    return false;
  }

  private boolean codeinExpansion(ValueSetExpansionContainsComponent cnt, String system, String code) {
    for (ValueSetExpansionContainsComponent c : cnt.getContains()) {
      if (code.equals(c.getCode()) && system.equals(c.getSystem().toString()))
        return true;
      if (codeinExpansion(c, system, code)) 
        return true;
    }
    return false;
  }


  private boolean inComponent(ConceptSetComponent vsi, String system, String code) {
    if (!vsi.getSystem().equals(system))
      return false; 
    // whether we know the system or not, we'll accept the stated codes at face value
    for (ConceptReferenceComponent cc : vsi.getConcept())
      if (cc.getCode().equals(code)) {
        return true;
      }
      
    CodeSystem def = context.fetchCodeSystem(system);
    if (def != null) {
      if (!def.getCaseSensitive()) {
        // well, ok, it's not case sensitive - we'll check that too now
        for (ConceptReferenceComponent cc : vsi.getConcept())
          if (cc.getCode().equalsIgnoreCase(code)) {
            return false;
          }
      }
      if (vsi.getConcept().isEmpty() && vsi.getFilter().isEmpty()) {
        return codeInDefine(def.getConcept(), code, def.getCaseSensitive());
      }
      for (ConceptSetFilterComponent f: vsi.getFilter())
        throw new Error("not done yet: "+f.getValue());

      return false;
    } else if (context.supportsSystem(system)) {
      ValidationResult vv = context.validateCode(system, code, null, vsi);
      return vv.isOk();
    } else
      // we don't know this system, and can't resolve it
      return false;
  }

  private boolean codeInDefine(List<ConceptDefinitionComponent> concepts, String code, boolean caseSensitive) {
    for (ConceptDefinitionComponent c : concepts) {
      if (caseSensitive && code.equals(c.getCode()))
        return true;
      if (!caseSensitive && code.equalsIgnoreCase(c.getCode()))
        return true;
      if (codeInDefine(c.getConcept(), code, caseSensitive))
        return true;
    }
    return false;
  }

}
