package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.cr.common.CqlConfig;
import ca.uhn.fhir.cr.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.service.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.opencds.cqf.cql.evaluator.spring.fhir.adapter.AdapterConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Import(CqlConfig.class)
@Configuration
public class CrR4Config {
	@Bean
	public CrProperties crProperties() {
		return new CrProperties();
	}

	@Bean
	public MeasureEvaluationOptions measureEvaluationOptions() {
		return crProperties().getMeasureEvaluation();
	}

	@Bean
	public MeasureOperationsProvider measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	@Bean
	public Function<RequestDetails, MeasureService> r4MeasureServiceFactory() {
		return r -> {
			var ms = r4measureService();
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService r4measureService() {
		return new MeasureService();
	}
}
