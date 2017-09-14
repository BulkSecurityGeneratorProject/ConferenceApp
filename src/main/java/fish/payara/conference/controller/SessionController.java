package fish.payara.conference.controller;

import fish.payara.conference.domain.Session;
import fish.payara.conference.repository.SessionRepository;
import fish.payara.conference.controller.util.HeaderUtil;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * REST controller for managing Session.
 */
@Path("/api/session")
public class SessionController {

    @Inject
    private Logger log;

    @Inject
    private SessionRepository sessionRepository;

    /**
     * POST : Create a new session.
     *
     * @param session the session to create
     * @return the Response with status 201 (Created) and with body the new
     * session, or with status 400 (Bad Request) if the session has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @POST
    public Response createSession(Session session) throws URISyntaxException {
        log.debug("REST request to save Session : {}", session);
        sessionRepository.create(session);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/session/" + session.getId())),
                "session", session.getId().toString())
                .entity(session).build();
    }

    /**
     * PUT : Updates an existing session.
     *
     * @param session the session to update
     * @return the Response with status 200 (OK) and with body the updated
     * session, or with status 400 (Bad Request) if the session is not valid, or
     * with status 500 (Internal Server Error) if the session couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    public Response updateSession(Session session) throws URISyntaxException {
        log.debug("REST request to update Session : {}", session);
        sessionRepository.edit(session);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "session", session.getId().toString())
                .entity(session).build();
    }

    /**
     * GET : get all the sessions.
     *
     * @return the Response with status 200 (OK) and the list of sessions in
     * body
     *
     */
    @GET
    public List<Session> getAllSessions() {
        log.debug("REST request to get all Sessions");
        List<Session> sessions = sessionRepository.findAll();
        return sessions;
    }

    /**
     * GET /:id : get the "id" session.
     *
     * @param id the id of the session to retrieve
     * @return the Response with status 200 (OK) and with body the session, or
     * with status 404 (Not Found)
     */
    @Path("/{id}")
    @GET
    public Response getSession(@PathParam("id") Long id) {
        log.debug("REST request to get Session : {}", id);
        Session session = sessionRepository.find(id);
        return Optional.ofNullable(session)
                .map(result -> Response.status(Response.Status.OK).entity(session).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" session.
     *
     * @param id the id of the session to delete
     * @return the Response with status 200 (OK)
     */
    @Path("/{id}")
    @DELETE
    public Response removeSession(@PathParam("id") Long id) {
        log.debug("REST request to delete Session : {}", id);
        sessionRepository.remove(sessionRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), "session", id.toString()).build();
    }

}
