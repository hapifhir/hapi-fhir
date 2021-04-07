package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.util.CoordCalculator;
import ca.uhn.fhir.jpa.util.CoordCalculatorTest;
import org.hibernate.search.engine.spatial.GeoBoundingBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PredicateBuilderCoordsTest {
	PredicateBuilderCoords myPredicateBuilderCoords;
	private LegacySearchBuilder mySearchBuilder;
	private CriteriaBuilder myBuilder;
	private From myFrom;

	@BeforeEach
	public void before() {
		mySearchBuilder = mock(LegacySearchBuilder.class);
		myBuilder = mock(CriteriaBuilder.class);
		myFrom = mock(From.class);
		myPredicateBuilderCoords = new PredicateBuilderCoords(mySearchBuilder);
	}

	@Test
	public void testLongitudePredicateFromBox() {
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTest.LATITUDE_CHIN, CoordCalculatorTest.LONGITUDE_CHIN, CoordCalculatorTest.DISTANCE_TAVEUNI);
		assertThat(box.bottomRight().longitude(), greaterThan(box.topLeft().longitude()));

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
		assertEquals(gteValue.getValue(), box.topLeft().longitude());
		assertEquals(lteValue.getValue(), box.bottomRight().longitude());
	}

	@Test
	public void testAntiMeridianLongitudePredicateFromBox() {
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTest.LATITUDE_TAVEUNI, CoordCalculatorTest.LONGITIDE_TAVEUNI, CoordCalculatorTest.DISTANCE_TAVEUNI);
		assertThat(box.bottomRight().longitude(), lessThan(box.topLeft().longitude()));
		assertTrue(box.bottomRight().longitude() < box.topLeft().longitude());

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
		assertEquals(gteValue.getValue(), box.bottomRight().longitude());
		assertEquals(lteValue.getValue(), box.topLeft().longitude());
	}

}
