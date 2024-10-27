package ca.uhn.fhir.jpa.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class JpaResourceDaoSearchParameterTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaResourceDaoSearchParameterTest.class);
	private FhirContext myCtx;
	private JpaResourceDaoSearchParameter<SearchParameter> myDao;
	@Mock
	private ApplicationContext myApplicationContext;
	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	@BeforeEach
	public void before() {
		myCtx = FhirContext.forR4Cached();

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(myCtx);

		myDao = new JpaResourceDaoSearchParameter<>();
		myDao.setContext(myCtx);
		JpaStorageSettings defaultConfig = new JpaStorageSettings();
		myDao.setStorageSettingsForUnitTest(defaultConfig);
		myDao.setResourceType(SearchParameter.class);
		myDao.setApplicationContext(myApplicationContext);
		myDao.setVersionCanonicalizerForUnitTest(versionCanonicalizer);
		SearchParameterDaoValidator validator = new SearchParameterDaoValidator(myCtx, defaultConfig, mySearchParamRegistry);
		myDao.setSearchParameterDaoValidatorForUnitTest(validator);
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
				nextp.getBase().forEach(nextSearchParameter::addBase);

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
			assertEquals(Msg.code(1121) + "Invalid FHIRPath format for SearchParameter.expression \"Patient.ex[[[\": Error in ?? at 1, 1: Found [ expecting a token name", e.getMessage());
		}
	}


}
