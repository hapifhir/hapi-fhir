package ca.uhn.fhir.jpa.searchparam.nickname;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NicknameInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(NicknameInterceptor.class);

	private final NicknameSvc myNicknameSvc;

	public NicknameInterceptor() throws IOException {
		myNicknameSvc = new NicknameSvc();
	}

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void expandNicknames(SearchParameterMap theSearchParameterMap) {
		for (Map.Entry<String, List<List<IQueryParameterType>>> set : theSearchParameterMap.entrySet()) {
			String paramName = set.getKey();
			List<List<IQueryParameterType>> andList = set.getValue();
			for (List<IQueryParameterType> orList : andList) {
				// here we will know if it's an _id param or not
				// from theSearchParameterMap.keySet()
				expandAnyNicknameParameters(paramName, orList);
			}
		}
	}

	/**
	 * If a Parameter is a string parameter, and it has been set to expand Nicknames, perform the expansion.
	 */
	private void expandAnyNicknameParameters(String theParamName, List<IQueryParameterType> orList) {
		List<IQueryParameterType> toAdd = new ArrayList<>();
		List<IQueryParameterType> toRemove = new ArrayList<>();
		for (IQueryParameterType iQueryParameterType : orList) {
			if (iQueryParameterType instanceof StringParam) {
				StringParam stringParam = (StringParam) iQueryParameterType;
				if (stringParam.isNicknameExpand()) {
					ourLog.debug("Found a nickname parameter to expand: {} {}", theParamName, stringParam);
					toRemove.add(stringParam);
					//First, attempt to expand as a formal name
					String name = stringParam.getValue().toLowerCase(Locale.ROOT);
					List<String> expansions = myNicknameSvc.getEquivalentNames(name);
					if (expansions == null) {
						continue;
					}
					ourLog.debug("Parameter has been expanded to: {} {}", theParamName, String.join(", ", expansions));
					expansions.stream()
						.map(StringParam::new)
						.forEach(toAdd::add);
				}
			}
		}
		orList.removeAll(toRemove);
		orList.addAll(toAdd);
	}
}
