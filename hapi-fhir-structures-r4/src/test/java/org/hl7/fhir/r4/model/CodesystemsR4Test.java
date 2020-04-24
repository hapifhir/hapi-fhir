package org.hl7.fhir.r4.model;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.hamcrest.Matchers;
import org.junit.*;

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
