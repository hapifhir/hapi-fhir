package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class HistoryMethodBinding extends BaseResourceReturningMethodBinding {

	private final Integer myIdParamIndex;
	private final RestOperationTypeEnum myResourceOperationType;
	private final String myResourceName;

	public HistoryMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(toReturnType(theMethod, theProvider), theMethod, theContext, theProvider);

		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());

		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IBaseResource> type = historyAnnotation.type();
		if (Modifier.isInterface(type.getModifiers())) {
			if (theProvider instanceof IResourceProvider) {
				type = ((IResourceProvider) theProvider).getResourceType();
				if (myIdParamIndex != null) {
					myResourceOperationType = RestOperationTypeEnum.HISTORY_INSTANCE;
				} else {
					myResourceOperationType = RestOperationTypeEnum.HISTORY_TYPE;
				}
			} else {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_SYSTEM;
			}
		} else {
			if (myIdParamIndex != null) {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_INSTANCE;
			} else {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_TYPE;
			}
		}

		if (type != IBaseResource.class && type != IResource.class) {
			myResourceName = theContext.getResourceType(type);
		} else {
			myResourceName = null;
		}

	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return BundleTypeEnum.HISTORY;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return myResourceOperationType;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	protected boolean isOffsetModeHistory() {
		return true;
	}

	// ObjectUtils.equals is replaced by a JDK7 method..
	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (!Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			return MethodMatchEnum.NONE;
		}
		if (theRequest.getResourceName() == null) {
			if (myResourceOperationType == RestOperationTypeEnum.HISTORY_SYSTEM) {
				return MethodMatchEnum.EXACT;
			} else {
				return MethodMatchEnum.NONE;
			}
		}
		if (!StringUtils.equals(theRequest.getResourceName(), myResourceName)) {
			return MethodMatchEnum.NONE;
		}

		boolean haveIdParam = theRequest.getId() != null && !theRequest.getId().isEmpty();
		boolean wantIdParam = myIdParamIndex != null;
		if (haveIdParam != wantIdParam) {
			return MethodMatchEnum.NONE;
		}

		if (theRequest.getId() == null) {
			if (myResourceOperationType != RestOperationTypeEnum.HISTORY_TYPE) {
				return MethodMatchEnum.NONE;
			}
		} else if (theRequest.getId().hasVersionIdPart()) {
			return MethodMatchEnum.NONE;
		}

		return MethodMatchEnum.EXACT;
	}


	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theRequest, theMethodParams);

		final IBundleProvider resources = toResourceList(response);

		/*
		 * We wrap the response so we can verify that it has the ID and version set,
		 * as is the contract for history
		 */
		return new IBundleProvider() {

			@Override
			public String getCurrentPageId() {
				return resources.getCurrentPageId();
			}

			@Override
			public String getNextPageId() {
				return resources.getNextPageId();
			}

			@Override
			public String getPreviousPageId() {
				return resources.getPreviousPageId();
			}

			@Override
			public IPrimitiveType<Date> getPublished() {
				return resources.getPublished();
			}

			@Nonnull
			@Override
			public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
				List<IBaseResource> retVal = resources.getResources(theFromIndex, theToIndex);
				int index = theFromIndex;
				for (IBaseResource nextResource : retVal) {
					if (nextResource.getIdElement() == null || isBlank(nextResource.getIdElement().getIdPart())) {
						throw new InternalErrorException(Msg.code(410) + "Server provided resource at index " + index + " with no ID set (using IResource#setId(IdDt))");
					}
					if (isBlank(nextResource.getIdElement().getVersionIdPart()) && nextResource instanceof IResource) {
						//TODO: Use of a deprecated method should be resolved.
						IdDt versionId = ResourceMetadataKeyEnum.VERSION_ID.get((IResource) nextResource);
						if (versionId == null || versionId.isEmpty()) {
							throw new InternalErrorException(Msg.code(411) + "Server provided resource at index " + index + " with no Version ID set (using IResource#setId(IdDt))");
						}
					}
					index++;
				}
				return retVal;
			}

			@Override
			public String getUuid() {
				return resources.getUuid();
			}

			@Override
			public Integer preferredPageSize() {
				return resources.preferredPageSize();
			}

			@Override
			public Integer size() {
				return resources.size();
			}
		};
	}

	private static Class<? extends IBaseResource> toReturnType(Method theMethod, Object theProvider) {
		if (theProvider instanceof IResourceProvider) {
			return ((IResourceProvider) theProvider).getResourceType();
		}
		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IBaseResource> type = historyAnnotation.type();
		if (type != IBaseResource.class && type != IResource.class) {
			return type;
		}
		return null;
	}

}
