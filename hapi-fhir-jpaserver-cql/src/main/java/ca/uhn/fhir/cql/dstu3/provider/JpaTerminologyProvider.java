package ca.uhn.fhir.cql.dstu3.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.rp.dstu3.ValueSetResourceProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcDstu3;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Schuler on 7/17/2017.
 */
@Component
public class JpaTerminologyProvider implements TerminologyProvider {

	private ITermReadSvcDstu3 terminologySvc;
	private ValueSetResourceProvider valueSetResourceProvider;
	private final IValidationSupport validationSupport;

	@Autowired
	public JpaTerminologyProvider(ITermReadSvcDstu3 terminologySvc,
											ValueSetResourceProvider valueSetResourceProvider, IValidationSupport validationSupport) {
		 this.terminologySvc = terminologySvc;
		 this.valueSetResourceProvider = valueSetResourceProvider;
		 this.validationSupport = validationSupport;
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
		List<Code> codes = new ArrayList<>();
		boolean needsExpand = false;
		ValueSet vs = null;
		if (valueSet.getId().startsWith("http://") || valueSet.getId().startsWith("https://")) {
			if (valueSet.getVersion() != null
				|| (valueSet.getCodeSystems() != null && valueSet.getCodeSystems().size() > 0)) {
				if (!(valueSet.getCodeSystems().size() == 1 && valueSet.getCodeSystems().get(0).getVersion() == null)) {
					throw new UnsupportedOperationException(String.format(
						"Could not expand value set %s; version and code system bindings are not supported at this time.",
						valueSet.getId()));
				}
			}
			IBundleProvider bundleProvider = valueSetResourceProvider.getDao()
				.search(new SearchParameterMap().add(ValueSet.SP_URL, new UriParam(valueSet.getId())));
			List<IBaseResource> valueSets = bundleProvider.getResources(0, bundleProvider.size());
			if (valueSets.isEmpty()) {
				throw new IllegalArgumentException(String.format("Could not resolve value set %s.", valueSet.getId()));
			} else if (valueSets.size() == 1) {
				vs = (ValueSet) valueSets.get(0);
			} else {
				throw new IllegalArgumentException("Found more than 1 ValueSet with url: " + valueSet.getId());
			}
		} else {
			vs = valueSetResourceProvider.getDao().read(new IdType(valueSet.getId()));
		}
		if (vs != null) {
			if (vs.hasCompose()) {
				if (vs.getCompose().hasInclude()) {
					for (ValueSet.ConceptSetComponent include : vs.getCompose().getInclude()) {
						if (include.hasValueSet() || include.hasFilter()) {
							needsExpand = true;
							break;
						}
						for (ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
							if (concept.hasCode()) {
								codes.add(new Code().withCode(concept.getCode()).withSystem(include.getSystem()));
							}
						}
					}
					if (!needsExpand) {
						return codes;
					}
				}
			}

			if (vs.hasExpansion() && vs.getExpansion().hasContains()) {
				for (ValueSetExpansionContainsComponent vsecc : vs.getExpansion().getContains()) {
					codes.add(new Code().withCode(vsecc.getCode()).withSystem(vsecc.getSystem()));
				}

				return codes;
			}
		}

		org.hl7.fhir.r4.model.ValueSet expansion = terminologySvc
			.expandValueSet(new ValueSetExpansionOptions().setCount(Integer.MAX_VALUE), valueSet.getId(), null);
		expansion.getExpansion().getContains()
			.forEach(concept -> codes.add(new Code().withCode(concept.getCode()).withSystem(concept.getSystem())));

		return codes;
	}

	@Override
	public Code lookup(Code code, CodeSystemInfo codeSystem) throws ResourceNotFoundException {
		LookupCodeResult cs = terminologySvc.lookupCode(new ValidationSupportContext(validationSupport), codeSystem.getId(), code.getCode());

		code.setDisplay(cs.getCodeDisplay());
		code.setSystem(codeSystem.getId());

		return code;
	}
}
