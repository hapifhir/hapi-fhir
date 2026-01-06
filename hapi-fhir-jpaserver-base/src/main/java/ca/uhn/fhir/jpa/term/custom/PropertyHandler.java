/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.term.custom;

import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermConceptPropertyTypeEnum;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class PropertyHandler implements IZipContentsHandlerCsv {

	private static final Logger ourLog = LoggerFactory.getLogger(PropertyHandler.class);

	public static final String CODE = "CODE";
	public static final String KEY = "KEY";
	public static final String VALUE = "VALUE";
	public static final String TYPE = "TYPE";
	private final Map<String, List<TermConceptProperty>> myCode2Properties;

	public PropertyHandler(Map<String, List<TermConceptProperty>> theCode2concept) {
		myCode2Properties = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get(CODE));
		String key = trim(theRecord.get(KEY));

		if (isNotBlank(code) && isNotBlank(key)) {
			String value = trim(theRecord.get(VALUE));
			String type = trim(theRecord.get(TYPE));

			List<TermConceptProperty> conceptProperties = myCode2Properties.get(code);
			if (conceptProperties == null) conceptProperties = new ArrayList<>();

			if (isDuplicateConceptProperty(conceptProperties, key, value, type)) {
				ourLog.info(
						"Duplicate concept property found for code {}, key {}, value {}, type {}. Skipping entry.",
						code,
						key,
						value,
						type);
				return;
			}

			TermConceptProperty conceptProperty = new TermConceptProperty();
			conceptProperty.setKey(key);
			conceptProperty.setValue(value);
			// TODO: check this for different types, other types should be added once TermConceptPropertyTypeEnum
			// contain different types
			conceptProperty.setType(TermConceptPropertyTypeEnum.STRING);
			conceptProperties.add(conceptProperty);
			myCode2Properties.put(code, conceptProperties);
		}
	}

	public boolean isDuplicateConceptProperty(
			List<TermConceptProperty> conceptProperties, String key, String value, String typeString) {
		if (CollectionUtils.isEmpty(conceptProperties)) {
			return false;
		}
		return conceptProperties.stream()
				.anyMatch(p -> Objects.equals(p.getKey(), key)
						&& Objects.equals(p.getValue(), value)
						&& Objects.equals(p.getType(), TermConceptPropertyTypeEnum.fromString(typeString)));
	}
}
