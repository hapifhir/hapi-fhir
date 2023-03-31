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

import ca.uhn.fhir.cr.r4.IQuestionnaireProcessorFactory;
import ca.uhn.fhir.cr.r4.IQuestionnaireResponseProcessorFactory;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor;
import org.opencds.cqf.cql.evaluator.questionnaireresponse.r4.QuestionnaireResponseProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides the $populate and $extract operations from the SDC IG.
 */
@Configuration
public class SdcR4Config extends BaseSdcConfig {
	@Bean
	IQuestionnaireProcessorFactory r4QuestionnaireProcessorFactory() {
		return r -> new QuestionnaireProcessor(r);
	}

	@Bean
	public QuestionnaireOperationsProvider r4QuestionnaireOperationsProvider() {
		return new QuestionnaireOperationsProvider();
	}

	@Bean
	IQuestionnaireResponseProcessorFactory r4QuestionnaireResponseProcessorFactory() {
		return r -> new QuestionnaireResponseProcessor(r);
	}

	@Bean
	public QuestionnaireResponseOperationsProvider r4QuestionnaireResponseOperationsProvider() {
		return new QuestionnaireResponseOperationsProvider();
	}
}
