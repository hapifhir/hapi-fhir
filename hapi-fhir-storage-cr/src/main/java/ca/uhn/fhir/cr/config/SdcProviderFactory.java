/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SdcProviderFactory {
	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private ApplicationContext myApplicationContext;

	private Object notSupported() {
		throw new ConfigurationException(Msg.code(2319) + "SDC is not supported for FHIR version " + myFhirContext.getVersion().getVersion());
	}

	public Object getQuestionnaireOperationsProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				return myApplicationContext.getBean(ca.uhn.fhir.cr.dstu3.questionnaire.QuestionnaireOperationsProvider.class);
			case R4:
				return myApplicationContext.getBean(ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireOperationsProvider.class);
			default:
				return notSupported();
		}
	}

	public Object getQuestionnaireResponseOperationsProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				return myApplicationContext.getBean(ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseOperationsProvider.class);
			case R4:
				return myApplicationContext.getBean(ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseOperationsProvider.class);
			default:
				return notSupported();
		}
	}
}
