package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorsDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CorsDstu3Test.class);

	@Test
	public void saveLocalOrigin() throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/Patient?name=test");
		get.addHeader(Constants.HEADER_CORS_ORIGIN, "file://");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		
		ourLog.info(resp.toString());
		
		IOUtils.closeQuietly(resp.getEntity().getContent());
		assertEquals(200, resp.getStatusLine().getStatusCode());
	}

}
