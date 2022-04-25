package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportHelperService;
import ca.uhn.fhir.jpa.bulk.export.svc.JpaBulkExportProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaBulkExportConfig {
	@Bean
	public IBulkExportProcessor jpaBulkExportProcessor() {
		return new JpaBulkExportProcessor();
	}

	// see BulkExportMongoConfig
	@Bean
	public BulkExportHelperService bulkExportHelperService() {
		return new BulkExportHelperService();
	}
}
