package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class ReadMethodBinding extends BaseResourceReturningMethodBinding implements IClientResponseHandlerHandlesBinary<Object> {
	private Integer myIdIndex;
	private boolean mySupportsVersion;
	private Class<? extends IIdType> myIdParameterType;

	@SuppressWarnings("unchecked")
	public ReadMethodBinding(Class<? extends IBaseResource> theAnnotatedResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theAnnotatedResourceType, theMethod, theContext, theProvider);

		Validate.notNull(theMethod, "Method must not be null");

		Integer idIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());

		Class<?>[] parameterTypes = theMethod.getParameterTypes();

		mySupportsVersion = theMethod.getAnnotation(Read.class).version();
		myIdIndex = idIndex;

		if (myIdIndex == null) {
			throw new ConfigurationException(Msg.code(1423) + "@" + Read.class.getSimpleName() + " method " + theMethod.getName() + " on type \"" + theMethod.getDeclaringClass().getName()
					+ "\" does not have a parameter annotated with @" + IdParam.class.getSimpleName());
		}
		myIdParameterType = (Class<? extends IIdType>) parameterTypes[myIdIndex];

		if (!IIdType.class.isAssignableFrom(myIdParameterType)) {
			throw new ConfigurationException(Msg.code(1424) + "ID parameter must be of type IdDt or IdType - Found: " + myIdParameterType);
		}

	}

	@Override
	public List<Class<?>> getAllowableParamAnnotations() {
		ArrayList<Class<?>> retVal = new ArrayList<Class<?>>();
		retVal.add(IdParam.class);
		retVal.add(Elements.class);
		return retVal;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return isVread() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public HttpGetClientInvocation invokeClient(Object[] theArgs) {
		HttpGetClientInvocation retVal;
		IIdType id = ((IIdType) theArgs[myIdIndex]);
		String resourceName = getResourceName();
		if (id.hasVersionIdPart()) {
			retVal = createVReadInvocation(getContext(), new IdDt(resourceName, id.getIdPart(), id.getVersionIdPart()), resourceName);
		} else {
			retVal = createReadInvocation(getContext(), id, resourceName);
		}

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	@Override
	public Object invokeClientForBinary(String theResponseMimeType, InputStream theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders)
			throws IOException, BaseServerResponseException {
		byte[] contents = IOUtils.toByteArray(theResponseReader);

		IBaseBinary resource = (IBaseBinary) getContext().getResourceDefinition("Binary").newInstance();
		resource.setContentType(theResponseMimeType);
		resource.setContent(contents);

		switch (getMethodReturnType()) {
		case LIST_OF_RESOURCES:
			return Collections.singletonList(resource);
		case RESOURCE:
			return resource;
		case BUNDLE_RESOURCE:
		case METHOD_OUTCOME:
			break;
		}

		throw new IllegalStateException(Msg.code(1425) + "" + getMethodReturnType()); // should not happen
	}

	@Override
	public boolean isBinary() {
		return "Binary".equals(getResourceName());
	}

	public boolean isVread() {
		return mySupportsVersion;
	}

	public static HttpGetClientInvocation createAbsoluteReadInvocation(FhirContext theContext, IIdType theId) {
		return new HttpGetClientInvocation(theContext, theId.toVersionless().getValue());
	}

	public static HttpGetClientInvocation createAbsoluteVReadInvocation(FhirContext theContext, IIdType theId) {
		return new HttpGetClientInvocation(theContext, theId.getValue());
	}

	public static HttpGetClientInvocation createReadInvocation(FhirContext theContext, IIdType theId, String theResourceName) {
		return new HttpGetClientInvocation(theContext, new IdDt(theResourceName, theId.getIdPart()).getValue());
	}

	public static HttpGetClientInvocation createVReadInvocation(FhirContext theContext, IIdType theId, String theResourceName) {
		return new HttpGetClientInvocation(theContext, new IdDt(theResourceName, theId.getIdPart(), theId.getVersionIdPart()).getValue());
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return null;
	}

}
