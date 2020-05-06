package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.helper.EmpiHelperConfig;
import ca.uhn.fhir.jpa.empi.helper.EmpiHelperR4;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static ca.uhn.fhir.empi.api.EmpiConstants.CODE_HAPI_EMPI_MANAGED;
import static ca.uhn.fhir.empi.api.EmpiConstants.SYSTEM_EMPI_MANAGED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {EmpiHelperConfig.class})
@TestPropertySource(properties = {
	"empi.prevent_eid_updates=true"
})
public class EmpiStorageInterceptorStrictEidModeTest extends BaseEmpiR4Test {

	private static final Logger ourLog = getLogger(EmpiStorageInterceptorStrictEidModeTest.class);

	/**
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("empi.prevent", "true");
	}

	@AfterClass
	public static void afterClass() {
		System.setProperty("empi.strict_mode", "false");
	}*/

	@Rule
	@Autowired
	public EmpiHelperR4 myEmpiHelper;

	@Test
	public void testStrictEidModeForbidsUpdatesToEidsOnTargets() throws InterruptedException {
		Patient jane = addExternalEID(buildJanePatient(), "some_eid");
		EmpiHelperR4.OutcomeAndLogMessageWrapper latch = myEmpiHelper.createWithLatch(jane);
		jane.setId(latch.getDaoMethodOutcome().getId());
		clearExternalEIDs(jane);
		jane = addExternalEID(jane, "some_new_eid");
		try {
			myEmpiHelper.doUpdateResource(jane, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), is(equalTo("While running in stric EID mode, EIDs may not be updated on Patient/Practitioner resources")));
		}
	}
}
