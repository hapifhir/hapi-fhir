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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.StringTokenizer;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class SortParameter implements IParameter {

	private FhirContext myContext;

	public SortParameter(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null || theInnerCollectionType != null) {
			throw new ConfigurationException(Msg.code(443) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + Sort.class.getName()
					+ " but can not be of collection type");
		}
		if (!theParameterType.equals(SortSpec.class)) {
			throw new ConfigurationException(Msg.code(444) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + Sort.class.getName()
					+ " but is an invalid type, must be: " + SortSpec.class.getCanonicalName());
		}

	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		if (!theRequest.getParameters().containsKey(Constants.PARAM_SORT)) {
			if (!theRequest.getParameters().containsKey(Constants.PARAM_SORT_ASC)) {
				if (!theRequest.getParameters().containsKey(Constants.PARAM_SORT_DESC)) {
					return null;
				}
			}
		}

		SortSpec outerSpec = null;
		SortSpec innerSpec = null;
		for (String nextParamName : theRequest.getParameters().keySet()) {
			SortOrderEnum order;
			if (Constants.PARAM_SORT.equals(nextParamName)) {
				order = null;
			} else if (Constants.PARAM_SORT_ASC.equals(nextParamName)) {
				order = SortOrderEnum.ASC;
			} else if (Constants.PARAM_SORT_DESC.equals(nextParamName)) {
				order = SortOrderEnum.DESC;
			} else {
				continue;
			}

			String[] values = theRequest.getParameters().get(nextParamName);
			if (values != null) {

				for (String nextValue : values) {

					if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2) && order == null) {
						StringTokenizer tok = new StringTokenizer(nextValue, ",");
						while (tok.hasMoreTokens()) {
							String next = tok.nextToken();
							if (isNotBlank(next) && !next.equals("-")) {
								order = SortOrderEnum.ASC;
								if (next.startsWith("-")) {
									order = SortOrderEnum.DESC;
									next = next.substring(1);
								}

								SortSpec spec = new SortSpec();
								spec.setOrder(order);
								spec.setParamName(next);
								if (innerSpec == null) {
									outerSpec = spec;
									innerSpec = spec;
								} else {
									innerSpec.setChain(spec);
									innerSpec = spec;
								}

							}
						}

					} else {

						if (isNotBlank(nextValue)) {
							SortSpec spec = new SortSpec();
							spec.setOrder(order);
							spec.setParamName(nextValue);
							if (innerSpec == null) {
								outerSpec = spec;
								innerSpec = spec;
							} else {
								innerSpec.setChain(spec);
								innerSpec = spec;
							}
						}
					}
				}
			}
		}

		return outerSpec;
	}

	public static String createSortStringDstu3(SortSpec ss) {
		StringBuilder val = new StringBuilder();
		while (ss != null) {

			if (isNotBlank(ss.getParamName())) {
				if (val.length() > 0) {
					val.append(',');
				}
				if (ss.getOrder() == SortOrderEnum.DESC) {
					val.append('-');
				}
				val.append(ParameterUtil.escape(ss.getParamName()));
			}

			ss = ss.getChain();
		}

		String string = val.toString();
		return string;
	}

}
