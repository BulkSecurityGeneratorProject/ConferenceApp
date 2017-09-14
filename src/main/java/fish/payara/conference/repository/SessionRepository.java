package fish.payara.conference.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import fish.payara.conference.domain.Session;

public class SessionRepository extends AbstractRepository<Session, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SessionRepository() {
        super(Session.class);
    }

}
