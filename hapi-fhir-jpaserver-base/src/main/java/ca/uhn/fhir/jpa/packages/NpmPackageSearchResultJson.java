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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Represents an NPM package search response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class NpmPackageSearchResultJson {

	@JsonProperty("objects")
	private List<ObjectElement> myObjects;
	@JsonProperty("total")
	private int myTotal;

	public List<ObjectElement> getObjects() {
		if (myObjects == null) {
			myObjects = new ArrayList<>();
		}
		return myObjects;
	}

	public ObjectElement addObject() {
		ObjectElement object = new ObjectElement();
		getObjects().add(object);
		return object;
	}

	public int getTotal() {
		return myTotal;
	}

	public void setTotal(int theTotal) {
		myTotal = theTotal;
	}

	public boolean hasPackageWithId(String thePackageId) {
		return getObjects().stream().anyMatch(t -> t.getPackage().getName().equals(thePackageId));
	}

	public Package getPackageWithId(String thePackageId) {
		return getObjects().stream().map(t -> t.getPackage()).filter(t -> t.getName().equals(thePackageId)).findFirst().orElseThrow(() -> new IllegalArgumentException());
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class ObjectElement {

		@JsonProperty("package")
		private Package myPackage;

		public Package getPackage() {
			if (myPackage == null) {
				myPackage = new Package();
			}
			return myPackage;
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class Package {

		@JsonProperty("name")
		private String myName;
		@JsonProperty("version")
		private String myVersion;
		@JsonProperty("description")
		private String myDescription;
		@JsonProperty("fhirVersion")
		private List<String> myFhirVersion;
		@Schema(description = "The size of this package in bytes", example = "1000")
		@JsonProperty("_bytes")
		private long myBytes;

		public long getBytes() {
			return myBytes;
		}

		public Package setBytes(long theBytes) {
			myBytes = theBytes;
			return this;
		}

		public String getName() {
			return myName;
		}

		public Package setName(String theName) {
			myName = theName;
			return this;
		}

		public String getDescription() {
			return myDescription;
		}

		public Package setDescription(String theDescription) {
			myDescription = theDescription;
			return this;
		}

		public List<String> getFhirVersion() {
			if (myFhirVersion == null) {
				myFhirVersion = new ArrayList<>();
			}
			return myFhirVersion;
		}

		public String getVersion() {
			return myVersion;
		}

		public Package setVersion(String theVersion) {
			myVersion = theVersion;
			return this;
		}

		public Package addFhirVersion(String theFhirVersionId) {
			if (!getFhirVersion().contains(theFhirVersionId)) {
				getFhirVersion().add(theFhirVersionId);
				getFhirVersion().sort(PackageVersionComparator.INSTANCE);
			}
			return this;
		}
	}
}
