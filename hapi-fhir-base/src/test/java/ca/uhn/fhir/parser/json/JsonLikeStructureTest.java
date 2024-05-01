package ca.uhn.fhir.parser.json;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonLikeStructureTest {
//	private static FhirContext ourCtx;
//	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonLikeStructureTest.class);

	private static final String TEST_STRUCTURELOADING_DATA = 
		"{" +
		"    \"resourceType\":\"Organization\"," +
		"    \"id\":\"11111\"," +
		"    \"meta\":{" +
		"        \"lastUpdated\":\"3900-09-20T10:10:10.000-07:00\"" +
		"    }," +
		"    \"identifier\":[" +
		"        {" +
		"            \"value\":\"15250\"" +
		"        }" +
		"    ]," +
		"    \"type\":{" +
		"        \"coding\":[" +
		"            {" +
		"                \"system\":\"http://test\"," +
		"                \"code\":\"ins\"," +
		"                \"display\":\"General Ledger System\"," +
		"                \"userSelected\":false" +
		"            }" +
		"        ]" +
		"    }," +
		"    \"name\":\"Acme Investments\"" +
		"}";

	@Test
	public void testStructureLoading() {
		StringReader reader = new StringReader(TEST_STRUCTURELOADING_DATA);
		JsonLikeStructure jsonStructure = new JacksonStructure();
		jsonStructure.load(reader);
		
		BaseJsonLikeObject rootObject = jsonStructure.getRootObject();

		assertNotNull(rootObject);
		assertThat(rootObject.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.OBJECT);
	}

	private static final String TEST_JSONTYPES_DATA = 
			"{" +
			"    \"scalar-string\":\"A scalar string\"," +
			"    \"scalar-number\":11111," +
			"    \"scalar-boolean\":true," +
			"    \"null-value\":null," +
			"    \"object-value\":{" +
			"        \"lastUpdated\":\"3900-09-20T10:10:10.000-07:00\"," +
			"        \"deleted\":\"3909-09-20T10:10:10.000-07:00\"" +
			"    }," +
			"    \"array-value\":[" +
			"        12345," +
			"        {" +
			"            \"value\":\"15250\"" +
			"        }" +
			"    ]" +
			"}";


	@Test
	public void testJsonAndDataTypes() {
		StringReader reader = new StringReader(TEST_JSONTYPES_DATA);
		JsonLikeStructure jsonStructure = new JacksonStructure();
		jsonStructure.load(reader);
		
		BaseJsonLikeObject rootObject = jsonStructure.getRootObject();

		assertNotNull(rootObject);
		
		BaseJsonLikeValue value = rootObject.get("object-value");
		assertNotNull(value);
		assertThat(value.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.OBJECT);
		assertThat(value.isObject()).isEqualTo(true);
		assertThat(value.isArray()).isEqualTo(false);
		assertThat(value.isScalar()).isEqualTo(false);
		assertThat(value.isNull()).isEqualTo(false);

		BaseJsonLikeObject obj = value.getAsObject();
		assertNotNull(obj);
		assertThat(obj.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.OBJECT);
		assertThat(obj.isObject()).isEqualTo(true);
		assertThat(obj.isArray()).isEqualTo(false);
		assertThat(obj.isScalar()).isEqualTo(false);
		assertThat(obj.isNull()).isEqualTo(false);
		
		value = rootObject.get("array-value");
		assertNotNull(value);
		assertThat(value.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.ARRAY);
		assertThat(value.isObject()).isEqualTo(false);
		assertThat(value.isArray()).isEqualTo(true);
		assertThat(value.isScalar()).isEqualTo(false);
		assertThat(value.isNull()).isEqualTo(false);

		BaseJsonLikeArray array = value.getAsArray();
		assertNotNull(array);
		assertThat(array.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.ARRAY);
		assertThat(array.isObject()).isEqualTo(false);
		assertThat(array.isArray()).isEqualTo(true);
		assertThat(array.isScalar()).isEqualTo(false);
		assertThat(array.isNull()).isEqualTo(false);

		value = rootObject.get("null-value");
		assertNotNull(value);
		assertThat(value.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.NULL);
		assertThat(value.isObject()).isEqualTo(false);
		assertThat(value.isArray()).isEqualTo(false);
		assertThat(value.isScalar()).isEqualTo(false);
		assertThat(value.isNull()).isEqualTo(true);

		value = rootObject.get("scalar-string");
		assertNotNull(value);
		assertThat(value.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.SCALAR);
		assertThat(value.isObject()).isEqualTo(false);
		assertThat(value.isArray()).isEqualTo(false);
		assertThat(value.isScalar()).isEqualTo(true);
		assertThat(value.isNull()).isEqualTo(false);
		assertThat(value.getDataType()).isEqualTo(BaseJsonLikeValue.ScalarType.STRING);
		assertThat("A scalar string").isEqualTo(value.getAsString());

		value = rootObject.get("scalar-number");
		assertNotNull(value);
		assertThat(value.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.SCALAR);
		assertThat(value.getDataType()).isEqualTo(BaseJsonLikeValue.ScalarType.NUMBER);
		assertThat("11111").isEqualTo(value.getAsString());

		value = rootObject.get("scalar-boolean");
		assertNotNull(value);
		assertThat(value.getJsonType()).isEqualTo(BaseJsonLikeValue.ValueType.SCALAR);
		assertThat(value.getDataType()).isEqualTo(BaseJsonLikeValue.ScalarType.BOOLEAN);
		assertThat("true").isEqualTo(value.getAsString());
	}

}
