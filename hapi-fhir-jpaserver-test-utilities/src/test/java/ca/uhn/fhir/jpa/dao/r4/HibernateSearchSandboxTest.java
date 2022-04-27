package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import com.google.common.collect.Lists;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.MatchPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_PARAM_NAME;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
@Disabled
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
		public void searchModelingMultipleAndWithOneOringClauseTest() {
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
		public void searchModelingMultipleAndWithMultipleOrClausesTest() {
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


	}

	@Nested
	public class NestedObjectQueries {

		/**
		 * Show that when there is only one and clause with "or" entries, we can add the shoulds
		 * at the top level
		 */
		@Test
		public void searchModelingAndMultipleAndWithOneOringClauseTest() {
			IIdType myResourceId = myTestDataBuilder.createObservation(myTestDataBuilder.withElementAt("valueQuantity",
				myTestDataBuilder.withPrimitiveAttribute("value", 0.6)
//			myTestDataBuilder.withPrimitiveAttribute("system", UCUM_CODESYSTEM_URL),
//			myTestDataBuilder.withPrimitiveAttribute("code", "mm[Hg]")
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
//			long totalHitCount = result.total().hitCount();
//			List<ResourceTable> hits = result.hits();
			});
		}


		/**
		 * Shows that when there is multiple "and" clause with "or" entries, we need to group each one in a "must" clause
		 * to be able to add a minimumShouldMatchNumber(1); to each group
		 */
		@Test
		public void searchModelingMultipleAndWithMultipleOrClausesTest() {
			IIdType myResourceId = myTestDataBuilder.createObservation(myTestDataBuilder.withElementAt("valueQuantity",
				myTestDataBuilder.withPrimitiveAttribute("value", 0.6)
//			myTestDataBuilder.withPrimitiveAttribute("system", UCUM_CODESYSTEM_URL),
//			myTestDataBuilder.withPrimitiveAttribute("code", "mm[Hg]")
			));

			runInTransaction(() -> {
				SearchSession searchSession = Search.session(myEntityManager);
				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
					.where(f -> f.bool(b -> {
						b.must(f.match().field("myResourceType").matching("Observation"));
						b.must(f.nested().objectField("nsp.value-quantity")
							.nest(f.bool()
								.must(f.range().field("nsp.value-quantity.quantity.value").lessThan(0.7))

								.must(f.bool(p -> {
									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.475, 0.525));
									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.57, 0.63));
									p.minimumShouldMatchNumber(1);
								}))

								.must(f.bool(p -> {
									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.2, 0.8));
									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.7, 0.9));
									p.minimumShouldMatchNumber(1);
								}))

								.minimumShouldMatchNumber(1)
							));
					}))
					.fetchAll();
//			long totalHitCount = result.total().hitCount();
//			List<ResourceTable> hits = result.hits();
			});
//			runInTransaction(() -> {
//				SearchSession searchSession = Search.session(myEntityManager);
//				SearchResult<ResourceTable> result = searchSession.search(ResourceTable.class)
//					.where(f -> f.bool(b -> {
//						b.must(f.match().field("myResourceType").matching("Observation"));
//						b.must(f.bool()
//								.must(f.range().field("nsp.value-quantity.quantity.value").lessThan(0.7))
//
//								.must(f.bool(p -> {
//									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.475, 0.525));
//									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.57, 0.63));
//									p.minimumShouldMatchNumber(1);
//								}))
//
//								.must(f.bool(p -> {
//									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.2, 0.8));
//									p.should(f.range().field("nsp.value-quantity.quantity.value").between(0.7, 0.9));
//									p.minimumShouldMatchNumber(1);
//								}))
//
//								.minimumShouldMatchNumber(1)
//							);
//					}))
//					.fetchAll();
////			long totalHitCount = result.total().hitCount();
////			List<ResourceTable> hits = result.hits();
//			});

		}
	}


	/**
	 * Following code is the beginning of refactoring the queries for cleaner structure, which means
	 * to try to achieve the clean query structure modeled by previous tests, but using generic methods
	 */
	@Nested
	public class FragmentedCodeNotNested {

		private SearchPredicateFactory fact;


		@Test
		public void searchModelingMultipleAndOneOrClauseTest() {
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
		public void searchModelingMultipleAndMultipleOrClauseTest() {
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

			theQuantityAndOrTerms.add(Lists.newArrayList(
				new QuantityParam().setValue(0.9),
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


	}




	private static class TestPredBuilder {

		private static final double QTY_APPROX_TOLERANCE_PERCENT = .10;
		private static final double QTY_TOLERANCE_PERCENT = .05;

		SearchPredicateFactory myPredicateFactory;

		public TestPredBuilder(SearchPredicateFactory theF) { myPredicateFactory = theF; }


		public PredicateFinalStep buildAndOrPredicates(
				String theSearchParamName, List<List<IQueryParameterType>> theAndOrTerms) {

			boolean isNested = isNested(theSearchParamName);

			// we need to know if there is more than one "and" predicate (outer list) with more than one "or" predicate (inner list)
			long maxOrPredicateSize = theAndOrTerms.stream().map(List::size).filter(s -> s > 1).count();

			BooleanPredicateClausesStep<?> topBool = myPredicateFactory.bool();
			topBool.must(myPredicateFactory.match().field("myResourceType").matching("Observation"));

			BooleanPredicateClausesStep<?> activeBool = topBool;
			if (isNested) {
				BooleanPredicateClausesStep<?> nestedBool = myPredicateFactory.bool();
				activeBool = nestedBool;
			}

			for (List<IQueryParameterType> andTerm : theAndOrTerms) {
				if (andTerm.size() == 1) {
					// buildSinglePredicate
//					activeBool.must(myPredicateFactory.match().field("nsp.value-quantity.quantity.value").matching(0.7));
					addOnePredicate(activeBool, true, theSearchParamName, andTerm.get(0));
					continue;
				}

				if (maxOrPredicateSize <= 1) {
					// this is the only list of or predicates with more than 1 entry so
					// no need to separate it in a group. Can be part of main and clauses
					for (IQueryParameterType orTerm : andTerm) {
						addOnePredicate(activeBool, false, theSearchParamName, orTerm);
					}
					activeBool.minimumShouldMatchNumber(1);

				} else {
					// this is not the only list of or predicates with more than 1 entry
					// so all of them need to be separated in groups with a minimumShouldMatchNumber(1)
					activeBool.must(myPredicateFactory.bool(p -> {
						for (IQueryParameterType orTerm : andTerm) {
							addOnePredicate(p, false, theSearchParamName, orTerm);
						}
						p.minimumShouldMatchNumber(1);
					}));
				}
			}

			if (isNested) {
				topBool.must(myPredicateFactory.nested().objectField("nsp.value-quantity").nest(activeBool));
			}
			return topBool;
		}



		private boolean isNested(String theSearchParamName) {
			if (theSearchParamName.equals("value-quantity")) {
				return true;
			}

			return false;
		}


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
							b.should(myPredicateFactory.range()
								.field(valueFieldPath)
								.between(null, value - defaultTolerance));
							b.should(myPredicateFactory.range()
								.field(valueFieldPath)
								.between(value + defaultTolerance, null));
							b.minimumShouldMatchNumber(1);
						});
			}
			throw new IllegalStateException("Should not reach here");
		}

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
