package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.StringParam;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;

import java.security.InvalidParameterException;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReplaceReferenceRequest {
	@Nonnull
	public final IIdType sourceId;

	@Nonnull
	public final IIdType targetId;

	@Nonnull
	public final int pageSize;

	public ReplaceReferenceRequest(@Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, int thePageSize) {
		sourceId = theSourceId;
		targetId = theTargetId;
		pageSize = thePageSize;
	}

	public void validateOrThrowInvalidParameterException() {
		if (isBlank(sourceId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2585) + "'" + OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID
							+ "' must be a resource type qualified id");
		}

		if (isBlank(targetId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2586) + "'" + OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID
							+ "' must be a resource type qualified id");
		}

		if (!targetId.getResourceType().equals(sourceId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2587) + "Source and target id parameters must be for the same resource type");
		}
	}

	// FIXME KHS remove
	public SearchParameterMap getSearchParameterMap() {
		SearchParameterMap retval = SearchParameterMap.newSynchronous();
		retval.add(PARAM_ID, new StringParam(sourceId.getValue()));
		retval.addRevInclude(new Include("*"));
		// Note we do not set the count since we will be streaming
		return retval;
	}
}
