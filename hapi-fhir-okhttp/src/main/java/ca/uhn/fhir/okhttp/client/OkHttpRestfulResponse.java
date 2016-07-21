package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import okhttp3.MediaType;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * Created by matthewcl on 18/07/16.
 */
public class OkHttpRestfulResponse implements IHttpResponse {

    private Response response;

    public OkHttpRestfulResponse(Response response) {
        this.response = response;
    }

    @Override
    public int getStatus() {
        return response.code();
    }

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public String getMimeType() {
        String contentType = response.header("Content-Type");
        if (contentType == null) {
            return null;
        }

        MediaType mediaType = MediaType.parse(contentType);
        if (mediaType == null) {
            return null;
        }

        return typeAndSubtypeOnly(mediaType).toString();
    }

    private MediaType typeAndSubtypeOnly(MediaType input) {
        return MediaType.parse(input.type() + "/" + input.subtype());
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        return response.headers().toMultimap();
    }

    @Override
    public String getStatusInfo() {
        return response.message();
    }

    @Override
    public Reader createReader() throws IOException {
        return response.body().charStream();
    }

    @Override
    public InputStream readEntity() throws IOException {
        return response.body().byteStream();
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public void bufferEntitity() throws IOException {

    }

}
