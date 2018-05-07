package sample.fhir.server.jersey;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SampleJerseyRestfulServerApplicationTest {

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
