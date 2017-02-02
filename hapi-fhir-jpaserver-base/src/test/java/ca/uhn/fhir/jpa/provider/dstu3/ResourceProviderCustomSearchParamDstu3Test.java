package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;

public class ResourceProviderCustomSearchParamDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderCustomSearchParamDstu3Test.class);

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.setDefaultSearchParamsCanBeOverridden(new DaoConfig().isDefaultSearchParamsCanBeOverridden());
	}

	@Override
	public void before() throws Exception {
		super.before();
	}

	@Test
	public void saveCreateSearchParamInvalidWithMissingStatus() throws IOException {
		SearchParameter sp = new SearchParameter();
		sp.setCode("foo");
		sp.setXpath("Patient.gender");
		sp.setXpathUsage(XPathUsageType.NORMAL);
		sp.setTitle("Foo Param");

		try {
			ourClient.create().resource(sp).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("", e.getMessage());
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
