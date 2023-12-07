/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodingConceptProperty;
import ca.uhn.fhir.context.support.IValidationSupport.ConceptDesignation;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class LookupCodeUtil {
	public static final String ourCodeSystemId = "CodeSystem-Example",
			ourCodeSystemUrl = "http://example/" + ourCodeSystemId;
	public static final String ourCode = "Code-WithProperties";
	public static final String ourPropertyA = "Property-A", ourPropertyB = "Property-B", ourPropertyC = "Property-C";
	public static final String ourPropertyValueA = "Value-A", ourPropertyValueB = "Value-B";
	public static final String propertyCodeSystem = "CodeSystem-C",
			propertyCode = "Code-C",
			propertyDisplay = "Display-C";

	public static Stream<Arguments> parametersPropertyNames() {
		return Stream.of(
				arguments(Collections.emptyList(), List.of(ourPropertyA, ourPropertyB, ourPropertyC)),
				arguments(List.of(ourPropertyB), List.of(ourPropertyB)),
				arguments(
						List.of(ourPropertyA, ourPropertyB, ourPropertyC),
						List.of(ourPropertyA, ourPropertyB, ourPropertyC)),
				arguments(List.of(ourPropertyB, ourPropertyA), List.of(ourPropertyB, ourPropertyA)),
				arguments(List.of(ourPropertyA, ourPropertyA), List.of(ourPropertyA, ourPropertyA)),
				arguments(List.of(ourPropertyB, "ABC"), List.of(ourPropertyB)),
				arguments(List.of("ABC", ourPropertyA), List.of(ourPropertyA)),
				arguments(List.of("ABC"), Collections.emptyList()));
	}
	public static Stream<Arguments> parametersPropertiesAndDesignations() {
		Collection<IValidationSupport.BaseConceptProperty> properties = List.of(
			new IValidationSupport.StringConceptProperty("birthDate", "1930-01-01"),
			new IValidationSupport.StringConceptProperty("propertyString", "value"),
			new CodingConceptProperty("propertyCode", "code", "system", "display"));
		Collection<ConceptDesignation> designations = List.of(
			new ConceptDesignation().setLanguage("en").setUseCode("code1").setUseSystem("system-1").setUseDisplay("display").setValue("some value"),
			new ConceptDesignation().setLanguage("en").setUseCode("code2").setUseSystem("system-1").setUseDisplay("display").setValue("some value"),
			new ConceptDesignation().setLanguage("en").setUseCode("code1").setUseSystem("system-2").setUseDisplay("display").setValue("some value"),
			new ConceptDesignation().setUseCode("code2").setUseSystem("system1").setUseDisplay("display").setValue("some value"),
			new ConceptDesignation().setUseCode("code3").setUseSystem("system1").setValue("some value"),
			new ConceptDesignation().setUseCode("code4").setUseSystem("system1"));

		return Stream.of(
			Arguments.arguments(properties, Collections.emptyList(), designations),
			Arguments.arguments(properties, Collections.emptyList(), designations),
			Arguments.arguments(properties, List.of(
				new IValidationSupport.StringConceptProperty("birthDate", "1930-01-01")), designations),
			Arguments.arguments(properties, List.of(
				new IValidationSupport.StringConceptProperty("birthDate", "1930-01-01"))),
			Arguments.arguments(properties, List.of(
				new CodingConceptProperty("propertyCode", "code", "system", "display"))),
			Arguments.arguments(properties, List.of(
				new IValidationSupport.StringConceptProperty("birthDate", "1930-01-01"),
				new IValidationSupport.StringConceptProperty("propertyString", "value"),
				new CodingConceptProperty("propertyCode", "code", "system", "display")))
		);
	}
}
