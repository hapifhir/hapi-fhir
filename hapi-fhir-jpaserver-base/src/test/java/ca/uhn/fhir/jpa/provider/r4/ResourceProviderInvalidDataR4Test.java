package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.GZipUtil;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ResourceProviderInvalidDataR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderInvalidDataR4Test.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		ourRestServer.getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testRetrieveDataSavedWithInvalidDecimal() throws IOException {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.setValue(new Quantity().setValue(100).setCode("km"));
		Long id = myObservationDao.create(obs).getId().getIdPartAsLong();

		// Manually set the value to be an invalid decimal number
		runInTransaction(() -> {
			ResourceHistoryTable resVer = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id, 1);
			byte[] bytesCompressed = resVer.getResource();
			String resourceText = GZipUtil.decompress(bytesCompressed);
			resourceText = resourceText.replace("100", "-.100");
			bytesCompressed = GZipUtil.compress(resourceText);
			resVer.setResource(bytesCompressed);
			myResourceHistoryTableDao.save(resVer);
		});

		HttpGet httpGet = new HttpGet(ourServerBase + "/Observation/" + id);
		httpGet.addHeader("Accept", "application/fhir+json");
		try (CloseableHttpResponse status = ourHttpClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response content: " + responseContent);
			assertThat(responseContent, containsString("\"value\":-0.100"));
		}
	}

}
