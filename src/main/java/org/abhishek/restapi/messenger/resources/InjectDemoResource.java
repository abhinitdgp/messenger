package org.abhishek.restapi.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/injectdemo")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class InjectDemoResource {

	@GET
	@Path("/annotations")
	public String getParamUsingAnnotations(@MatrixParam("param") String mparam,
			@HeaderParam("customHeader") String value, @CookieParam("name") String cookie) {
		return "Matrix param is: " + mparam + " and customer header value is: " + value;
	}

	@GET
	@Path("/context")
	public String getParamsUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders headers) {
		String path = uriInfo.getAbsolutePath().toString();
		String header = headers.getCookies().toString();
		return "Path: " + path + " media type: " + header;
	}

	// One more Way to get QueryParam using BeanParam
	// refer video 24:
	// https://www.youtube.com/watch?v=4nZUT1oBI1g&list=PLqq-6Pq4lTTZh5U8RbdXq0WaYvZBz2rbn&index=24
}
