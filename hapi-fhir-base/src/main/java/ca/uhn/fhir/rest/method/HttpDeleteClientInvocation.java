package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class HttpDeleteClientInvocation extends BaseHttpClientInvocation {

	private String myUrlPath;

	public HttpDeleteClientInvocation(IdDt theId) {
		super();
		myUrlPath = theId.toUnqualifiedVersionless().getValue();
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(myUrlPath);

		appendExtraParamsWithQuestionMark(theExtraParams, b,b.indexOf("?") == -1);

		HttpDelete retVal = new HttpDelete(b.toString());
		super.addHeadersToRequest(retVal);
		return retVal;
	}


}
