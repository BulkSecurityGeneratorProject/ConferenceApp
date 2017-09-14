package fish.payara.conference.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import fish.payara.conference.domain.Speaker;

public class SpeakerRepository extends AbstractRepository<Speaker, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SpeakerRepository() {
        super(Speaker.class);
    }

}
