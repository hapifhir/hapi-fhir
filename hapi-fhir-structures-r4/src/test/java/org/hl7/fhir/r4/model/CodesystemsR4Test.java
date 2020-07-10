package org.hl7.fhir.r4.model;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*; import static org.hamcrest.MatcherAssert.assertThat;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.ValidationResult;

public class CodesystemsR4Test {

	@Test
	public void testCodesystemsPresent() throws ClassNotFoundException {
		Class.forName(org.hl7.fhir.r4.model.codesystems.W3cProvenanceActivityType.class.getName());
	}
}
