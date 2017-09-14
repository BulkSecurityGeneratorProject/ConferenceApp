package fish.payara.conference.controller;

import fish.payara.conference.app.config.SecurityConfig;
import fish.payara.conference.app.config.Constants;
import fish.payara.conference.app.security.SecurityUtils;
import fish.payara.conference.app.producer.TemplateEngineProducer;
import fish.payara.conference.app.service.MailService;
import fish.payara.conference.app.util.RandomUtil;
import fish.payara.conference.app.service.UserService;
import fish.payara.conference.controller.dto.LoginDTO;
import fish.payara.conference.controller.dto.UserDTO;
import fish.payara.conference.domain.AbstractAuditingEntity;
import fish.payara.conference.domain.AuditListner;
import fish.payara.conference.domain.Authority;
import fish.payara.conference.domain.User;
import fish.payara.conference.repository.AuthorityRepository;
import fish.payara.conference.repository.UserRepository;
import static fish.payara.conference.controller.AbstractTest.buildArchive;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;

/**
 * Abstract class for application packaging.
 *
 */
public abstract class ApplicationTest extends AbstractTest {

    protected static final String USERNAME = "admin";
    protected static final String PASSWORD = "admin";
    protected static final String INVALID_PASSWORD = "invalid_password";
    protected static final String INCORRECT_PASSWORD = "pw";
    private static final String AUTH_RESOURCE_PATH = "api/authenticate";

    protected String tokenId;

    public static WebArchive buildApplication() {
        return buildArchive()
                .addPackages(true,
                        SecurityConfig.class.getPackage(),
                        MailService.class.getPackage(),
                        UserDTO.class.getPackage(),
                        SecurityUtils.class.getPackage(),
                        RandomUtil.class.getPackage())
                .addClass(TemplateEngineProducer.class)
                .addClass(User.class)
                .addClass(Authority.class)
                .addClass(AbstractAuditingEntity.class)
                .addClass(AuditListner.class)
                .addClass(UserRepository.class)
                .addClass(AuthorityRepository.class)
                .addClass(UserService.class)
                .addClass(AuthenticationController.class)
                .addClass(AbstractTest.class)
                .addClass(ApplicationTest.class)
                .addAsResource(new ClassLoaderAsset("META-INF/microprofile-config.properties"), "META-INF/microprofile-config.properties")
                .addAsResource(new ClassLoaderAsset("i18n/messages.properties"), "i18n/messages.properties");
    }

    @Before
    public void setUp() throws Exception {
        login(USERNAME, PASSWORD);
    }

    @After
    public void tearDown() {
        logout();
    }

    protected Response login(String username, String password) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        Response response = target(AUTH_RESOURCE_PATH).post(Entity.json(loginDTO));
        tokenId = response.getHeaderString(Constants.AUTHORIZATION_HEADER);
        return response;
    }

    protected void logout() {
        tokenId = null;
    }

    @Override
    protected Invocation.Builder target(String path) {
        return super.target(path).header(Constants.AUTHORIZATION_HEADER, tokenId);
    }

    @Override
    protected Invocation.Builder target(String path, Map<String, Object> params) {
        return super.target(path, params).header(Constants.AUTHORIZATION_HEADER, tokenId);
    }

}
