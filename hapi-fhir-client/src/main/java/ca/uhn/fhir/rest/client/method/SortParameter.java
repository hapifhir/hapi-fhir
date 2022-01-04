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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class SortParameter implements IParameter {

	private FhirContext myContext;

	public SortParameter(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null || theInnerCollectionType != null) {
			throw new ConfigurationException(Msg.code(1463) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + Sort.class.getName() + " but can not be of collection type");
		}
		if (!theParameterType.equals(SortSpec.class)) {
			throw new ConfigurationException(Msg.code(1464) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + Sort.class.getName() + " but is an invalid type, must be: " + SortSpec.class.getCanonicalName());
		}

	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		SortSpec ss = (SortSpec) theSourceClientArgument;

		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
			String string = createSortStringDstu3(ss);
			if (string.length() > 0) {
				if (!theTargetQueryArguments.containsKey(Constants.PARAM_SORT)) {
					theTargetQueryArguments.put(Constants.PARAM_SORT, new ArrayList<String>());
				}
				theTargetQueryArguments.get(Constants.PARAM_SORT).add(string);
			}

		} else {

			while (ss != null) {
				String name;
				if (ss.getOrder() == null) {
					name = Constants.PARAM_SORT;
				} else if (ss.getOrder() == SortOrderEnum.ASC) {
					name = Constants.PARAM_SORT_ASC;
				} else {
					name = Constants.PARAM_SORT_DESC;
				}

				if (ss.getParamName() != null) {
					if (!theTargetQueryArguments.containsKey(name)) {
						theTargetQueryArguments.put(name, new ArrayList<String>());
					}

					theTargetQueryArguments.get(name).add(ss.getParamName());
				}
				ss = ss.getChain();
			}
		}
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
