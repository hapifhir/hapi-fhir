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
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class AnyBundle {
	private final FhirVersionEnum myFhirVersion;
	private final IBaseBundle myBundle;

	public static AnyBundle fromFhirContext(FhirContext theFhirContext) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		switch (version) {
			case DSTU2:
				return new AnyBundle(new ca.uhn.fhir.model.dstu2.resource.Bundle());
			case DSTU3:
				return new AnyBundle(new org.hl7.fhir.dstu3.model.Bundle());
			case R4:
				return new AnyBundle(new org.hl7.fhir.r4.model.Bundle());
			default:
				throw new UnsupportedOperationException(version + " not supported");
		}
	}

	public AnyBundle(ca.uhn.fhir.model.dstu2.resource.Bundle theBundleR2) {
		myFhirVersion = FhirVersionEnum.DSTU2;
		myBundle = theBundleR2;
	}

	public AnyBundle(org.hl7.fhir.dstu3.model.Bundle theBundleR3) {
		myFhirVersion = FhirVersionEnum.DSTU3;
		myBundle = theBundleR3;
	}

	public AnyBundle(org.hl7.fhir.r4.model.Bundle theBundleR4) {
		myFhirVersion = FhirVersionEnum.R4;
		myBundle = theBundleR4;
	}

	public static AnyBundle fromResource(IBaseResource theBundle) {
		if (theBundle instanceof ca.uhn.fhir.model.dstu2.resource.Bundle) {
			return new AnyBundle((ca.uhn.fhir.model.dstu2.resource.Bundle) theBundle);
		} else if (theBundle instanceof org.hl7.fhir.dstu3.model.Bundle) {
			return new AnyBundle((org.hl7.fhir.dstu3.model.Bundle) theBundle);
		} else if (theBundle instanceof org.hl7.fhir.r4.model.Bundle) {
			return new AnyBundle((org.hl7.fhir.r4.model.Bundle) theBundle);
		} else {
			throw new UnsupportedOperationException("Cannot convert " + theBundle.getClass().getName() + " to AnyBundle");
		}
	}

	public IBaseBundle get() {
		return myBundle;
	}

	public ca.uhn.fhir.model.dstu2.resource.Bundle getDstu2() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.DSTU2);
		return (ca.uhn.fhir.model.dstu2.resource.Bundle) get();
	}

	public org.hl7.fhir.dstu3.model.Bundle getDstu3() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.DSTU3);
		return (org.hl7.fhir.dstu3.model.Bundle) get();
	}

	public org.hl7.fhir.r4.model.Bundle getR4() {
		Validate.isTrue(myFhirVersion == FhirVersionEnum.R4);
		return (org.hl7.fhir.r4.model.Bundle) get();
	}

	public void addResource(IBaseResource theResource) {
		switch (myFhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent entry = new org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent();
				entry.setResource((org.hl7.fhir.dstu3.model.Resource) theResource);
				getDstu3().getEntry().add(entry);
				break;
			case R4:
				org.hl7.fhir.r4.model.Bundle.BundleEntryComponent entryr4 = new org.hl7.fhir.r4.model.Bundle.BundleEntryComponent();
				entryr4.setResource((org.hl7.fhir.r4.model.Resource) theResource);
				getR4().getEntry().add(entryr4);
				break;
			default:
				throw new UnsupportedOperationException(myFhirVersion + " not supported");
		}

	}
}
