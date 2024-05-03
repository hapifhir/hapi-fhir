/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import org.springframework.beans.factory.annotation.Autowired;

public class ValueSetConceptAccumulatorFactory {

	@Autowired
	private ITermValueSetDao myValueSetDao;

	@Autowired
	private ITermValueSetConceptDao myValueSetConceptDao;

	@Autowired
	private ITermValueSetConceptDesignationDao myValueSetConceptDesignationDao;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	public ValueSetConceptAccumulator create(TermValueSet theTermValueSet) {
		ValueSetConceptAccumulator valueSetConceptAccumulator = new ValueSetConceptAccumulator(
				theTermValueSet, myValueSetDao, myValueSetConceptDao, myValueSetConceptDesignationDao);

		valueSetConceptAccumulator.setSupportLegacyLob(myStorageSettings.isWriteToLegacyLobColumns());

		return valueSetConceptAccumulator;
	}
}
