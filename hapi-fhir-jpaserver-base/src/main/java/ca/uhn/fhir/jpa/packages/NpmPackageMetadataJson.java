package ca.uhn.fhir.jpa.packages;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Schema(description = "Represents an NPM package metadata response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class NpmPackageMetadataJson {

	@JsonProperty("dist-tags")
	private DistTags myDistTags;
	@JsonProperty("modified")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myModified;
	@JsonProperty("name")
	private String myName;
	@JsonProperty("versions")
	private Map<String, Version> myVersionIdToVersion;

	public void addVersion(Version theVersion) {
		getVersions().put(theVersion.getVersion(), theVersion);
	}

	@Nonnull
	public Map<String, Version> getVersions() {
		if (myVersionIdToVersion == null) {
			myVersionIdToVersion = new LinkedHashMap<>();
		}
		return myVersionIdToVersion;
	}

	public DistTags getDistTags() {
		return myDistTags;
	}

	public void setDistTags(DistTags theDistTags) {
		myDistTags = theDistTags;
	}

	public void setModified(Date theModified) {
		myModified = theModified;
	}

	public void setName(String theName) {
		myName = theName;
	}


	public static class DistTags {

		@JsonProperty("latest")
		private String myLatest;

		public String getLatest() {
			return myLatest;
		}

		public DistTags setLatest(String theLatest) {
			myLatest = theLatest;
			return this;
		}
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class Version {

		@JsonProperty("name")
		private String myName;
		@JsonProperty("version")
		private String myVersion;
		@JsonProperty("description")
		private String myDescription;
		@JsonProperty("fhirVersion")
		private String myFhirVersion;
		@Schema(description = "The size of this package in bytes", example = "1000")
		@JsonProperty("_bytes")
		private long myBytes;

		public String getName() {
			return myName;
		}

		public void setName(String theName) {
			myName = theName;
		}

		public String getDescription() {
			return myDescription;
		}

		public void setDescription(String theDescription) {
			myDescription = theDescription;
		}

		public String getFhirVersion() {
			return myFhirVersion;
		}

		public void setFhirVersion(String theFhirVersion) {
			myFhirVersion = theFhirVersion;
		}

		public String getVersion() {
			return myVersion;
		}

		public void setVersion(String theVersion) {
			myVersion = theVersion;
		}

		public long getBytes() {
			return myBytes;
		}

		public void setBytes(long theBytes) {
			myBytes = theBytes;
		}
	}
}
