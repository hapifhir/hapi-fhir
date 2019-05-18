package ca.uhn.fhir.jpa.model.any;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

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
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void addStringExtension(String theUrl, String theValue) {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Measure measure = getDstu3();
				measure.addExtension().setUrl(theUrl).setValue(new org.hl7.fhir.dstu3.model.StringType(theValue));
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getStringExtensionValueOrNull(String theUrl) {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Measure measure = getDstu3();
				List<org.hl7.fhir.dstu3.model.Extension> targetTypes = measure.getExtensionsByUrl(theUrl);
				if (targetTypes.size() < 1) {
					return null;
				}
				org.hl7.fhir.dstu3.model.StringType targetType = (org.hl7.fhir.dstu3.model.StringType) targetTypes.get(0).getValue();
				return targetType.getValue();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public String getIdentifierFirstRep() {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Measure measure = getDstu3();
				return measure.getIdentifierFirstRep().getValue();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public void setComposedOf(String theReferenceId) {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Measure measure = getDstu3();
				org.hl7.fhir.dstu3.model.RelatedArtifact artifact = new org.hl7.fhir.dstu3.model.RelatedArtifact();
				artifact.setType(org.hl7.fhir.dstu3.model.RelatedArtifact.RelatedArtifactType.COMPOSEDOF);
				artifact.setResource(new org.hl7.fhir.dstu3.model.Reference(theReferenceId));
				measure.getRelatedArtifact().add(artifact);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}

	public IBaseReference getComposedOf() {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Measure measure = getDstu3();
				return measure.getRelatedArtifactFirstRep().getResource();
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}
	}
}
