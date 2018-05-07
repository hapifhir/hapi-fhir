package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ExtensionConstants;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ServerR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerR4Test.class);


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


	@Test
	public void testMetadataIncludesResourceCounts() {
		Patient p = new Patient();
		p.setActive(true);
		myClient.create().resource(p).execute();

		/*
		 * Initial fetch after a clear should return
		 * no results
		 */
		ourResourceCountsCache.clear();

		CapabilityStatement capabilityStatement = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.execute();

		Extension patientCountExt = capabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals("Patient"))
			.findFirst()
			.orElseThrow(() -> new InternalErrorException("No patient"))
			.getExtensionByUrl(ExtensionConstants.CONF_RESOURCE_COUNT);
		assertNull(patientCountExt);

		/*
		 * Now run a background pass (the update
		 * method is called by the scheduler normally)
		 */
		ourResourceCountsCache.update();

		capabilityStatement = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.execute();

		patientCountExt = capabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals("Patient"))
			.findFirst()
			.orElseThrow(() -> new InternalErrorException("No patient"))
			.getExtensionByUrl(ExtensionConstants.CONF_RESOURCE_COUNT);
		assertEquals("1", patientCountExt.getValueAsPrimitive().getValueAsString());

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
