package org.hl7.fhir.dstu2016may.terminologies;

import java.io.FileNotFoundException;
import java.io.IOException;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.DateTimeType;
import org.hl7.fhir.dstu2016may.model.Factory;
import org.hl7.fhir.dstu2016may.model.PrimitiveType;
import org.hl7.fhir.dstu2016may.model.Type;
import org.hl7.fhir.dstu2016may.model.UriType;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.FilterOperator;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionParameterComponent;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.Utilities;

public class ValueSetExpanderSimple implements ValueSetExpander {

  private IWorkerContext context;
  private List<ValueSetExpansionContainsComponent> codes = new ArrayList<ValueSet.ValueSetExpansionContainsComponent>();
  private Map<String, ValueSetExpansionContainsComponent> map = new HashMap<String, ValueSet.ValueSetExpansionContainsComponent>();
  private ValueSet focus;

	private ValueSetExpanderFactory factory;
  
  public ValueSetExpanderSimple(IWorkerContext context, ValueSetExpanderFactory factory) {
    super();
    this.context = context;
    this.factory = factory;
  }
  
  @Override
  public ValueSetExpansionOutcome expand(ValueSet source) {

    try {
      focus = source.copy();
      focus.setExpansion(new ValueSet.ValueSetExpansionComponent());
      focus.getExpansion().setTimestampElement(DateTimeType.now());
      focus.getExpansion().setIdentifier(Factory.createUUID());

      if (source.hasCompose()) 
        handleCompose(source.getCompose(), focus.getExpansion().getParameter());

      for (ValueSetExpansionContainsComponent c : codes) {
        if (map.containsKey(key(c))) {
          focus.getExpansion().getContains().add(c);
        }
      }
      return new ValueSetExpansionOutcome(focus, null);
    } catch (RuntimeException e) {
   	 // TODO: we should put something more specific instead of just Exception below, since
   	 // it swallows bugs.. what would be expected to be caught there?
   	 throw e;
    } catch (Exception e) {
      // well, we couldn't expand, so we'll return an interface to a checker that can check membership of the set
      // that might fail too, but it might not, later.
      return new ValueSetExpansionOutcome(new ValueSetCheckerSimple(source, factory, context), e.getMessage());
    }
  }

	private void handleCompose(ValueSetComposeComponent compose, List<ValueSetExpansionParameterComponent> params) throws TerminologyServiceException, ETooCostly, FileNotFoundException, IOException {
  	for (UriType imp : compose.getImport()) 
  		importValueSet(imp.getValue(), params);
  	for (ConceptSetComponent inc : compose.getInclude()) 
  		includeCodes(inc, params);
  	for (ConceptSetComponent inc : compose.getExclude()) 
  		excludeCodes(inc, params);

  }

	private void importValueSet(String value, List<ValueSetExpansionParameterComponent> params) throws ETooCostly, TerminologyServiceException, FileNotFoundException, IOException {
	  if (value == null)
	  	throw new TerminologyServiceException("unable to find value set with no identity");
	  ValueSet vs = context.fetchResource(ValueSet.class, value);
	  if (vs == null)
			throw new TerminologyServiceException("Unable to find imported value set "+value);
	  ValueSetExpansionOutcome vso = factory.getExpander().expand(vs);
	  if (vso.getService() != null)
      throw new TerminologyServiceException("Unable to expand imported value set "+value);
    if (vs.hasVersion())
      if (!existsInParams(params, "version", new UriType(vs.getUrl()+"?version="+vs.getVersion())))
        params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(vs.getUrl()+"?version="+vs.getVersion())));
    for (ValueSetExpansionParameterComponent p : vso.getValueset().getExpansion().getParameter()) {
      if (!existsInParams(params, p.getName(), p.getValue()))
          params.add(p);
    }
    
	  for (ValueSetExpansionContainsComponent c : vso.getValueset().getExpansion().getContains()) {
	  	addCode(c.getSystem(), c.getCode(), c.getDisplay());
	  }	  
  }

	private boolean existsInParams(List<ValueSetExpansionParameterComponent> params, String name, Type value) {
    for (ValueSetExpansionParameterComponent p : params) {
      if (p.getName().equals(name) && PrimitiveType.compareDeep(p.getValue(), value, false))
        return true;
    }
    return false;
  }

  private void includeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params) throws TerminologyServiceException, ETooCostly {
	  if (context.supportsSystem(inc.getSystem())) {
      try {
        int i = codes.size();
        addCodes(context.expandVS(inc), params);
        if (codes.size() > i)
      return;
      } catch (Exception e) {
        // ok, we'll try locally
      }
	  }
	    
	  CodeSystem cs = context.fetchCodeSystem(inc.getSystem());
	  if (cs == null)
	  	throw new TerminologyServiceException("unable to find code system "+inc.getSystem().toString());
	  if (cs.hasVersion())
      if (!existsInParams(params, "version", new UriType(cs.getUrl()+"?version="+cs.getVersion())))
        params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(cs.getUrl()+"?version="+cs.getVersion())));
	  if (inc.getConcept().size() == 0 && inc.getFilter().size() == 0) {
	    // special case - add all the code system
	    for (ConceptDefinitionComponent def : cs.getConcept()) {
        addCodeAndDescendents(cs, inc.getSystem(), def);
	    }
	  }
	    
	  for (ConceptReferenceComponent c : inc.getConcept()) {
	  	addCode(inc.getSystem(), c.getCode(), Utilities.noString(c.getDisplay()) ? getCodeDisplay(cs, c.getCode()) : c.getDisplay());
	  }
	  if (inc.getFilter().size() > 1)
	    throw new TerminologyServiceException("Multiple filters not handled yet"); // need to and them, and this isn't done yet. But this shouldn't arise in non loinc and snomed value sets
    if (inc.getFilter().size() == 1) {
	    ConceptSetFilterComponent fc = inc.getFilter().get(0);
	  	if ("concept".equals(fc.getProperty()) && fc.getOp() == FilterOperator.ISA) {
	  		// special: all non-abstract codes in the target code system under the value
	  		ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), fc.getValue());
	  		if (def == null)
	  			throw new TerminologyServiceException("Code '"+fc.getValue()+"' not found in system '"+inc.getSystem()+"'");
	  		addCodeAndDescendents(cs, inc.getSystem(), def);
	  	} else
	  		throw new NotImplementedException("not done yet");
	  }
  }

	private void addCodes(ValueSetExpansionComponent expand, List<ValueSetExpansionParameterComponent> params) throws ETooCostly {
	  if (expand.getContains().size() > 500) 
	    throw new ETooCostly("Too many codes to display (>"+Integer.toString(expand.getContains().size())+")");
    for (ValueSetExpansionParameterComponent p : expand.getParameter()) {
      if (!existsInParams(params, p.getName(), p.getValue()))
          params.add(p);
    }
	  
    for (ValueSetExpansionContainsComponent c : expand.getContains()) {
      addCode(c.getSystem(), c.getCode(), c.getDisplay());
    }   
  }

	private void addCodeAndDescendents(CodeSystem cs, String system, ConceptDefinitionComponent def) {
		if (!CodeSystemUtilities.isDeprecated(cs, def)) {  
			if (!CodeSystemUtilities.isAbstract(cs, def))
				addCode(system, def.getCode(), def.getDisplay());
			for (ConceptDefinitionComponent c : def.getConcept()) 
				addCodeAndDescendents(cs, system, c);
		}
  }

	private void excludeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params) throws TerminologyServiceException {
	  CodeSystem cs = context.fetchCodeSystem(inc.getSystem().toString());
	  if (cs == null)
	  	throw new TerminologyServiceException("unable to find value set "+inc.getSystem().toString());
    if (inc.getConcept().size() == 0 && inc.getFilter().size() == 0) {
      // special case - add all the code system
//      for (ConceptDefinitionComponent def : cs.getDefine().getConcept()) {
//!!!!        addCodeAndDescendents(inc.getSystem(), def);
//      }
    }
      

	  for (ConceptReferenceComponent c : inc.getConcept()) {
	  	// we don't need to check whether the codes are valid here- they can't have gotten into this list if they aren't valid
	  	map.remove(key(inc.getSystem(), c.getCode()));
	  }
	  if (inc.getFilter().size() > 0)
	  	throw new NotImplementedException("not done yet");
  }

	
	private String getCodeDisplay(CodeSystem cs, String code) throws TerminologyServiceException {
		ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), code);
		if (def == null)
			throw new TerminologyServiceException("Unable to find code '"+code+"' in code system "+cs.getUrl());
		return def.getDisplay();
  }

	private ConceptDefinitionComponent getConceptForCode(List<ConceptDefinitionComponent> clist, String code) {
		for (ConceptDefinitionComponent c : clist) {
			if (code.equals(c.getCode()))
			  return c;
			ConceptDefinitionComponent v = getConceptForCode(c.getConcept(), code);   
			if (v != null)
			  return v;
		}
		return null;
  }
	
	private String key(ValueSetExpansionContainsComponent c) {
		return key(c.getSystem(), c.getCode());
	}

	private String key(String uri, String code) {
		return "{"+uri+"}"+code;
	}

	private void addCode(String system, String code, String display) {
		ValueSetExpansionContainsComponent n = new ValueSet.ValueSetExpansionContainsComponent();
		n.setSystem(system);
	  n.setCode(code);
	  n.setDisplay(display);
	  String s = key(n);
	  if (!map.containsKey(s)) { 
	  	codes.add(n);
	  	map.put(s, n);
	  }
  }

  
}
