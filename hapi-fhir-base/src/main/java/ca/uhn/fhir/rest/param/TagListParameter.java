package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TagListParameter implements IParameter {
	private static final String SCHEME = "scheme=\"";
	private static final String LABEL = "label=\"";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TagListParameter.class);

	@Override
	public void translateClientArgumentIntoQueryArgument(Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, BaseClientInvocation theClientInvocation) throws InternalErrorException {
		TagList list = (TagList) theSourceClientArgument;
		if (list != null) {
			for (Tag tag : list) {
				if (StringUtils.isNotBlank(tag.getTerm())) {
					theClientInvocation.addHeader(Constants.HEADER_CATEGORY, tag.toHeaderValue());
				}
			}
		}
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(Request theRequest, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		TagList retVal = new TagList();

		for (Enumeration<String> enumeration = theRequest.getServletRequest().getHeaders(Constants.HEADER_CATEGORY); enumeration.hasMoreElements();) {
			String nextTagComplete = enumeration.nextElement();
			StringBuilder next = new StringBuilder(nextTagComplete);

			parseTagValue(retVal, nextTagComplete, next);

		}
		return retVal;
	}

	private void parseTagValue(TagList theTagList, String theCompleteHeaderValue, StringBuilder theBuffer) {
		int firstSemicolon = theBuffer.indexOf(";");
		int deleteTo;
		if (firstSemicolon == -1) {
			firstSemicolon = theBuffer.indexOf(",");
			if (firstSemicolon == -1) {
				firstSemicolon = theBuffer.length();
				deleteTo = theBuffer.length();
			} else {
				deleteTo = firstSemicolon;
			}
		} else {
			deleteTo = firstSemicolon + 1;
		}

		String term = theBuffer.substring(0, firstSemicolon);
		String scheme = null;
		String label = null;
		if (isBlank(term)) {
			return;
		}

		theBuffer.delete(0, deleteTo);
		while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
			theBuffer.deleteCharAt(0);
		}

		while (theBuffer.length() > 0) {
			boolean foundSomething = false;
			if (theBuffer.length() > SCHEME.length() && theBuffer.substring(0, SCHEME.length()).equals(SCHEME)) {
				int closeIdx = theBuffer.indexOf("\"", SCHEME.length());
				scheme = theBuffer.substring(SCHEME.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			if (theBuffer.length() > LABEL.length() && theBuffer.substring(0, LABEL.length()).equals(LABEL)) {
				int closeIdx = theBuffer.indexOf("\"", LABEL.length());
				label = theBuffer.substring(LABEL.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			// TODO: support enc2231-string as described in
			// http://tools.ietf.org/html/draft-johnston-http-category-header-02
			// TODO: support multiple tags in one header as described in
			// http://hl7.org/implement/standards/fhir/http.html#tags

			while (theBuffer.length() > 0 && (theBuffer.charAt(0) == ' ' || theBuffer.charAt(0) == ';')) {
				theBuffer.deleteCharAt(0);
			}

			if (!foundSomething) {
				break;
			}
		}

		if (theBuffer.length() > 0 && theBuffer.charAt(0) == ',') {
			theBuffer.deleteCharAt(0);
			while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
				theBuffer.deleteCharAt(0);
			}
			theTagList.add(new Tag(term, label, scheme));
			parseTagValue(theTagList, theCompleteHeaderValue, theBuffer);
		} else {
			theTagList.add(new Tag(term, label, scheme));
		}

		if (theBuffer.length() > 0) {
			ourLog.warn("Ignoring extra text at the end of " + Constants.HEADER_CATEGORY + " tag '" + theBuffer.toString() + "' - Complete tag value was: " + theCompleteHeaderValue);
		}

	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// TODO Auto-generated method stub

	}

}
