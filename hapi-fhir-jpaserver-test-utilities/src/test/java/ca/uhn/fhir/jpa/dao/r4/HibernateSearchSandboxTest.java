package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.search.HSearchClauseProvider;
import ca.uhn.fhir.jpa.dao.search.IHSearchParamHelperProvider;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.MatchPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_PARAM_NAME;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.UCUM_CODESYSTEM_URL;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Just a sandbox. Never intended to run by pipes
 */
@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {
	TestR4Config.class,
	TestHibernateSearchAddInConfig.Elasticsearch.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
//fixme jm: disable
//@Disabled
public class HibernateSearchSandboxTest extends BaseJpaTest {

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	private ITestDataBuilder myTestDataBuilder;

	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;

	@Autowired
	@Qualifier("mySystemDaoR4")
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;

	@Autowired
	@Qualifier("myObservationDaoR4")
	private IFhirResourceDao<Observation> myObservationDao;

	@Autowired
	private TestDaoSearch myTestDaoSearch;

	@Autowired
	private IHSearchParamHelperProvider myHSearchParamHelperProvider;

	@Autowired
	@Qualifier("myRiskAssessmentDaoR4")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;


//	@BeforeEach
//	public void beforePurgeDatabase() {
//		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
//	}

	@BeforeEach
	public void enableContainsAndLucene() {
		myDaoConfig.setAllowContainsSearches(true);
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}


	@Nested
	public class NotNestedObjectQueries {
		/**
		 * Show that when there is only one and clause with "or" entries, we can add the shoulds
		 * at the top level
		 */
		@Test
		public void searchWithMultipleAndWithOneOrClauseTest() {
			String system = "http://loinc.org";
			Observation obs1 = new Observation();
			obs1.getCode().setText("Systolic Blood Pressure");
			obs1.getCode().addCoding().setCode("obs1").setSystem(system).setDisplay("Systolic Blood Pressure");
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.setValue(new Quantity(123));
			obs1.getNoteFirstRep().setText("obs1");
			IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching("Observation"));
						b.must(f.match().field("sp.code.token.system").matching("http://loinc.org"));
						b.should(f.match().field("sp.code.token.code").matching("obs3"));
						b.should(f.match().field("sp.code.token.code").matching("obs1"));
						b.minimumShouldMatchNumber(1);
					}))
					.fetchAll();
				long totalHitCount = result.total().hitCount();
//			List<ResourceTable> hits = result.hits();
			});
		}


		/**
		 * Shows that when there is multiple "and" clause with "or" entries, we need to group each one in a "must" clause
		 * to be able to add a minimumShouldMatchNumber(1); to each group
		 */
		@Test
		public void searchWithMultipleAndWithMultipleOrClausesTest() {
			String system = "http://loinc.org";
			Observation obs1 = new Observation();
			obs1.getCode().setText("Systolic Blood Pressure");
			obs1.getCode().addCoding().setCode("obs1").setSystem(system).setDisplay("Systolic Blood Pressure");
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.setValue(new Quantity(123));
			obs1.getNoteFirstRep().setText("obs1");
			IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching("Observation"));
						b.must(f.match().field("sp.code.token.system").matching("http://loinc.org"));

						b.must(f.bool(p -> {
							p.should(f.match().field("sp.code.token.code").matching("obs3"));
							p.should(f.match().field("sp.code.token.code").matching("obs1"));
							p.minimumShouldMatchNumber(1);
						}));

						b.must(f.bool(p -> {
							p.should(f.match().field("sp.code.token.code").matching("obs5"));
							p.should(f.match().field("sp.code.token.code").matching("obs1"));
							p.minimumShouldMatchNumber(1);
						}));
					}))
					.fetchAll();
				long totalHitCount = result.total().hitCount();
//			List<ResourceTable> hits = result.hits();
			});
		}

		@Test
		public void searchWithMultipleAndMultipleOrClauseTestForNumber() {
			String raId1 = createRiskAssessmentWithPredictionProbability(0.15).getIdPart();
			String raId2 = createRiskAssessmentWithPredictionProbability(0.20).getIdPart();
			String raId3 = createRiskAssessmentWithPredictionProbability(0.25).getIdPart();
			String raId4 = createRiskAssessmentWithPredictionProbability(0.35).getIdPart();
			String raId5 = createRiskAssessmentWithPredictionProbability(0.45).getIdPart();
			String raId6 = createRiskAssessmentWithPredictionProbability(0.55).getIdPart();

			String paramName = "probability";
			String resourceTypeName = "RiskAssessment";
			// logically stupid but to test an and with 1 or and one with 2 ors
			List<List<IQueryParameterType>> theAndOrTerms = List.of(
				List.of(
					new NumberParam().setValue( BigDecimal.valueOf(.35) )
				),
				List.of(
					new NumberParam().setValue( BigDecimal.valueOf(.80) ),
					new NumberParam().setValue( BigDecimal.valueOf(.35) )
				)
			);

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)

					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching(resourceTypeName));

						HSearchClauseProvider clausesProvider = new HSearchClauseProvider(myHSearchParamHelperProvider, f, b);
						clausesProvider.addAndConsumeAndPlusOrClauses(resourceTypeName, paramName, theAndOrTerms);
					}))
					.fetchAll();

				List<String> theResourceIds = result.hits().stream().map(r -> r.getIdType(myFhirContext).getIdPart()).collect(Collectors.toList());
				assertThat(theResourceIds, Matchers.contains(raId4));
			});
		}

		@Test
		public void searchWithMultipleAndMultipleOrClauseTestForNumberWithPrefix() {
			String raId1 = createRiskAssessmentWithPredictionProbability(99).getIdPart();
			String raId2 = createRiskAssessmentWithPredictionProbability(100).getIdPart();
			String raId3 = createRiskAssessmentWithPredictionProbability(101).getIdPart();
			// [parameter]=le100	Values that are less or equal to exactly 100
//			assertFindIds("when le", Set.of(raId1, raId2), "/RiskAssessment?probability=le100");

			String paramName = "probability";
			String resourceTypeName = "RiskAssessment";
			// logically stupid but to test an and with 1 or and one with 2 ors
			List<List<IQueryParameterType>> theAndOrTerms = List.of(
				List.of(
					new NumberParam().setValue( BigDecimal.valueOf(100) ).setPrefix(ParamPrefixEnum.LESSTHAN_OR_EQUALS)
			));

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)

					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching(resourceTypeName));

						HSearchClauseProvider clausesProvider = new HSearchClauseProvider(myHSearchParamHelperProvider, f, b);
						clausesProvider.addAndConsumeAndPlusOrClauses(resourceTypeName, paramName, theAndOrTerms);
					}))
					.fetchAll();

				List<String> theResourceIds = result.hits().stream().map(r -> r.getIdType(myFhirContext).getIdPart()).collect(Collectors.toList());
				assertThat(theResourceIds, Matchers.contains(raId1, raId2));
			});
		}

		@Test
		public void searchWithMultipleAndMultipleOrClauseTestForUri() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withProfile("http://example.com/theProfile"),
				myTestDataBuilder.withProfile("http://example.com/anotherProfile"))).getIdPart();

			String paramName = "_profile";
			String resourceTypeName = "Observation";
			// logically stupid but to test an and with 1 or and one with 2 ors
			List<List<IQueryParameterType>> theAndOrTerms = List.of(
				List.of(
					new UriParam().setValue("http://example.com/theProfile")
				),
				List.of(
					new UriParam().setValue("http://example.com/non-existing-profile"),
					new UriParam().setValue("http://example.com/anotherProfile")
				)
			);

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)

					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching(resourceTypeName));

						HSearchClauseProvider clausesProvider = new HSearchClauseProvider(myHSearchParamHelperProvider, f, b);
						clausesProvider.addAndConsumeAndPlusOrClauses(resourceTypeName, paramName, theAndOrTerms);
					}))
					.fetchAll();

				List<String> theResourceIds = result.hits().stream().map(r -> r.getIdType(myFhirContext).getIdPart()).collect(Collectors.toList());
				assertThat(theResourceIds, Matchers.contains(id));
			});
		}


	}



	@Nested
	public class NestedObjectQueries {

		/**
		 * Show that when there is only one and clause with "or" entries, we can add the shoulds
		 * at the top level
		 */
		@Test
		public void searchWithAndMultipleAndWithOneOrClauseForQuantity() {
			IIdType myResourceId = myTestDataBuilder.createObservation(myTestDataBuilder.withElementAt("valueQuantity",
				myTestDataBuilder.withPrimitiveAttribute("value", 0.6)
			));

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching("Observation"));
						b.must(f.nested().objectField("nsp.value-quantity")
							.nest(f.bool()
								.must(f.range().field("nsp.value-quantity.quantity.value").lessThan(0.7))
								.should(f.range().field("nsp.value-quantity.quantity.value").between(0.475, 0.525))
								.should(f.range().field("nsp.value-quantity.quantity.value").between(0.57, 0.63))
								.minimumShouldMatchNumber(1)
							));
					}))
					.fetchAll();
			});
		}

		/**
		 * Shows that when there is multiple "and" clause with "or" entries, we need to group each one in a "must" clause
		 * to be able to add a minimumShouldMatchNumber(1); to each group
		 */
		@Test
		public void searchWithMultipleAndWithMultipleOrClausesForQuantity() {
			String obsId = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withQuantityAtPath("valueQuantity", 0.02, UCUM_CODESYSTEM_URL, "10*6/L")
			)).getIdPart();

//			runInTransaction(() -> {
//				SearchSession searchSession = Search.session(myEntityManager);
//				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
//					.where(f -> f.bool(b -> {
//						b.must(f.match().field("myResourceType").matching("Observation"));
//						b.must(f.nested().objectField("nsp.value-quantity")
//							.nest(f.bool()
//								.must(f.range().field("nsp.value-quantity.quantity.value").lessThan(0.7))
//
//								.must(f.bool(b1 -> {
//									b1.should(f.range().field("nsp.value-quantity.quantity.value").between(0.475, 0.525));
//									b1.should(f.range().field("nsp.value-quantity.quantity.value").between(0.57, 0.63));
//									b1.minimumShouldMatchNumber(1);
//								}))
//
//								.must(f.bool(b1 -> {
//									b1.should(f.range().field("nsp.value-quantity.quantity.value").between(0.2, 0.8));
//									b1.should(f.range().field("nsp.value-quantity.quantity.value").between(0.7, 0.9));
//									b1.minimumShouldMatchNumber(1);
//								}))
//							));
//					}))
//					.fetchAll();
//				assertEquals(1, result.total().hitCount());
//			});

			String paramName = "value-quantity";
			String resourceTypeName = "Observation";
			List<List<IQueryParameterType>> theAndOrTerms = List.of(
				List.of(
					new QuantityParam().setSystem(UCUM_CODESYSTEM_URL).setValue(0.02).setUnits("10*6/L") ),
				List.of(
					new QuantityParam().setSystem(UCUM_CODESYSTEM_URL).setValue(0.02).setUnits("10*6/L"),
					new QuantityParam().setSystem("http://example.com").setValue(0.04).setUnits("10*3/L")
				)
			);

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)

					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching(resourceTypeName));

						HSearchClauseProvider clausesProvider = new HSearchClauseProvider(myHSearchParamHelperProvider, f, b);
						clausesProvider.addAndConsumeAndPlusOrClauses(resourceTypeName, paramName, theAndOrTerms);
					}))
					.fetchAll();
				assertEquals(1, result.total().hitCount());
			});

		}

		@Test
		public void searchWithMultipleAndWithMultipleOrClausesForQuantityWithPrefix() {
			String obsId = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withQuantityAtPath("valueQuantity", 0.02, UCUM_CODESYSTEM_URL, "10*6/L")
			)).getIdPart();

			String paramName = "value-quantity";
			String resourceTypeName = "Observation";
			List<List<IQueryParameterType>> theAndOrTerms = List.of(
				List.of(
					new QuantityParam().setSystem(UCUM_CODESYSTEM_URL).setValue(0.028).setUnits("10*6/L").setPrefix(ParamPrefixEnum.LESSTHAN_OR_EQUALS) ),
				List.of(
					new QuantityParam().setSystem("http://example.com").setValue(0.03).setUnits("10*6/L").setPrefix(ParamPrefixEnum.GREATERTHAN),
					new QuantityParam().setSystem(UCUM_CODESYSTEM_URL).setValue(0.025).setUnits("10*3/L").setPrefix(ParamPrefixEnum.LESSTHAN)
				)
			);

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)

					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching(resourceTypeName));

						HSearchClauseProvider clausesProvider = new HSearchClauseProvider(myHSearchParamHelperProvider, f, b);
						clausesProvider.addAndConsumeAndPlusOrClauses(resourceTypeName, paramName, theAndOrTerms);
					}))
					.fetchAll();
				assertEquals(1, result.total().hitCount());
			});

		}

		@Test
		public void searchWithMultipleAndMultipleOrClauseTestForToken() {
			String id = myTestDataBuilder.createObservation(List.of(
				myTestDataBuilder.withSecurity("http://example1.com", "security-label-1"),
				myTestDataBuilder.withSecurity("http://example2.com", "security-label-2"))).getIdPart();

//			List<String> allIds = myTestDaoSearch.searchForIds("/Observation" +
//				"?_security=http://example1.com|security-label-1" +
//				"&_security=http://example3.com|non-existing-security-label,http://example1.com|security-label-1" +
//				"&_security=http://example3.com|other-non-existing-security-label,http://example2.com|security-label-2");
//
//			assertThat(allIds, contains(id));

			String paramName = "_security";
			String resourceTypeName = "Observation";
			List<List<IQueryParameterType>> theAndOrTerms = List.of(
				List.of(
					new TokenParam().setSystem("http://example1.com").setValue("security-label-1")),
				List.of(
					new TokenParam().setSystem("http://example3.com").setValue("non-existing-security-label"),
					new TokenParam().setSystem("http://example1.com").setValue("security-label-1")
				)
			);

//////////////   model query. Don't delete /////////////////////////////////////

//			runInTransaction(() -> {
//				SearchSession searchSession = Search.session(myEntityManager);
//				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
//
//					.where(f -> f.bool(b -> {
//						b.must(f.match().field("myResourceType").matching("Observation"));
//
//						b.must(f.nested().objectField("nsp._security")
//							.nest( f.bool( b1 -> {
////								// and clause with 1 or clause
//								b1.must(f.match().field("nsp._security.token.system").matching("http://example1.com"));
//								b1.must(f.match().field("nsp._security.token.code").matching("security-label-1"));
//
//								// and clause with 2 (composite) or clauses
//								b1.must( f.bool( b2 -> {
//									b2.minimumShouldMatchNumber(1);
//
//									var paramPred1 = f.bool();
//									paramPred1.must( f.match().field("nsp._security.token.system").matching("http://example3.com") );
//									paramPred1.must( f.match().field("nsp._security.token.code").matching("non-existing-security-label") );
//									b2.should(paramPred1);
//
//									var paramPred2 = f.bool();
//									paramPred2.must( f.match().field("nsp._security.token.system").matching("http://example1.com") );
//									paramPred2.must( f.match().field("nsp._security.token.code").matching("security-label-1") );
//									b2.should(paramPred2);
//								}));
//							}))
//						);
//					}))
//					.fetchAll();
//				assertEquals(1, result.total().hitCount());
//			});

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)

					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching(resourceTypeName));

						HSearchClauseProvider clausesProvider = new HSearchClauseProvider(myHSearchParamHelperProvider, f, b);
						clausesProvider.addAndConsumeAndPlusOrClauses(resourceTypeName, paramName, theAndOrTerms);
					}))
					.fetchAll();
				assertEquals(1, result.total().hitCount());
			});
		}


		@Test
		void testMultipleComponentsHandlesAndOr() {
			Observation obs1 = getObservation();
			addComponentWithCodeAndQuantity(obs1, "8480-6", 107);
			addComponentWithCodeAndQuantity(obs1, "8462-4", 60);

			IIdType obs1Id = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

			Observation obs2 = getObservation();
			addComponentWithCodeAndQuantity(obs2, "8480-6",107);
			addComponentWithCodeAndQuantity(obs2, "8462-4",260);

			myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

//			// andClauses
//			{
//				String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=60";
//				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
//				assertThat("when same component with qtys 107 and 60", resourceIds, hasItem(equalTo(obs1Id.getIdPart())));
//			}

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> f.bool(b -> {
						b.must( f.match().field("myResourceType").matching("Observation"));
						b.must( f.nested().objectField("nsp.component-value-quantity")
							.nest( f.range().field("nsp.component-value-quantity.quantity.value").between(101.65, 112.35)) );
						b.must( f.nested().objectField("nsp.component-value-quantity")
							.nest( f.range().field("nsp.component-value-quantity.quantity.value").between(57.0, 63.0)) );
					}))
					.fetchAll();
				assertEquals(1, result.total().hitCount());
			});
			//			{
//				String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=260";
//				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
//				assertThat("when same component with qtys 107 and 260", resourceIds, empty());
//			}
//
//			//andAndOrClauses
//			{
//				String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=gt50,lt70";
//				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
//				assertThat("when same component with qtys 107 and lt70,gt80", resourceIds, hasItem(equalTo(obs1Id.getIdPart())));
//			}
//			{
//				String theUrl = "/Observation?component-value-quantity=50,70&component-value-quantity=260";
//				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
//				assertThat("when same component with qtys 50,70 and 260", resourceIds, empty());
//			}
//
//			// multipleAndsWithMultipleOrsEach
//			{
//				String theUrl = "/Observation?component-value-quantity=50,60&component-value-quantity=105,107";
//				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
//				assertThat("when same component with qtys 50,60 and 105,107", resourceIds, hasItem(equalTo(obs1Id.getIdPart())));
//			}
//			{
//				String theUrl = "/Observation?component-value-quantity=50,60&component-value-quantity=250,260";
//				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
//				assertThat("when same component with qtys 50,60 and 250,260", resourceIds, empty());
//			}
		}


		private Observation getObservation() {
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("85354-9").setSystem("http://loinc.org");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			return obs;
		}

		private Quantity getQuantity(double theValue) {
			return new Quantity().setValue(theValue).setUnit("mmHg").setSystem("http://unitsofmeasure.org").setCode("mm[Hg]");
		}

		private Observation.ObservationComponentComponent addComponentWithCodeAndQuantity(Observation theObservation, String theConceptCode, double theQuantityValue) {
			Observation.ObservationComponentComponent comp = theObservation.addComponent();
			CodeableConcept cc1_1 = new CodeableConcept();
			cc1_1.addCoding().setCode(theConceptCode).setSystem("http://loinc.org");
			comp.setCode(cc1_1);
			comp.setValue(getQuantity(theQuantityValue));
			return comp;
		}

	}

	//////////////////// last model before moving it to HSearchClauseProvider class - DON'T DELETE ///////////////

//	private PredicateFinalStep getAndPlusOrClauses(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB,
//			String theResourceTypeName, String theParamName, List<List<IQueryParameterType>> theAndOrTerms) {
//
//		boolean isPropertyNested = isNested(theParamName);
//
//		BooleanPredicateClausesStep<?> topBool = theF.bool();
//		// need an extra bool level for nested properties (embedding it under topBool before leaving method)
//		BooleanPredicateClausesStep<?> activeBool = isPropertyNested ? theF.bool()  : topBool;
//
//		for (List<IQueryParameterType> orTerms : theAndOrTerms) {
//			if (orTerms.size() == 1) {
//				addOrClause(theF, activeBool, theResourceTypeName, theParamName, orTerms.get(0));
//				continue;
//			}
//
//			// multiple or predicates must be in must group with multiple should(s) with a minimumShouldMatchNumber(1)
//			activeBool.must(theF.bool(b2 -> {
//				b2.minimumShouldMatchNumber(1);
//
//				for (IQueryParameterType orTerm : orTerms) {
//					var paramBool = theF.bool();
//					addOrClause(theF, paramBool, theResourceTypeName, theParamName, orTerm);
//					b2.should(paramBool);
//				}
//			}));
//		}
//
//		if ( isPropertyNested ) {
//			topBool.must(theF.nested().objectField("nsp." + theParamName).nest(activeBool));
//		}
//
//		return topBool;
//	}
//
//
//	private HSearchParamHelper<?> getParamHelper(String theResourceTypeName, String theParamName) {
//		return myHSearchParamHelperProvider.provideHelper(theResourceTypeName, theParamName);
//	}
//
//
//	/**
//	 * Adding an OR clause doesn't necessarily mean one must clause, because for parameters with more than
//	 * one property, a must clause must be added for each property present
//	 */
//	private void addOrClause(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theBool,
//			String theResourceTypeName, String theParamName, IQueryParameterType theParam) {
//
//		HSearchParamHelper<?> paramHelper = getParamHelper(theResourceTypeName, theParamName);
//		List<String> paramProperties = paramHelper.getParamPropertiesForParameter(theParamName, theParam);
//		for (String paramProperty : paramProperties) {
//			Optional<Object> paramPropertyValue = paramHelper.getParamPropertyValue(theParam, paramProperty);
//			paramPropertyValue.ifPresent(v -> theBool.must(theF.match().field(paramProperty).matching(v)) );
//		}
//	}








	/**
	 * Following code is the beginning of refactoring the queries for cleaner structure, which means
	 * to try to achieve the clean query structure modeled by previous tests, but using generic methods
	 */
	@Nested
	public class FragmentedCodeNotNested {

		private SearchPredicateFactory fact;

		@Test
		public void searchWithMultipleAndOneOrClauseTest() {
			String system = "http://loinc.org";
			Observation obs1 = new Observation();
			obs1.getCode().setText("Systolic Blood Pressure");
			obs1.getCode().addCoding().setCode("obs1").setSystem(system).setDisplay("Systolic Blood Pressure");
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.setValue(new Quantity(123));
			obs1.getNoteFirstRep().setText("obs1");
			IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

			String paramName = "value-quantity";
			List<List<IQueryParameterType>> theQuantityAndOrTerms = Lists.newArrayList();

			theQuantityAndOrTerms.add(Collections.singletonList(
				new QuantityParam().setValue(0.7)));

			theQuantityAndOrTerms.add(Lists.newArrayList(
				new QuantityParam().setValue(0.5),
				new QuantityParam().setValue(0.6)
			));

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> {
						TestPredBuilder builder = new TestPredBuilder(f);
						return builder.buildAndOrPredicates(paramName, theQuantityAndOrTerms);
					})
					.fetchAll();
				long totalHitCount = result.total().hitCount();
//			List<ResourceTable> hits = result.hits();
			});
		}

		@Test
		public void searchWithMultipleAndMultipleOrClauseTestWithQuantity() {
			IIdType myResourceId = myTestDataBuilder.createObservation(myTestDataBuilder.withElementAt("valueQuantity",
				myTestDataBuilder.withPrimitiveAttribute("value", 0.6)
			));

			String paramName = "value-quantity";
			List<List<IQueryParameterType>> theQuantityAndOrTerms = List.of(
				List.of(
					new QuantityParam().setValue(0.61)),
				List.of(
					new QuantityParam().setValue(0.5),
					new QuantityParam().setValue(0.6)
				),
				List.of(
					new QuantityParam().setValue(0.9),
					new QuantityParam().setValue(0.6)
				)
			);

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> {
						var b = f.bool();
						b.must(f.match().field("myResourceType").matching("Observation"));

						for (List<IQueryParameterType> orTerms : theQuantityAndOrTerms) {
							b.must( getOrTermClauses(f, paramName, orTerms) );
						}

						TestPredBuilder builder = new TestPredBuilder(f);
						b.must( builder.buildAndOrPredicates(paramName, theQuantityAndOrTerms) );
						return b;
					})
					.fetchAll();
				long totalHitCount = result.total().hitCount();
//			List<ResourceTable> hits = result.hits();
				assertEquals(1, totalHitCount);
			});
		}

		@Test
		public void searchWithMultipleAndMultipleOrClauseTestWithUri() {
			String obsId1 = myTestDataBuilder.createObservation(List.of(myTestDataBuilder.withProfile("http://example.org/profile-1"))).getIdPart();

			String paramName = "_profile";
			List<List<IQueryParameterType>> theQuantityAndOrTerms = List.of(
				List.of(
					new UriParam().setValue("http://example.org/profile-1")),
				List.of(
					new UriParam().setValue("http://example.org/profile-2"),
					new UriParam().setValue("http://example.org/profile-1")
				),
				List.of(
					new UriParam().setValue("http://example.org/profile-3"),
					new UriParam().setValue("http://example.org/profile-1")
				)
			);

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching("Observation"));

						addAndUriClauses(f, b, paramName, theQuantityAndOrTerms);
					}))
					.fetchAll();
				long totalHitCount = result.total().hitCount();
				assertEquals(Collections.singletonList(obsId1), result.hits().stream()
					.map(r -> r.getIdType(getFhirContext()).getIdPart()).collect(Collectors.toList()));
			});
		}


		private void addAndUriClauses(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b,
												String theParamName, List<List<IQueryParameterType>> theAndOrTerms) {

			String propertyPath = getPathForParamName(theParamName);

			for (List<IQueryParameterType> orTerms : theAndOrTerms) {

				if (orTerms.size() == 1) {
					b.must(f.match().field(propertyPath).matching( ((UriParam) orTerms.get(0)).getValue() ));
					continue;
				}

				b.must( f.bool( b1 -> {
					for (IQueryParameterType orTerm : orTerms) {
						b1.should(f.match().field(propertyPath).matching( ((UriParam) orTerm).getValue() ));
						b1.should(f.match().field(propertyPath).matching( ((UriParam) orTerm).getValue() ));
					}
					b1.minimumShouldMatchNumber(1);
				}));
			}
		}
	}















	private List<String> getResultIds(IBundleProvider theResult) {
		return theResult.getAllResources().stream().map(r -> r.getIdElement().getIdPart()).collect(Collectors.toList());
	}

	private PredicateFinalStep getOrTermClauses(SearchPredicateFactory theF, String theParamName, List<IQueryParameterType> theOrTerms) {
		if (theOrTerms.size() == 1) {
			return theF.match().field( getPathForParamName(theParamName) + ".system" ).matching( ((TokenParam) theOrTerms.get(0)).getSystem() );
		}

		// multiple or clauses
		var boolStep = theF.bool();
		for (IQueryParameterType orTerm : theOrTerms) {
			boolStep.should(theF.match().field( getPathForParamName(theParamName) + ".code" ).matching( ((TokenParam) orTerm).getValue() ) );
		}

		return boolStep;
	}


	private String getPathForParamName(String theParamName) {
		switch (theParamName) {
			case "code": return "sp.code.token";
			case "_profile": return "sp._profile.uri-value";
		}

		throw new InvalidParameterException("don't know how to handle paramName: " + theParamName);
	}





	private class TestPredBuilder {

		private static final double QTY_APPROX_TOLERANCE_PERCENT = .10;
		private static final double QTY_TOLERANCE_PERCENT = .05;

		SearchPredicateFactory myPredicateFactory;

		public TestPredBuilder(SearchPredicateFactory theF) { myPredicateFactory = theF; }


		public PredicateFinalStep buildAndOrPredicates(
				String theSearchParamName, List<List<IQueryParameterType>> theAndOrTerms) {

			boolean isNested = isNested(theSearchParamName);

//			// we need to know if there is any "and" predicate (outer list) with more than one "or" predicate (inner list)
//			long maxOrPredicateSize = theAndOrTerms.stream().map(List::size).filter(s -> s > 1).count();

			BooleanPredicateClausesStep<?> topBool = myPredicateFactory.bool();
			// need an extra bool level for nested properties
			BooleanPredicateClausesStep<?> activeBool = isNested ? topBool : myPredicateFactory.bool();

			for (List<IQueryParameterType> orTerms : theAndOrTerms) {
				// multiple or predicates must be in must group of should(s) with a minimumShouldMatchNumber(1)
				activeBool.must(myPredicateFactory.bool(p -> {
//					p.must( getOrPredicates(p, theSearchParamName, orTerms) );
				}));
			}

			if (isNested) {
				topBool.must(myPredicateFactory.nested().objectField("nsp.value-quantity").nest(activeBool));
			}
			return topBool;
		}


//		private PredicateFinalStep getOrPredicates(SearchPredicateFactory theF, String theParamName, List<IQueryParameterType> theOrTerms) {
//			if (theOrTerms.size() == 1) {
//				theF.should( getPredicate(theBool, theParamName, theOrTerms.get(0)) );
//			} else {
//				theF.should(f -> f.match().field("").matching(""));
//			}
//		}

//		private PredicateFinalStep getPredicate(BooleanPredicateClausesStep<?> theBool, String theParamName, IQueryParameterType theParameterType) {
//			if (theParameterType instanceof QuantityParam) {
//				addQuantityOrClauses(theBool, theParamName, theParameterType);
//				return;
//			}
//
//			throw new IllegalStateException("Shouldn't reach this code");
//		}












		private void addOnePredicate(BooleanPredicateClausesStep<?> theTopBool, boolean theIsMust,
				String theParamName, IQueryParameterType theParameterType) {

			if (theParameterType instanceof QuantityParam) {
				addQuantityOrClauses(theTopBool, theIsMust, theParamName, theParameterType);
				return;
			}

			throw new IllegalStateException("Shouldn't reach this code");
		}


		private void addQuantityOrClauses(BooleanPredicateClausesStep<?> theTopBool, boolean theIsMust,
				String theSearchParamName, IQueryParameterType theParamType) {

			String fieldPath = NESTED_SEARCH_PARAM_ROOT + "." + theSearchParamName + "." + QTY_PARAM_NAME;

			QuantityParam qtyParam = QuantityParam.toQuantityParam(theParamType);
			ParamPrefixEnum activePrefix = qtyParam.getPrefix() == null ? ParamPrefixEnum.EQUAL : qtyParam.getPrefix();

//			if (myModelConfig.getNormalizedQuantitySearchLevel() == NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED) {
//				QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull(qtyParam);
//				if (canonicalQty != null) {
//					String valueFieldPath = fieldPath + "." + QTY_VALUE_NORM;
//					setPrefixedQuantityPredicate(orQuantityTerms, activePrefix, canonicalQty, valueFieldPath);
//					orQuantityTerms.must(myPredicateFactory.match()
//						.field(fieldPath + "." + QTY_CODE_NORM)
//						.matching(canonicalQty.getUnits()));
//					return orQuantityTerms;
//				}
//			}

			// not NORMALIZED_QUANTITY_SEARCH_SUPPORTED or non-canonicalizable parameter
			addQuantityTerms(theTopBool, theIsMust, activePrefix, qtyParam, fieldPath);
		}


		private void addQuantityTerms(BooleanPredicateClausesStep<?> theTopBool, boolean theIsMust,
				ParamPrefixEnum theActivePrefix, QuantityParam theQtyParam, String theFieldPath) {

			String valueFieldPath = theFieldPath + "." + QTY_VALUE;
			PredicateFinalStep rangePred = getPrefixedRangePredicate(theActivePrefix, theQtyParam, valueFieldPath);
			addMustOrShould(theIsMust, theTopBool, rangePred);

			if (isNotBlank(theQtyParam.getSystem())) {
				addFieldPredicate(theIsMust, theTopBool, theFieldPath + "." + QTY_SYSTEM, theQtyParam.getSystem());
			}

			if (isNotBlank(theQtyParam.getUnits())) {
				addFieldPredicate(theIsMust, theTopBool, theFieldPath + "." + QTY_CODE, theQtyParam.getUnits());
			}
		}


		private void addFieldPredicate(boolean theIsMust, BooleanPredicateClausesStep<?> theTopBool, String theFieldPath, String theValue) {
			MatchPredicateOptionsStep<?> pred = myPredicateFactory.match().field(theFieldPath).matching(theValue);
			addMustOrShould(theIsMust, theTopBool, pred);
		}

		private void addMustOrShould(boolean theIsMust, BooleanPredicateClausesStep<?> theTopBool, PredicateFinalStep thePredicate) {
			if (theIsMust) {
				theTopBool.must(thePredicate);
			} else {
				theTopBool.should(thePredicate);
			}

		}


		private PredicateFinalStep getPrefixedRangePredicate(
				ParamPrefixEnum thePrefix, QuantityParam theQuantity, String valueFieldPath) {

			double value = theQuantity.getValue().doubleValue();
			double approxTolerance = value * QTY_APPROX_TOLERANCE_PERCENT;
			double defaultTolerance = value * QTY_TOLERANCE_PERCENT;

			switch (thePrefix) {
				//	searches for resource quantity between passed param value +/- 10%
				case APPROXIMATE:
						return myPredicateFactory.range()
							.field(valueFieldPath)
							.between(value - approxTolerance, value + approxTolerance);

				// searches for resource quantity between passed param value +/- 5%
				case EQUAL:
					return myPredicateFactory.range()
							.field(valueFieldPath)
							.between(value - defaultTolerance, value + defaultTolerance);

				// searches for resource quantity > param value
				case GREATERTHAN:
				case STARTS_AFTER:  // treated as GREATERTHAN because search doesn't handle ranges
					return myPredicateFactory.range()
							.field(valueFieldPath)
							.greaterThan(value);

				// searches for resource quantity not < param value
				case GREATERTHAN_OR_EQUALS:
					return myPredicateFactory.range()
							.field(valueFieldPath)
							.atLeast(value);

				// searches for resource quantity < param value
				case LESSTHAN:
				case ENDS_BEFORE:  // treated as LESSTHAN because search doesn't handle ranges
					return myPredicateFactory.range()
							.field(valueFieldPath)
							.lessThan(value);

				// searches for resource quantity not > param value
				case LESSTHAN_OR_EQUALS:
					return myPredicateFactory.range()
							.field(valueFieldPath)
							.atMost(value);

				// NOT_EQUAL: searches for resource quantity not between passed param value +/- 5%
				case NOT_EQUAL:
					return myPredicateFactory.bool(b -> {
							b.must(myPredicateFactory.range()
								.field(valueFieldPath)
								.between(null, value - defaultTolerance));
							b.must(myPredicateFactory.range()
								.field(valueFieldPath)
								.between(value + defaultTolerance, null));
							b.minimumShouldMatchNumber(1);
						});
			}
			throw new IllegalStateException("Should not reach here");
		}

	}

	private boolean isNested(String theParamName) {
		switch (theParamName) {
			case "_security":
			case "value-quantity":
				return true;
			default:
				return false;
		}
	}

	private String getParamPath(String theParamName) {
		switch (theParamName) {
			case "_security": return "nsp._security.token";
			default:
				fail("Don't know the path name for param: " + theParamName);
		}
		fail("Don't know the path name for param: " + theParamName);
		return null;
	}



	private IIdType createRiskAssessmentWithPredictionProbability(Number theProbability) {
		RiskAssessment ra1 = new RiskAssessment();
		if (theProbability != null) {
			RiskAssessment.RiskAssessmentPredictionComponent component = ra1.addPrediction();
			component.setProbability(new DecimalType(theProbability.doubleValue()));
		}
		return myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless();
	}

	private void assertFindIds(String theMessage, Collection<String> theResourceIds, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertEquals(theResourceIds, new HashSet<>(resourceIds), theMessage);
	}




	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}



}
