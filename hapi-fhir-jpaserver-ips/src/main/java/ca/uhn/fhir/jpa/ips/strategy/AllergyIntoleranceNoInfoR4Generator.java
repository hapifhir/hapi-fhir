/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.ips.strategy;

import ca.uhn.fhir.jpa.ips.api.INoInfoGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;

public class AllergyIntoleranceNoInfoR4Generator implements INoInfoGenerator {
	@Override
	public IBaseResource generate(IIdType theSubjectId) {
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.setCode(new CodeableConcept()
						.addCoding(new Coding()
								.setCode("no-allergy-info")
								.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
								.setDisplay("No information about allergies")))
				.setPatient(new Reference(theSubjectId))
				.setClinicalStatus(new CodeableConcept()
						.addCoding(new Coding()
								.setCode("active")
								.setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")));
		return allergy;
	}
}
