<<<<<<< HEAD
package org.hl7.fhir.dstu3.terminologies;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Factory;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent;
import org.hl7.fhir.dstu3.utils.IWorkerContext;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.Utilities;

public class ValueSetExpanderSimple implements ValueSetExpander {

	private List<ValueSetExpansionContainsComponent> codes = new ArrayList<ValueSet.ValueSetExpansionContainsComponent>();
	private IWorkerContext context;
	private Set<String> excludeKeys = new HashSet<String>();
	private ValueSetExpanderFactory factory;
	private ValueSet focus;
	private int maxExpansionSize = 500;

	private Map<String, ValueSetExpansionContainsComponent> map = new HashMap<String, ValueSet.ValueSetExpansionContainsComponent>();
	private int total;

	public ValueSetExpanderSimple(IWorkerContext context, ValueSetExpanderFactory factory) {
		super();
		this.context = context;
		this.factory = factory;
	}

	public void setMaxExpansionSize(int theMaxExpansionSize) {
		maxExpansionSize = theMaxExpansionSize;
	}

	private void addCode(String system, String code, String display) {
		ValueSetExpansionContainsComponent n = new ValueSet.ValueSetExpansionContainsComponent();
		n.setSystem(system);
		n.setCode(code);
		n.setDisplay(display);
		String s = key(n);
		if (!map.containsKey(s) && !excludeKeys.contains(s)) {
			codes.add(n);
			map.put(s, n);
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

	private void addCodes(ValueSetExpansionComponent expand, List<ValueSetExpansionParameterComponent> params) throws ETooCostly {
		if (expand.getContains().size() > maxExpansionSize)
			throw new ETooCostly("Too many codes to display (>" + Integer.toString(expand.getContains().size()) + ")");
		for (ValueSetExpansionParameterComponent p : expand.getParameter()) {
			if (!existsInParams(params, p.getName(), p.getValue()))
				params.add(p);
		}

		for (ValueSetExpansionContainsComponent c : expand.getContains()) {
			addCode(c.getSystem(), c.getCode(), c.getDisplay());
		}
		
		total = expand.getTotal();
	}

	private void excludeCode(String theSystem, String theCode) {
		ValueSetExpansionContainsComponent n = new ValueSet.ValueSetExpansionContainsComponent();
		n.setSystem(theSystem);
		n.setCode(theCode);
		String s = key(n);
		excludeKeys.add(s);
	}

	private void excludeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params) throws TerminologyServiceException {
		if (inc.getConcept().size() == 0 && inc.getFilter().size() == 0) {
			return;
		}

		CodeSystem cs = context.fetchCodeSystem(inc.getSystem());
		if ((cs == null || cs.getContent() != CodeSystemContentMode.COMPLETE) && context.supportsSystem(inc.getSystem())) {
			excludeCodes(context.expandVS(inc), params);
			return;
		}

		for (ConceptReferenceComponent c : inc.getConcept()) {
			excludeCode(inc.getSystem(), c.getCode());
		}
		
		if (inc.getFilter().size() > 0)
			throw new NotImplementedException("not done yet");
	}

	private void excludeCodes(ValueSetExpansionComponent expand, List<ValueSetExpansionParameterComponent> params) {
		for (ValueSetExpansionContainsComponent c : expand.getContains()) {
			excludeCode(c.getSystem(), c.getCode());
		}
	}

	private boolean existsInParams(List<ValueSetExpansionParameterComponent> params, String name, Type value) {
		for (ValueSetExpansionParameterComponent p : params) {
			if (p.getName().equals(name) && PrimitiveType.compareDeep(p.getValue(), value, false))
				return true;
		}
		return false;
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
			
			if (total > 0) {
				focus.getExpansion().setTotal(total);
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

	private String getCodeDisplay(CodeSystem cs, String code) throws TerminologyServiceException {
		ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), code);
		if (def == null)
			throw new TerminologyServiceException("Unable to find code '" + code + "' in code system " + cs.getUrl());
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

	private void handleCompose(ValueSetComposeComponent compose, List<ValueSetExpansionParameterComponent> params) throws TerminologyServiceException, ETooCostly, FileNotFoundException, IOException {
		// Exclude comes first because we build up a map of things to exclude
		for (ConceptSetComponent inc : compose.getExclude())
			excludeCodes(inc, params);
		for (UriType imp : compose.getImport())
			importValueSet(imp.getValue(), params);
		for (ConceptSetComponent inc : compose.getInclude())
			includeCodes(inc, params);

	}

	private void importValueSet(String value, List<ValueSetExpansionParameterComponent> params) throws ETooCostly, TerminologyServiceException, FileNotFoundException, IOException {
		if (value == null)
			throw new TerminologyServiceException("unable to find value set with no identity");
		ValueSet vs = context.fetchResource(ValueSet.class, value);
		if (vs == null)
			throw new TerminologyServiceException("Unable to find imported value set " + value);
		ValueSetExpansionOutcome vso = factory.getExpander().expand(vs);
		
		if (isNotBlank(vso.getError())) {
			throw new TerminologyServiceException("Unable to expand imported value set \"" + value + "\" - Error was: " + vso.getError());
		}
		if (vso.getService() != null) {
			throw new TerminologyServiceException("Unable to expand imported value set \"" + value + "\"");
		}
		if (vs.hasVersion())
			if (!existsInParams(params, "version", new UriType(vs.getUrl() + "?version=" + vs.getVersion())))
				params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(vs.getUrl() + "?version=" + vs.getVersion())));
		for (ValueSetExpansionParameterComponent p : vso.getValueset().getExpansion().getParameter()) {
			if (!existsInParams(params, p.getName(), p.getValue()))
				params.add(p);
		}

		for (ValueSetExpansionContainsComponent c : vso.getValueset().getExpansion().getContains()) {
			addCode(c.getSystem(), c.getCode(), c.getDisplay());
		}
	}

	private void includeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params) throws ETooCostly, org.hl7.fhir.exceptions.TerminologyServiceException {
		CodeSystem cs = context.fetchCodeSystem(inc.getSystem());
		if ((cs == null || cs.getContent() != CodeSystemContentMode.COMPLETE) && context.supportsSystem(inc.getSystem())) {
			addCodes(context.expandVS(inc), params);
			return;
		}

		if (cs == null)
			throw new TerminologyServiceException("unable to find code system " + inc.getSystem().toString());
		if (cs.getContent() != CodeSystemContentMode.COMPLETE)
			throw new TerminologyServiceException("Code system " + inc.getSystem().toString() + " is incomplete");
		if (cs.hasVersion())
			if (!existsInParams(params, "version", new UriType(cs.getUrl() + "?version=" + cs.getVersion())))
				params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(cs.getUrl() + "?version=" + cs.getVersion())));
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
					throw new TerminologyServiceException("Code '" + fc.getValue() + "' not found in system '" + inc.getSystem() + "'");
				addCodeAndDescendents(cs, inc.getSystem(), def);
			} else if ("display".equals(fc.getProperty()) && fc.getOp() == FilterOperator.EQUAL) {
				ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), fc.getValue());
				if (def != null) {
					if (isNotBlank(def.getDisplay()) && isNotBlank(fc.getValue())) {
						if (def.getDisplay().contains(fc.getValue())) {
							addCode(inc.getSystem(), def.getCode(), def.getDisplay());
						}
					}
				}
			} else
				throw new NotImplementedException("Search by property[" + fc.getProperty() + "] and op[" + fc.getOp() + "] is not supported yet");
		}
	}

	private String key(String uri, String code) {
		return "{" + uri + "}" + code;
	}

	private String key(ValueSetExpansionContainsComponent c) {
		return key(c.getSystem(), c.getCode());
	}

}
=======
package org.hl7.fhir.dstu3.terminologies;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Factory;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent;
import org.hl7.fhir.dstu3.utils.IWorkerContext;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.Utilities;

public class ValueSetExpanderSimple implements ValueSetExpander {

	private List<ValueSetExpansionContainsComponent> codes = new ArrayList<ValueSet.ValueSetExpansionContainsComponent>();
  private List<ValueSetExpansionContainsComponent> roots = new ArrayList<ValueSet.ValueSetExpansionContainsComponent>();
  private Map<String, ValueSetExpansionContainsComponent> map = new HashMap<String, ValueSet.ValueSetExpansionContainsComponent>();
	private IWorkerContext context;
  private boolean canBeHeirarchy = true;
	private Set<String> excludeKeys = new HashSet<String>();
  private Set<String> excludeSystems = new HashSet<String>();
	private ValueSetExpanderFactory factory;
	private ValueSet focus;
	private int maxExpansionSize = 500;

	private int total;

	public ValueSetExpanderSimple(IWorkerContext context, ValueSetExpanderFactory factory) {
		super();
		this.context = context;
		this.factory = factory;
	}

	public void setMaxExpansionSize(int theMaxExpansionSize) {
		maxExpansionSize = theMaxExpansionSize;
	}


  private ValueSetExpansionContainsComponent addCode(String system, String code, String display, ValueSetExpansionContainsComponent parent, List<ConceptDefinitionDesignationComponent> designations, ExpansionProfile profile, boolean isAbstract) {
		ValueSetExpansionContainsComponent n = new ValueSet.ValueSetExpansionContainsComponent();
		n.setSystem(system);
		n.setCode(code);
    if (isAbstract) 
      n.setAbstract(true);
    
    if (profile.getIncludeDesignations() && designations != null) {
      for (ConceptDefinitionDesignationComponent t : designations) {
         ToolingExtensions.addLanguageTranslation(n, t.getLanguage(), t.getValue());
      }
    }
    ConceptDefinitionDesignationComponent t = profile.hasLanguage() ? getMatchingLang(designations, profile.getLanguage()) : null;
    if (t == null)
		n.setDisplay(display);
    else
      n.setDisplay(t.getValue());
    
		String s = key(n);
		if (map.containsKey(s) || excludeKeys.contains(s)) {
		  canBeHeirarchy = false;
		} else {
			codes.add(n);
			map.put(s, n);
		}
		if (canBeHeirarchy) {
		  if (parent != null)
		    parent.getContains().add(n);
		  else
		    roots.add(n);
		}
		return n;
	}

  private ConceptDefinitionDesignationComponent getMatchingLang(List<ConceptDefinitionDesignationComponent> list, String lang) {
    for (ConceptDefinitionDesignationComponent t : list)
      if (t.getLanguage().equals(lang))
        return t;
    for (ConceptDefinitionDesignationComponent t : list)
      if (t.getLanguage().startsWith(lang))
        return t;
    return null;
  }

  private void addCodeAndDescendents(CodeSystem cs, String system, ConceptDefinitionComponent def, ValueSetExpansionContainsComponent parent, ExpansionProfile profile) {
		if (!CodeSystemUtilities.isDeprecated(cs, def)) {
		  ValueSetExpansionContainsComponent np = null;
      boolean abs = CodeSystemUtilities.isAbstract(cs, def);
      if (canBeHeirarchy || !abs)
        np = addCode(system, def.getCode(), def.getDisplay(), parent, def.getDesignation(), profile, abs);
			for (ConceptDefinitionComponent c : def.getConcept())
        addCodeAndDescendents(cs, system, c, np, profile);
		}
	}

  private void addCodes(ValueSetExpansionComponent expand, List<ValueSetExpansionParameterComponent> params, ExpansionProfile profile) throws ETooCostly {
		if (expand.getContains().size() > maxExpansionSize)
			throw new ETooCostly("Too many codes to display (>" + Integer.toString(expand.getContains().size()) + ")");
		for (ValueSetExpansionParameterComponent p : expand.getParameter()) {
			if (!existsInParams(params, p.getName(), p.getValue()))
				params.add(p);
		}

    copyImportContains(expand.getContains(), null, profile);
		
		total = expand.getTotal();
	}

	private void excludeCode(String theSystem, String theCode) {
		ValueSetExpansionContainsComponent n = new ValueSet.ValueSetExpansionContainsComponent();
		n.setSystem(theSystem);
		n.setCode(theCode);
		String s = key(n);
		excludeKeys.add(s);
	}

	private void excludeCodes(ConceptSetComponent exc, List<ValueSetExpansionParameterComponent> params) throws TerminologyServiceException {
		if (exc.getConcept().size() == 0 && exc.getFilter().size() == 0) {
			excludeSystems.add(exc.getSystem());
		}

		CodeSystem cs = context.fetchCodeSystem(exc.getSystem());
		if ((cs == null || cs.getContent() != CodeSystemContentMode.COMPLETE) && context.supportsSystem(exc.getSystem())) {
			excludeCodes(context.expandVS(exc, false), params);
			return;
		}

		for (ConceptReferenceComponent c : exc.getConcept()) {
			excludeCode(exc.getSystem(), c.getCode());
		}
		
		if (exc.getFilter().size() > 0)
			throw new NotImplementedException("not done yet");
	}

	private void excludeCodes(ValueSetExpansionComponent expand, List<ValueSetExpansionParameterComponent> params) {
		for (ValueSetExpansionContainsComponent c : expand.getContains()) {
			excludeCode(c.getSystem(), c.getCode());
		}
	}

	private boolean existsInParams(List<ValueSetExpansionParameterComponent> params, String name, Type value) {
		for (ValueSetExpansionParameterComponent p : params) {
			if (p.getName().equals(name) && PrimitiveType.compareDeep(p.getValue(), value, false))
				return true;
		}
		return false;
	}

	@Override
  public ValueSetExpansionOutcome expand(ValueSet source, ExpansionProfile profile) {

    if (profile == null)
      profile = makeDefaultExpansion();
		try {
			focus = source.copy();
			focus.setExpansion(new ValueSet.ValueSetExpansionComponent());
			focus.getExpansion().setTimestampElement(DateTimeType.now());
			focus.getExpansion().setIdentifier(Factory.createUUID());
      if (!profile.getUrl().startsWith("urn:uuid:"))
        focus.getExpansion().addParameter().setName("profile").setValue(new UriType(profile.getUrl()));

			if (source.hasCompose())
        handleCompose(source.getCompose(), focus.getExpansion().getParameter(), profile);

      if (canBeHeirarchy) {
        for (ValueSetExpansionContainsComponent c : roots) {
          focus.getExpansion().getContains().add(c);
        }
      } else {
			for (ValueSetExpansionContainsComponent c : codes) {
          if (map.containsKey(key(c)) && !c.getAbstract()) { // we may have added abstract codes earlier while we still thought it might be heirarchical, but later we gave up, so now ignore them
					focus.getExpansion().getContains().add(c);
            c.getContains().clear(); // make sure any heirarchy is wiped
          }
				}
			}
			
			if (total > 0) {
				focus.getExpansion().setTotal(total);
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

	private ExpansionProfile makeDefaultExpansion() {
	  ExpansionProfile res = new ExpansionProfile();
	  res.setUrl("urn:uuid:"+UUID.randomUUID().toString().toLowerCase());
	  res.setExcludeNested(true);
    res.setIncludeDesignations(false);
    return res;
  }

  private void addToHeirarchy(List<ValueSetExpansionContainsComponent> target, List<ValueSetExpansionContainsComponent> source) {
    for (ValueSetExpansionContainsComponent s : source) {
      target.add(s);
    }
  }

	private String getCodeDisplay(CodeSystem cs, String code) throws TerminologyServiceException {
		ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), code);
		if (def == null)
			throw new TerminologyServiceException("Unable to find code '" + code + "' in code system " + cs.getUrl());
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

	private void handleCompose(ValueSetComposeComponent compose, List<ValueSetExpansionParameterComponent> params, ExpansionProfile profile) throws TerminologyServiceException, ETooCostly, FileNotFoundException, IOException {
		// Exclude comes first because we build up a map of things to exclude
		for (ConceptSetComponent inc : compose.getExclude())
			excludeCodes(inc, params);
    canBeHeirarchy = !profile.getExcludeNested() && excludeKeys.isEmpty() && excludeSystems.isEmpty();
		for (UriType imp : compose.getImport())
  		importValueSet(imp.getValue(), params, profile);
  	boolean first = true;
  	for (ConceptSetComponent inc : compose.getInclude()) {
  	  if (first == true)
  	    first = false;
  	  else
  	    canBeHeirarchy = false;
      includeCodes(inc, params, profile);
  	}

	}

	private void importValueSet(String value, List<ValueSetExpansionParameterComponent> params, ExpansionProfile profile) throws ETooCostly, TerminologyServiceException, FileNotFoundException, IOException {
		if (value == null)
			throw new TerminologyServiceException("unable to find value set with no identity");
		ValueSet vs = context.fetchResource(ValueSet.class, value);
		if (vs == null)
			throw new TerminologyServiceException("Unable to find imported value set " + value);
	  ValueSetExpansionOutcome vso = factory.getExpander().expand(vs, profile);
    if (vso.getError() != null)
      throw new TerminologyServiceException("Unable to expand imported value set: "+vso.getError());
		if (vso.getService() != null)
			throw new TerminologyServiceException("Unable to expand imported value set " + value);
		if (vs.hasVersion())
			if (!existsInParams(params, "version", new UriType(vs.getUrl() + "?version=" + vs.getVersion())))
				params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(vs.getUrl() + "?version=" + vs.getVersion())));
		for (ValueSetExpansionParameterComponent p : vso.getValueset().getExpansion().getParameter()) {
			if (!existsInParams(params, p.getName(), p.getValue()))
				params.add(p);
		}
    canBeHeirarchy = false; // if we're importing a value set, we have to be combining, so we won't try for a heirarchy
    copyImportContains(vso.getValueset().getExpansion().getContains(), null, profile);
  }

  private void copyImportContains(List<ValueSetExpansionContainsComponent> list, ValueSetExpansionContainsComponent parent, ExpansionProfile profile) {
    for (ValueSetExpansionContainsComponent c : list) {
      ValueSetExpansionContainsComponent np = addCode(c.getSystem(), c.getCode(), c.getDisplay(), parent, null, profile, c.getAbstract());
      copyImportContains(c.getContains(), np, profile);
		}
	}

  private void includeCodes(ConceptSetComponent inc, List<ValueSetExpansionParameterComponent> params, ExpansionProfile profile) throws ETooCostly, org.hl7.fhir.exceptions.TerminologyServiceException {
		CodeSystem cs = context.fetchCodeSystem(inc.getSystem());
		if ((cs == null || cs.getContent() != CodeSystemContentMode.COMPLETE) && context.supportsSystem(inc.getSystem())) {
      addCodes(context.expandVS(inc, canBeHeirarchy), params, profile);
			return;
		}

		if (cs == null)
			throw new TerminologyServiceException("unable to find code system " + inc.getSystem().toString());
		if (cs.getContent() != CodeSystemContentMode.COMPLETE)
			throw new TerminologyServiceException("Code system " + inc.getSystem().toString() + " is incomplete");
		if (cs.hasVersion())
			if (!existsInParams(params, "version", new UriType(cs.getUrl() + "?version=" + cs.getVersion())))
				params.add(new ValueSetExpansionParameterComponent().setName("version").setValue(new UriType(cs.getUrl() + "?version=" + cs.getVersion())));
	  
		if (inc.getConcept().size() == 0 && inc.getFilter().size() == 0) {
			// special case - add all the code system
			for (ConceptDefinitionComponent def : cs.getConcept()) {
        addCodeAndDescendents(cs, inc.getSystem(), def, null, profile);
			}
		}

	  if (!inc.getConcept().isEmpty()) {
	    canBeHeirarchy = false;
		for (ConceptReferenceComponent c : inc.getConcept()) {
        addCode(inc.getSystem(), c.getCode(), Utilities.noString(c.getDisplay()) ? getCodeDisplay(cs, c.getCode()) : c.getDisplay(), null, convertDesignations(c.getDesignation()), profile, false);
		}
	  }
	  if (inc.getFilter().size() > 1) {
	    canBeHeirarchy = false; // which will bt the case if we get around to supporting this
			throw new TerminologyServiceException("Multiple filters not handled yet"); // need to and them, and this isn't done yet. But this shouldn't arise in non loinc and snomed value sets
	  }
		if (inc.getFilter().size() == 1) {
			ConceptSetFilterComponent fc = inc.getFilter().get(0);
			if ("concept".equals(fc.getProperty()) && fc.getOp() == FilterOperator.ISA) {
				// special: all non-abstract codes in the target code system under the value
				ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), fc.getValue());
				if (def == null)
					throw new TerminologyServiceException("Code '" + fc.getValue() + "' not found in system '" + inc.getSystem() + "'");
        addCodeAndDescendents(cs, inc.getSystem(), def, null, profile);
			} else if ("display".equals(fc.getProperty()) && fc.getOp() == FilterOperator.EQUAL) {
			  // gg; note: wtf is this: if the filter is display=v, look up the code 'v', and see if it's diplsay is 'v'?
			  canBeHeirarchy = false;
				ConceptDefinitionComponent def = getConceptForCode(cs.getConcept(), fc.getValue());
				if (def != null) {
					if (isNotBlank(def.getDisplay()) && isNotBlank(fc.getValue())) {
						if (def.getDisplay().contains(fc.getValue())) {
              addCode(inc.getSystem(), def.getCode(), def.getDisplay(), null, def.getDesignation(), profile, CodeSystemUtilities.isAbstract(cs,  def));
						}
					}
				}
			} else
				throw new NotImplementedException("Search by property[" + fc.getProperty() + "] and op[" + fc.getOp() + "] is not supported yet");
		}
	}

  private List<ConceptDefinitionDesignationComponent> convertDesignations(List<ConceptReferenceDesignationComponent> list) {
    List<ConceptDefinitionDesignationComponent> res = new ArrayList<CodeSystem.ConceptDefinitionDesignationComponent>();
    for (ConceptReferenceDesignationComponent t : list) {
      ConceptDefinitionDesignationComponent c = new ConceptDefinitionDesignationComponent();
      c.setLanguage(t.getLanguage());
      c.setUse(t.getUse());
      c.setValue(t.getValue());
    }
    return res;
  }

	private String key(String uri, String code) {
		return "{" + uri + "}" + code;
	}

	private String key(ValueSetExpansionContainsComponent c) {
		return key(c.getSystem(), c.getCode());
	}

}
>>>>>>> refs/heads/master
