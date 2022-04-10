package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.dstu3.model.BodySite;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ResourceProviderSearchModifierDstu3Test extends BaseResourceProviderDstu3Test{
	@Autowired
	MatchUrlService myMatchUrlService;

	@Test
	public void testReplicateBugWithNotDuringChain() {
		Encounter enc = new Encounter();
		enc.setType(Collections.singletonList(new CodeableConcept().addCoding(new Coding("system", "value", "display"))));
		IIdType encId = myEncounterDao.create(enc).getId();

		Observation obs = new Observation();
		obs.setContext(new Reference(encId));
		myObservationDao.create(obs).getId();

		{
			//Works when not chained:
			String encounterSearchString = "Encounter?type:not=system|value";
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(encounterSearchString);
			SearchParameterMap searchParameterMap = resourceSearch.getSearchParameterMap();
			IBundleProvider search = myEncounterDao.search(searchParameterMap);
			assertThat(search.size(), is(equalTo(0)));
		}
		{
			//Works without the NOT qualifier.
			String resultSearchString = "Observation?context.type=system|value";
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(resultSearchString);
			SearchParameterMap searchParameterMap = resourceSearch.getSearchParameterMap();
			IBundleProvider search = myObservationDao.search(searchParameterMap);
			assertThat(search.size(), is(equalTo(1)));
		}

		{
			//Works in a chain
			String noResultSearchString = "Observation?context.type:not=system|value";
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(noResultSearchString);
			SearchParameterMap searchParameterMap = resourceSearch.getSearchParameterMap();
			IBundleProvider search = myObservationDao.search(searchParameterMap);
			assertThat(search.size(), is(equalTo(0)));
		}
		{
			//Works in a chain with only value
			String noResultSearchString = "Observation?context.type:not=value";
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(noResultSearchString);
			SearchParameterMap searchParameterMap = resourceSearch.getSearchParameterMap();
			IBundleProvider search = myObservationDao.search(searchParameterMap);
			assertThat(search.size(), is(equalTo(0)));
		}
	}
}
