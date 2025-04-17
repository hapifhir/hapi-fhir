/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.common.RepositoryFactoryForRepositoryInterface;
import ca.uhn.fhir.cr.common.StringTimePeriodHandler;
import ca.uhn.fhir.cr.config.CrBaseConfig;
import ca.uhn.fhir.cr.config.ProviderLoader;
import ca.uhn.fhir.cr.config.ProviderSelector;
import ca.uhn.fhir.cr.config.RepositoryConfig;
import ca.uhn.fhir.cr.r4.ICareGapsServiceFactory;
import ca.uhn.fhir.cr.r4.ICollectDataServiceFactory;
import ca.uhn.fhir.cr.r4.ICqlExecutionServiceFactory;
import ca.uhn.fhir.cr.r4.IDataRequirementsServiceFactory;
import ca.uhn.fhir.cr.r4.IMeasureServiceFactory;
import ca.uhn.fhir.cr.r4.ISubmitDataProcessorFactory;
import ca.uhn.fhir.cr.r4.cpg.CqlExecutionOperationProvider;
import ca.uhn.fhir.cr.r4.measure.CareGapsOperationProvider;
import ca.uhn.fhir.cr.r4.measure.CollectDataOperationProvider;
import ca.uhn.fhir.cr.r4.measure.DataRequirementsOperationProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.measure.SubmitDataProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.opencds.cqf.fhir.cr.cpg.r4.R4CqlExecutionService;
import org.opencds.cqf.fhir.cr.measure.CareGapsProperties;
import org.opencds.cqf.fhir.cr.measure.MeasureEvaluationOptions;
import org.opencds.cqf.fhir.cr.measure.common.MeasurePeriodValidator;
import org.opencds.cqf.fhir.cr.measure.r4.R4CareGapsService;
import org.opencds.cqf.fhir.cr.measure.r4.R4CollectDataService;
import org.opencds.cqf.fhir.cr.measure.r4.R4DataRequirementsService;
import org.opencds.cqf.fhir.cr.measure.r4.R4MeasureService;
import org.opencds.cqf.fhir.cr.measure.r4.R4SubmitDataService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Map;

@Configuration
@Import({RepositoryConfig.class, CrBaseConfig.class})
public class CrR4Config {

	@Bean
	IMeasureServiceFactory r4MeasureServiceFactory(
			RepositoryFactoryForRepositoryInterface theRepositoryFactory,
			MeasureEvaluationOptions theEvaluationOptions,
			MeasurePeriodValidator theMeasurePeriodValidator) {
		return rd ->
				new R4MeasureService(theRepositoryFactory.create(rd), theEvaluationOptions, theMeasurePeriodValidator);
	}

	@Bean
	ISubmitDataProcessorFactory r4SubmitDataProcessorFactory(IRepositoryFactory theRepositoryFactory) {
		return rd -> new R4SubmitDataService(theRepositoryFactory.create(rd));
	}

	@Bean
	ICqlExecutionServiceFactory r4CqlExecutionServiceFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new R4CqlExecutionService(theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	CqlExecutionOperationProvider r4CqlExecutionOperationProvider() {
		return new CqlExecutionOperationProvider();
	}

	@Bean
	CollectDataOperationProvider r4CollectDataOperationProvider(
			ICollectDataServiceFactory theR4CollectDataServiceFactory,
			StringTimePeriodHandler theStringTimePeriodHandler) {
		return new CollectDataOperationProvider(theR4CollectDataServiceFactory, theStringTimePeriodHandler);
	}

	@Bean
	ICollectDataServiceFactory collectDataServiceFactory(
			IRepositoryFactory theRepositoryFactory, MeasureEvaluationOptions theMeasureEvaluationOptions) {
		return rd -> new R4CollectDataService(theRepositoryFactory.create(rd), theMeasureEvaluationOptions);
	}

	@Bean
	DataRequirementsOperationProvider r4DataRequirementsOperationProvider() {
		return new DataRequirementsOperationProvider();
	}

	@Bean
	IDataRequirementsServiceFactory dataRequirementsServiceFactory(
			IRepositoryFactory theRepositoryFactory, MeasureEvaluationOptions theMeasureEvaluationOptions) {
		return rd -> new R4DataRequirementsService(theRepositoryFactory.create(rd), theMeasureEvaluationOptions);
	}

	@Bean
	ICareGapsServiceFactory careGapsServiceFactory(
			IRepositoryFactory theRepositoryFactory,
			CareGapsProperties theCareGapsProperties,
			MeasureEvaluationOptions theMeasureEvaluationOptions,
			MeasurePeriodValidator theMeasurePeriodValidator) {
		return rd -> new R4CareGapsService(
				theCareGapsProperties,
				theRepositoryFactory.create(rd),
				theMeasureEvaluationOptions,
				rd.getFhirServerBase(),
				theMeasurePeriodValidator);
	}

	@Bean
	CareGapsOperationProvider r4CareGapsOperationProvider(
			ICareGapsServiceFactory theR4CareGapsProcessorFactory, StringTimePeriodHandler theStringTimePeriodHandler) {
		return new CareGapsOperationProvider(theR4CareGapsProcessorFactory, theStringTimePeriodHandler);
	}

	@Bean
	SubmitDataProvider r4SubmitDataProvider() {
		return new SubmitDataProvider();
	}

	@Bean
	MeasureOperationsProvider r4MeasureOperationsProvider(
			IMeasureServiceFactory theR4MeasureServiceFactory, StringTimePeriodHandler theStringTimePeriodHandler) {
		return new MeasureOperationsProvider(theR4MeasureServiceFactory, theStringTimePeriodHandler);
	}

	@Bean
	public ProviderLoader r4PdLoader(
			ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {

		var selector = new ProviderSelector(
				theFhirContext,
				Map.of(
						FhirVersionEnum.R4,
						Arrays.asList(
								MeasureOperationsProvider.class,
								SubmitDataProvider.class,
								CareGapsOperationProvider.class,
								CqlExecutionOperationProvider.class,
								CollectDataOperationProvider.class,
								DataRequirementsOperationProvider.class)));

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
