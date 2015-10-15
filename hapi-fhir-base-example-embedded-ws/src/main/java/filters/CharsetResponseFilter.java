package filters;


import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CharsetResponseFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(final ContainerRequest request,
			final ContainerResponse response) {

		final MediaType contentType = response.getMediaType();
		if (contentType != null) {
			response.getHttpHeaders().putSingle(HttpHeaders.CONTENT_TYPE,
					contentType.toString() + ";charset=UTF-8");
		}
		return response;
	}
}