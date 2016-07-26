package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.server.EncodingEnum;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by matthewcl on 26/07/16.
 */
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