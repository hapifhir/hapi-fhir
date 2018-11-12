package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FhirResourceDaoSearchParameterR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoSearchParameterR4Test.class);
	private FhirContext myCtx;
	private FhirResourceDaoSearchParameterR4 myDao;

	@Before
	public void before() {
		myCtx = FhirContext.forR4();
		myDao = new FhirResourceDaoSearchParameterR4();
		myDao.setContext(myCtx);
		myDao.setConfig(new DaoConfig());
	}

	@Test
	public void testValidateAllBuiltInSearchParams() {

		for (String nextResource : myCtx.getResourceNames()) {
			RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(nextResource);
			for (RuntimeSearchParam nextp : nextResDef.getSearchParams()) {
				if (nextp.getName().equals("_id")) {
					continue;
				}
				if (nextp.getName().equals("_language")) {
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
		nextSearchParameter.setExpression("Patient////");
		nextSearchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		nextSearchParameter.setType(Enumerations.SearchParamType.STRING);
		nextSearchParameter.addBase("Patient");

		try {
			myDao.validateResourceForStorage(nextSearchParameter, null);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"Patient////\": Error at 1, 1: Premature ExpressionNode termination at unexpected token \"////\"", e.getMessage());
		}
	}

}
