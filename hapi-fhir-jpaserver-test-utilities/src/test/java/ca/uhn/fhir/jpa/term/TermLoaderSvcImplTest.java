package ca.uhn.fhir.jpa.term;

/*
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

import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincXmlFileZipContentsHandler;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TermLoaderSvcImplTest {

	@Mock private ITermDeferredStorageSvc theDeferredStorageSvc;
	@Mock private ITermCodeSystemStorageSvc theCodeSystemStorageSvc;

	@Mock private LoadedFileDescriptors theDescriptors;
	@Mock private RequestDetails theRequestDetails;
	@Mock private Properties theUploadProperties;
	@Mock private LoincXmlFileZipContentsHandler myZipContentsHandler;

	private final TermLoaderSvcImpl myTermLoaderSvc = TermLoaderSvcImpl.withoutProxyCheck(
		theDeferredStorageSvc, theCodeSystemStorageSvc);
	private final TermLoaderSvcImpl testedClass = spy(myTermLoaderSvc);

	public static final String versionedLoinXmlsString =
		"<CodeSystem xmlns=\"http://hl7.org/fhir\">" +
			"	<id value=\"loinc\"/>" +
			"	<url value=\"http://loinc.org\"/>" +
			"	<version value=\"2.70\"/>" +
			"</CodeSystem>";


	@Test
	void loadLoincWithoutLoincXmlFileThrows() {
		InvalidRequestException thrown = assertThrows(
			InvalidRequestException.class,
			() -> testedClass.processLoincFiles(
				theDescriptors, theRequestDetails, theUploadProperties, true));

		assertTrue(thrown.getMessage().contains("Did not find loinc.xml in the ZIP distribution."));
	}

	@Test
	void loadLoincWithLoincXmlFileWithVersionThrows() {
		when(testedClass.getLoincXmlFileZipContentsHandler()).thenReturn(myZipContentsHandler);
		when(myZipContentsHandler.getContents()).thenReturn(versionedLoinXmlsString);

		InvalidRequestException thrown = assertThrows(
			InvalidRequestException.class,
			() -> testedClass.processLoincFiles(
				theDescriptors, theRequestDetails, theUploadProperties, true));

		assertTrue(thrown.getMessage().contains("'loinc.xml' file must not have a version defined."));
	}


}
