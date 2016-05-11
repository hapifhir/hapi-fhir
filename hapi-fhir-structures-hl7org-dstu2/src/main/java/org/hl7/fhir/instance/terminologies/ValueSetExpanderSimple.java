package org.hl7.fhir.instance.terminologies;

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

import org.hl7.fhir.instance.model.DateTimeType;
import org.hl7.fhir.instance.model.Factory;
import org.hl7.fhir.instance.model.PrimitiveType;
import org.hl7.fhir.instance.model.Type;
import org.hl7.fhir.instance.model.UriType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.instance.model.ValueSet.FilterOperator;
import org.hl7.fhir.instance.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionParameterComponent;
import org.hl7.fhir.instance.utilities.Utilities;
import org.hl7.fhir.instance.utils.IWorkerContext;
import org.hl7.fhir.instance.utils.ToolingExtensions;

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

      handleDefine(source, focus.getExpansion().getParameter());
      if (source.hasCompose()) 
        handleCompose(source.getCompose(), focus.getExpansion().getParameter());

      for (ValueSetExpansionContainsComponent c : codes) {
        if (map.containsKey(key(c))) {
          focus.getExpansion().getContains().add(c);
        }
      }
      return new ValueSetExpansionOutcome(focus, null);
    } catch (Exception e) {
      // well, we couldn't expand, so we'll return an interface to a checker that can check membership of the set
      // that might fail too, but it might not, later.
      return new ValueSetExpansionOutcome(new ValueSetCheckerSimple(source, factory, context), e.getMessage());
    }
  }

	private void handleCompose(ValueSetComposeComponent compose, List<ValueSetExpansionParameterComponent> params) throws Exception {
  	for (UriType imp : compose.getImport()) 
  		importValueSet(imp.getValue(), params);
  	for (ConceptSetComponent inc : compose.getInclude()) 
  		includeCodes(inc, params);
  	for (ConceptSetComponent inc : compose.getExclude()) 
  		excludeCodes(inc, params);

  }

	private void importValueSet(String value, List<ValueSetExpansionParameterComponent> params) throws Exception {
	  if (value == null)
	  	throw new Exception("unable to find value set with no identity");
	  ValueSet vs = context.fetchResource(ValueSet.class, value);
	  if (vs == null)
			throw new Exception("Unable to find imported value set "+value);
	  ValueSetExpansionOutcome vso = factory.getExpander().expand(vs);
	  if (vso.getService() != null)
      throw new Exception("Unable to expand imported value set "+value);
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

  private void includeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params) throws Exception {
	  if (context.supportsSystem(inc.getSystem())) {
        addCodes(context.expandVS(inc), params);
      return;
	  }
	    
	  ValueSet cs = context.fetchCodeSystem(inc.getSystem());
	  if (cs == null)
	  	throw new Exception("unable to find code system "+inc.getSystem().toString());
	  if (cs.hasVersion())
      if (!existsInParams(params, "version", new UriType(cs.getUrl()+"?version="+cs.getVersion())))
        params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(cs.getUrl()+"?version="+cs.getVersion())));
	  if (inc.getConcept().size() == 0 && inc.getFilter().size() == 0) {
	    // special case - add all the code system
	    for (ConceptDefinitionComponent def : cs.getCodeSystem().getConcept()) {
        addCodeAndDescendents(inc.getSystem(), def);
	    }
	  }
	    
	  for (ConceptReferenceComponent c : inc.getConcept()) {
	  	addCode(inc.getSystem(), c.getCode(), Utilities.noString(c.getDisplay()) ? getCodeDisplay(cs, c.getCode()) : c.getDisplay());
	  }
	  if (inc.getFilter().size() > 1)
	    throw new Exception("Multiple filters not handled yet"); // need to and them, and this isn't done yet. But this shouldn't arise in non loinc and snomed value sets
    if (inc.getFilter().size() == 1) {
	    ConceptSetFilterComponent fc = inc.getFilter().get(0);
	  	if ("concept".equals(fc.getProperty()) && fc.getOp() == FilterOperator.ISA) {
	  		// special: all non-abstract codes in the target code system under the value
	  		ConceptDefinitionComponent def = getConceptForCode(cs.getCodeSystem().getConcept(), fc.getValue());
	  		if (def == null)
	  			throw new Exception("Code '"+fc.getValue()+"' not found in system '"+inc.getSystem()+"'");
	  		addCodeAndDescendents(inc.getSystem(), def);
	  	} else
	  		throw new Exception("not done yet");
	  }
  }

	private void addCodes(ValueSetExpansionComponent expand, List<ValueSetExpansionParameterComponent> params) throws Exception {
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

	private void addCodeAndDescendents(String system, ConceptDefinitionComponent def) {
		if (!ToolingExtensions.hasDeprecated(def)) {  
			if (!def.hasAbstractElement() || !def.getAbstract())
				addCode(system, def.getCode(), def.getDisplay());
			for (ConceptDefinitionComponent c : def.getConcept()) 
				addCodeAndDescendents(system, c);
		}
  }

	private void excludeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params) throws Exception {
	  ValueSet cs = context.fetchCodeSystem(inc.getSystem().toString());
	  if (cs == null)
	  	throw new Exception("unable to find value set "+inc.getSystem().toString());
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
	  	throw new Exception("not done yet");
  }

	
	private String getCodeDisplay(ValueSet cs, String code) throws Exception {
		ConceptDefinitionComponent def = getConceptForCode(cs.getCodeSystem().getConcept(), code);
		if (def == null)
			throw new Exception("Unable to find code '"+code+"' in code system "+cs.getCodeSystem().getSystem());
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
	
	private void handleDefine(ValueSet vs, List<ValueSetExpansionParameterComponent> list) {
	  if (vs.hasVersion())
	    list.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(vs.getUrl()+"?version="+vs.getVersion())));
	  if (vs.hasCodeSystem()) {
      // simple case: just generate the return
    	for (ConceptDefinitionComponent c : vs.getCodeSystem().getConcept()) 
    		addDefinedCode(vs, vs.getCodeSystem().getSystem(), c);
   	}
  }

	private String key(ValueSetExpansionContainsComponent c) {
		return key(c.getSystem(), c.getCode());
	}

	private String key(String uri, String code) {
		return "{"+uri+"}"+code;
	}

	private void addDefinedCode(ValueSet vs, String system, ConceptDefinitionComponent c) {
		if (!ToolingExtensions.hasDeprecated(c)) { 

			if (!c.hasAbstractElement() || !c.getAbstract()) {
				addCode(system, c.getCode(), c.getDisplay());
			}
			for (ConceptDefinitionComponent g : c.getConcept()) 
				addDefinedCode(vs, vs.getCodeSystem().getSystem(), g);
		}
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
