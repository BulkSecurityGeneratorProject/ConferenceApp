package fish.payara.conference.controller;

import fish.payara.conference.repository.UserRepository;
import fish.payara.conference.domain.User;
import fish.payara.conference.app.service.MailService;
import fish.payara.conference.app.service.UserService;
import fish.payara.conference.controller.dto.ManagedUserDTO;
import fish.payara.conference.controller.dto.UserDTO;
import fish.payara.conference.controller.util.HeaderUtil;
import fish.payara.conference.controller.util.Page;
import fish.payara.conference.controller.util.PaginationUtil;

import org.slf4j.Logger;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import static java.util.stream.Collectors.toList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * REST controller for managing users.
 *
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of
 * authorities.</p>
 */
@Path("/api")
public class UserController {

    @Inject
    private Logger log;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    /**
     * POST /users : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends
     * an mail with an activation link. The user needs to be activated on
     * creation.
     * </p>
     *
     * @param managedUserDTO the user to create
     * @return the Response with status 201 (Created) and with body the new
     * user, or with status 400 (Bad Request) if the login or email is already
     * in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @Path(value = "/users")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(ManagedUserDTO managedUserDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserDTO);

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserDTO.getLogin().toLowerCase()).isPresent()) {
            return HeaderUtil.createFailureAlert(Response.status(BAD_REQUEST), "userManagement", "userexists", "Login already in use").build();
        } else if (userRepository.findOneByEmail(managedUserDTO.getEmail()).isPresent()) {
            return HeaderUtil.createFailureAlert(Response.status(BAD_REQUEST), "userManagement", "emailexists", "Email already in use").build();
        } else {
            User newUser = userService.createUser(managedUserDTO);
            mailService.sendCreationEmail(newUser);
            return HeaderUtil.createAlert(Response.created(new URI("/resources/api/users/" + newUser.getLogin())),
                    "userManagement.created", newUser.getLogin()).entity(new UserDTO(newUser)).build();
        }
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param managedUserDTO the user to update
     * @return the Response with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be
     * updated
     */
    @Path(value = "/users")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(ManagedUserDTO managedUserDTO) {
        log.debug("REST request to update User : {}", managedUserDTO);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserDTO.getId()))) {
            return HeaderUtil.createFailureAlert(Response.status(BAD_REQUEST), "userManagement", "emailexists", "Email already in use").build();
        }
        existingUser = userRepository.findOneByLogin(managedUserDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserDTO.getId()))) {
            return HeaderUtil.createFailureAlert(Response.status(BAD_REQUEST), "userManagement", "userexists", "Login already in use").build();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(managedUserDTO);

        return updatedUser.map(userDTO -> HeaderUtil.createAlert(Response.ok(userDTO),
                "userManagement.updated", managedUserDTO.getLogin()).build())
                .orElseGet(() -> Response.status(NOT_FOUND).build());
    }

    /**
     * GET /users : get all users.
     *
     * @param page the pagination information
     * @param size the pagination size information
     * @return the Response with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be
     * generated
     */
    @Path(value = "/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@QueryParam("page") int page, @QueryParam("size") int size) throws URISyntaxException {
        List<User> userList = userRepository.getUsersWithAuthorities(page * size, size);
        List<UserDTO> userDTOs = userList.stream()
                .map(UserDTO::new)
                .collect(toList());

        ResponseBuilder builder = Response.ok(userDTOs);
        PaginationUtil.generatePaginationHttpHeaders(builder, new Page(page, size, userRepository.count()), "/resources/api/users");
        return builder.build();
    }

    /**
     * @return a string list of the all of the roles
     */
    @Path("/users/authorities")
    @GET
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the Response with status 200 (OK) and with body the "login" user,
     * or with status 404 (Not Found)
     */
    @Path(value = "/users/{login}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("login") String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new)
                .map(userDTO -> Response.ok(userDTO).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the Response with status 200 (OK)
     */
    @Path(value = "/users/{login}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("login") String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return HeaderUtil.createAlert(Response.ok(), "userManagement.deleted", login).build();
    }
}
