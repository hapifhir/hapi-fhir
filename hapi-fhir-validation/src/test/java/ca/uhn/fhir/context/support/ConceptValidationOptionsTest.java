package ca.uhn.fhir.context.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ConceptValidationOptions}, which is part of the validation cache key.
 */
// Created by Claude Opus 5.5
class ConceptValidationOptionsTest {

	@Test
	void copy_valueSetUrlUnresolved_isCopiedAndPartOfEquality() {
		ConceptValidationOptions unresolved = new ConceptValidationOptions().setValueSetUrlUnresolved(true);

		ConceptValidationOptions copy = ConceptValidationOptions.copy(unresolved);

		assertTrue(copy.isValueSetUrlUnresolved());
		assertEquals(unresolved, copy);
		assertEquals(unresolved.hashCode(), copy.hashCode());
		assertNotEquals(new ConceptValidationOptions(), unresolved);
	}
}
