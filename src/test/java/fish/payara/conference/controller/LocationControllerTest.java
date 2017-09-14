package fish.payara.conference.controller;

import fish.payara.conference.repository.LocationRepository;
import fish.payara.conference.domain.Location;
import static java.util.Collections.singletonMap;
import java.util.List;
import javax.inject.Inject;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasContentType;
import static org.valid4j.matchers.http.HttpResponseMatchers.hasStatus;

/**
 * Test class for the LocationController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class LocationControllerTest extends ApplicationTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_HALL_NUMBER = "A";
    private static final String UPDATED_HALL_NUMBER = "B";
    private static final String RESOURCE_PATH = "api/location";

    @Deployment
    public static WebArchive createDeployment() {
        return buildApplication()
                .addClass(Location.class)
                .addClass(LocationRepository.class)
                .addClass(LocationController.class);
    }

    private static Location location;

    @Inject
    private LocationRepository locationRepository;

    @Test
    @InSequence(1)
    public void createLocation() throws Exception {

        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location
        location = new Location();
        location.setName(DEFAULT_NAME);
        location.setHallNumber(DEFAULT_HALL_NUMBER);
        Response response = target(RESOURCE_PATH).post(json(location));
        assertThat(response, hasStatus(Status.CREATED));
        location = response.readEntity(Location.class);

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations.size(), is(databaseSizeBeforeCreate + 1));
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getName(), is(DEFAULT_NAME));
        assertThat(testLocation.getHallNumber(), is(DEFAULT_HALL_NUMBER));
    }

    @Test
    @InSequence(2)
    public void getAllLocations() throws Exception {

        int databaseSize = locationRepository.findAll().size();
        // Get all the locations
        Response response = target(RESOURCE_PATH).get();
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));

        List<Location> locations = response.readEntity(List.class);
        assertThat(locations.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getLocation() throws Exception {

        // Get the location
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", location.getId())).get();
        Location testLocation = response.readEntity(Location.class);
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testLocation.getId(), is(location.getId()));
        assertThat(testLocation.getName(), is(DEFAULT_NAME));
        assertThat(testLocation.getHallNumber(), is(DEFAULT_HALL_NUMBER));
    }

    @Test
    @InSequence(4)
    public void getNonExistingLocation() throws Exception {

        // Get the location
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", Long.MAX_VALUE)).get();
        assertThat(response, hasStatus(Status.NOT_FOUND));
    }

    @Test
    @InSequence(5)
    public void updateLocation() throws Exception {

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = new Location();
        updatedLocation.setId(location.getId());
        updatedLocation.setName(UPDATED_NAME);
        updatedLocation.setHallNumber(UPDATED_HALL_NUMBER);

        Response response = target(RESOURCE_PATH).put(json(updatedLocation));
        assertThat(response, hasStatus(Status.OK));

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations.size(), is(databaseSizeBeforeUpdate));
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getName(), is(UPDATED_NAME));
        assertThat(testLocation.getHallNumber(), is(UPDATED_HALL_NUMBER));
    }

    @Test
    @InSequence(6)
    public void removeLocation() throws Exception {

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", location.getId())).delete();
        assertThat(response, hasStatus(Status.OK));

        // Validate the database is empty
        List<Location> locations = locationRepository.findAll();
        assertThat(locations.size(), is(databaseSizeBeforeDelete - 1));
    }

}
