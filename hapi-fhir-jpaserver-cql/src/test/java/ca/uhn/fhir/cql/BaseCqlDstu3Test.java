package ca.uhn.fhir.cql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.cql.config.CqlDstu3Config;
import ca.uhn.fhir.cql.config.TestCqlConfig;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestCqlConfig.class, SubscriptionProcessorConfig.class, CqlDstu3Config.class})
public class BaseCqlDstu3Test extends BaseJpaDstu3Test implements CqlProviderTestBase {
	public static Logger ourLog = LoggerFactory.getLogger(BaseCqlDstu3Test.class);

	@Autowired
	protected
	DaoRegistry myDaoRegistry;
	@Autowired
	protected
	FhirContext myFhirContext;
	@Autowired
	IFhirSystemDao mySystemDao;
	protected final RequestDetails mySrd = new SystemRequestDetails();
	private final MyTestInterceptor myInterceptor = new MyTestInterceptor();
	@Autowired
	IInterceptorService myIInterceptorService;
	@Autowired
	PartitionSettings myPartitionSettings;

	@BeforeEach
	public void registerInterceptor() {
		myIInterceptorService.registerInterceptor(myInterceptor);
		myPartitionSettings.setPartitioningEnabled(true);
	}

	@AfterEach
	public void unregisterInterceptor() {
		myIInterceptorService.unregisterInterceptor(myInterceptor);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}


	protected int loadDataFromDirectory(String theDirectoryName) throws IOException {
		int count = 0;
		ourLog.info("Reading files in directory: {}", theDirectoryName);
		ClassPathResource dir = new ClassPathResource(theDirectoryName);
		Collection<File> files = FileUtils.listFiles(dir.getFile(), null, false);
		ourLog.info("{} files found.", files.size());
		for (File file : files) {
			String filename = file.getAbsolutePath();
			ourLog.info("Processing filename '{}'", filename);
			if (filename.endsWith(".cql") || filename.contains("expectedresults")) {
				// Ignore .cql and expectedresults files
				ourLog.info("Ignoring file: '{}'", filename);
			} else if (filename.endsWith(".json")) {
				if (filename.contains("bundle")) {
					loadBundle(filename);
				} else {
					loadResource(filename, mySrd);
				}
				count++;
			} else {
				ourLog.info("Ignoring file: '{}'", filename);
			}
		}
		return count;
	}

	protected Bundle parseBundle(String theLocation) throws IOException  {
		String json = stringFromResource(theLocation);
		Bundle bundle = (Bundle) myFhirContext.newJsonParser().parseResource(json);
		return bundle;
	}

	protected Bundle loadBundle(Bundle bundle) {
		return (Bundle) mySystemDao.transaction(mySrd, bundle);
	}

	protected Bundle loadBundle(String theLocation) throws IOException {
		Bundle bundle = parseBundle(theLocation);
		return loadBundle(bundle);
	}

	private static class MyTestInterceptor {
		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public void partitionIdentifyRead(RequestPartitionId theRequestPartitionId, ServletRequestDetails theRequestDetails) {
			if (theRequestDetails == null) {
				ourLog.info("oops");
			}
			assertNotNull(theRequestPartitionId);
			assertNotNull(theRequestDetails);
		}
	}
}
