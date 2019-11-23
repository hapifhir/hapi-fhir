package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchBuilderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchBuilderTest.class);

	@Test
	public void testCalculateMultiplierEqualNoDecimal() {
		BigDecimal in = new BigDecimal("200");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertEquals("0.5", out.toPlainString());
	}
	
	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_() {
		BigDecimal in = new BigDecimal("200.");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertEquals("0.5", out.toPlainString());
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision123_010() {
		BigDecimal in = new BigDecimal("123.010");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.0005"));
		
		BigDecimal low = in.subtract(out, MathContext.DECIMAL64);
		BigDecimal high = in.add(out, MathContext.DECIMAL64);
		ourLog.info("{} <= {} <= {}", new Object[] {low.toPlainString(), in.toPlainString(), high.toPlainString()});
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_0() {
		BigDecimal in = new BigDecimal("200.0");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.05000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_3() {
		BigDecimal in = new BigDecimal("200.3");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.05000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_300() {
		BigDecimal in = new BigDecimal("200.300");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.0005000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_30000000() {
		BigDecimal in = new BigDecimal("200.30000000");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.000000005000000"));
	}

	@Test
	public void testCalculateMultiplierEqualDecimalPrecision200_300000001() {
		BigDecimal in = new BigDecimal("200.300000001");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.EQUAL, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("0.0000000005000000"));
	}

	@Test
	public void testCalculateMultiplierApprox() {
		BigDecimal in = new BigDecimal("200");
		BigDecimal out = SearchBuilder.calculateFuzzAmount(ParamPrefixEnum.APPROXIMATE, in);
		ourLog.info(out.toPlainString());
		assertThat(out.toPlainString(), startsWith("20.000"));
	}

	@Test
	public void testIncludeIterator() {
		BaseHapiFhirDao<?> mockDao = mock(BaseHapiFhirDao.class);
		when(mockDao.getConfig()).thenReturn(new DaoConfig());
		SearchBuilder searchBuilder = new SearchBuilder(mockDao);

		searchBuilder.setParamsForUnitTest(new SearchParameterMap());
		EntityManager mockEntityManager = mock(EntityManager.class);
		searchBuilder.setEntityManagerForUnitTest(mockEntityManager);

		Set<ResourcePersistentId> pidSet = new HashSet<>();
		pidSet.add(new ResourcePersistentId(1L));
		pidSet.add(new ResourcePersistentId(2L));

		TypedQuery mockQuery = mock(TypedQuery.class);
		when(mockEntityManager.createQuery(any(), any())).thenReturn(mockQuery);
		List<ResourceLink> resultList = new ArrayList<>();
		ResourceLink link = new ResourceLink();
		ResourceTable target = new ResourceTable();
		target.setId(1L);
		link.setTargetResource(target);
		resultList.add(link);
		when(mockQuery.getResultList()).thenReturn(resultList);

		SearchBuilder.IncludesIterator includesIterator = searchBuilder.new IncludesIterator(pidSet, null);
		// hasNext() should return false if the pid added was already on our list going in.
		assertFalse(includesIterator.hasNext());
	}
}
