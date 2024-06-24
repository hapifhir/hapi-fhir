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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Fail.fail;

/**
 * Assertj extension to ease testing json strings with few nested quoted strings
 */
public class AssertJsn extends AbstractAssert<AssertJsn, String> {

	public AssertJsn(String actual) {
		super(actual, AssertJsn.class);
	}

	public static AssertJsn assertThat(String actual) {
		return new AssertJsn(actual);
	}

	public void hasPath(String thePath) {
		isNotNull();
		isNotEmpty(thePath);

		Assertions.assertThat(isJsonObjStr(actual)).isTrue();
		Map<String, Object> actualMap = getMap(actual);
		Assertions.assertThat(actualMap).isNotNull();
		getPathMap(thePath);

	}

	private void isNotEmpty(String thePath) {
		Assertions.assertThat(thePath).isNotEmpty();
	}

	public void hasKeys(Collection<String> theKeys) {
		isNotNull();

		Map<String, Object> map = getMap(actual);
		Assertions.assertThat(map).isNotNull();

		Assertions.assertThat(
			map.keySet()).containsAll(theKeys);
	}

	public void hasExactlyKeys(Collection<String> theKeys) {
		isNotNull();

		Map<String, Object> map = getMap(actual);
		Assertions.assertThat(map).isNotNull();

		Assertions.assertThat(
			map.keySet()).hasSameElementsAs(theKeys);
	}

	public void hasKeyWithValue(String theKey, Object theExpectedValue) {
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
			return;
		}

		if (isJsonList(theExpectedValue)) {
			assertJsonList(actualMap, theKey, theExpectedValue);
			return;
		}

		Assertions.assertThat(actualMap)
			.extracting(theKey)
			.isEqualTo(theExpectedValue);
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
		return theString.trim().replaceAll("\\n", "").replaceAll("\\t", "");
	}

	private boolean isValidJson(String theStrValue) {
		getMap(theStrValue);
		return true;
	}

	public void hasKeysWithValues(List<String> theKeys, List<Object> theValues) {
		isNotNull();

		checkSizes(theKeys.size(), theValues.size());

		Map<String, Object> map = getMap(actual);

		Assertions.assertThat(map).isNotNull();
		Assertions.assertThat(map.keySet()).containsAll(theKeys);
		checkKeysAndValues(map, theKeys, theValues);
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

	private void checkSizes(int keysSize, int valuesSize) {
		if (keysSize != valuesSize) {
			fail("Keys and values should have same size. Received " + keysSize + " keys and " + valuesSize + " values.");
		}
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
		return null;
	}


	public void hasPaths(String... thePaths) {
		for (String path : thePaths) {
			hasPath(path);
		}
	}

	public void hasPathWithValue(String thePath, String theValue) {
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
			return;
		}

		if (isJsonList(theValue)) {
			Assertions.assertThat(pathMap)
				.extracting(lastPathElement)
				.asList()
				.hasSameElementsAs(getList(theValue));
			return;
		}

		// check last path element's value
		Assertions.assertThat(pathMap)
			.extracting(pathElements[pathElements.length-1])
				.isEqualTo(theValue);
	}

	public void hasPathsWithValues(List<String> thePaths, List<String> theValues) {
		if (thePaths.size() != theValues.size()) {
			fail("Paths size (" + thePaths.size() + ") is different than values size (" + theValues.size() + ")");
			return;
		}

		for (int i = 0; i < thePaths.size(); i++) {
			hasPathWithValue(thePaths.get(i), theValues.get(i));
		}
	}

	private Map<String, Object> getPathMap(String thePath) {
		String[] pathElements = thePath.split("\\.");
		String pathSoFar = "";

		Map<String, Object> pathMap = getMap(actual);

		for (int i = 0; i < pathElements.length-1; i++) {
			String pathElement = pathElements[i];
			pathSoFar += StringUtils.isNotEmpty(pathSoFar) ? "." + pathElement : pathElement;
			Object pathValue = pathMap.get(pathElement);

			// all path values, other than the last, must be json objects (maps)
			assertIsJsonObject(pathSoFar, pathValue);

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

		if (theValue instanceof String) {
			if (!isJsonObjStr(theValue)) {
				fail(thePath + " doesn't contain a json object but a plain string");
				return;
			}

			try {
				getMap((String) theValue);
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
