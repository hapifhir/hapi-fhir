package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RuntimeSearchParamCacheTest {

	private RuntimeSearchParamCache myCache;

	@BeforeEach
	public void before() {
		myCache = new RuntimeSearchParamCache();
	}

	@Test
	public void remove_anExistingSP_removesAllInstances() {
		// setup
		RuntimeSearchParam rtsp = new RuntimeSearchParam(
			null,
			"http://example.com",
			"name",
			"description",
			"Patient.name",
			RestSearchParameterTypeEnum.STRING,
			null,
			Set.of("Patient"),
			RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE,
			Set.of("Patient")
		);

		// set our sp
		myCache.add("Patient", rtsp.getName(), rtsp);

		// make sure it's there
		{
			// by name
			RuntimeSearchParam param = myCache.get("Patient", "name");
			assertNotNull(param);
			assertEquals(rtsp.getName(), param.getName());
			assertEquals(rtsp.getUri(), param.getUri());
		}
		{
			// by uri
			RuntimeSearchParam param = myCache.getByUrl(rtsp.getUri());
			assertNotNull(param);
			assertEquals(rtsp.getName(), param.getName());
			assertEquals(rtsp.getUri(), param.getUri());
		}

		// test
		// first remove
		myCache.remove("Patient", rtsp.getName(), rtsp.getUri());

		// verify
		// by name
		assertNull(myCache.get("Patient", rtsp.getName()));
		// by url
		assertNull(myCache.getByUrl(rtsp.getUri()));
	}
}
