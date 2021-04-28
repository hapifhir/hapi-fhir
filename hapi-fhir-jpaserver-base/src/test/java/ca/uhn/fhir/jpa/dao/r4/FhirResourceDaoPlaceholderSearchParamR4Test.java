package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.index.searchparameter.PlaceholderReferenceSearchParamLoader;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"ConstantConditions"})
public class FhirResourceDaoPlaceholderSearchParamR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoPlaceholderSearchParamR4Test.class);



	@BeforeEach
	private void loadSearchParameters() {
		//We just instantiate one here since I don't want to create a whole new context configuration where the DaoConfig
		//has autoCreatePlaceholderReferences set to true before it does context refresh.
		PlaceholderReferenceSearchParamLoader placeholderReferenceSearchParamLoader = new PlaceholderReferenceSearchParamLoader(myDaoRegistry, myFhirCtx);
		placeholderReferenceSearchParamLoader.start();
		mySearchParamRegistry.forceRefresh();
	}

	@Test
	public void testPlaceholderSearchParameterWorks() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.create(o, mySrd);
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.add("resource-placeholder", new TokenParam("true"));
		IBundleProvider search = myPatientDao.search(searchParameterMap);
		assertThat(search.size(), is(equalTo(1)));
	}

	@Test
	public void testNonExistentSearchParameterDoesntReturnAnIndexRow() {
		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		myObservationDao.create(o, mySrd);

		String theFhirPath = "Observation" + PlaceholderReferenceSearchParamLoader.PLACEHOLDER_FHIRPATH_SUFFIX;
		List<IBase> evaluate = myFhirCtx.newFhirPath().evaluate(o, theFhirPath, IBase.class);
		assertThat(evaluate, hasSize(0));
	}
}
