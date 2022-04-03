package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class FhirResourceDaoSearchParameterR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoSearchParameterR4Test.class);
	private FhirContext myCtx;
	private FhirResourceDaoSearchParameterR4 myDao;
	@Mock
	private ApplicationContext myApplicationContext;

	@BeforeEach
	public void before() {
		myCtx = FhirContext.forR4Cached();

		myDao = new FhirResourceDaoSearchParameterR4();
		myDao.setContext(myCtx);
		myDao.setDaoConfigForUnitTest(new DaoConfig());
		myDao.setApplicationContext(myApplicationContext);
		myDao.start();
	}

	@Test
	public void testValidateAllBuiltInSearchParams() {

		for (String nextResource : myCtx.getResourceTypes()) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(nextResource);
			for (RuntimeSearchParam nextp : nextResDef.getSearchParams()) {
				if (nextp.getName().equals("_id")) {
					continue;
				}
				if (isBlank(nextp.getPath())) {
					continue;
				}

				SearchParameter nextSearchParameter = new SearchParameter();
				nextSearchParameter.setExpression(nextp.getPath());
				nextSearchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
				nextSearchParameter.setType(Enumerations.SearchParamType.fromCode(nextp.getParamType().getCode()));
				nextp.getBase().forEach(t -> nextSearchParameter.addBase(t));

				ourLog.info("Validating {}.{}", nextResource, nextp.getName());
				myDao.validateResourceForStorage(nextSearchParameter, null);
			}
		}


	}


	@Test
	public void testValidateInvalidExpression() {
		SearchParameter nextSearchParameter = new SearchParameter();
		nextSearchParameter.setExpression("Patient.ex[[[");
		nextSearchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		nextSearchParameter.setType(Enumerations.SearchParamType.STRING);
		nextSearchParameter.addBase("Patient");

		try {
			myDao.validateResourceForStorage(nextSearchParameter, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1121) + "Invalid SearchParameter.expression value \"Patient.ex[[[\": Error in ?? at 1, 1: Found [ expecting a token name", e.getMessage());
		}
	}


}
