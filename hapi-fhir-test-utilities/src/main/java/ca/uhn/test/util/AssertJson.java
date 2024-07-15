/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Fail.fail;

/**
 * Assertj extension to ease testing json strings with few nested quoted strings
 */
public class AssertJson extends AbstractAssert<AssertJson, String> {

	public AssertJson(String actual) {
		super(actual, AssertJson.class);
	}

	public static AssertJson assertThat(String actual) {
		return new AssertJson(actual);
	}

	public AssertJson hasPath(String thePath) {
		isNotNull();
		isNotEmpty(thePath);

		Assertions.assertThat(isJsonObjStr(actual)).isTrue();
		Map<String, Object> actualMap = getMap(actual);
		Assertions.assertThat(actualMap).isNotNull();
		getPathMap(thePath);
		return this;
	}

	private AssertJson isNotEmpty(String thePath) {
		Assertions.assertThat(thePath).isNotEmpty();
		return this;
	}

	public AssertJson hasKeys(String... theKeys) {
		isNotNull();

		Map<String, Object> map = getMap(actual);
		Assertions.assertThat(map).isNotNull();

		Assertions.assertThat(
			map.keySet()).containsAll(Arrays.asList(theKeys));
		return this;
	}

	public AssertJson hasExactlyKeys(String... theKeys) {
		isNotNull();

		Map<String, Object> map = getMap(actual);
		Assertions.assertThat(map).isNotNull();

		Assertions.assertThat(
			map.keySet()).hasSameElementsAs(Arrays.asList(theKeys));
		return this;
	}

	public AssertJson hasExactlyKeysWithValues(List<String> theKeys, List<? extends Serializable> theValues) {
		isNotNull();

		if (!checkSizes(theKeys.size(), theValues.size())) {
			return this;
		}

		Map<String, Object> map = getMap(actual);
		Assertions.assertThat(map).isNotNull();

		Assertions.assertThat(
			map.keySet()).hasSameElementsAs(theKeys);

		for (int i = 0; i <theKeys.size(); i++) {
			hasKeyWithValue(theKeys.get(i), theValues.get(i));
		}

		return this;
	}


	public AssertJson hasKeyWithValue(String theKey, Object theExpectedValue) {
		isNotNull();

		Map<String, Object> actualMap = getMap(actual);
		Assertions.assertThat(actualMap).isNotNull();
		Object actualValue = actualMap.get(theKey);

		JsonTestTypes actualValueType = getType(actualValue);
		JsonTestTypes expectedValueType = getType(theExpectedValue);

		if (actualValueType != expectedValueType) {
			fail(getDifferentTypesMessage(theKey, actualValueType, expectedValueType));
		}

		if (isJsonObjStr(theExpectedValue)) {
			assertJsonObject(actualMap, theKey, theExpectedValue);
			return this;
		}

		if (isJsonList(theExpectedValue)) {
			assertJsonList(actualMap, theKey, theExpectedValue);
			return this;
		}

		Assertions.assertThat(actualMap)
			.extracting(theKey)
			.isEqualTo(theExpectedValue);
		return this;
	}

	private void assertJsonList(Map<String, Object> theActualMap, String theKey, Object theExpectedValue) {
		List<?> expectedValueList = getList((String) theExpectedValue);
		Assertions.assertThat(expectedValueList).isNotNull();

		Assertions.assertThat(theActualMap.get(theKey)).isNotNull().isInstanceOf(Collection.class);
		List<?> actualValueList =  (List<?>) theActualMap.get(theKey);

		Assertions.assertThat(actualValueList)
			.asList()
			.hasSameElementsAs(expectedValueList);
	}

	private JsonTestTypes getType(Object theValue) {
		if (theValue instanceof Map<?, ?>) {
			return JsonTestTypes.JSON_OBJECT;
		}

		if (isJsonObjStr(theValue)) {
			getMap((String) theValue);
			return JsonTestTypes.JSON_OBJECT;
		}

		if (isJsonList(theValue)) {
			return JsonTestTypes.JSON_LIST;
		}

		return JsonTestTypes.STRING_NOT_JSON;
	}

	private String getDifferentTypesMessage(String theKey, JsonTestTypes theActualValueType, JsonTestTypes theExpectedValueType) {
		return "Types mismatch. Te expected " + (theKey == null ? " " : "'" + theKey + "' ") +
			"value is a " + theExpectedValueType.myDisplay +
			" whereas the actual value is a " + theActualValueType.myDisplay;
	}

	private boolean isJsonList(Object theValue) {
		return theValue instanceof Collection<?> ||
			(theValue instanceof String stringValue
				&& stringValue.trim().startsWith("[")
				&& stringValue.trim().endsWith("]"));
	}

	private void assertJsonObject(Map<String, Object> theActualMap, String theKey, Object theValue) {
		Map<String, Object> expectedValueMap = getMap((String) theValue);
		Assertions.assertThat(expectedValueMap).isNotNull();

		Assertions.assertThat(theActualMap.get(theKey)).isNotNull().isInstanceOf(Map.class);
		@SuppressWarnings("unchecked")
		Map<String, Object> actualValueMap =  (Map<String, Object>) theActualMap.get(theKey);

		SoftAssertions lazyly = new SoftAssertions();
		for (String key : actualValueMap.keySet()) {
			lazyly.assertThat(actualValueMap)
				.as("Unexpected value for key: " + key + ":")
				.extracting(key).isEqualTo(expectedValueMap.get(key));
		}
		lazyly.assertAll();
	}

	private boolean isJsonObjStr(Object theValue) {
		if (theValue instanceof String strValue) {
			String trimmed = trimAll(strValue);
			return trimmed.startsWith("{") && trimmed.endsWith("}") && isValidJson(trimmed);
		}
		return false;
	}

	private String trimAll(String theString) {
		return theString.trim().replace("\n", "").replace("\t", "");
	}

	private boolean isValidJson(String theStrValue) {
		getMap(theStrValue);
		return true;
	}

	public AssertJson hasKeysWithValues(List<String> theKeys, List<Object> theValues) {
		isNotNull();

		checkSizes(theKeys.size(), theValues.size());

		Map<String, Object> map = getMap(actual);

		Assertions.assertThat(map).isNotNull();
		Assertions.assertThat(map.keySet()).containsAll(theKeys);
		checkKeysAndValues(map, theKeys, theValues);
		return this;
	}

	private void checkKeysAndValues(Map<String, Object> theExpected, List<String> theKeys, List<Object> theValues) {
		SoftAssertions lazyly = new SoftAssertions();
		for (int i = 0; i < theKeys.size(); i++) {
			lazyly.assertThat(theExpected)
				.as("Unexpected value for key: " + theKeys.get(i) + ":")
				.extracting(theKeys.get(i)).isEqualTo(theValues.get(i));
		}
		lazyly.assertAll();
	}

	private boolean checkSizes(int keysSize, int valuesSize) {
		if (keysSize != valuesSize) {
			fail("Keys and values should have same size. Received " + keysSize + " keys and " + valuesSize + " values.");
			return false;
		}
		return true;
	}

	@Nonnull
	private static Map<String, Object> getMap(String theJsonString) {
		try {
			return new ObjectMapper()
				.readValue(new ByteArrayInputStream(theJsonString.getBytes()), new TypeReference<>() {});

		} catch (IOException theE) {
			fail("IOException: " + theE);
		}
		return Collections.emptyMap();
	}

	private List<?> getList(String theJsonString) {
		try {
			return new ObjectMapper()
				.readValue(new ByteArrayInputStream(theJsonString.getBytes()), new TypeReference<>() {});

		} catch (IOException theE) {
			fail("IOException: " + theE);
		}
		return Collections.emptyList();
	}


	public AssertJson hasPaths(String... thePaths) {
		for (String path : thePaths) {
			hasPath(path);
		}
		return this;
	}

	public AssertJson hasPathWithValue(String thePath, String theValue) {
		String[] pathElements = thePath.split("\\.");
		if (pathElements.length == 1) {
			hasKeyWithValue(thePath, theValue);
		}

		Map<String, Object> pathMap = getPathMap(thePath);
		String lastPathElement = pathElements[pathElements.length - 1];

		if (isJsonObjStr(theValue)) {
			Assertions.assertThat(pathMap)
				.extracting(lastPathElement)
				.isEqualTo(getMap(theValue));
			return this;
		}

		if (isJsonList(theValue)) {
			Assertions.assertThat(pathMap)
				.extracting(lastPathElement)
				.asList()
				.hasSameElementsAs(getList(theValue));
			return this;
		}

		// check last path element's value
		Assertions.assertThat(pathMap)
			.extracting(pathElements[pathElements.length-1])
				.isEqualTo(theValue);
		return this;
	}

	public AssertJson hasPathsWithValues(List<String> thePaths, List<String> theValues) {
		if (thePaths.size() != theValues.size()) {
			fail("Paths size (" + thePaths.size() + ") is different than values size (" + theValues.size() + ")");
			return this;
		}

		for (int i = 0; i < thePaths.size(); i++) {
			hasPathWithValue(thePaths.get(i), theValues.get(i));
		}
		return this;
	}

	private Map<String, Object> getPathMap(String thePath) {
		String[] pathElements = thePath.split("\\.");
		StringBuilder pathSoFar = new StringBuilder();

		Map<String, Object> pathMap = getMap(actual);

		for (int i = 0; i < pathElements.length-1; i++) {
			String pathElement = pathElements[i];
			pathSoFar.append(StringUtils.isNotEmpty(pathSoFar) ? "." + pathElement : pathElement);
			Object pathValue = pathMap.get(pathElement);

			// all path values, other than the last, must be json objects (maps)
			assertIsJsonObject(pathSoFar.toString(), pathValue);

			@SuppressWarnings("unchecked")
			Map<String, Object> aMap = (Map<String, Object>) pathValue;
			pathMap = aMap;
		}

		return pathMap;
	}

	private void assertIsJsonObject(String thePath, Object theValue) {
		if (theValue instanceof Map<?, ?>) {
			return;
		}

		if (theValue instanceof String stringValue) {
			if (!isJsonObjStr(theValue)) {
				fail(thePath + " doesn't contain a json object but a plain string");
				return;
			}

			try {
				getMap(stringValue);
			} catch (Exception theE) {
				fail(thePath + " doesn't contain a json object");
			}
			return;
		}

		String msg = "Path: " + thePath + "' is not a json object but a Json  list";
		if (isJsonList(theValue)) {
			Assertions.assertThat(theValue)
				.as(msg)
				.isInstanceOf(Map.class);
		}
	}

	enum JsonTestTypes {

		STRING_NOT_JSON("plain string (not json)"),
		JSON_OBJECT("json object"),
		JSON_LIST("json list"),
		JSON_STRING("json string");

		final String myDisplay;

		JsonTestTypes(String theDisplay) {
			myDisplay = theDisplay;
		}
	}
}
