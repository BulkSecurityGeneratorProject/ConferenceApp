package fish.payara.conference.controller;

import fish.payara.conference.domain.Location;
import fish.payara.conference.repository.LocationRepository;
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
 * REST controller for managing Location.
 */
@Path("/api/location")
public class LocationController {

    @Inject
    private Logger log;

    @Inject
    private LocationRepository locationRepository;

    /**
     * POST : Create a new location.
     *
     * @param location the location to create
     * @return the Response with status 201 (Created) and with body the new
     * location, or with status 400 (Bad Request) if the location has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @POST
    public Response createLocation(Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        locationRepository.create(location);
        return HeaderUtil.createEntityCreationAlert(Response.created(new URI("/resources/api/location/" + location.getId())),
                "location", location.getId().toString())
                .entity(location).build();
    }

    /**
     * PUT : Updates an existing location.
     *
     * @param location the location to update
     * @return the Response with status 200 (OK) and with body the updated
     * location, or with status 400 (Bad Request) if the location is not valid,
     * or with status 500 (Internal Server Error) if the location couldn't be
     * updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PUT
    public Response updateLocation(Location location) throws URISyntaxException {
        log.debug("REST request to update Location : {}", location);
        locationRepository.edit(location);
        return HeaderUtil.createEntityUpdateAlert(Response.ok(), "location", location.getId().toString())
                .entity(location).build();
    }

    /**
     * GET : get all the locations.
     *
     * @return the Response with status 200 (OK) and the list of locations in
     * body
     *
     */
    @GET
    public List<Location> getAllLocations() {
        log.debug("REST request to get all Locations");
        List<Location> locations = locationRepository.findAll();
        return locations;
    }

    /**
     * GET /:id : get the "id" location.
     *
     * @param id the id of the location to retrieve
     * @return the Response with status 200 (OK) and with body the location, or
     * with status 404 (Not Found)
     */
    @Path("/{id}")
    @GET
    public Response getLocation(@PathParam("id") Long id) {
        log.debug("REST request to get Location : {}", id);
        Location location = locationRepository.find(id);
        return Optional.ofNullable(location)
                .map(result -> Response.status(Response.Status.OK).entity(location).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * DELETE /:id : remove the "id" location.
     *
     * @param id the id of the location to delete
     * @return the Response with status 200 (OK)
     */
    @Path("/{id}")
    @DELETE
    public Response removeLocation(@PathParam("id") Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationRepository.remove(locationRepository.find(id));
        return HeaderUtil.createEntityDeletionAlert(Response.ok(), "location", id.toString()).build();
    }

}
