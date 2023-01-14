package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpsGeneratorSvcImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(IpsGeneratorSvcImplTest.class);
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private DaoRegistry myDaoRegistry = new DaoRegistry(myFhirContext);

	@Test
	public void testGenerateIps() {
		registerResourceDao(
			AllergyIntolerance.class,
			CarePlan.class,
			Condition.class,
			Consent.class,
			ClinicalImpression.class,
			DeviceUseStatement.class,
			DiagnosticReport.class,
			Immunization.class,
			MedicationRequest.class,
			MedicationStatement.class,
			MedicationAdministration.class,
			MedicationDispense.class,
			Observation.class,
			Patient.class,
			Procedure.class
		);

		IIpsGenerationStrategy strategy = new DefaultIpsGenerationStrategy();
		IpsGeneratorSvcImpl svc = new IpsGeneratorSvcImpl(myFhirContext, strategy, myDaoRegistry);

		IBaseBundle outcome = svc.generateIps(new SystemRequestDetails(), new TokenParam("http://foo", "bar"));

		ourLog.info("Generated IPS:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
	}

	private IBundleProvider bundleProviderWithAllOfType(Bundle theSourceData, Class<? extends IBaseResource> theType) {
		List<Resource> resources = theSourceData
			.getEntry()
			.stream()
			.filter(t -> t.getResource() != null && theType.isAssignableFrom(t.getResource().getClass()))
			.map(t -> t.getResource())
			.collect(Collectors.toList());
		return new SimpleBundleProvider(resources);
	}


	@SuppressWarnings("rawtypes")
	@SafeVarargs
	private void registerResourceDao(Class<? extends Resource>... theType) {
		Bundle sourceData = ClasspathUtil.loadCompressedResource(myFhirContext, Bundle.class, "/small-patient-everything.json.gz");

		List<IFhirResourceDao> daos = new ArrayList<>();
		for (var nextType : theType) {
			IFhirResourceDao dao = mock(IFhirResourceDao.class);
			when(dao.getResourceType()).thenReturn(nextType);
			when(dao.search(any(), any())).thenReturn(bundleProviderWithAllOfType(sourceData, nextType));
			daos.add(dao);
		}

		myDaoRegistry.setResourceDaos(daos);
	}

}
