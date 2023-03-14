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

import ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionService;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionService;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireService;
import ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class CrR4Config extends BaseClinicalReasoningConfig {

	// Measure

	@Bean
	public Function<RequestDetails, MeasureService> r4MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService r4measureService() {
		return new MeasureService();
	}

	@Bean
	public MeasureOperationsProvider r4measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	// ActivityDefinition

	@Bean
	public Function<RequestDetails, ActivityDefinitionService> r4ActivityDefinitionServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(ActivityDefinitionService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public ActivityDefinitionService r4ActivityDefinitionService() {
		return new ActivityDefinitionService();
	}

	@Bean
	public ActivityDefinitionOperationsProvider r4ActivityDefinitionOperationsProvider() {
		return new ActivityDefinitionOperationsProvider();
	}

	// PlanDefinition

	@Bean
	public Function<RequestDetails, PlanDefinitionService> r4PlanDefinitionServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(PlanDefinitionService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public PlanDefinitionService r4PlanDefinitionService() {
		return new PlanDefinitionService();
	}

	@Bean
	public PlanDefinitionOperationsProvider r4PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}

	// Questionnaire

	@Bean
	public Function<RequestDetails, QuestionnaireService> r4QuestionnaireServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(QuestionnaireService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public QuestionnaireService r4QuestionnaireService() {
		return new QuestionnaireService();
	}

	@Bean
	public QuestionnaireOperationsProvider r4QuestionnaireOperationsProvider() {
		return new QuestionnaireOperationsProvider();
	}

	// QuestionnaireResponse

	@Bean
	public Function<RequestDetails, QuestionnaireResponseService> r4QuestionnaireResponseServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var service = theApplicationContext.getBean(QuestionnaireResponseService.class);
			service.setRequestDetails(r);
			return service;
		};
	}

	@Bean
	@Scope("prototype")
	public QuestionnaireResponseService r4QuestionnaireResponseService() {
		return new QuestionnaireResponseService();
	}

	@Bean
	public QuestionnaireResponseOperationsProvider r4QuestionnaireResponseOperationsProvider() {
		return new QuestionnaireResponseOperationsProvider();
	}
}
