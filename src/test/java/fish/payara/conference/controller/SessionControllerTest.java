package fish.payara.conference.controller;

import fish.payara.conference.repository.SessionRepository;
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
 * Test class for the SessionController REST controller.
 *
 */
@RunWith(Arquillian.class)
public class SessionControllerTest extends ApplicationTest {

    private static final String DEFAULT_TITLE = "A";
    private static final String UPDATED_TITLE = "B";
    private static final String DEFAULT_DESCRIPTION = "A";
    private static final String UPDATED_DESCRIPTION = "B";
    private static final String RESOURCE_PATH = "api/session";

    @Deployment
    public static WebArchive createDeployment() {
        return buildApplication()
                .addClass(Location.class)
                .addClass(SessionType.class)
                .addClass(Speaker.class)
                .addClass(Session.class)
                .addClass(DesignationType.class)
                .addClass(SessionRepository.class)
                .addClass(SessionController.class);
    }

    private static Session session;

    @Inject
    private SessionRepository sessionRepository;

    @Test
    @InSequence(1)
    public void createSession() throws Exception {

        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session
        session = new Session();
        session.setTitle(DEFAULT_TITLE);
        session.setDescription(DEFAULT_DESCRIPTION);
        Response response = target(RESOURCE_PATH).post(json(session));
        assertThat(response, hasStatus(Status.CREATED));
        session = response.readEntity(Session.class);

        // Validate the Session in the database
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(databaseSizeBeforeCreate + 1));
        Session testSession = sessions.get(sessions.size() - 1);
        assertThat(testSession.getTitle(), is(DEFAULT_TITLE));
        assertThat(testSession.getDescription(), is(DEFAULT_DESCRIPTION));
    }

    @Test
    @InSequence(2)
    public void getAllSessions() throws Exception {

        int databaseSize = sessionRepository.findAll().size();
        // Get all the sessions
        Response response = target(RESOURCE_PATH).get();
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));

        List<Session> sessions = response.readEntity(List.class);
        assertThat(sessions.size(), is(databaseSize));
    }

    @Test
    @InSequence(3)
    public void getSession() throws Exception {

        // Get the session
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", session.getId())).get();
        Session testSession = response.readEntity(Session.class);
        assertThat(response, hasStatus(Status.OK));
        assertThat(response, hasContentType(MediaType.APPLICATION_JSON_TYPE));
        assertThat(testSession.getId(), is(session.getId()));
        assertThat(testSession.getTitle(), is(DEFAULT_TITLE));
        assertThat(testSession.getDescription(), is(DEFAULT_DESCRIPTION));
    }

    @Test
    @InSequence(4)
    public void getNonExistingSession() throws Exception {

        // Get the session
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", Long.MAX_VALUE)).get();
        assertThat(response, hasStatus(Status.NOT_FOUND));
    }

    @Test
    @InSequence(5)
    public void updateSession() throws Exception {

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        Session updatedSession = new Session();
        updatedSession.setId(session.getId());
        updatedSession.setTitle(UPDATED_TITLE);
        updatedSession.setDescription(UPDATED_DESCRIPTION);

        Response response = target(RESOURCE_PATH).put(json(updatedSession));
        assertThat(response, hasStatus(Status.OK));

        // Validate the Session in the database
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(databaseSizeBeforeUpdate));
        Session testSession = sessions.get(sessions.size() - 1);
        assertThat(testSession.getTitle(), is(UPDATED_TITLE));
        assertThat(testSession.getDescription(), is(UPDATED_DESCRIPTION));
    }

    @Test
    @InSequence(6)
    public void removeSession() throws Exception {

        int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Delete the session
        Response response = target(RESOURCE_PATH + "/{id}", singletonMap("id", session.getId())).delete();
        assertThat(response, hasStatus(Status.OK));

        // Validate the database is empty
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(databaseSizeBeforeDelete - 1));
    }

}
