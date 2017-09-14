package fish.payara.conference.repository;

import fish.payara.conference.domain.Authority;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public class AuthorityRepository extends AbstractRepository<Authority, String> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuthorityRepository() {
        super(Authority.class);
    }
}
