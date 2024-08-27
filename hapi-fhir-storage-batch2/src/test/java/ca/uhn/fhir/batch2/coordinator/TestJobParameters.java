package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.PasswordField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class TestJobParameters implements IModelJson {

	@JsonProperty("param1")
	@NotBlank
	private String myParam1;

	@JsonProperty("param2")
	@NotBlank
	@Length(min = 5, max = 100)
	private String myParam2;

	@JsonProperty(value = "password")
	@PasswordField
	private String myPassword;

	public String getPassword() {
		return myPassword;
	}

	public TestJobParameters setPassword(String thePassword) {
		myPassword = thePassword;
		return this;
	}

	public String getParam1() {
		return myParam1;
	}

	public TestJobParameters setParam1(String theParam1) {
		myParam1 = theParam1;
		return this;
	}

	public String getParam2() {
		return myParam2;
	}

	public TestJobParameters setParam2(String theParam2) {
		myParam2 = theParam2;
		return this;
	}

}
