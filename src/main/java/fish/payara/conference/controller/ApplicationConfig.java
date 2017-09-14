/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.conference.controller;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author gaura
 */
@javax.ws.rs.ApplicationPath("resources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(fish.payara.conference.app.security.SecurityUtils.class);
        resources.add(fish.payara.conference.app.web.CORSFilter.class);
        resources.add(fish.payara.conference.controller.AccountController.class);
        resources.add(fish.payara.conference.controller.LocationController.class);
        resources.add(fish.payara.conference.controller.SessionController.class);
        resources.add(fish.payara.conference.controller.SpeakerController.class);
        resources.add(fish.payara.conference.controller.UserController.class);
    }
    
}
