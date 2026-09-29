package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.dao.JpaPidFk;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderInvalidDataR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderInvalidDataR4Test.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myServer.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testRetrieveDataSavedWithInvalidDecimal() {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.setValue(new Quantity().setValue(100).setCode("km"));
		Long id = myObservationDao.create(obs).getId().getIdPartAsLong();

		// Manually set the value to be an invalid decimal number
		runInTransaction(() -> {
			ResourceHistoryTable resVer = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(id), 1);
			String resourceText = resVer.getResourceTextVc();
			resourceText = resourceText.replace("100", "-.100");
			resVer.setResourceTextVc(resourceText);
			myResourceHistoryTableDao.save(resVer);
		});

		String responseContent = myServer.fhirRequest("/Observation/" + id)
			.withHeader("Accept", "application/fhir+json")
			.get()
			.getBody();
		ourLog.info("Response content: " + responseContent);
		assertThat(responseContent).contains("\"value\":-0.100");
	}

}
