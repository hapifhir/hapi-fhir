package ca.uhn.fhir.mdm.rules.config;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MdmSettings implements IMdmSettings {
	private final IMdmRuleValidator myMdmRuleValidator;

	private boolean myEnabled;
	private int myConcurrentConsumers = MDM_DEFAULT_CONCURRENT_CONSUMERS;
	private String myScriptText;
	private MdmRulesJson myMdmRules;
	private boolean myPreventEidUpdates;

	/**
	 * If disabled, the underlying MDM system will operate under the following assumptions:
	 *
	 * 1. Source resource may have more than 1 EID of the same system simultaneously.
	 * 2. During linking, incoming patient EIDs will be merged with existing Golden Resource EIDs.
	 */
	private boolean myPreventMultipleEids;

	@Autowired
	public MdmSettings(IMdmRuleValidator theMdmRuleValidator) {
		myMdmRuleValidator = theMdmRuleValidator;
	}

	@Override
	public boolean isEnabled() {
		return myEnabled;
	}

	public MdmSettings setEnabled(boolean theEnabled) {
		myEnabled = theEnabled;
		return this;
	}

	@Override
	public int getConcurrentConsumers() {
		return myConcurrentConsumers;
	}

	public MdmSettings setConcurrentConsumers(int theConcurrentConsumers) {
		myConcurrentConsumers = theConcurrentConsumers;
		return this;
	}

	public String getScriptText() {
		return myScriptText;
	}

	public MdmSettings setScriptText(String theScriptText) throws IOException {
		myScriptText = theScriptText;
		setMdmRules(JsonUtil.deserialize(theScriptText, MdmRulesJson.class));
		return this;
	}

	@Override
	public MdmRulesJson getMdmRules() {
		return myMdmRules;
	}

	@Override
	public boolean isPreventEidUpdates() {
		return myPreventEidUpdates;
	}

	public MdmSettings setPreventEidUpdates(boolean thePreventEidUpdates) {
		myPreventEidUpdates = thePreventEidUpdates;
		return this;
	}

	public MdmSettings setMdmRules(MdmRulesJson theMdmRules) {
		myMdmRuleValidator.validate(theMdmRules);
		myMdmRules = theMdmRules;
		return this;
	}

	public boolean isPreventMultipleEids() {
		return myPreventMultipleEids;
	}

	@Override
	public String getRuleVersion() {
		return myMdmRules.getVersion();
	}

	public MdmSettings setPreventMultipleEids(boolean thePreventMultipleEids) {
		myPreventMultipleEids = thePreventMultipleEids;
		return this;
	}
}
