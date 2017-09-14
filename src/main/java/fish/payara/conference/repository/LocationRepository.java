package fish.payara.conference.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import fish.payara.conference.domain.Location;

public class LocationRepository extends AbstractRepository<Location, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LocationRepository() {
        super(Location.class);
    }

}
