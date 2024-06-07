package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CorsR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CorsR4Test.class);

	@Test
	public void saveLocalOrigin() throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/Patient?name=test");
		get.addHeader("Origin", "file://");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		
		ourLog.info(resp.toString());
		
		IOUtils.closeQuietly(resp.getEntity().getContent());
		assertEquals(200, resp.getStatusLine().getStatusCode());
	}



}
