package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.packages.loader.PackageLoaderSvc;
import org.hl7.fhir.utilities.npm.PackageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PackageLoaderConfig {

	@Bean
	public PackageLoaderSvc packageLoaderSvc() {
		PackageLoaderSvc svc = new PackageLoaderSvc();
		svc.getPackageServers().clear();
		svc.getPackageServers().add(PackageClient.PRIMARY_SERVER);
		svc.getPackageServers().add(PackageClient.SECONDARY_SERVER);
		return svc;
	}
}
