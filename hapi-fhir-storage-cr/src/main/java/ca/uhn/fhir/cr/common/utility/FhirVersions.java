package ca.uhn.fhir.cr.common.utility;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBase;

import static com.google.common.base.Preconditions.checkNotNull;


public class FhirVersions {

	private FhirVersions() {
	}

	 /**
	 * Returns a FhirVersionEnum for a given BaseType
	 * 
	 * @param <BaseType>       an IBase type
	 * @param theBaseTypeClass the class of the resource to get the version for
	 * @return the FhirVersionEnum corresponding to the theBaseTypeClass
	 */
	public static <BaseType extends IBase> FhirVersionEnum forClass(
			final Class<? extends BaseType> theBaseTypeClass) {
		checkNotNull(theBaseTypeClass);

		String packageName = theBaseTypeClass.getPackage().getName();
		if (packageName.contains("r5")) {
			return FhirVersionEnum.R5;
		} else if (packageName.contains("r4")) {
			return FhirVersionEnum.R4;
		} else if (packageName.contains("dstu3")) {
			return FhirVersionEnum.DSTU3;
		} else if (packageName.contains("dstu2016may")) {
			return FhirVersionEnum.DSTU2_1;
		} else if (packageName.contains("org.hl7.fhir.dstu2")) {
			return FhirVersionEnum.DSTU2_HL7ORG;
		} else if (packageName.contains("ca.uhn.fhir.model.dstu2")) {
			return FhirVersionEnum.DSTU2;
		} else {
			throw new IllegalArgumentException(String.format(
					"Unable to determine FHIR version for IBaseResource type: %s", theBaseTypeClass.getName()));
		}
	}

}
