package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.*;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.lastn.config.TestIntegratedObservationIndexSearchConfig;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestIntegratedObservationIndexSearchConfig.class })
public class FhirResourceDaoR4LastNTest extends BaseJpaTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4LastNTest.class);

	@Autowired
	@Qualifier("myPatientDaoR4")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;

	@Autowired
	@Qualifier("myObservationDaoR4")
	protected IFhirResourceDaoObservation<Observation> myObservationDao;

	@Autowired
	protected DaoConfig myDaoConfig;

	@Autowired
	protected FhirContext myFhirCtx;

	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myPlatformTransactionManager;
	}

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	private ServletRequestDetails mockSrd() {
		return mySrd;
	}

	@Test
	public void testLastN() {
		Patient pt = new Patient();
		pt.addName().setFamily("Lastn").addGiven("Arthur");
		IIdType ptId = myPatientDao.create(pt, mockSrd()).getId().toUnqualifiedVersionless();

		Map<String, String[]> requestParameters = new HashMap<>();
		String[] maxParam = new String[1];
		maxParam[0] = "1";
		requestParameters.put("max", maxParam);
		when(mySrd.getParameters()).thenReturn(requestParameters);

		Map<IIdType, CodeableConcept> observationIds = new HashMap<>();
		for(int observationIdx = 0 ; observationIdx < 20 ; observationIdx++) {
			Calendar observationDate = new GregorianCalendar();
			String idxSuffix = String.valueOf(observationIdx);

			Observation obs = new Observation();
			obs.getText().setDivAsString("<div>OBSTEXT0_" + idxSuffix + "</div>");
			obs.getSubject().setReferenceElement(ptId);
			obs.getCode().addCoding().setCode("CODE_" + idxSuffix).setSystem("http://mycode.com");
			obs.setValue(new StringType("obsvalue0_" + idxSuffix));
			observationDate.add(Calendar.HOUR, -2);
			Date effectiveDtm = observationDate.getTime();
			obs.setEffective(new DateTimeType(effectiveDtm));
			observationIds.put(myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless(), obs.getCode());

			obs = new Observation();
			obs.getText().setDivAsString("<div>OBSTEXT1_" + idxSuffix + "</div>");
			obs.getSubject().setReferenceElement(ptId);
			obs.getCode().addCoding().setCode("CODE_" + idxSuffix).setSystem("http://mycode.com");
			obs.setValue(new StringType("obsvalue1_" + idxSuffix));
			observationDate.add(Calendar.HOUR, -1);
			effectiveDtm = observationDate.getTime();
			obs.setEffective(new DateTimeType(effectiveDtm));
			observationIds.put(myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless(), obs.getCode());

			obs = new Observation();
			obs.getText().setDivAsString("<div>OBSTEXT2_" + idxSuffix + "</div>");
			obs.getSubject().setReferenceElement(ptId);
			obs.getCode().addCoding().setCode("CODE_" + idxSuffix).setSystem("http://mycode.com");
			obs.setValue(new StringType("obsvalue2_" + idxSuffix));
			observationDate.add(Calendar.HOUR, -0);
			effectiveDtm = observationDate.getTime();
			obs.setEffective(new DateTimeType(effectiveDtm));
			observationIds.put(myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless(), obs.getCode());
		}

		HttpServletRequest request;
		List<String> actual;
		SearchParameterMap params = new SearchParameterMap();
		params.setLastN(true);
		actual = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(),null));

		assertEquals(20, actual.size());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
