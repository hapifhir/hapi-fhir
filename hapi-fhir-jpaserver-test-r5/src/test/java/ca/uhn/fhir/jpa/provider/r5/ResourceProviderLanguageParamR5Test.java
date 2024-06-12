package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceProviderLanguageParamR5Test extends BaseResourceProviderR5Test {

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithLanguageParamEnabled() {
		myStorageSettings.setLanguageSearchParameterEnabled(true);
		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setLanguage("en");
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setLanguage("fr");
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		List<String> foundResources;
		Bundle result;

		result = myClient
			.search()
			.forResource(Patient.class)
			.where(new TokenClientParam(Constants.PARAM_LANGUAGE).exactly().code("en"))
			.returnBundle(Bundle.class)
			.execute();

		foundResources = toUnqualifiedVersionlessIdValues(result);
		assertThat(foundResources).contains(patId.getValue());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithLanguageParamDisabled() {
		myStorageSettings.setLanguageSearchParameterEnabled(new JpaStorageSettings().isLanguageSearchParameterEnabled());
		mySearchParamRegistry.forceRefresh();

		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new TokenClientParam(Constants.PARAM_LANGUAGE).exactly().code("en"))
				.returnBundle(Bundle.class)
				.execute();
		});
		assertThat(exception.getMessage()).contains(Msg.code(1223));
	}
}
