package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import com.google.common.collect.Lists;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl.DEFAULT_MASS_INDEXER_OBJECT_LOADING_THREADS;
import static ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl.MAX_MASS_INDEXER_OBJECT_LOADING_THREADS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ITermReadSvcTest {

	private final ITermReadSvc testedClass = new TermReadSvcR4();

	@Mock private ITermValueSetDao myTermValueSetDao;
	@Mock private DaoRegistry myDaoRegistry;
	@Mock private IFhirResourceDao<CodeSystem> myFhirResourceDao;


	@Nested
	public class FindCurrentTermValueSet {

		@BeforeEach
		public void setup() {
			ReflectionTestUtils.setField(testedClass, "myTermValueSetDao", myTermValueSetDao);
		}

		@Test
		void forLoinc() {
			String valueSetId = "a-loinc-value-set";
			testedClass.findCurrentTermValueSet("http://loinc.org/vs/" + valueSetId);

			verify(myTermValueSetDao, times(1)).findTermValueSetByForcedId(valueSetId);
			verify(myTermValueSetDao, never()).findTermValueSetByUrl(isA(Pageable.class), anyString());
		}

		@Test
		void forNotLoinc() {
			String valueSetId = "not-a-loin-c-value-set";
			testedClass.findCurrentTermValueSet("http://not-loin-c.org/vs/" + valueSetId);

			verify(myTermValueSetDao, never()).findTermValueSetByForcedId(valueSetId);
			verify(myTermValueSetDao, times(1)).findTermValueSetByUrl(isA(Pageable.class), anyString());
		}
	}


	@Nested
	public class GetValueSetId {

		@Test
		void doesntStartWithGenericVSReturnsEmpty() {
			Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId("http://boing.org");
			assertFalse(vsIdOpt.isPresent());
		}

		@Test
		void doesntStartWithGenericVSPlusSlashReturnsEmpty() {
			Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId("http://loinc.org/vs-no-slash-after-vs");
			assertFalse(vsIdOpt.isPresent());
		}

		@Test
		void blankVsIdReturnsEmpty() {
			Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId("http://loinc.org");
			assertFalse(vsIdOpt.isPresent());
		}

		@Test
		void startsWithGenericPlusSlashPlusIdReturnsValid() {
			Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId("http://loinc.org/vs/some-vs-id");
			assertTrue(vsIdOpt.isPresent());
		}

	}


	@Nested
	public class IsLoincUnversionedCodeSystem {

		@Test
		void doesntContainLoincReturnsFalse() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedCodeSystem("http://boing.org");
			assertFalse(ret);
		}

		@Test
		void hasVersionReturnsFalse() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedCodeSystem("http://boing.org|v2.68");
			assertFalse(ret);
		}

		@Test
		void containsLoincAndNoVersionReturnsTrue() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedCodeSystem("http://anything-plus-loinc.org");
			assertTrue(ret);
		}
	}

	@Nested
	public class IsLoincUnversionedValueSet {

		@Test
		void notLoincReturnsFalse() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedValueSet("http://anything-but-loin-c.org");
			assertFalse(ret);
		}

		@Test
		void isLoincAndHasVersionReturnsFalse() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedValueSet("http://loinc.org|v2.67");
			assertFalse(ret);
		}

		@Test
		void isLoincNoVersionButEqualsLoincAllReturnsTrue() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedValueSet("http://loinc.org/vs");
			assertTrue(ret);
		}

		@Test
		void isLoincNoVersionStartsWithGenericValueSetPlusSlashPlusIdReturnsTrue() {
			boolean ret = TermReadSvcUtil.isLoincUnversionedValueSet("http://loinc.org/vs/vs-id");
			assertTrue(ret);
		}

	}


	@Nested
	public class ReadByForcedId {

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private EntityManager myEntityManager;

		@Mock private ResourceTable resource1;
		@Mock private ResourceTable resource2;
		@Mock private IBaseResource myCodeSystemResource;


		@BeforeEach
		public void setup() {
			ReflectionTestUtils.setField(testedClass, "myEntityManager", myEntityManager);
		}


		@Test
		void getNoneReturnsOptionalEmpty() {
			when(myEntityManager.createQuery(anyString()).getResultList())
				.thenReturn(Collections.emptyList());

			Optional<IBaseResource> result = testedClass.readCodeSystemByForcedId("a-cs-id");
			assertFalse(result.isPresent());
		}

		@Test
		void getMultipleThrows() {
			when(myEntityManager.createQuery(anyString()).getResultList())
				.thenReturn(Lists.newArrayList(resource1, resource2));

			NonUniqueResultException thrown = assertThrows(
				NonUniqueResultException.class,
				() -> testedClass.readCodeSystemByForcedId("a-cs-id"));

			assertTrue(thrown.getMessage().contains("More than one CodeSystem is pointed by forcedId:"));
		}

		@Test
		void getOneConvertToResource() {
			ReflectionTestUtils.setField(testedClass, "myDaoRegistry", myDaoRegistry);

			when(myEntityManager.createQuery(anyString()).getResultList())
				.thenReturn(Lists.newArrayList(resource1));
			when(myDaoRegistry.getResourceDao("CodeSystem")).thenReturn(myFhirResourceDao);
			when(myFhirResourceDao.toResource(resource1, false)).thenReturn(myCodeSystemResource);


			testedClass.readCodeSystemByForcedId("a-cs-id");


			verify(myFhirResourceDao, times(1)).toResource(any(), eq(false));
		}

	}


	@Nested
	public class TestGetMultipleCodeParentPids {

		public static final String CODE_1 = "code-1";
		public static final String CODE_2 = "code-2";
		public static final String CODE_3 = "code-3";
		public static final String CODE_4 = "code-4";
		public static final String CODE_5 = "code-5";

		@Mock TermConcept termConceptCode1;
		@Mock TermConcept termConceptCode3;
		@Mock TermConcept termConceptCode4;

		@Test
		public void morePropertiesThanValues() {
			when(termConceptCode1.getCode()).thenReturn(CODE_1);
			when(termConceptCode3.getCode()).thenReturn(CODE_3);
			when(termConceptCode4.getCode()).thenReturn(CODE_4);

			List<TermConcept> termConcepts = Lists.newArrayList(termConceptCode1, termConceptCode3, termConceptCode4);
			List<String> values = Arrays.asList(CODE_1, CODE_2, CODE_3, CODE_4, CODE_5);
			String msg = ReflectionTestUtils.invokeMethod(
				testedClass, "getTermConceptsFetchExceptionMsg", termConcepts, values);

			assertNotNull(msg);
			assertTrue(msg.contains("No TermConcept(s) were found"));
			assertFalse(msg.contains(CODE_1));
			assertTrue(msg.contains(CODE_2));
			assertFalse(msg.contains(CODE_3));
			assertFalse(msg.contains(CODE_4));
			assertTrue(msg.contains(CODE_5));
		}

		@Test
		void moreValuesThanProperties() {
			when(termConceptCode1.getCode()).thenReturn(CODE_1);
			when(termConceptCode1.getId()).thenReturn(1L);
			when(termConceptCode3.getCode()).thenReturn(CODE_3);
			when(termConceptCode3.getId()).thenReturn(3L);

			List<TermConcept> termConcepts = Lists.newArrayList(termConceptCode1, termConceptCode3);
			List<String> values = List.of(CODE_3);
			String msg = ReflectionTestUtils.invokeMethod(
				testedClass, "getTermConceptsFetchExceptionMsg", termConcepts, values);

			assertNotNull(msg);
			assertTrue(msg.contains("More TermConcepts were found than indicated codes"));
			assertFalse(msg.contains("Queried codes: [" + CODE_3 + "]"));
			assertTrue(msg.contains("Obtained TermConcept IDs, codes: [1, code-1; 3, code-3]"));
		}
	}


	@Nested
	public class TestReindexTerminology {
		@Mock private SearchSession mySearchSession;

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private MassIndexer myMassIndexer;

		@Mock private IFulltextSearchSvc myFulltextSearchSvc;
		@Mock private ITermDeferredStorageSvc myDeferredStorageSvc;
		@Mock private HibernatePropertiesProvider myHibernatePropertiesProvider;

		@InjectMocks
		@Spy private BaseTermReadSvcImpl myTermReadSvc = (BaseTermReadSvcImpl) spy(testedClass);


		@Test
		void testReindexTerminology() throws InterruptedException {
			doReturn(mySearchSession).when(myTermReadSvc).getSearchSession();
			doReturn(false).when(myTermReadSvc).isBatchTerminologyTasksRunning();
			doReturn(10).when(myTermReadSvc).calculateObjectLoadingThreadNumber();
			when(mySearchSession.massIndexer(TermConcept.class)).thenReturn(myMassIndexer);

			myTermReadSvc.reindexTerminology();
			verify(mySearchSession, times(1)).massIndexer(TermConcept.class);
		}

		@Nested
		public class TestCalculateObjectLoadingThreadNumber {

			private final BasicDataSource myBasicDataSource = new BasicDataSource();
			private final ProxyDataSource myProxyDataSource = new ProxyDataSource(myBasicDataSource) ;

			@BeforeEach
			void setUp() {
				doReturn(myProxyDataSource).when(myHibernatePropertiesProvider).getDataSource();
			}

			@Test
			void testLessThanSix() {
				myBasicDataSource.setMaxTotal(5);

				int retMaxConnectionSize = myTermReadSvc.calculateObjectLoadingThreadNumber();

				assertEquals(1, retMaxConnectionSize);
			}

			@Test
			void testMoreThanSixButLessThanLimit() {
				myBasicDataSource.setMaxTotal(10);

				int retMaxConnectionSize = myTermReadSvc.calculateObjectLoadingThreadNumber();

				assertEquals(5, retMaxConnectionSize);
			}

			@Test
			void testMoreThanSixAndMoreThanLimit() {
				myBasicDataSource.setMaxTotal(35);

				int retMaxConnectionSize = myTermReadSvc.calculateObjectLoadingThreadNumber();

				assertEquals(MAX_MASS_INDEXER_OBJECT_LOADING_THREADS, retMaxConnectionSize);
			}

		}

		@Nested
		public class TestCalculateObjectLoadingThreadNumberDefault {

			@Mock private DataSource myDataSource = new BasicDataSource();

			@BeforeEach
			void setUp() {
				doReturn(myDataSource).when(myHibernatePropertiesProvider).getDataSource();
			}

			@Test
			void testDefaultWhenCantGetMaxConnections() {
				int retMaxConnectionSize = myTermReadSvc.calculateObjectLoadingThreadNumber();

				assertEquals(DEFAULT_MASS_INDEXER_OBJECT_LOADING_THREADS, retMaxConnectionSize);
			}

		}
	}
}
