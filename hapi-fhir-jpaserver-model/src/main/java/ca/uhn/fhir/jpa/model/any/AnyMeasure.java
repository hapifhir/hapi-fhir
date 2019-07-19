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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnyMeasure {
	private final FhirVersionEnum myFhirVersion;
	private final IBaseResource myMeasure;

	public static AnyMeasure fromFhirContext(FhirContext theFhirContext) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		switch (version) {
			case DSTU3:
				return new AnyMeasure(new org.hl7.fhir.dstu3.model.Measure());
			case R4:
				return new AnyMeasure(new org.hl7.fhir.r4.model.Measure());
			default:
				throw new UnsupportedOperationException(version + " not supported");
		}
	}

	public AnyMeasure(org.hl7.fhir.dstu3.model.Measure theMeasureR3) {
		myFhirVersion = FhirVersionEnum.DSTU3;
		myMeasure = theMeasureR3;
	}

	public AnyMeasure(org.hl7.fhir.r4.model.Measure theMeasureR4) {
		myFhirVersion = FhirVersionEnum.R4;
		myMeasure = theMeasureR4;
	}

	public static AnyMeasure fromResource(IBaseResource theMeasure) {
		if (theMeasure instanceof org.hl7.fhir.dstu3.model.Measure) {
			return new AnyMeasure((org.hl7.fhir.dstu3.model.Measure) theMeasure);
		} else if (theMeasure instanceof org.hl7.fhir.r4.model.Measure) {
			return new AnyMeasure((org.hl7.fhir.r4.model.Measure) theMeasure);
		} else {
			throw new UnsupportedOperationException("Cannot convert " + theMeasure.getClass().getName() + " to AnyList");
		}
	}

	public IBaseResource get() {
		return myMeasure;
	}

	public org.hl7.fhir.dstu3.model.Measure getDstu3() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.DSTU3);
		return (org.hl7.fhir.dstu3.model.Measure) get();
	}

	public org.hl7.fhir.r4.model.Measure getR4() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.R4);
		return (org.hl7.fhir.r4.model.Measure) get();
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

	public void addType(String theSystem, String theCode) {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.CodeableConcept codeableConcept = new org.hl7.fhir.dstu3.model.CodeableConcept();
				codeableConcept.addCoding().setSystem(theSystem).setCode(theCode);
				getDstu3().getType().add(codeableConcept);
				break;
			case R4:
				org.hl7.fhir.r4.model.CodeableConcept codeableConceptR4 = new org.hl7.fhir.r4.model.CodeableConcept();
				codeableConceptR4.addCoding().setSystem(theSystem).setCode(theCode);
				getR4().getType().add(codeableConceptR4);
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

	public String getIdentifierFirstRep() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getIdentifierFirstRep().getValue();
			case R4:
				return getR4().getIdentifierFirstRep().getValue();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setComposedOf(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getRelatedArtifactDstu3(theReferenceId, org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.COMPOSEDOF);
				break;
			case R4:
				getRelatedArtifactR4(theReferenceId, org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.COMPOSEDOF);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	private void getRelatedArtifactDstu3(String theReferenceId, org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType theArtifactType) {
		org.hl7.fhir.dstu3.model.RelatedArtifact artifact = new org.hl7.fhir.dstu3.model.RelatedArtifact();
		artifact.setType(theArtifactType);
		artifact.setResource(new org.hl7.fhir.dstu3.model.Reference(theReferenceId));
		getDstu3().getRelatedArtifact().add(artifact);
	}

	private void getRelatedArtifactR4(String theReferenceId, org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType theArtifactType) {
		org.hl7.fhir.r4.model.RelatedArtifact artifact = new org.hl7.fhir.r4.model.RelatedArtifact();
		artifact.setType(theArtifactType);
		artifact.setResource(theReferenceId);
		getR4().getRelatedArtifact().add(artifact);
	}

	public IBaseReference getComposedOf() {
		switch (myFhirVersion) {
			case DSTU3:
				return getArtifactOfTypeDstu3(getDstu3(), org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.COMPOSEDOF);
			case R4:
				return getArtifactOfTypeR4(getR4(), org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.COMPOSEDOF);
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setPredecessor(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getRelatedArtifactDstu3(theReferenceId, org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.PREDECESSOR);
				break;
			case R4:
				getRelatedArtifactR4(theReferenceId, org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.PREDECESSOR);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}


	public IBaseReference getPredecessor() {
		switch (myFhirVersion) {
			case DSTU3:
				return getArtifactOfTypeDstu3(getDstu3(), org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.PREDECESSOR);
			case R4:
				return getArtifactOfTypeR4(getR4(), org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.PREDECESSOR);
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public IBaseReference getDerivedFrom() {
		switch (myFhirVersion) {
			case DSTU3:
				return getArtifactOfTypeDstu3(getDstu3(), org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.DERIVEDFROM);
			case R4:
				return getArtifactOfTypeR4(getR4(), org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.DERIVEDFROM);
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setDerivedFrom(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getRelatedArtifactDstu3(theReferenceId, org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.DERIVEDFROM);
				break;
			case R4:
				getRelatedArtifactR4(theReferenceId, org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.DERIVEDFROM);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public IBaseReference getSuccessor() {
		switch (myFhirVersion) {
			case DSTU3:
				return getArtifactOfTypeDstu3(getDstu3(), org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.SUCCESSOR);
			case R4:
				return getArtifactOfTypeR4(getR4(), org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.SUCCESSOR);
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setSuccessor(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				getRelatedArtifactDstu3(theReferenceId, org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.SUCCESSOR);
				break;
			case R4:
				getRelatedArtifactR4(theReferenceId, org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType.SUCCESSOR);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	private IBaseReference getArtifactOfTypeDstu3(org.hl7.fhir.dstu3.model.Measure theMeasure, org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType theType) {
		return theMeasure.getRelatedArtifact()
			.stream()
			.filter(artifact -> theType == artifact.getType())
			.map(org.hl7.fhir.dstu3.model.RelatedArtifact::getResource)
			.findFirst()
			.get();
	}

	private IBaseReference getArtifactOfTypeR4(org.hl7.fhir.r4.model.Measure theMeasure, org.hl7.fhir.r4.model.RelatedArtifact.RelatedArtifactType theType) {
		return new org.hl7.fhir.r4.model.Reference(theMeasure.getRelatedArtifact()
			.stream()
			.filter(artifact -> theType == artifact.getType())
			.map(org.hl7.fhir.r4.model.RelatedArtifact::getResource)
			.findFirst()
			.get());
	}

	public void setPublisher(String thePublisher) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().setPublisher(thePublisher);
				break;
			case R4:
				getR4().setPublisher(thePublisher);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getPublisher() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getPublisher();
			case R4:
				return getR4().getPublisher();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setName(String theName) {
		switch (myFhirVersion) {
			case DSTU3:
				getDstu3().setName(theName);
				break;
			case R4:
				getR4().setName(theName);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getName() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getName();
			case R4:
				return getR4().getName();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setEffectivePeriod(Date start, Date end) {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Period effectivePeriod = new org.hl7.fhir.dstu3.model.Period();
				effectivePeriod.setStart(start);
				effectivePeriod.setEnd(end);
				getDstu3().setEffectivePeriod(effectivePeriod);
				break;
			case R4:
				org.hl7.fhir.r4.model.Period effectivePeriodr4 = new org.hl7.fhir.r4.model.Period();
				effectivePeriodr4.setStart(start);
				effectivePeriodr4.setEnd(end);
				getR4().setEffectivePeriod(effectivePeriodr4);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public Date getEffectivePeriodStart() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getEffectivePeriod().getStart();
			case R4:
				return getR4().getEffectivePeriod().getStart();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public Date getEffectivePeriodEnd() {
		switch (myFhirVersion) {
			case DSTU3:
				return getDstu3().getEffectivePeriod().getEnd();
			case R4:
				return getR4().getEffectivePeriod().getEnd();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setTopics(List<TokenParam> theTokenParamList) {
		switch (myFhirVersion) {
			case DSTU3:
				setTopicsDstu3(theTokenParamList);
				break;
			case R4:
				setTopicsR4(theTokenParamList);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	private void setTopicsDstu3(List<TokenParam> theTokenParamList) {
		List<org.hl7.fhir.dstu3.model.CodeableConcept> topicList = new ArrayList<>();

		for (TokenParam tokenParam : theTokenParamList) {
			org.hl7.fhir.dstu3.model.CodeableConcept codeableConcept = new org.hl7.fhir.dstu3.model.CodeableConcept();
			codeableConcept.addCoding().setSystem(tokenParam.getSystem()).setCode(tokenParam.getValue());
			topicList.add(codeableConcept);
		}
		getDstu3().setTopic(topicList);
	}

	private void setTopicsR4(List<TokenParam> theTokenParamList) {
		List<org.hl7.fhir.r4.model.CodeableConcept> topicList = new ArrayList<>();

		for (TokenParam tokenParam : theTokenParamList) {
			org.hl7.fhir.r4.model.CodeableConcept codeableConcept = new org.hl7.fhir.r4.model.CodeableConcept();
			codeableConcept.addCoding().setSystem(tokenParam.getSystem()).setCode(tokenParam.getValue());
			topicList.add(codeableConcept);
		}
		getR4().setTopic(topicList);
	}

	public TokenParam getTopicFirstRep() {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Coding codingDstu3 = getDstu3().getTopicFirstRep().getCodingFirstRep();
				return new TokenParam(codingDstu3.getSystem(), codingDstu3.getCode());
			case R4:
				org.hl7.fhir.r4.model.Coding codingR4 = getR4().getTopicFirstRep().getCodingFirstRep();
				return new TokenParam(codingR4.getSystem(), codingR4.getCode());
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public TokenParam getTopicSecondRepOrNull() {
		switch (myFhirVersion) {
			case DSTU3:
				if (getDstu3().getTopic().size() < 2) {
					return null;
				}
				org.hl7.fhir.dstu3.model.Coding codingDstu3 = getDstu3().getTopic().get(1).getCodingFirstRep();
				return new TokenParam(codingDstu3.getSystem(), codingDstu3.getCode());
			case R4:
				if (getR4().getTopic().size() < 2) {
					return null;
				}
				org.hl7.fhir.r4.model.Coding codingR4 = getR4().getTopic().get(1).getCodingFirstRep();
				return new TokenParam(codingR4.getSystem(), codingR4.getCode());
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}
}
