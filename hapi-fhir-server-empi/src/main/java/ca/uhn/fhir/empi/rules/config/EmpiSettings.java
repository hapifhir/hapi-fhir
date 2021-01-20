package ca.uhn.fhir.empi.rules.config;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmpiSettings implements IEmpiSettings {
	private final IEmpiRuleValidator myEmpiRuleValidator;

	private boolean myEnabled;
	private int myConcurrentConsumers = EMPI_DEFAULT_CONCURRENT_CONSUMERS;
	private String myScriptText;
	private EmpiRulesJson myEmpiRules;
	private boolean myPreventEidUpdates;

	/**
	 * If disabled, the underlying EMPI system will operate under the following assumptions:
	 *
	 * 1. Patients/Practitioners may have more than 1 EID of the same system simultaneously.
	 * 2. During linking, incoming patient EIDs will be merged with existing Person EIDs.
	 *
	 */
	private boolean myPreventMultipleEids;

	@Autowired
	public EmpiSettings(IEmpiRuleValidator theEmpiRuleValidator) {
		myEmpiRuleValidator = theEmpiRuleValidator;
	}

	@Override
	public boolean isEnabled() {
		return myEnabled;
	}

	public EmpiSettings setEnabled(boolean theEnabled) {
		myEnabled = theEnabled;
		return this;
	}

	@Override
	public int getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	public EmpiSettings setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
		return this;
	}

	public String getScriptText() {
		return myScriptText;
	}

	public EmpiSettings setScriptText(String theScriptText) throws IOException {
		myScriptText = theScriptText;
		setEmpiRules(JsonUtil.deserialize(theScriptText, EmpiRulesJson.class));
		return this;
	}

	@Override
	public EmpiRulesJson getEmpiRules() {
		return myEmpiRules;
	}

	@Override
	public boolean isPreventEidUpdates() {
		return myPreventEidUpdates;
	}

	public EmpiSettings setPreventEidUpdates(boolean thePreventEidUpdates) {
		myPreventEidUpdates = thePreventEidUpdates;
		return this;
	}

	public EmpiSettings setEmpiRules(EmpiRulesJson theEmpiRules) {
		myEmpiRuleValidator.validate(theEmpiRules);
		myEmpiRules = theEmpiRules;
		return this;
	}

	public boolean isPreventMultipleEids() {
		return myPreventMultipleEids;
	}

	@Override
	public String getRuleVersion() {
		return myEmpiRules.getVersion();
	}

	public EmpiSettings setPreventMultipleEids(boolean thePreventMultipleEids) {
		myPreventMultipleEids = thePreventMultipleEids;
		return this;
	}
}
