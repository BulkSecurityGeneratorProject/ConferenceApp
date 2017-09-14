package fish.payara.conference.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static fish.payara.conference.controller.AuthenticationController.AUTHENTICATION_ENDPOINT;

@WebServlet(name = "authenticate", urlPatterns = {AUTHENTICATION_ENDPOINT})
public class AuthenticationController extends HttpServlet {

    public static final String AUTHENTICATION_ENDPOINT = "/resources/api/authenticate";

    /**
     * Authenticate the credential using JWTAuthenticationMechanism
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // authentication completed
    }

}
