package ca.uhn.fhir.jpa.api.model;

/*
 * #%L
 * HAPI FHIR Storage api
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;

public class TranslationQuery {
	private Coding myCoding;
	private Long myResourceId;
	private UriType myUrl;
	private StringType myConceptMapVersion;
	private UriType mySource;
	private UriType myTarget;
	private UriType myTargetSystem;

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

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public boolean hasUrl() {
		return myUrl != null && myUrl.hasValue();
	}

	public UriType getUrl() {
		return myUrl;
	}

	public void setUrl(UriType theUrl) {
		myUrl = theUrl;
	}

	public boolean hasConceptMapVersion() {
		return myConceptMapVersion != null && myConceptMapVersion.hasValue();
	}

	public StringType getConceptMapVersion() {
		return myConceptMapVersion;
	}

	public void setConceptMapVersion(StringType theConceptMapVersion) {
		myConceptMapVersion = theConceptMapVersion;
	}

	public boolean hasSource() {
		return mySource != null && mySource.hasValue();
	}

	public UriType getSource() {
		return mySource;
	}

	public void setSource(UriType theSource) {
		mySource = theSource;
	}

	public boolean hasTarget() {
		return myTarget != null && myTarget.hasValue();
	}

	public UriType getTarget() {
		return myTarget;
	}

	public void setTarget(UriType theTarget) {
		myTarget = theTarget;
	}

	public boolean hasTargetSystem() {
		return myTargetSystem != null && myTargetSystem.hasValue();
	}

	public UriType getTargetSystem() {
		return myTargetSystem;
	}

	public void setTargetSystem(UriType theTargetSystem) {
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
