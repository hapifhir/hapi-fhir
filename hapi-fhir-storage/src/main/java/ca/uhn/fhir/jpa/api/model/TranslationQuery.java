/*
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TranslationQuery {
	private Coding myCoding;
	private IIdType myResourceId;
	private String myUrl;
	private String myConceptMapVersion;
	private String mySource;
	private String myTarget;
	private String myTargetSystem;

	public TranslationQuery() {
		super();

		myCoding = new Coding();
	}

	public Coding getCoding() {
		return myCoding;
	}

	public void setCoding(Coding theCoding) {
		myCoding = theCoding;
	}

	public boolean hasResourceId() {
		return myResourceId != null;
	}

	public IIdType getResourceId() {
		return myResourceId;
	}

	public void setResourceId(IIdType theResourceId) {
		myResourceId = theResourceId;
	}

	public boolean hasUrl() {
		return isNotBlank(myUrl);
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public boolean hasConceptMapVersion() {
		return isNotBlank(myConceptMapVersion);
	}

	public String getConceptMapVersion() {
		return myConceptMapVersion;
	}

	public void setConceptMapVersion(String theConceptMapVersion) {
		myConceptMapVersion = theConceptMapVersion;
	}

	public boolean hasSource() {
		return isNotBlank(mySource);
	}

	public String getSource() {
		return mySource;
	}

	public void setSource(String theSource) {
		mySource = theSource;
	}

	public boolean hasTarget() {
		return isNotBlank(myTarget);
	}

	public String getTarget() {
		return myTarget;
	}

	public void setTarget(String theTarget) {
		myTarget = theTarget;
	}

	public boolean hasTargetSystem() {
		return isNotBlank(myTargetSystem);
	}

	public String getTargetSystem() {
		return myTargetSystem;
	}

	public void setTargetSystem(String theTargetSystem) {
		myTargetSystem = theTargetSystem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof TranslationQuery)) return false;

		TranslationQuery that = (TranslationQuery) o;

		return new EqualsBuilder()
				.append(getCoding().getCode(), that.getCoding().getCode())
				.append(getCoding().getSystem(), that.getCoding().getSystem())
				.append(getCoding().getVersion(), that.getCoding().getVersion())
				.append(getResourceId(), that.getResourceId())
				.append(getUrl(), that.getUrl())
				.append(getConceptMapVersion(), that.getConceptMapVersion())
				.append(getSource(), that.getSource())
				.append(getTarget(), that.getTarget())
				.append(getTargetSystem(), that.getTargetSystem())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(getCoding().getCode())
				.append(getCoding().getSystem())
				.append(getCoding().getVersion())
				.append(getResourceId())
				.append(getUrl())
				.append(getConceptMapVersion())
				.append(getSource())
				.append(getTarget())
				.append(getTargetSystem())
				.toHashCode();
	}
}
