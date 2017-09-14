package fish.payara.conference.controller;

import fish.payara.conference.repository.SpeakerRepository;
import fish.payara.conference.domain.Location;
import fish.payara.conference.domain.SessionType;
import fish.payara.conference.domain.Speaker;
import fish.payara.conference.domain.Session;
import fish.payara.conference.domain.DesignationType;
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
 * Test class for the SpeakerController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class SpeakerControllerTest extends ApplicationTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_BIO = "A";
    private static final String UPDATED_BIO = "B";
    private static final boolean DEFAULT_FEATURED = false;
    private static final boolean UPDATED_FEATURED = true;
    private static final String RESOURCE_PATH = "api/speaker";

    @Deployment
    public static WebArchive createDeployment() {
        return buildApplication()
                .addClass(Location.class)
                .addClass(SessionType.class)
                .addClass(Speaker.class)
                .addClass(Session.class)
                .addClass(DesignationType.class)
                .addClass(SpeakerRepository.class)
                .addClass(SpeakerController.class);
    }

    private static Speaker speaker;

    @Inject
    private SpeakerRepository speakerRepository;

    @Test
    @InSequence(1)
    public void createSpeaker() throws Exception {

        int databaseSizeBeforeCreate = speakerRepository.findAll().size();

        // Create the Speaker
        speaker = new Speaker();
        speaker.setName(DEFAULT_NAME);
        speaker.setBio(DEFAULT_BIO);
        speaker.setFeatured(DEFAULT_FEATURED);
        Response response = target(RESOURCE_PATH).post(json(speaker));
        assertThat(response, hasStatus(Status.CREATED));
        speaker = response.readEntity(Speaker.class);

        // Validate the Speaker in the database
        List<Speaker> speakers = speakerRepository.findAll();
        assertThat(speakers.size(), is(databaseSizeBeforeCreate + 1));
        Speaker testSpeaker = speakers.get(speakers.size() - 1);
        assertThat(testSpeaker.getName(), is(DEFAULT_NAME));
        assertThat(testSpeaker.getBio(), is(DEFAULT_BIO));
        assertThat(testSpeaker.isFeatured(), is(DEFAULT_FEATURED));
    }

    @Test
    @InSequence(2)
    public void getAllSpeakers() throws Exception {

        int databaseSize = speakerRepository.findAll().size();
        // Get all the speakers
        Response response = target(RESOURCE_PATH).get();
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));

        List<Speaker> speakers = response.readEntity(List.class);
        assertThat(speakers.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getSpeaker() throws Exception {

        // Get the speaker
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", speaker.getId())).get();
        Speaker testSpeaker = response.readEntity(Speaker.class);
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testSpeaker.getId(), is(speaker.getId()));
        assertThat(testSpeaker.getName(), is(DEFAULT_NAME));
        assertThat(testSpeaker.getBio(), is(DEFAULT_BIO));
        assertThat(testSpeaker.isFeatured(), is(DEFAULT_FEATURED));
    }

    @Test
    @InSequence(4)
    public void getNonExistingSpeaker() throws Exception {

        // Get the speaker
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", Long.MAX_VALUE)).get();
        assertThat(response, hasStatus(Status.NOT_FOUND));
    }

    @Test
    @InSequence(5)
    public void updateSpeaker() throws Exception {

        int databaseSizeBeforeUpdate = speakerRepository.findAll().size();

        // Update the speaker
        Speaker updatedSpeaker = new Speaker();
        updatedSpeaker.setId(speaker.getId());
        updatedSpeaker.setName(UPDATED_NAME);
        updatedSpeaker.setBio(UPDATED_BIO);
        updatedSpeaker.setFeatured(UPDATED_FEATURED);

        Response response = target(RESOURCE_PATH).put(json(updatedSpeaker));
        assertThat(response, hasStatus(Status.OK));

        // Validate the Speaker in the database
        List<Speaker> speakers = speakerRepository.findAll();
        assertThat(speakers.size(), is(databaseSizeBeforeUpdate));
        Speaker testSpeaker = speakers.get(speakers.size() - 1);
        assertThat(testSpeaker.getName(), is(UPDATED_NAME));
        assertThat(testSpeaker.getBio(), is(UPDATED_BIO));
        assertThat(testSpeaker.isFeatured(), is(UPDATED_FEATURED));
    }

    @Test
    @InSequence(6)
    public void removeSpeaker() throws Exception {

        int databaseSizeBeforeDelete = speakerRepository.findAll().size();

        // Delete the speaker
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", speaker.getId())).delete();
        assertThat(response, hasStatus(Status.OK));

        // Validate the database is empty
        List<Speaker> speakers = speakerRepository.findAll();
        assertThat(speakers.size(), is(databaseSizeBeforeDelete - 1));
    }

}
