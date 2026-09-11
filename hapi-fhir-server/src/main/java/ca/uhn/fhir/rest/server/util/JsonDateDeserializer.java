/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.model.primitive.DateTimeDt;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JsonDateDeserializer extends ValueDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser theParser, DeserializationContext theDeserializationContext) {
		String string = theParser.getValueAsString();
		if (isNotBlank(string)) {
			return new DateTimeDt(string).getValue();
		}
		return null;
	}
}
