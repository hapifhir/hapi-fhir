package ca.uhn.fhir.util;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.SensitiveNoDisplay;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class JsonUtilTest {

	@JsonFilter(IModelJson.SENSITIVE_DATA_FILTER_NAME)
	class TestObject implements IModelJson {
		@JsonProperty("sensitiveField")
		@SensitiveNoDisplay
		private String mySensitiveField;

		@JsonProperty(value = "publicField")
		private String myPublicField;

		public String getPrivateField() {
			return mySensitiveField;
		}

		public void setSensitiveField(String thePrivateField) {
			this.mySensitiveField = thePrivateField;
		}

		public String getPublicField() {
			return myPublicField;
		}

		public void setPublicField(String thePublicField) {
			this.myPublicField = thePublicField;
		}
	}

	@Test
	public void testSensitiveNoDisplayAnnotationIsHiddenFromBasicSerialization() {
		TestObject object = new TestObject();
		object.setPublicField("Public Value!");
		object.setSensitiveField("Sensitive Value!");

		String sensitiveExcluded  = JsonUtil.serializeOrInvalidRequest(object);
		assertThat(sensitiveExcluded, is(not(containsString("Sensitive Value!"))));

		String sensitiveIncluded  = JsonUtil.serializeWithSensitiveData(object);
		assertThat(sensitiveIncluded, is(containsString("Sensitive Value!")));
	}
}
