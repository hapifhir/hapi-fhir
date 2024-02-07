package ca.uhn.fhir.okhttp.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OkHttpRestfulClientTest {

    @Test
    public void testNewHeaderBuilder_urlHasTrailingSlash_shouldTrim() {
        StringBuilder headerBuilder = OkHttpRestfulClient.newHeaderBuilder(new StringBuilder("http://localhost/"));

			assertThat(headerBuilder.toString()).isEqualTo("http://localhost");
    }

    @Test
    public void testNewHeaderBuilder_urlHasNoTrailingSlash_shouldNotTrimLastCharacter() {
        StringBuilder headerBuilder = OkHttpRestfulClient.newHeaderBuilder(new StringBuilder("http://example.com"));

			assertThat(headerBuilder.toString()).isEqualTo("http://example.com");
    }

}
