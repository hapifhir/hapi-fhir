package ca.uhn.fhir.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntCounterTest {

	private IntCounter myCounter;

	@BeforeEach
	void before() {
		myCounter = new IntCounter(5);
	}


	@Test
	void testIncrement() {
		myCounter.increment(2);
		assertEquals(7, myCounter.get());
	}

	@Test
	void testDecrement() {
		myCounter.decrement(2);
		assertEquals(3, myCounter.get());
	}


}
