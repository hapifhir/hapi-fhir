package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.config.CaptureQueriesListener;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.r4.model.Subscription.SubscriptionStatus;
import org.junit.*;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"unchecked", "Duplicates"})
public class FhirResourceDaoR4FilterTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4FilterTest.class);

	@After
	public void afterResetSearchSize() {
	}

	@Before
	public void beforeDisableCacheReuse() {
	}

	@Test
	public void testMalformedFilter() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith))"));
		try {
			myPatientDao.search(map);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	public void testChainWithMultipleTypePossibilities() {

		Patient p= new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(name eq smith) or (name eq jones)"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1,id2));

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
