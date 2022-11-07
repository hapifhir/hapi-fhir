package ca.uhn.fhir.cr.common.utility;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class Parameters {

	private Parameters() {
	}

	private static BaseRuntimeChildDefinition getParameterChild(FhirContext fhirContext) {
		return fhirContext.getResourceDefinition("Parameters").getChildByName("parameter");
	}

	private static BaseRuntimeElementDefinition<?> getParameterElement(FhirContext fhirContext) {
		return getParameterChild(fhirContext).getChildByName("parameter");
	}

	private static BaseRuntimeChildDefinition.IMutator getValueMutator(FhirContext fhirContext) {
		return getParameterElement(fhirContext)
			.getChildByName("value[x]").getMutator();
	}

	private static void validateNameAndValue(String name, Object value) {
		checkNotNull(name);
		checkNotNull(value);
	}

	public static IBaseParameters newParameters(FhirContext fhirContext, IIdType theId, IBase... parts) {
		checkNotNull(theId);
		IBaseParameters newParameters = ParametersUtil.newInstance(fhirContext);
		newParameters.setId(theId);
		BaseRuntimeChildDefinition.IMutator mutator = getParameterChild(fhirContext).getMutator();
		for (IBase part : parts) {
			mutator.addValue(newParameters, part);
		}
		return newParameters;
	}

	public static IBaseParameters newParameters(FhirContext fhirContext, String theId, IBase... parts) {
		checkNotNull(theId);
		IIdType id = (IIdType) Objects.requireNonNull(fhirContext.getElementDefinition("id")).newInstance();
		id.setValue(theId);
		return newParameters(fhirContext, id, parts);
	}

	public static IBaseParameters newParameters(FhirContext fhirContext, IBase... parts) {
		IBaseParameters newParameters = ParametersUtil.newInstance(fhirContext);
		BaseRuntimeChildDefinition.IMutator mutator = getParameterChild(fhirContext).getMutator();
		for (IBase part : parts) {
			mutator.addValue(newParameters, part);
		}
		return newParameters;
	}

	public static IBase newPart(FhirContext fhirContext, String name, IBase... parts) {
		checkNotNull(name);
		BaseRuntimeChildDefinition.IMutator nameMutator = getParameterElement(fhirContext)
			.getChildByName("name").getMutator();
		BaseRuntimeChildDefinition.IMutator partMutator = getParameterElement(fhirContext)
			.getChildByName("part").getMutator();
		IBase parameterBase = getParameterElement(fhirContext).newInstance();
		IBase theName = Objects.requireNonNull(fhirContext.getElementDefinition("string")).newInstance(name);
		nameMutator.setValue(parameterBase, theName);
		for (IBase part : parts) {
			partMutator.addValue(parameterBase, part);
		}
		return parameterBase;
	}

	public static <T extends IBaseDatatype> IBase newPart(FhirContext fhirContext, Class<T> type,
																			String name, Object value, IBase... parts) {
		validateNameAndValue(name, value);
		IBase newPpc = newPart(fhirContext, name, parts);
		IBase typeValue = Objects.requireNonNull(fhirContext.getElementDefinition(type)).newInstance(value);
		getValueMutator(fhirContext).setValue(newPpc, typeValue);
		return newPpc;
	}

	public static IBase newPart(FhirContext fhirContext, String typeName,
										 String name, Object value, IBase... parts) {
		validateNameAndValue(name, value);
		IBase newPpc = newPart(fhirContext, name, parts);
		IBase typeValue = Objects.requireNonNull(fhirContext.getElementDefinition(typeName)).newInstance(value.toString());
		getValueMutator(fhirContext).setValue(newPpc, typeValue);
		return newPpc;
	}

	public static IBase newPart(FhirContext fhirContext, String name, IBaseResource value, IBase... parts) {
		validateNameAndValue(name, value);
		IBase newPpc = newPart(fhirContext, name, parts);
		getParameterElement(fhirContext).getChildByName("resource").getMutator().setValue(newPpc, value);
		return newPpc;
	}

	public static Optional<String> getSingularStringPart(FhirContext fhirContext, IBaseResource parameters, String name) {
		checkNotNull(parameters);
		checkNotNull(name);
		return ParametersUtil.getNamedParameterValueAsString(fhirContext, (IBaseParameters) parameters, name);
	}

	public static List<IBase> getPartsByName(FhirContext fhirContext, IBaseResource parameters, String name) {
		checkNotNull(parameters);
		checkNotNull(name);
		return ParametersUtil.getNamedParameters(fhirContext, parameters, name);
	}

	public static IBase newBase64BinaryPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "base64binary", name, value, parts);
	}

	public static IBase newBooleanPart(FhirContext fhirContext, String name, boolean value, IBase... parts) {
		return newPart(fhirContext, "boolean", name, value, parts);
	}

	public static IBase newCanonicalPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "canonical", name, value, parts);
	}

	public static IBase newCodePart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "code", name, value, parts);
	}

	public static IBase newDatePart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "date", name, value, parts);
	}

	public static IBase newDateTimePart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "datetime", name, value, parts);
	}

	public static IBase newDecimalPart(FhirContext fhirContext, String name, double value, IBase... parts) {
		return newPart(fhirContext, "decimal", name, value, parts);
	}

	public static IBase newIdPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "id", name, value, parts);
	}

	public static IBase newInstantPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "instant", name, value, parts);
	}

	public static IBase newIntegerPart(FhirContext fhirContext, String name, int value, IBase... parts) {
		return newPart(fhirContext, "integer", name, value, parts);
	}

	public static IBase newInteger64Part(FhirContext fhirContext, String name, long value, IBase... parts) {
		return newPart(fhirContext, "integer64", name, value, parts);
	}

	public static IBase newMarkdownPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "markdown", name, value, parts);
	}

	public static IBase newOidPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "oid", name, value, parts);
	}

	public static IBase newPositiveIntPart(FhirContext fhirContext, String name, int value, IBase... parts) {
		return newPart(fhirContext, "positiveint", name, value, parts);
	}

	public static IBase newStringPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "string", name, value, parts);
	}

	public static IBase newTimePart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "time", name, value, parts);
	}

	public static IBase newUnsignedIntPart(FhirContext fhirContext, String name, int value, IBase... parts) {
		return newPart(fhirContext, "unsignedint", name, value, parts);
	}

	public static IBase newUriPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "uri", name, value, parts);
	}

	public static IBase newUrlPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "url", name, value, parts);
	}

	public static IBase newUuidPart(FhirContext fhirContext, String name, String value, IBase... parts) {
		return newPart(fhirContext, "uuid", name, value, parts);
	}
}
