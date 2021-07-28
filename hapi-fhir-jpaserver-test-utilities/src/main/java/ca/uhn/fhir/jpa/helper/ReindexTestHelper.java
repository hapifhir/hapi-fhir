package ca.uhn.fhir.jpa.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// FIXME KHS use in cdr
public class ReindexTestHelper {
	public static final String ALLELE_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/observation-geneticsAlleleName";
	public static final String ALLELE_SP_CODE = "alleleName";
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexTestHelper.class);
	private static final String TEST_ALLELE_VALUE = "HERC";

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final IFhirResourceDao<SearchParameter> mySearchParameterDao;
	private final IFhirResourceDao<Observation> myObservationDao;

	public ReindexTestHelper(FhirContext theFhirContext, DaoRegistry theDaoRegistry, ISearchParamRegistry theSearchParamRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		mySearchParamRegistry = theSearchParamRegistry;
		mySearchParameterDao = myDaoRegistry.getResourceDao(SearchParameter.class);
		myObservationDao = myDaoRegistry.getResourceDao(Observation.class);
	}

	public void createAlleleSearchParameter() {
		createAlleleSearchParameter(ALLELE_SP_CODE);
	}

	public void createAlleleSearchParameter(String theCode) {
		SearchParameter alleleName = new SearchParameter();
		alleleName.setId("SearchParameter/alleleName");
		alleleName.setStatus(Enumerations.PublicationStatus.ACTIVE);
		alleleName.addBase("Observation");
		alleleName.setCode(theCode);
		alleleName.setType(Enumerations.SearchParamType.TOKEN);
		alleleName.setTitle("AlleleName");
		alleleName.setExpression("Observation.extension('" + ALLELE_EXTENSION_URL + "')");
		alleleName.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		mySearchParameterDao.create(alleleName);
		mySearchParamRegistry.forceRefresh();
	}

	public IIdType createObservationWithAlleleExtension(Observation.ObservationStatus theStatus) {
		Observation observation = new Observation();
		observation.addExtension(ALLELE_EXTENSION_URL, new StringType(TEST_ALLELE_VALUE));
		observation.setStatus(theStatus);
		return myObservationDao.create(observation).getId();
	}

	public List<String> getAlleleObservationIds() {
		return getAlleleObservationIds(ALLELE_SP_CODE, null);
	}

	public List<String> getAlleleObservationIds(String theCode, String theIdentifier) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(theCode, new TokenParam(TEST_ALLELE_VALUE));
		if (theIdentifier != null) {
			map.add(Observation.SP_IDENTIFIER, new TokenParam(theIdentifier));
		}
		ourLog.info("Searching with url {}", map.toNormalizedQueryString(myFhirContext));
		IBundleProvider result = myObservationDao.search(map);
		return result.getAllResourceIds();
	}
}
