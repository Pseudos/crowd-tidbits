package server.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sydney
 *
 */

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class RedirectRoot {
	
	protected Logger getLog() {
		return LoggerFactory.getLogger(getClass());
	}

    @GET
    public Response index(@Context HttpServletRequest request) throws URISyntaxException {
        getLog().debug("Redirecting to ussd/ui");
        return Response.temporaryRedirect(new URI(request.getServletPath()+"/ui")).build();
    }
}