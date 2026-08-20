package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.json.BaseJsonLikeArray;
import ca.uhn.fhir.parser.json.BaseJsonLikeObject;
import ca.uhn.fhir.parser.json.BaseJsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JacksonStructureTest {

	private static final String STRING_FIELD = "string-value";
	private static final String NUMBER_FIELD = "number-value";
	private static final String BOOLEAN_FIELD = "boolean-value";
	private static final String OBJECT_FIELD = "object-value";
	private static final String ARRAY_FIELD = "array-value";
	private static final String STRING_VALUE = "text";
	private static final String NUMBER_VALUE = "123";
	private static final String BOOLEAN_VALUE = "true";
	private static final String NULL_FIELD = "null-value";
	private static final String NESTED_FIELD = "nested";
	private static final String NESTED_VALUE = "value";
	private static final int ARRAY_VALUE_ONE = 1;
	private static final int ARRAY_VALUE_TWO = 2;

	@Test
	void getInstanceReturnsNewJacksonStructure() {
		JacksonStructure structure = new JacksonStructure();

		JsonLikeStructure instance = structure.getInstance();

		assertThat(instance).isInstanceOf(JacksonStructure.class);
		assertThat(instance).isNotSameAs(structure);
	}

	@Test
	void loadsObjectRootAndExposesExpectedValueTypes() {
		JacksonStructure structure = new JacksonStructure();

		structure.load(new StringReader("""
				{
				  "%s": "%s",
				  "%s": %s,
				  "%s": %s,
				  "%s": null,
				  "%s": {"%s": "%s"},
				  "%s": [%d, {"%s": %d}]
				}
				""".formatted(STRING_FIELD, STRING_VALUE, NUMBER_FIELD, NUMBER_VALUE, BOOLEAN_FIELD, BOOLEAN_VALUE, NULL_FIELD, OBJECT_FIELD, NESTED_FIELD, NESTED_VALUE, ARRAY_FIELD, ARRAY_VALUE_ONE, NESTED_FIELD, ARRAY_VALUE_TWO)));

		BaseJsonLikeObject root = structure.getRootObject();
		assertThat(root).isNotNull();

		assertThat(root.get(STRING_FIELD))
			.extracting(BaseJsonLikeValue::getJsonType, BaseJsonLikeValue::getDataType, BaseJsonLikeValue::getAsString)
			.containsExactly(BaseJsonLikeValue.ValueType.SCALAR, BaseJsonLikeValue.ScalarType.STRING, STRING_VALUE);

		assertThat(root.get(NUMBER_FIELD))
			.extracting(BaseJsonLikeValue::getJsonType, BaseJsonLikeValue::getDataType, BaseJsonLikeValue::getAsString)
			.containsExactly(BaseJsonLikeValue.ValueType.SCALAR, BaseJsonLikeValue.ScalarType.NUMBER, NUMBER_VALUE);

		assertThat(root.get(BOOLEAN_FIELD))
			.extracting(BaseJsonLikeValue::getJsonType, BaseJsonLikeValue::getDataType, BaseJsonLikeValue::getAsBoolean)
			.containsExactly(BaseJsonLikeValue.ValueType.SCALAR, BaseJsonLikeValue.ScalarType.BOOLEAN, true);

		assertThat(root.get(NULL_FIELD).isNull()).isTrue();

		BaseJsonLikeObject nestedObject = root.get(OBJECT_FIELD).getAsObject();
		assertThat(nestedObject).isNotNull();
		assertThat(nestedObject.get(NESTED_FIELD).getAsString()).isEqualTo(NESTED_VALUE);

		BaseJsonLikeArray array = root.get(ARRAY_FIELD).getAsArray();
		assertThat(array).isNotNull();
		assertThat(array.size()).isEqualTo(2);
		assertThat(array.get(0).getAsNumber()).isEqualTo(ARRAY_VALUE_ONE);
		assertThat(array.get(1).getAsObject().get("nested").getAsNumber()).isEqualTo(ARRAY_VALUE_TWO);
	}

	@Test
	void loadsArrayRootWhenAllowedAndRejectsItFromGetRootObject() {
		JacksonStructure structure = new JacksonStructure();

		structure.load(new StringReader("[\"a\", 2, false]"), true);

		assertThatThrownBy(structure::getRootObject)
			.isInstanceOf(DataFormatException.class)
			.hasMessageContaining("must start with '{'");
	}

	@Test
	void rejectsArrayRootWhenNotAllowed() {
		JacksonStructure structure = new JacksonStructure();

		assertThatThrownBy(() -> structure.load(new StringReader("[1, 2, 3]")))
			.isInstanceOf(DataFormatException.class)
			.hasMessageContaining("must be '{'");
	}

	@Test
	void rejectsMalformedJsonContent() {
		JacksonStructure structure = new JacksonStructure();

		assertThatThrownBy(() -> structure.load(new StringReader("{\"resourceType\":\"Patient\"} trailing")))
			.isInstanceOf(DataFormatException.class)
			.hasMessageContaining("Failed to parse JSON encoded FHIR content");
	}
}
