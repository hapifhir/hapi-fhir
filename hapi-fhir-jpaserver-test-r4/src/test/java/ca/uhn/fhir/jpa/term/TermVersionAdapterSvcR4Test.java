package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.InvalidParameterException;

import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TermVersionAdapterSvcR4Test {

	private final TermVersionAdapterSvcR4 testedClass = new TermVersionAdapterSvcR4();

	@Mock private IFhirResourceDao<CodeSystem> myCodeSystemResourceDao;
	@Mock ServletRequestDetails theRequestDetails;
	@Mock DaoMethodOutcome theDaoMethodOutcome;

	@Test
	void createOrUpdateCodeSystemMustHaveId() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("a-loinc-system");

		InvalidParameterException thrown = assertThrows(
			InvalidParameterException.class,
			() -> testedClass.createOrUpdateCodeSystem(codeSystem, new ServletRequestDetails()));

		assertTrue(thrown.getMessage().contains("'loinc' CodeSystem must have an 'ID' element"));
	}


	@Test
	void createOrUpdateCodeSystemWithIdNoException() {
		ReflectionTestUtils.setField(testedClass, "myCodeSystemResourceDao", myCodeSystemResourceDao);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("a-loinc-system").setId(LOINC_LOW);

		when(myCodeSystemResourceDao.update(codeSystem, theRequestDetails)).thenReturn(theDaoMethodOutcome);

		testedClass.createOrUpdateCodeSystem(codeSystem, theRequestDetails);

		verify(myCodeSystemResourceDao, Mockito.times(1)).update(codeSystem, theRequestDetails);
	}
}
