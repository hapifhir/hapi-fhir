/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;
import org.opencds.cqf.cql.evaluator.fhir.util.Canonicals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class provides an implementation of the cql-engine's TerminologyProvider
 * interface, which is used for Terminology operations
 * in CQL
 */
public class HapiTerminologyProvider implements TerminologyProvider {
	private final IValidationSupport myValidationSupport;
	private final Map<VersionedIdentifier, List<Code>> myGlobalCodeCache;

	public HapiTerminologyProvider(
			IValidationSupport theValidationSupport, Map<VersionedIdentifier, List<Code>> theGlobalCodeCache) {
		this(theValidationSupport, theGlobalCodeCache, null);
	}

	public HapiTerminologyProvider(
			IValidationSupport theValidationSupport,
			Map<VersionedIdentifier, List<Code>> theGlobalCodeCache,
			RequestDetails theRequestDetails) {
		myValidationSupport = theValidationSupport;
		myGlobalCodeCache = theGlobalCodeCache;
	}

	@Override
	public boolean in(Code code, ValueSetInfo valueSet) throws ResourceNotFoundException {
		for (Code c : expand(valueSet)) {
			if (c == null) {
				continue;
			}
			if (c.getCode().equals(code.getCode()) && c.getSystem().equals(code.getSystem())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterable<Code> expand(ValueSetInfo theValueSet) throws ResourceNotFoundException {
		// This could possibly be refactored into a single call to the underlying HAPI
		// Terminology service. Need to think through that..,

		VersionedIdentifier vsId =
				new VersionedIdentifier().withId(theValueSet.getId()).withVersion(theValueSet.getVersion());

		if (this.myGlobalCodeCache.containsKey(vsId)) {
			return this.myGlobalCodeCache.get(vsId);
		}

		ValueSetExpansionOptions valueSetExpansionOptions = new ValueSetExpansionOptions();
		valueSetExpansionOptions.setFailOnMissingCodeSystem(false);
		valueSetExpansionOptions.setCount(Integer.MAX_VALUE);

		if (theValueSet.getVersion() != null
				&& Canonicals.getUrl(theValueSet.getId()) != null
				&& Canonicals.getVersion(theValueSet.getId()) == null) {
			theValueSet.setId(theValueSet.getId() + "|" + theValueSet.getVersion());
		}

		IValidationSupport.ValueSetExpansionOutcome vs = myValidationSupport.expandValueSet(
				new ValidationSupportContext(myValidationSupport), valueSetExpansionOptions, theValueSet.getId());

		List<Code> codes = getCodes(vs.getValueSet());
		this.myGlobalCodeCache.put(vsId, codes);
		return codes;
	}

	@Override
	public Code lookup(Code theCode, CodeSystemInfo theCodeSystem) throws ResourceNotFoundException {

		LookupCodeResult cs = myValidationSupport.lookupCode(
				new ValidationSupportContext(myValidationSupport), theCodeSystem.getId(), theCode.getCode());

		if (cs != null) {
			theCode.setDisplay(cs.getCodeDisplay());
		}
		theCode.setSystem(theCodeSystem.getId());

		return theCode;
	}

	protected List<Code> getCodes(IBaseResource theValueSet) {

		FhirVersionEnum version = theValueSet.getStructureFhirVersionEnum();
		switch (version) {
			case DSTU2_1:
				return getCodesDstu21((org.hl7.fhir.dstu2016may.model.ValueSet) theValueSet);
			case DSTU2_HL7ORG:
				return getCodesDstu2Hl7((org.hl7.fhir.dstu2.model.ValueSet) theValueSet);
			case DSTU3:
				return getCodesDstu3((org.hl7.fhir.dstu3.model.ValueSet) theValueSet);
			case R4:
				return getCodesR4((org.hl7.fhir.r4.model.ValueSet) theValueSet);
			case R4B:
				return getCodesR4B((org.hl7.fhir.r4b.model.ValueSet) theValueSet);
			case R5:
				return getCodesR5((org.hl7.fhir.r5.model.ValueSet) theValueSet);
			default:
				throw new IllegalArgumentException(Msg.code(2225)
						+ String.format("FHIR version %s is unsupported.", version.getFhirVersionString()));
		}
	}

	protected List<Code> getCodesDstu2Hl7(org.hl7.fhir.dstu2.model.ValueSet theValueSet) {
		var codes = new ArrayList<Code>();
		for (var vse : theValueSet.getExpansion().getContains()) {
			codes.add(new Code().withCode(vse.getCode()).withSystem(vse.getSystem()));
		}

		return codes;
	}

	protected List<Code> getCodesDstu21(org.hl7.fhir.dstu2016may.model.ValueSet theValueSet) {
		var codes = new ArrayList<Code>();
		for (var vse : theValueSet.getExpansion().getContains()) {
			codes.add(new Code().withCode(vse.getCode()).withSystem(vse.getSystem()));
		}

		return codes;
	}

	protected List<Code> getCodesDstu3(org.hl7.fhir.dstu3.model.ValueSet theValueSet) {
		var codes = new ArrayList<Code>();
		for (var vse : theValueSet.getExpansion().getContains()) {
			codes.add(new Code().withCode(vse.getCode()).withSystem(vse.getSystem()));
		}

		return codes;
	}

	protected List<Code> getCodesR4(org.hl7.fhir.r4.model.ValueSet theValueSet) {
		var codes = new ArrayList<Code>();
		for (var vse : theValueSet.getExpansion().getContains()) {
			codes.add(new Code().withCode(vse.getCode()).withSystem(vse.getSystem()));
		}

		return codes;
	}

	protected List<Code> getCodesR4B(org.hl7.fhir.r4b.model.ValueSet theValueSet) {
		var codes = new ArrayList<Code>();
		for (var vse : theValueSet.getExpansion().getContains()) {
			codes.add(new Code().withCode(vse.getCode()).withSystem(vse.getSystem()));
		}

		return codes;
	}

	protected List<Code> getCodesR5(org.hl7.fhir.r5.model.ValueSet theValueSet) {
		var codes = new ArrayList<Code>();
		for (var vse : theValueSet.getExpansion().getContains()) {
			codes.add(new Code().withCode(vse.getCode()).withSystem(vse.getSystem()));
		}

		return codes;
	}
}
