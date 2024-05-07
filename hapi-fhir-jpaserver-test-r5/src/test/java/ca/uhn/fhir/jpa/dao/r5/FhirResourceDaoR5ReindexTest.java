package ca.uhn.fhir.jpa.dao.r5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5ReindexTest extends BaseJpaR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5ReindexTest.class);


	@Test
	public void testReindexDeletedSearchParameter() {

		SearchParameter sp = new SearchParameter();
		sp.setCode("foo");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Patient.name");
		sp.setType(Enumerations.SearchParamType.STRING);
		IIdType id = mySearchParameterDao.create(sp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParameterDao.delete(id, mySrd);

		runInTransaction(() -> {
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_RESOURCE set sp_uri_present = true").executeUpdate());
			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow();
			assertTrue(table.isParamsUriPopulated());
			ResourceIndexedSearchParamUri uri = new ResourceIndexedSearchParamUri(new PartitionSettings(), "SearchParameter", "url", "http://foo");
			uri.setResource(table);
			uri.calculateHashes();
			myResourceIndexedSearchParamUriDao.save(uri);
		});

		Parameters outcome = (Parameters) myInstanceReindexService.reindex(mySrd, id);
		ourLog.info("Outcome: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertThat(outcome.getParameter("Narrative").getValueStringType().getValue()).contains("Reindex completed in");
		assertEquals("REMOVE", outcome.getParameter("UriIndexes").getPartFirstRep().getPartFirstRep().getValueCodeType().getValue());

		runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow();
			assertFalse(table.isParamsUriPopulated());
		});
	}

}
