package ca.uhn.fhir.jpa.model.any;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.stream.Stream;

public class AnyListResource {
	private final FhirVersionEnum myFhirVersion;
	private final IBaseResource myListResource;

	public static AnyListResource fromFhirContext(FhirContext theFhirContext) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		switch (version) {
			case DSTU2:
				return new AnyListResource(new ca.uhn.fhir.model.dstu2.resource.ListResource());
			case DSTU3:
				return new AnyListResource(new org.hl7.fhir.dstu3.model.ListResource());
			case R4:
				return new AnyListResource(new org.hl7.fhir.r4.model.ListResource());
			default:
				throw new UnsupportedOperationException(version + " not supported");
		}
	}

	public AnyListResource(ca.uhn.fhir.model.dstu2.resource.ListResource theListResourceR2) {
		myFhirVersion = FhirVersionEnum.DSTU2;
		myListResource = theListResourceR2;
	}

	public AnyListResource(org.hl7.fhir.dstu3.model.ListResource theListResourceR3) {
		myFhirVersion = FhirVersionEnum.DSTU3;
		myListResource = theListResourceR3;
	}

	public AnyListResource(org.hl7.fhir.r4.model.ListResource theListResourceR4) {
		myFhirVersion = FhirVersionEnum.R4;
		myListResource = theListResourceR4;
	}

	public static AnyListResource fromResource(IBaseResource theListResource) {
		if (theListResource instanceof ca.uhn.fhir.model.dstu2.resource.ListResource) {
			return new AnyListResource((ca.uhn.fhir.model.dstu2.resource.ListResource) theListResource);
		} else if (theListResource instanceof org.hl7.fhir.dstu3.model.ListResource) {
			return new AnyListResource((org.hl7.fhir.dstu3.model.ListResource) theListResource);
		} else if (theListResource instanceof org.hl7.fhir.r4.model.ListResource) {
			return new AnyListResource((org.hl7.fhir.r4.model.ListResource) theListResource);
		} else {
			throw new UnsupportedOperationException("Cannot convert " + theListResource.getClass().getName() + " to AnyList");
		}
	}

	public IBaseResource get() {
		return myListResource;
	}

	public ca.uhn.fhir.model.dstu2.resource.ListResource getDstu2() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.DSTU2);
		return (ca.uhn.fhir.model.dstu2.resource.ListResource) get();
	}

	public org.hl7.fhir.dstu3.model.ListResource getDstu3() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.DSTU3);
		return (org.hl7.fhir.dstu3.model.ListResource) get();
	}

	public org.hl7.fhir.r4.model.ListResource getR4() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.R4);
		return (org.hl7.fhir.r4.model.ListResource) get();
	}

	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public void addCode(String theSystem, String theCode) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().getCode().addCoding().setSystem(theSystem).setCode(theCode);
				break;
			case R4:
				getR4().getCode().addCoding().setSystem(theSystem).setCode(theCode);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void addIdentifier(String theSystem, String theValue) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().getIdentifier().add(new org.hl7.fhir.dstu3.model.Identifier().setSystem(theSystem).setValue(theValue));
				break;
			case R4:
				getR4().getIdentifier().add(new org.hl7.fhir.r4.model.Identifier().setSystem(theSystem).setValue(theValue));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void addStringExtension(String theUrl, String theValue) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().addExtension().setUrl(theUrl).setValue(new org.hl7.fhir.dstu3.model.StringType(theValue));
				break;
			case R4:
				getR4().addExtension().setUrl(theUrl).setValue(new org.hl7.fhir.r4.model.StringType(theValue));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getStringExtensionValueOrNull(String theUrl) {
		switch (myFhirVersion) {
			case DSTU3:
				return getStringExtensionValueOrNullDstu3(theUrl);
			case R4:
				return getStringExtensionValueOrNullR4(theUrl);
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	private String getStringExtensionValueOrNullDstu3(String theUrl) {
		List<org.hl7.fhir.dstu3.model.Extension> targetTypes = getDstu3().getExtensionsByUrl(theUrl);
		if (targetTypes.size() < 1) {
			return null;
		}
		org.hl7.fhir.dstu3.model.StringType targetType = (org.hl7.fhir.dstu3.model.StringType) targetTypes.get(0).getValue();
		return targetType.getValue();
	}

	private String getStringExtensionValueOrNullR4(String theUrl) {
		List<org.hl7.fhir.r4.model.Extension> targetTypes = getR4().getExtensionsByUrl(theUrl);
		if (targetTypes.size() < 1) {
			return null;
		}
		org.hl7.fhir.r4.model.StringType targetType = (org.hl7.fhir.r4.model.StringType) targetTypes.get(0).getValue();
		return targetType.getValue();
	}

	public void addReference(IBaseReference theReference) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().addEntry().setItem((org.hl7.fhir.dstu3.model.Reference) theReference);
				break;
			case R4:
				getR4().addEntry().setItem((org.hl7.fhir.r4.model.Reference) theReference);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void addReference(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().addEntry().setItem(new org.hl7.fhir.dstu3.model.Reference(theReferenceId));
				break;
			case R4:
				getR4().addEntry().setItem(new org.hl7.fhir.r4.model.Reference(theReferenceId));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public Stream<String> getReferenceStream() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getEntry().stream()
					.map(entry -> entry.getItem().getReference())
					.map(reference -> new org.hl7.fhir.dstu3.model.IdType(reference).toUnqualifiedVersionless().getValue());
			case R4:
				return getR4().getEntry().stream()
					.map(entry -> entry.getItem().getReference())
					.map(reference -> new org.hl7.fhir.r4.model.IdType(reference).toUnqualifiedVersionless().getValue());
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public boolean removeItem(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				return removeItemDstu3(theReferenceId);
			case R4:
				return removeItemR4(theReferenceId);
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	private boolean removeItemDstu3(String theReferenceId) {
		boolean removed = false;
		for (org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent entry : getDstu3().getEntry()) {
			if (theReferenceId.equals(entry.getItem().getReference()) && !entry.getDeleted()) {
				entry.setDeleted(true);
				removed = true;
				break;
			}
		}

		if (removed) {
			getDstu3().getEntry().removeIf(entry -> entry.getDeleted());
		}
		return removed;
	}

	private boolean removeItemR4(String theReferenceId) {
		boolean removed = false;
		for (org.hl7.fhir.r4.model.ListResource.ListEntryComponent entry : getR4().getEntry()) {
			if (theReferenceId.equals(entry.getItem().getReference()) && !entry.getDeleted()) {
				entry.setDeleted(true);
				removed = true;
				break;
			}
		}

		if (removed) {
			getR4().getEntry().removeIf(entry -> entry.getDeleted());
		}
		return removed;
	}

	public TokenParam getCodeFirstRep() {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Coding codingDstu3 = getDstu3().getCode().getCodingFirstRep();
				return new TokenParam(codingDstu3.getSystem(), codingDstu3.getCode());
			case R4:
				org.hl7.fhir.r4.model.Coding codingR4 = getR4().getCode().getCodingFirstRep();
				return new TokenParam(codingR4.getSystem(), codingR4.getCode());
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public boolean isEmpty() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getEntry().isEmpty();
			case R4:
				return getR4().getEntry().isEmpty();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}
}
