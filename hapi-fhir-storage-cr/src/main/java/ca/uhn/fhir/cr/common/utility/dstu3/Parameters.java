package ca.uhn.fhir.cr.common.utility.dstu3;

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

import ca.uhn.fhir.cr.common.utility.Ids;
import org.hl7.fhir.dstu3.model.Base64BinaryType;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.OidType;
import org.hl7.fhir.dstu3.model.PositiveIntType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.TimeType;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.dstu3.model.UriType;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for getting or creating DSTU3 model parameters
 */
public class Parameters {

	private Parameters() {
	}

	/**
	 * Method to create parameters for fhir Dstu3
	 *
	 * @param theId IdType of measure
	 * @param parts Parameter dstu3 components
	 * @return parameters
	 */
	public static org.hl7.fhir.dstu3.model.Parameters parameters(
		IdType theId, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(theId);
		org.hl7.fhir.dstu3.model.Parameters p = parameters(parts);
		p.setId(theId);
		return p;
	}

	/**
	 * Method to create parameters for fhir Dstu3
	 *
	 * @param theIdPart String representation of Id parameter
	 * @param parts     Parameter dstu3 components
	 * @return parameters
	 */
	public static org.hl7.fhir.dstu3.model.Parameters parameters(
		String theIdPart, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(theIdPart);
		return parameters((IdType) Ids.newId(org.hl7.fhir.dstu3.model.Parameters.class, theIdPart), parts);
	}

	/**
	 * Method to create parameters for fhir Dstu3 from component parts
	 *
	 * @param parts Parameter dstu3 components
	 * @return parameters
	 */
	public static org.hl7.fhir.dstu3.model.Parameters parameters(
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		org.hl7.fhir.dstu3.model.Parameters p = new org.hl7.fhir.dstu3.model.Parameters();
		for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent c : parts) {
			p.addParameter(c);
		}
		return p;
	}

	/**
	 * Method to create parameter part for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of component value
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent part(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		checkNotNull(value);
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent c = part(name, parts);
		c.setValue(new StringType(value));
		return c;
	}

	/**
	 * Method to create parameter part for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent part(
		String name, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent c = new org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent();
		c.setName(name);
		for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent p : parts) {
			c.addPart(p);
		}
		return c;
	}

	/**
	 * Method to create parameter part for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of component value
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent part(
		String name, org.hl7.fhir.dstu3.model.Type value,
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		checkNotNull(value);
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent c = part(name, parts);
		c.setValue(value);
		return c;
	}

	/**
	 * Method to create parameter part for fhir Dstu3
	 *
	 * @param name     String name of component parameter
	 * @param resource dstu3 fhir model used
	 * @param parts    Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent part(
		String name, org.hl7.fhir.dstu3.model.Resource resource,
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		checkNotNull(name);
		checkNotNull(resource);
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent c = part(name, parts);
		c.setResource(resource);
		return c;
	}

	/**
	 * Method to get parameter parts by parameter component name for fhir Dstu3
	 *
	 * @param name       String name of component parameter
	 * @param parameters dstu3 model resource parameters
	 * @return parameter
	 */
	public static List<org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent> getPartsByName(
		org.hl7.fhir.dstu3.model.Parameters parameters, String name) {
		checkNotNull(parameters);
		checkNotNull(name);
		return parameters.getParameter().stream().filter(x -> name.equals(x.getName())).collect(Collectors.toList());
	}

	/**
	 * Method to create parameter part base64binary for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent base64BinaryPart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new Base64BinaryType(value), parts);
	}

	/**
	 * Method to create boolean part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent booleanPart(
		String name, boolean value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new BooleanType(value), parts);
	}

	/**
	 * Method to create code part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value boolean representation of parameter component
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent codePart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new CodeType(value), parts);
	}

	/**
	 * Method to create date part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent datePart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new DateType(value), parts);
	}

	/**
	 * Method to create dateTime part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent dateTimePart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new DateTimeType(value), parts);
	}

	/**
	 * Method to create decimal part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value double representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent decimalPart(
		String name, double value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new DecimalType(value), parts);
	}

	/**
	 * Method to create id part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent idPart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new IdType(value), parts);
	}

	/**
	 * Method to create instant part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent instantPart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new InstantType(value), parts);
	}

	/**
	 * Method to create integer part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value Integer representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent integerPart(
		String name, int value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new IntegerType(value), parts);
	}

	/**
	 * Method to create oid part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent oidPart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new OidType(value), parts);
	}

	/**
	 * Method to create postive integer part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value Integer representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent positiveIntPart(
		String name, int value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new PositiveIntType(value), parts);
	}

	/**
	 * Method to create code part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent stringPart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new StringType(value), parts);
	}

	/**
	 * Method to create time part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent timePart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new TimeType(value), parts);
	}

	/**
	 * Method to create unsigned integer part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value Integer representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent unsignedIntPart(
		String name, int value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new UnsignedIntType(value), parts);
	}

	/**
	 * Method to create uri part parameter for fhir Dstu3
	 *
	 * @param name  String name of component parameter
	 * @param value String representation of parameter component type
	 * @param parts Parameter dstu3 components
	 * @return parameter
	 */
	public static org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent uriPart(
		String name, String value, org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent... parts) {
		return part(name, new UriType(value), parts);
	}
}
