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
import java.util.Set;

class SinceOrAtParameter extends SearchParameter {

//	private Class<?> myType;
//	private String myParamName;
//	private Class<?> myAnnotationType;

	public SinceOrAtParameter(String theParamName, Class<?> theAnnotationType) {
		super(theParamName, false);
//		myParamName = theParamName;
//		myAnnotationType = theAnnotationType;
	}

	@Override
	public Set<String> getQualifierBlacklist() {
		return null;
	}

	@Override
	public Set<String> getQualifierWhitelist() {
		return null;
	}

//	@Override
//	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
//		if (theSourceClientArgument != null) {
//			InstantDt since = ParameterUtil.toInstant(theSourceClientArgument);
//			if (since.isEmpty() == false) {
//				theTargetQueryArguments.put(myParamName, Collections.singletonList(since.getValueAsString()));
//			}
//		}
//	}
//
//	@Override
//	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
//		String[] sinceParams = theRequest.getParameters().remove(myParamName);
//		if (sinceParams != null) {
//			if (sinceParams.length > 0) {
//				if (StringUtils.isNotBlank(sinceParams[0])) {
//					try {
//						return ParameterUtil.fromInstant(myType, sinceParams);
//					} catch (DataFormatException e) {
//						throw new InvalidRequestException(Msg.code(451) + "Invalid " + Constants.PARAM_SINCE + " value: " + sinceParams[0]);
//					}
//				}
//			}
//		}
//		return ParameterUtil.fromInstant(myType, null);
//	}
//
//	@Override
//	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
//		if (theOuterCollectionType != null) {
//			throw new ConfigurationException(Msg.code(452) + "Method '" + theMethod.getName() + "' in type '" + "' is annotated with @" + myAnnotationType.getName() + " but can not be of collection type");
//		}
//		if (ParameterUtil.getBindableInstantTypes().contains(theParameterType)) {
//			myType = theParameterType;
//		} else { 
//			throw new ConfigurationException(Msg.code(453) + "Method '" + theMethod.getName() + "' in type '" + "' is annotated with @" + myAnnotationType.getName() + " but is an invalid type, must be one of: " + ParameterUtil.getBindableInstantTypes());
//		}
//	}

}
