package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import okhttp3.MediaType;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by matthewcl on 18/07/16.
 */
public class OkHttpRestfulResponse implements IHttpResponse {

    private Response response;
    private boolean myEntityBuffered = false;
    private byte[] myEntityBytes;

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
        String contentType = response.header(Constants.HEADER_CONTENT_TYPE);
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
        if (!myEntityBuffered && response.body() == null) {
            return new StringReader("");
        } else {
            return new InputStreamReader(readEntity());
        }
    }

    @Override
    public InputStream readEntity() throws IOException {
        if (this.myEntityBuffered) {
            return new ByteArrayInputStream(myEntityBytes);
        } else if (response.body() != null) {
            return response.body().byteStream();
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public void bufferEntitity() throws IOException {
        if (myEntityBuffered) {
            return;
        }
        InputStream responseEntity = readEntity();
        if (responseEntity != null) {
            myEntityBuffered = true;
            try {
                myEntityBytes = IOUtils.toByteArray(responseEntity);
            } catch (IllegalStateException e) {
                throw new InternalErrorException(e);
            }
        }
    }

}
