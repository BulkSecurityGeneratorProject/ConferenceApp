package fish.payara.conference.controller;

import fish.payara.conference.domain.Speaker;
import fish.payara.conference.repository.SpeakerRepository;
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
 * REST controller for managing Speaker.
 */
@Path("/api/speaker")
public class SpeakerController {

    @Inject
    private Logger log;

    @Inject
    private SpeakerRepository speakerRepository;

    /**
     * POST : Create a new speaker.
     *
     * @param speaker the speaker to create
     * @return the Response with status 201 (Created) and with body the new
     * speaker, or with status 400 (Bad Request) if the speaker has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @POST
    public Response createSpeaker(Speaker speaker) throws URISyntaxException {
        log.debug("REST request to save Speaker : {}", speaker);
        speakerRepository.create(speaker);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/speaker/" + speaker.getId())),
                "speaker", speaker.getId().toString())
                .entity(speaker).build();
    }

    /**
     * PUT : Updates an existing speaker.
     *
     * @param speaker the speaker to update
     * @return the Response with status 200 (OK) and with body the updated
     * speaker, or with status 400 (Bad Request) if the speaker is not valid, or
     * with status 500 (Internal Server Error) if the speaker couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    public Response updateSpeaker(Speaker speaker) throws URISyntaxException {
        log.debug("REST request to update Speaker : {}", speaker);
        speakerRepository.edit(speaker);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "speaker", speaker.getId().toString())
                .entity(speaker).build();
    }

    /**
     * GET : get all the speakers.
     *
     * @return the Response with status 200 (OK) and the list of speakers in
     * body
     *
     */
    @GET
    public List<Speaker> getAllSpeakers() {
        log.debug("REST request to get all Speakers");
        List<Speaker> speakers = speakerRepository.findAll();
        return speakers;
    }

    /**
     * GET /:id : get the "id" speaker.
     *
     * @param id the id of the speaker to retrieve
     * @return the Response with status 200 (OK) and with body the speaker, or
     * with status 404 (Not Found)
     */
    @Path("/{id}")
    @GET
    public Response getSpeaker(@PathParam("id") Long id) {
        log.debug("REST request to get Speaker : {}", id);
        Speaker speaker = speakerRepository.find(id);
        return Optional.ofNullable(speaker)
                .map(result -> Response.status(Response.Status.OK).entity(speaker).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" speaker.
     *
     * @param id the id of the speaker to delete
     * @return the Response with status 200 (OK)
     */
    @Path("/{id}")
    @DELETE
    public Response removeSpeaker(@PathParam("id") Long id) {
        log.debug("REST request to delete Speaker : {}", id);
        speakerRepository.remove(speakerRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), "speaker", id.toString()).build();
    }

}
