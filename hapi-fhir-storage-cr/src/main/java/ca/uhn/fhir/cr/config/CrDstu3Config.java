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

import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.cr.dstu3.activitydefinition.ActivityDefinitionOperationsProvider;
import ca.uhn.fhir.cr.dstu3.activitydefinition.ActivityDefinitionService;
import ca.uhn.fhir.cr.dstu3.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.cr.dstu3.plandefinition.PlanDefinitionService;
import ca.uhn.fhir.cr.dstu3.questionnaire.QuestionnaireOperationsProvider;
import ca.uhn.fhir.cr.dstu3.questionnaire.QuestionnaireService;
import ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class CrDstu3Config extends BaseClinicalReasoningConfig {

	// Measure

	@Bean
	public Function<RequestDetails, MeasureService> dstu3MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService dstu3measureService() {
		return new MeasureService();
	}

	@Bean
	public MeasureOperationsProvider dstu3measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	// ActivityDefinition

	@Bean
	public Function<RequestDetails, ActivityDefinitionService> dstu3ActivityDefinitionServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(ActivityDefinitionService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public ActivityDefinitionService dstu3ActivityDefinitionService() {
		return new ActivityDefinitionService();
	}

	@Bean
	public ActivityDefinitionOperationsProvider dstu3ActivityDefinitionOperationsProvider() {
		return new ActivityDefinitionOperationsProvider();
	}

	// PlanDefinition

	@Bean
	public Function<RequestDetails, PlanDefinitionService> dstu3PlanDefinitionServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(PlanDefinitionService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public PlanDefinitionService dstu3PlanDefinitionService() {
		return new PlanDefinitionService();
	}

	@Bean
	public PlanDefinitionOperationsProvider dstu3PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}

	// Questionnaire

	@Bean
	public Function<RequestDetails, QuestionnaireService> dstu3QuestionnaireServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(QuestionnaireService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public QuestionnaireService dstu3QuestionnaireService() {
		return new QuestionnaireService();
	}

	@Bean
	public QuestionnaireOperationsProvider dstu3QuestionnaireOperationsProvider() {
		return new QuestionnaireOperationsProvider();
	}

	// QuestionnaireResponse

	@Bean
	public Function<RequestDetails, QuestionnaireResponseService> dstu3QuestionnaireResponseServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(QuestionnaireResponseService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public QuestionnaireResponseService dstu3QuestionnaireResponseService() {
		return new QuestionnaireResponseService();
	}

	@Bean
	public QuestionnaireResponseOperationsProvider dstu3QuestionnaireResponseOperationsProvider() {
		return new QuestionnaireResponseOperationsProvider();
	}
}
