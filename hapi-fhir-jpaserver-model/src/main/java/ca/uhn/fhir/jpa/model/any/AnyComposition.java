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
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class AnyComposition {
	private final FhirVersionEnum myFhirVersion;
	private final IBaseResource myComposition;

	public static AnyComposition fromFhirContext(FhirContext theFhirContext) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		switch (version) {
			case DSTU3:
				return new AnyComposition(new org.hl7.fhir.dstu3.model.Composition());
			case R4:
				return new AnyComposition(new org.hl7.fhir.r4.model.Composition());
			default:
				throw new UnsupportedOperationException(version + " not supported");
		}
	}

	public AnyComposition(org.hl7.fhir.dstu3.model.Composition theCompositionR3) {
		myFhirVersion = FhirVersionEnum.DSTU3;
		myComposition = theCompositionR3;
	}

	public AnyComposition(org.hl7.fhir.r4.model.Composition theCompositionR4) {
		myFhirVersion = FhirVersionEnum.R4;
		myComposition = theCompositionR4;
	}

	public static AnyComposition fromResource(IBaseResource theComposition) {
		if (theComposition instanceof org.hl7.fhir.dstu3.model.Composition) {
			return new AnyComposition((org.hl7.fhir.dstu3.model.Composition) theComposition);
		} else if (theComposition instanceof org.hl7.fhir.r4.model.Composition) {
			return new AnyComposition((org.hl7.fhir.r4.model.Composition) theComposition);
		} else {
			throw new UnsupportedOperationException("Cannot convert " + theComposition.getClass().getName() + " to AnyList");
		}
	}

	public IBaseResource get() {
		return myComposition;
	}

	public org.hl7.fhir.dstu3.model.Composition getDstu3() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.DSTU3);
		return (org.hl7.fhir.dstu3.model.Composition) get();
	}

	public org.hl7.fhir.r4.model.Composition getR4() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.R4);
		return (org.hl7.fhir.r4.model.Composition) get();
	}

	public void setIdentifier(String theSystem, String theValue) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().setIdentifier(new org.hl7.fhir.dstu3.model.Identifier().setSystem(theSystem).setValue(theValue));
				break;
			case R4:
				getR4().setIdentifier(new org.hl7.fhir.r4.model.Identifier().setSystem(theSystem).setValue(theValue));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getIdentifier() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getIdentifier().getValue();
			case R4:
				return getR4().getIdentifier().getValue();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setClass(String theSystem, String theCode) {
		switch (myFhirVersion) {
			case DSTU3:
				setClassDstu3(theSystem, theCode);
				break;
			case R4:
				setClassR4(theSystem, theCode);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	private void setClassDstu3(String theSystem, String theCode) {
		org.hl7.fhir.dstu3.model.CodeableConcept codeableConcept = new org.hl7.fhir.dstu3.model.CodeableConcept();
		codeableConcept.addCoding().setSystem(theSystem).setCode(theCode);
		getDstu3().setClass_(codeableConcept);
	}

	private void setClassR4(String theSystem, String theCode) {
		org.hl7.fhir.r4.model.CodeableConcept codeableConcept = new org.hl7.fhir.r4.model.CodeableConcept();
		codeableConcept.addCoding().setSystem(theSystem).setCode(theCode);
		getR4().addCategory(codeableConcept);
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

	// TODO KHS Consolidate with other classes in this package
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

	public void setSubject(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().setSubject(new org.hl7.fhir.dstu3.model.Reference(theReferenceId));
				break;
			case R4:
				getR4().setSubject(new org.hl7.fhir.r4.model.Reference(theReferenceId));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setTitle(String theTitle) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().setTitle(theTitle);
				break;
			case R4:
				getR4().setTitle(theTitle);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getTitle() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getTitle();
			case R4:
				return getR4().getTitle();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void addEntry(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().getSectionFirstRep().addEntry(new org.hl7.fhir.dstu3.model.Reference(theReferenceId));
				break;
			case R4:
				getR4().getSectionFirstRep().addEntry(new org.hl7.fhir.r4.model.Reference(theReferenceId));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setRandomUuid() {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().setId(org.hl7.fhir.dstu3.model.IdType.newRandomUuid());
				break;
			case R4:
				getR4().setId(org.hl7.fhir.r4.model.IdType.newRandomUuid());
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}

	}
}
