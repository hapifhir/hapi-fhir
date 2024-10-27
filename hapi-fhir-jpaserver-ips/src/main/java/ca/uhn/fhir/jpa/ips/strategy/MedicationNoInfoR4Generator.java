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
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Reference;

public class MedicationNoInfoR4Generator implements INoInfoGenerator {
	@Override
	public IBaseResource generate(IIdType theSubjectId) {
		MedicationStatement medication = new MedicationStatement();
		// setMedicationCodeableConcept is not available
		medication
				.setMedication(new CodeableConcept()
						.addCoding(new Coding()
								.setCode("no-medication-info")
								.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
								.setDisplay("No information about medications")))
				.setSubject(new Reference(theSubjectId))
				.setStatus(MedicationStatement.MedicationStatementStatus.UNKNOWN);
		return medication;
	}
}
