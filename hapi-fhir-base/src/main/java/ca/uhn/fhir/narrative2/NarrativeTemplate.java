/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.narrative2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NarrativeTemplate implements INarrativeTemplate {

	private final Set<String> myAppliesToProfiles = new HashSet<>();
	private final Set<String> myAppliesToResourceTypes = new HashSet<>();
	private final Set<String> myAppliesToDataTypes = new HashSet<>();
	private final Set<Class<? extends IBase>> myAppliesToClasses = new HashSet<>();
	private final Set<String> myAppliesToFragmentNames = new HashSet<>();
	private String myTemplateFileName;
	private TemplateTypeEnum myTemplateType = TemplateTypeEnum.THYMELEAF;
	private String myContextPath;
	private String myTemplateName;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("name", myTemplateName)
				.append("fileName", myTemplateFileName)
				.toString();
	}

	public Set<String> getAppliesToDataTypes() {
		return Collections.unmodifiableSet(myAppliesToDataTypes);
	}

	public Set<String> getAppliesToFragmentNames() {
		return Collections.unmodifiableSet(myAppliesToFragmentNames);
	}

	void addAppliesToFragmentName(String theAppliesToFragmentName) {
		myAppliesToFragmentNames.add(theAppliesToFragmentName);
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
		return NarrativeTemplateManifest.loadResource(getTemplateFileName());
	}

	void addAppliesToDatatype(String theDataType) {
		myAppliesToDataTypes.add(theDataType);
	}
}
