package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.util.CoordCalculator;
import ca.uhn.fhir.jpa.util.CoordCalculatorTest;
import ca.uhn.fhir.jpa.util.SearchBox;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PredicateBuilderCoordsTest {
	PredicateBuilderCoords myPredicateBuilderCoords;
	private SearchBuilder mySearchBuilder;
	private CriteriaBuilder myBuilder;
	private From myFrom;

	@Before
	public void before() {
		mySearchBuilder = mock(SearchBuilder.class);
		myBuilder = mock(CriteriaBuilder.class);
		myFrom = mock(From.class);
		myPredicateBuilderCoords = new PredicateBuilderCoords(mySearchBuilder);
	}

	@Test
	public void testLongitudePredicateFromBox() {
		SearchBox box = CoordCalculator.getBox(CoordCalculatorTest.LATITUDE_CHIN, CoordCalculatorTest.LONGITUDE_CHIN, CoordCalculatorTest.DISTANCE_TAVEUNI);
		assertThat(box.getNorthEast().getLongitude(), greaterThan(box.getSouthWest().getLongitude()));

		ArgumentCaptor<Predicate> andLeft = ArgumentCaptor.forClass(Predicate.class);
		ArgumentCaptor<Predicate> andRight = ArgumentCaptor.forClass(Predicate.class);

		ArgumentCaptor<Double> gteValue = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> lteValue = ArgumentCaptor.forClass(Double.class);

		Predicate gte = mock(Predicate.class);
		Predicate lte = mock(Predicate.class);
		when(myBuilder.greaterThanOrEqualTo(any(), gteValue.capture())).thenReturn(gte);
		when(myBuilder.lessThanOrEqualTo(any(),lteValue.capture())).thenReturn(lte);
		myPredicateBuilderCoords.longitudePredicateFromBox(myBuilder, myFrom, box);
		verify(myBuilder).and(andLeft.capture(), andRight.capture());
		assertEquals(andLeft.getValue(), gte);
		assertEquals(andRight.getValue(), lte);
		assertEquals(gteValue.getValue(), box.getSouthWest().getLongitude());
		assertEquals(lteValue.getValue(), box.getNorthEast().getLongitude());
	}

	@Test
	public void testAntiMeridianLongitudePredicateFromBox() {
		SearchBox box = CoordCalculator.getBox(CoordCalculatorTest.LATITUDE_TAVEUNI, CoordCalculatorTest.LONGITIDE_TAVEUNI, CoordCalculatorTest.DISTANCE_TAVEUNI);
		assertThat(box.getNorthEast().getLongitude(), lessThan(box.getSouthWest().getLongitude()));
		assertTrue(box.crossesAntiMeridian());

		ArgumentCaptor<Predicate> orLeft = ArgumentCaptor.forClass(Predicate.class);
		ArgumentCaptor<Predicate> orRight = ArgumentCaptor.forClass(Predicate.class);

		ArgumentCaptor<Double> gteValue = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> lteValue = ArgumentCaptor.forClass(Double.class);

		Predicate gte = mock(Predicate.class);
		Predicate lte = mock(Predicate.class);
		when(myBuilder.greaterThanOrEqualTo(any(), gteValue.capture())).thenReturn(gte);
		when(myBuilder.lessThanOrEqualTo(any(),lteValue.capture())).thenReturn(lte);
		myPredicateBuilderCoords.longitudePredicateFromBox(myBuilder, myFrom, box);
		verify(myBuilder).or(orLeft.capture(), orRight.capture());
		assertEquals(orLeft.getValue(), gte);
		assertEquals(orRight.getValue(), lte);
		assertEquals(gteValue.getValue(), box.getNorthEast().getLongitude());
		assertEquals(lteValue.getValue(), box.getSouthWest().getLongitude());
	}

}
