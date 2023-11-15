package sample.fhir.server.jersey;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SampleJerseyRestfulServerApplicationTest {

	static {
		/*
		 * The following is needed because of this bug, which is fixed but not released
		 * as of 2022-11-07. At some point in the future we can upgrade boot again and
		 * remove this.
		 * https://github.com/spring-projects/spring-boot/issues/12649
		 */
		System.setProperty(LoggingSystem.SYSTEM_PROPERTY, LoggingSystem.NONE);
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void metadata() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity(
			"/fhir/metadata",
			String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).contains("\"status\": \"active\"");
	}

	@Test
	public void patientResource() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity(
			"/fhir/Patient/1",
			String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).contains("\"family\": \"Van Houte\"");
	}

}
