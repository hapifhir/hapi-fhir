/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents details of an NPM resource and its associated package")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
		creatorVisibility = JsonAutoDetect.Visibility.NONE,
		fieldVisibility = JsonAutoDetect.Visibility.NONE,
		getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE,
		setterVisibility = JsonAutoDetect.Visibility.NONE)
public class NpmFhirIdPackageIdAndVersionJson {
	@JsonProperty("Resource ID")
	private String myFhirId;

	@JsonProperty("Canonical URL")
	private String myCanonicalUrl;

	@JsonProperty("FHIR Version")
	private FhirVersionEnum myFhirVersion;

	@JsonProperty("Package ID")
	private String myPackageId;

	@JsonProperty("Package Version")
	private String myVersion;

	public NpmFhirIdPackageIdAndVersionJson() {
		// empty constructor for serialization
		this(null, null, null, null, null);
	}

	public NpmFhirIdPackageIdAndVersionJson(
			String theFhirId,
			String theCanonicalUrl,
			FhirVersionEnum theFhirVersion,
			String thePackageId,
			String theVersion) {
		myFhirId = theFhirId;
		myCanonicalUrl = theCanonicalUrl;
		myFhirVersion = theFhirVersion;
		myPackageId = thePackageId;
		myVersion = theVersion;
	}

	public String getFhirId() {
		return myFhirId;
	}

	public NpmFhirIdPackageIdAndVersionJson setFhirId(String theFhirId) {
		this.myFhirId = theFhirId;
		return this;
	}

	public String getCanonicalUrl() {
		return myCanonicalUrl;
	}

	public NpmFhirIdPackageIdAndVersionJson setCanonicalUrl(String theCanonicalUrl) {
		this.myCanonicalUrl = theCanonicalUrl;
		return this;
	}

	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public NpmFhirIdPackageIdAndVersionJson setFhirVersion(FhirVersionEnum theFhirVersion) {
		this.myFhirVersion = theFhirVersion;
		return this;
	}

	public String getPackageId() {
		return myPackageId;
	}

	public NpmFhirIdPackageIdAndVersionJson setPackageId(String thePackageId) {
		this.myPackageId = thePackageId;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public NpmFhirIdPackageIdAndVersionJson setVersion(String theVersion) {
		this.myVersion = theVersion;
		return this;
	}
}
