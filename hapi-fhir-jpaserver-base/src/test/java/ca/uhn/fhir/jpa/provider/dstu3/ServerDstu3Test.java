package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class ServerDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerDstu3Test.class);

	
	
	/**
	 * See #519
	 */
	@Test
	public void saveIdParamOnlyAppearsOnce() throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/metadata?_pretty=true&_format=xml");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			ourLog.info(resp.toString());
			assertEquals(200, resp.getStatusLine().getStatusCode());
			
			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(respString);
			
			CapabilityStatement cs = myFhirCtx.newXmlParser().parseResource(CapabilityStatement.class, respString);
			
			for (CapabilityStatementRestResourceComponent nextResource : cs.getRest().get(0).getResource()) {
				ourLog.info("Testing resource: " + nextResource.getType());
				Set<String> sps = new HashSet<String>();
				for (CapabilityStatementRestResourceSearchParamComponent nextSp : nextResource.getSearchParam()) {
					if (sps.add(nextSp.getName()) == false) {
						fail("Duplicate search parameter " + nextSp.getName() + " for resource " + nextResource.getType());
					}
				}
				
				if (!sps.contains("_id")) {
					fail("No search parameter _id for resource " + nextResource.getType());
				}
			}			
		} finally {
		IOUtils.closeQuietly(resp.getEntity().getContent());
		}
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
