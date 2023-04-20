package com.example;

import java.net.URI;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class RootSlashAddingFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) {
		UriInfo uriInfo = requestContext.getUriInfo();
		var path = uriInfo.getRequestUri().toString();
		if (path.endsWith("/message")) {
			var redirectTo = path + "/";
			requestContext.abortWith(
					Response.seeOther(URI.create(redirectTo))
							.build());
			return;

		}
	}
}