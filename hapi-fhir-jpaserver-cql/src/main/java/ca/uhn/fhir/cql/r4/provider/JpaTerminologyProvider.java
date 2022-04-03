package ca.uhn.fhir.cql.r4.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class JpaTerminologyProvider implements TerminologyProvider {

	private final ITermReadSvcR4 myTerminologySvc;
	private final DaoRegistry myDaoRegistry;
	private final IValidationSupport myValidationSupport;

	private IFhirResourceDao<ValueSet> myValueSetDao;

	@Autowired
	public JpaTerminologyProvider(ITermReadSvcR4 theTerminologySvc, DaoRegistry theDaoRegistry, IValidationSupport theValidationSupport) {
		myTerminologySvc = theTerminologySvc;
		myDaoRegistry = theDaoRegistry;
		myValidationSupport = theValidationSupport;
	}

	@PostConstruct
	public void init() {
		myValueSetDao = myDaoRegistry.getResourceDao(ValueSet.class);
	}

	@Override
	public boolean in(Code code, ValueSetInfo valueSet) throws ResourceNotFoundException {
		for (Code c : expand(valueSet)) {
			if (c == null)
				continue;
			if (c.getCode().equals(code.getCode()) && c.getSystem().equals(code.getSystem())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterable<Code> expand(ValueSetInfo valueSet) throws ResourceNotFoundException {
		// This could possibly be refactored into a single call to the underlying HAPI Terminology service. Need to think through that..,
		ValueSet vs;
		if (valueSet.getId().startsWith("http://") || valueSet.getId().startsWith("https://")) {
			if (valueSet.getVersion() != null
				|| (valueSet.getCodeSystems() != null && valueSet.getCodeSystems().size() > 0)) {
				if (!(valueSet.getCodeSystems().size() == 1 && valueSet.getCodeSystems().get(0).getVersion() == null)) {
					throw new UnsupportedOperationException(Msg.code(1667) + String.format(
						"Could not expand value set %s; version and code system bindings are not supported at this time.",
						valueSet.getId()));
				}
			}

			IBundleProvider bundleProvider = myValueSetDao
				.search(SearchParameterMap.newSynchronous().add(ValueSet.SP_URL, new UriParam(valueSet.getId())));
			List<IBaseResource> valueSets = bundleProvider.getAllResources();
			if (valueSets.isEmpty()) {
				throw new IllegalArgumentException(Msg.code(1668) + String.format("Could not resolve value set %s.", valueSet.getId()));
			} else if (valueSets.size() == 1) {
				vs = (ValueSet) valueSets.get(0);
			} else {
				throw new IllegalArgumentException(Msg.code(1669) + "Found more than 1 ValueSet with url: " + valueSet.getId());
			}
		} else {
			vs = myValueSetDao.read(new IdType(valueSet.getId()));
			if (vs == null) {
				throw new IllegalArgumentException(Msg.code(1670) + String.format("Could not resolve value set %s.", valueSet.getId()));
			}
		}

		// Attempt to expand the ValueSet if it's not already expanded.
		if (!(vs.hasExpansion() && vs.getExpansion().hasContains())) {
			vs = myTerminologySvc.expandValueSet(
				new ValueSetExpansionOptions().setCount(Integer.MAX_VALUE).setFailOnMissingCodeSystem(false), vs);
		}

		List<Code> codes = new ArrayList<>();

		// If expansion was successful, use the codes.
		if (vs.hasExpansion() && vs.getExpansion().hasContains()) {
			for (ValueSetExpansionContainsComponent vsecc : vs.getExpansion().getContains()) {
				codes.add(new Code().withCode(vsecc.getCode()).withSystem(vsecc.getSystem()));
			}
		}
		// If not, best-effort based on codes. Should probably make this configurable to match the behavior of the
		// underlying terminology service implementation
		else if (vs.hasCompose() && vs.getCompose().hasInclude()) {
			for (ValueSet.ConceptSetComponent include : vs.getCompose().getInclude()) {
				for (ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCode()) {
						codes.add(new Code().withCode(concept.getCode()).withSystem(include.getSystem()));
					}
				}
			}
		}

		return codes;
	}

	@Override
	public Code lookup(Code code, CodeSystemInfo codeSystem) throws ResourceNotFoundException {
		LookupCodeResult cs = myTerminologySvc.lookupCode(new ValidationSupportContext(myValidationSupport),
			codeSystem.getId(), code.getCode());

		code.setDisplay(cs.getCodeDisplay());
		code.setSystem(codeSystem.getId());

		return code;
	}
}
