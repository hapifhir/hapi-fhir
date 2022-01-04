package ca.uhn.fhir.narrative2;

/*-
 * #%L
 * HAPI FHIR - Core Library
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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NarrativeTemplate implements INarrativeTemplate {

	private String myTemplateFileName;
	private Set<String> myAppliesToProfiles = new HashSet<>();
	private Set<String> myAppliesToResourceTypes = new HashSet<>();
	private Set<String> myAppliesToDataTypes = new HashSet<>();
	private Set<Class<? extends IBase>> myAppliesToClasses = new HashSet<>();
	private TemplateTypeEnum myTemplateType = TemplateTypeEnum.THYMELEAF;
	private String myContextPath;
	private String myTemplateName;

	public Set<String> getAppliesToDataTypes() {
		return Collections.unmodifiableSet(myAppliesToDataTypes);
	}

	@Override
	public String getContextPath() {
		return myContextPath;
	}

	public void setContextPath(String theContextPath) {
		myContextPath = theContextPath;
	}

	private String getTemplateFileName() {
		return myTemplateFileName;
	}

	void setTemplateFileName(String theTemplateFileName) {
		myTemplateFileName = theTemplateFileName;
	}

	@Override
	public Set<String> getAppliesToProfiles() {
		return Collections.unmodifiableSet(myAppliesToProfiles);
	}

	void addAppliesToProfile(String theAppliesToProfile) {
		myAppliesToProfiles.add(theAppliesToProfile);
	}

	@Override
	public Set<String> getAppliesToResourceTypes() {
		return Collections.unmodifiableSet(myAppliesToResourceTypes);
	}

	void addAppliesToResourceType(String theAppliesToResourceType) {
		myAppliesToResourceTypes.add(theAppliesToResourceType);
	}

	@Override
	public Set<Class<? extends IBase>> getAppliesToClasses() {
		return Collections.unmodifiableSet(myAppliesToClasses);
	}

	void addAppliesToClass(Class<? extends IBase> theAppliesToClass) {
		myAppliesToClasses.add(theAppliesToClass);
	}

	@Override
	public TemplateTypeEnum getTemplateType() {
		return myTemplateType;
	}

	void setTemplateType(TemplateTypeEnum theTemplateType) {
		myTemplateType = theTemplateType;
	}

	@Override
	public String getTemplateName() {
		return myTemplateName;
	}

	NarrativeTemplate setTemplateName(String theTemplateName) {
		myTemplateName = theTemplateName;
		return this;
	}

	@Override
	public String getTemplateText() {
		try {
			return NarrativeTemplateManifest.loadResource(getTemplateFileName());
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(1866) + e);
		}
	}

	void addAppliesToDatatype(String theDataType) {
		myAppliesToDataTypes.add(theDataType);
	}

}
