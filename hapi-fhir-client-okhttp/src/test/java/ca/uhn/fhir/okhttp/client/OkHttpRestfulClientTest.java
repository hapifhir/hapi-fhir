package ca.uhn.fhir.okhttp.client;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OkHttpRestfulClientTest {

    @Test
    public void testNewHeaderBuilder_urlHasTrailingSlash_shouldTrim() {
        StringBuilder headerBuilder = OkHttpRestfulClient.newHeaderBuilder(new StringBuilder("http://localhost/"));

        assertThat(headerBuilder.toString(), equalTo("http://localhost"));
    }

    @Test
    public void testNewHeaderBuilder_urlHasNoTrailingSlash_shouldNotTrimLastCharacter() {
        StringBuilder headerBuilder = OkHttpRestfulClient.newHeaderBuilder(new StringBuilder("http://example.com"));

        assertThat(headerBuilder.toString(), equalTo("http://example.com"));
    }

}
