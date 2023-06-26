package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
@Import(TestCrConfig.class)
public class TestCrDstu3Config {
	@Bean
	public Function<RequestDetails, MeasureService> dstu3MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	static class CqlTranslatorOptionsHelper {
		void setCqlCompatibilityLevelTo13(CqlTranslatorOptions theCqlTranslatorOptions) {
			theCqlTranslatorOptions.setCompatibilityLevel("1.3");
		}
	}

	@Bean
	CqlTranslatorOptionsHelper setTranslatorOptionsForTest(CqlTranslatorOptions theCqlTranslatorOptions) {
		var helper = new CqlTranslatorOptionsHelper();
		helper.setCqlCompatibilityLevelTo13(theCqlTranslatorOptions);
		return helper;
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
}
