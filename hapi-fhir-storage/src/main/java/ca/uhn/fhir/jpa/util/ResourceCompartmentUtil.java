package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResourceCompartmentUtil {

	/**
	 * Extracts and returns an optional compartment of the received resource
	 * @param theResource source resource which compartment is extracted
	 * @param theCompartmentSps the RuntimeSearchParam list involving the searched compartment
	 * @param mySearchParamExtractor the ISearchParamExtractor to be used to extract the parameter values
	 * @return optional compartment of the received resource
	 */
	public static Optional<String> getResourceCompartment(IBaseResource theResource,
														  List<RuntimeSearchParam> theCompartmentSps,
														  ISearchParamExtractor mySearchParamExtractor) {
		return theCompartmentSps.stream()
			.flatMap(param -> Arrays.stream(BaseSearchParamExtractor.splitPathsR4(param.getPath())))
			.filter(StringUtils::isNotBlank)
			.map(path -> mySearchParamExtractor
				.getPathValueExtractor(theResource, path)
				.get())
			.filter(t -> !t.isEmpty())
			.map(t -> t.get(0))
			.filter(t -> t instanceof IBaseReference)
			.map(t -> (IBaseReference) t)
			.map(t -> t.getReferenceElement().getValue())
			.map(t -> new IdType(t).getIdPart())
			.filter(StringUtils::isNotBlank)
			.findFirst();
	}

	/**
	 * Returns a {@code RuntimeSearchParam} list with the parameters extracted from the received
	 * {@code RuntimeResourceDefinition}, which are of type REFERENCE and have a membership compartment
	 * for "Patient" resource
	 * @param resourceDef the RuntimeResourceDefinition providing the RuntimeSearchParam list
	 * @return the RuntimeSearchParam filtered list
	 */
	@Nonnull
	public static List<RuntimeSearchParam> getPatientCompartmentSearchParams(RuntimeResourceDefinition resourceDef) {
		return resourceDef.getSearchParams().stream()
			.filter(param -> param.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
			.filter(param -> param.getProvidesMembershipInCompartments() != null
				&& param.getProvidesMembershipInCompartments().contains("Patient"))
			.collect(Collectors.toList());
	}


}
