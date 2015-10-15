package filters;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CorsResponseFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(final ContainerRequest req,
			final ContainerResponse contResp) {

		final ResponseBuilder resp = Response.fromResponse(contResp
				.getResponse());
		resp.header("Access-Control-Allow-Origin", "*").header(
				"Access-Control-Allow-Methods", "GET, POST, OPTIONS");

		final String reqHead = req
				.getHeaderValue("Access-Control-Request-Headers");

		if (null != reqHead && !reqHead.equals("")) {
			resp.header("Access-Control-Allow-Headers", reqHead);
		}

		contResp.setResponse(resp.build());
		return contResp;
	}

}