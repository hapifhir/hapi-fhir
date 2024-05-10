package ca.uhn.fhir.okhttp.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OkHttpRestfulClientTest {

    @Test
    public void testNewHeaderBuilder_urlHasTrailingSlash_shouldTrim() {
        StringBuilder headerBuilder = OkHttpRestfulClient.newHeaderBuilder(new StringBuilder("http://localhost/"));

			assertEquals("http://localhost", headerBuilder.toString());
    }

    @Test
    public void testNewHeaderBuilder_urlHasNoTrailingSlash_shouldNotTrimLastCharacter() {
        StringBuilder headerBuilder = OkHttpRestfulClient.newHeaderBuilder(new StringBuilder("http://example.com"));

			assertEquals("http://example.com", headerBuilder.toString());
    }

}
