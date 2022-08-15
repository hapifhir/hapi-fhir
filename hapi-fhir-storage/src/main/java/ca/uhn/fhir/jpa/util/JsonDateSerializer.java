package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hl7.fhir.dstu3.model.InstantType;

import java.io.IOException;
import java.util.Date;

public class JsonDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date theValue, JsonGenerator theGen, SerializerProvider theSerializers) throws IOException {
		if (theValue != null) {
			theGen.writeString(new InstantType(theValue).getValueAsString());
		}
	}

}
