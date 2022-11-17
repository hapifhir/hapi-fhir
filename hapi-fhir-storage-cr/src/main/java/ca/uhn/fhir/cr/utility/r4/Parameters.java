package ca.uhn.fhir.cr.utility.r4;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.cr.utility.Ids;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.OidType;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.TimeType;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UrlType;
import org.hl7.fhir.r4.model.UuidType;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for getting or creating r4 parameters
 */
public class Parameters {

	private Parameters() {
	}

	/**
	 * Method to create parameters for fhir r4
	 *
	 * @param theId IdType of measure
	 * @param parts Parameter r4 components
	 * @return parameters
	 */
	public static org.hl7.fhir.r4.model.Parameters parameters(
		IdType theId, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(theId);
		org.hl7.fhir.r4.model.Parameters p = parameters(parts);
		p.setId(theId);
		return p;
	}

	/**
	 * Method to create parameters for fhir r4 model via submission of IdPart
	 *
	 * @param theIdPart String representation of the Id used to create IDtype
	 * @param parts     Parameter r4 components
	 * @return parameters
	 */
	public static org.hl7.fhir.r4.model.Parameters parameters(
		String theIdPart, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(theIdPart);
		return parameters((IdType) Ids.newId(org.hl7.fhir.r4.model.Parameters.class, theIdPart), parts);
	}

	/**
	 * Method to create parameters for fhir r4 model via submission of parameter component
	 *
	 * @param parts Parameter r4 components
	 * @return parameters
	 */
	public static org.hl7.fhir.r4.model.Parameters parameters(
		org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		org.hl7.fhir.r4.model.Parameters p = new org.hl7.fhir.r4.model.Parameters();
		for (org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent c : parts) {
			p.addParameter(c);
		}
		return p;
	}

	/**
	 * Method to create parameters part for fhir r4 model via submission of parameter component
	 *
	 * @param name  String of Parameter component name
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent part(
		String name, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent c = new org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent();
		c.setName(name);
		for (org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent p : parts) {
			c.addPart(p);
		}
		return c;
	}

	/**
	 * Method to create parameters part for fhir r4 model via submission of parameter component
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of parameter component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent part(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		checkNotNull(value);
		org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent c = part(name, parts);
		c.setValue(new StringType(value));
		return c;
	}

	/**
	 * Method to create parameters part for fhir r4 model via submission of parameter component
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of parameter component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent part(
		String name, org.hl7.fhir.r4.model.Type value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		checkNotNull(value);
		org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent c = part(name, parts);
		c.setValue(value);
		return c;
	}

	/**
	 * Method to create parameters part for fhir r4 model via submission of parameter component
	 *
	 * @param name     String of parameter component name
	 * @param resource Model resource type
	 * @param parts    Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent part(
		String name, org.hl7.fhir.r4.model.Resource resource,
		org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		checkNotNull(resource);
		org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent c = part(name, parts);
		c.setResource(resource);
		return c;
	}

	/**
	 * Method to get parameter component part with component part name
	 *
	 * @param name       String of parameter component name
	 * @param parameters fhir model parameters
	 * @return parameter component
	 */
	public static List<org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent> getPartsByName(
		org.hl7.fhir.r4.model.Parameters parameters, String name) {
		checkNotNull(parameters);
		checkNotNull(name);
		return parameters.getParameter().stream().filter(x -> name.equals(x.getName())).collect(Collectors.toList());
	}

	/**
	 * Method to create parameter component with base64binary part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent base64BinaryPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new Base64BinaryType(value), parts);
	}

	/**
	 * Method to create parameter component with boolean part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent booleanPart(
		String name, boolean value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new BooleanType(value), parts);
	}

	/**
	 * Method to create parameter component with canonical part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent canonicalPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new CanonicalType(value), parts);
	}

	/**
	 * Method to create parameter component with code part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent codePart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new CodeType(value), parts);
	}

	/**
	 * Method to create parameter component with date part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent datePart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new DateType(value), parts);
	}

	/**
	 * Method to create parameter component with dateTime part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent dateTimePart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new DateTimeType(value), parts);
	}

	/**
	 * Method to create parameter component with decimal part
	 *
	 * @param name  String of parameter component name
	 * @param value double representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent decimalPart(
		String name, double value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new DecimalType(value), parts);
	}

	/**
	 * Method to create parameter component with id part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent idPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new IdType(value), parts);
	}

	/**
	 * Method to create parameter component with instant part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent instantPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new InstantType(value), parts);
	}

	/**
	 * Method to create parameter component with integer part
	 *
	 * @param name  String of parameter component name
	 * @param value Int representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent integerPart(
		String name, int value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new IntegerType(value), parts);
	}

	/**
	 * Method to create parameter component with markdown part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent markdownPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new MarkdownType(value), parts);
	}

	/**
	 * Method to create parameter component with oid part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent oidPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new OidType(value), parts);
	}

	/**
	 * Method to create parameter component with postivie integer part
	 *
	 * @param name  String of parameter component name
	 * @param value Integer representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent positiveIntPart(
		String name, int value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new PositiveIntType(value), parts);
	}

	/**
	 * Method to create parameter component with String part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent stringPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new StringType(value), parts);
	}

	/**
	 * Method to create parameter component with time part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent timePart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new TimeType(value), parts);
	}

	/**
	 * Method to create parameter component with unsigned int part
	 *
	 * @param name  String of parameter component name
	 * @param value Integer representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent unsignedIntPart(
		String name, int value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new UnsignedIntType(value), parts);
	}

	/**
	 * Method to create parameter component with uri part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent uriPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new UriType(value), parts);
	}

	/**
	 * Method to create parameter component with url part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent urlPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new UrlType(value), parts);
	}

	/**
	 * Method to create parameter component with uuid part
	 *
	 * @param name  String of parameter component name
	 * @param value String representation of component value
	 * @param parts Parameter r4 components
	 * @return parameter component
	 */
	public static org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent uuidPart(
		String name, String value, org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new UuidType(value), parts);
	}
}
