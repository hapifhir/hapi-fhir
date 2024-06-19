package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import java.util.List;

public class ReindexTestHelper {
	public static final String ALLELE_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/observation-geneticsAlleleName";
	public static final String EYECOLOUR_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/patient-eyeColour";
	public static final String ALLELE_SP_CODE = "alleleName";
	public static final String EYECOLOUR_SP_CODE= "eyecolour";
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexTestHelper.class);
	private static final String TEST_ALLELE_VALUE = "HERC";
	private static final String TEST_EYECOLOUR_VALUE = "blue";

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final IFhirResourceDao<SearchParameter> mySearchParameterDao;
	private final IFhirResourceDao<Observation> myObservationDao;
	private final IFhirResourceDao<Patient> myPatientDao;



	public ReindexTestHelper(FhirContext theFhirContext, DaoRegistry theDaoRegistry, ISearchParamRegistry theSearchParamRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		mySearchParamRegistry = theSearchParamRegistry;
		mySearchParameterDao = myDaoRegistry.getResourceDao(SearchParameter.class);
		myObservationDao = myDaoRegistry.getResourceDao(Observation.class);
		myPatientDao = myDaoRegistry.getResourceDao(Patient.class);
	}

	public void createAlleleSearchParameter() {
		createAlleleSearchParameter(ALLELE_SP_CODE);
	}
	public void createEyeColourSearchParameter() {
		createEyeColourSearchParameter(EYECOLOUR_SP_CODE);
	}

	public DaoMethodOutcome createEyeColourPatient(boolean theActive) {
		Patient patient = buildPatientWithEyeColourExtension(theActive);
		return myPatientDao.create(patient);

	}

	private Patient buildPatientWithEyeColourExtension(boolean theActive) {
		Patient p = new Patient();
		p.addExtension().setUrl(EYECOLOUR_EXTENSION_URL).setValue(new StringType(TEST_EYECOLOUR_VALUE));
		p.setActive(theActive);
		return p;
	}

	public DaoMethodOutcome createAlleleSearchParameter(String theCode) {
		SearchParameter alleleName = new SearchParameter();
		alleleName.setId("SearchParameter/alleleName");
		alleleName.setStatus(Enumerations.PublicationStatus.ACTIVE);
		alleleName.addBase("Observation");
		alleleName.setCode(theCode);
		alleleName.setType(Enumerations.SearchParamType.TOKEN);
		alleleName.setTitle("AlleleName");
		alleleName.setExpression("Observation.extension('" + ALLELE_EXTENSION_URL + "')");
		alleleName.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		DaoMethodOutcome daoMethodOutcome = mySearchParameterDao.update(alleleName);
		mySearchParamRegistry.forceRefresh();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createEyeColourSearchParameter(String theCode) {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.setId("SearchParameter/eye-colour");
		eyeColourSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode(theCode);
		eyeColourSp.setType(Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('" + EYECOLOUR_EXTENSION_URL+ "')");
		eyeColourSp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		DaoMethodOutcome daoMethodOutcome = mySearchParameterDao.update(eyeColourSp);
		mySearchParamRegistry.forceRefresh();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createUniqueCodeSearchParameter() {
		createCodeSearchParameter();
		SearchParameter uniqueCodeSp = new SearchParameter();
		uniqueCodeSp.setId("SearchParameter/unique-code");
		uniqueCodeSp.addExtension(new Extension().setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique").setValue(new BooleanType(true)));
		uniqueCodeSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		uniqueCodeSp.setCode("observation-code");
		uniqueCodeSp.addBase("Observation");
		uniqueCodeSp.setType(Enumerations.SearchParamType.COMPOSITE);
		uniqueCodeSp.setExpression("Observation");
		uniqueCodeSp.addComponent(new SearchParameter.SearchParameterComponentComponent().setDefinition("SearchParameter/clinical-code").setExpression("Observation"));

		DaoMethodOutcome daoMethodOutcome = mySearchParameterDao.update(uniqueCodeSp);
		mySearchParamRegistry.forceRefresh();
		return daoMethodOutcome;
	}

	public void createNonUniqueStatusAndCodeSearchParameter() {
		createCodeSearchParameter();
		createStatusSearchParameter();
		SearchParameter uniqueCodeSp = new SearchParameter();
		uniqueCodeSp.setId("SearchParameter/nonunique-status-code");
		uniqueCodeSp.addExtension(new Extension().setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique").setValue(new BooleanType(false)));
		uniqueCodeSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		uniqueCodeSp.setCode("observation-status-and-code");
		uniqueCodeSp.addBase("Observation");
		uniqueCodeSp.setType(Enumerations.SearchParamType.COMPOSITE);
		uniqueCodeSp.setExpression("Observation");
		uniqueCodeSp.addComponent(new SearchParameter.SearchParameterComponentComponent().setDefinition("SearchParameter/clinical-code").setExpression("Observation"));
		uniqueCodeSp.addComponent(new SearchParameter.SearchParameterComponentComponent().setDefinition("SearchParameter/clinical-status").setExpression("Observation"));

		mySearchParameterDao.update(uniqueCodeSp);
		mySearchParamRegistry.forceRefresh();
	}


	public DaoMethodOutcome createCodeSearchParameter() {
		SearchParameter codeSp = new SearchParameter();
		codeSp.setId("SearchParameter/clinical-code");
		codeSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSp.setCode("code");
		codeSp.addBase("Observation");
		codeSp.setType(Enumerations.SearchParamType.TOKEN);
		codeSp.setExpression("Observation.code");

		DaoMethodOutcome daoMethodOutcome = mySearchParameterDao.update(codeSp);
		mySearchParamRegistry.forceRefresh();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createStatusSearchParameter() {
		SearchParameter codeSp = new SearchParameter();
		codeSp.setId("SearchParameter/clinical-status");
		codeSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSp.setCode("status");
		codeSp.addBase("Observation");
		codeSp.setType(Enumerations.SearchParamType.TOKEN);
		codeSp.setExpression("Observation.status");

		DaoMethodOutcome daoMethodOutcome = mySearchParameterDao.update(codeSp);
		mySearchParamRegistry.forceRefresh();
		return daoMethodOutcome;
	}

	public IIdType createObservationWithAlleleExtension(Observation.ObservationStatus theStatus) {
		Observation observation = buildObservationWithAlleleExtension(theStatus);
		return myObservationDao.create(observation).getId();
	}

	@Nonnull
	public Observation buildObservationWithAlleleExtension(Observation.ObservationStatus theStatus) {
		Observation observation = new Observation();
		observation.addExtension(ALLELE_EXTENSION_URL, new StringType(TEST_ALLELE_VALUE));
		observation.setStatus(theStatus);
		return observation;
	}

	public IIdType createObservationWithStatusAndCode() {
		Observation observation = buildObservationWithStatusAndCode();
		return myObservationDao.create(observation).getId();
	}

	public Observation buildObservationWithStatusAndCode() {
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding().setCode("29463-7").setSystem("http://loinc.org").setDisplay("Body Weight"));
		observation.setCode(codeableConcept);
		return observation;
	}

	public List<String> getEyeColourPatientIds() {
		return getEyeColourPatientIds(EYECOLOUR_SP_CODE, null);
	}

	public List<String> getAlleleObservationIds() {
		return getAlleleObservationIds(ALLELE_SP_CODE, null);
	}

	public List<String> getEyeColourPatientIds(String theCode, String theIdentifier) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(theCode, new TokenParam(TEST_EYECOLOUR_VALUE));
		if (theIdentifier != null) {
			map.add(Observation.SP_IDENTIFIER, new TokenParam(theIdentifier));
		}
		ourLog.info("Searching for Patients with url {}", map.toNormalizedQueryString(myFhirContext));
		IBundleProvider result = myPatientDao.search(map);
		return result.getAllResourceIds();
	}

	public List<String> getAlleleObservationIds(String theCode, String theIdentifier) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(theCode, new TokenParam(TEST_ALLELE_VALUE));
		if (theIdentifier != null) {
			map.add(Observation.SP_IDENTIFIER, new TokenParam(theIdentifier));
		}
		ourLog.info("Searching for Observations with url {}", map.toNormalizedQueryString(myFhirContext));
		IBundleProvider result = myObservationDao.search(map);
		return result.getAllResourceIds();
	}

	public IBaseResource buildObservationWithAlleleExtension() {
		return buildObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
	}

	public List<String> getAlleleObservationIds(IGenericClient theClient) {
		IBaseBundle result = theClient.search()
			.forResource("Observation")
			.where(new StringClientParam(ALLELE_SP_CODE).matches().value(TEST_ALLELE_VALUE))
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();
		return BundleUtil.toListOfResourceIds(myFhirContext, result);
	}

	/**
	 * Creates a transaction bundle with 20 Observations which will create rows for indexes
	 * created by {@link #createNonUniqueStatusAndCodeSearchParameter()} and
	 * {@link #createUniqueCodeSearchParameter()}.
	 */
	public Bundle createTransactionBundleWith20Observation(boolean theUseClientAssignedIds) {
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 20; i++) {
			Observation observation = new Observation();
			if (theUseClientAssignedIds) {
				observation.setId("OBS" + i);
			}
			observation.addIdentifier().setSystem("http://foo").setValue("ident" + i);
			observation.setStatus(Observation.ObservationStatus.FINAL);
			observation.getCode().addCoding().setSystem("http://foo").setCode("" + i);
			if (theUseClientAssignedIds) {
				bb.addTransactionUpdateEntry(observation);
			} else {
				bb.addTransactionCreateEntry(observation);
			}
		}
		return bb.getBundleTyped();
	}
}
