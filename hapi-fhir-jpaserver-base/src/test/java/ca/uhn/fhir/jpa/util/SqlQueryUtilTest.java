package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SqlQueryUtilTest {

	@Mock
	private HibernatePropertiesProvider myHibernatePropertiesProvider;

	@Spy
	private SqlQueryUtil tested = spy(new SqlQueryUtil(myHibernatePropertiesProvider));


	@BeforeEach
	void setUp() {
		tested = spy(new SqlQueryUtil(myHibernatePropertiesProvider));
	}


	@Test
	public void emptyCollectionReturnsFalse() {
		String result = tested.buildInList("x.id", Collections.emptyList(), 10);

		assertEquals(" 1=2 -- replaced empty 'in' parameter list: x.id in () " +
			System.getProperty("line.separator"), result);
	}

	@Nested
	public class WhenUtilityCheckingDialect {

		@BeforeEach
		void setUp() {
			when(myHibernatePropertiesProvider.getDialect()).thenReturn(new Oracle12cDialect());
		}

		@Nested
		public class ForNumberLists {

			@Test
			public void buildInListNotOracle() {
				when(myHibernatePropertiesProvider.getDialect()).thenReturn(new PostgreSQL9Dialect());
				String expected = " x.id in (1, 12, 123) ";

				String generated = tested.buildInListIfNeeded("x.id", List.of(1L, 12L, 123L));

				assertEquals(expected, generated);
				verify(tested, never()).buildInList(any(), any(), eq(1_000) );
			}


			@Test
			public void buildInListLessThanOneThousand() {
				when(myHibernatePropertiesProvider.getDialect()).thenReturn(new Oracle12cDialect());
				String expected = " x.id in (1, 2, 3, 4, 5) ";

				String generated = tested.buildInListIfNeeded("x.id", List.of(1L, 2L, 3L, 4L, 5L));

				assertEquals(expected, generated);
				verify(tested, never()).buildInList(any(), any(), eq(1_000) );
			}


			@Test
			public void buildInListOracleAndMoreThanOneThousand() {
				when(myHibernatePropertiesProvider.getDialect()).thenReturn(new Oracle12cDialect());
				String expected = " ( x.id in (" +
					toCsv(getLongListOfSize(0, 1_000)) + ") or x.id in (" +
					toCsv(getLongListOfSize(1_000, 1000)) + ") or x.id in (" +
					toCsv(getLongListOfSize(2_000, 10)) + ") ) ";

				List<Long> bigList = getLongListOfSize(0, 2_010);

				String generated = tested.buildInListIfNeeded("x.id", bigList);

				assertEquals(expected, generated);
				verify(tested, times(1)).buildInList("x.id", bigList, 1_000 );
			}


			private List<Long> getLongListOfSize(int theFirstIncluded, int theSize) {
				return LongStream.range(theFirstIncluded, theFirstIncluded + theSize).boxed().collect(Collectors.toList());
			}

			private String toCsv(Collection<Long> theCollection) {
				return theCollection.stream().map(String::valueOf).collect(Collectors.joining(", "));
			}
		}

	}

	@Nested
	public class WhenUtilityNotCheckingDialect {

		@Nested
		public class ForNumberLists {

			@Test
			public void buildInListNotSplitting() {
				String expected = " ( x.id in (1, 12, 123) ) ";

				String generated = tested.buildInList("x.id", List.of(1L, 12L, 123L), 10);

				assertEquals(expected, generated);
			}


			@Test
			public void buildInListSplitting() {
				String expected = " ( x.id in (1, 2, 3) or x.id in (4, 5) ) ";

				String generated = tested.buildInList("x.id", List.of(1L, 2L, 3L, 4L, 5L), 3);

				assertEquals(expected, generated);
			}

		}
	}

}
