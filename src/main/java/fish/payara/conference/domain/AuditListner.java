package fish.payara.conference.domain;

import fish.payara.conference.app.security.SecurityUtils;
import java.time.Instant;
import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Entity listener class for audit info
 */
public class AuditListner {

    @Inject
    private SecurityUtils securityUtils;

    @PrePersist
    void onCreate(AbstractAuditingEntity entity) {
        entity.setCreatedDate(Instant.now());
        entity.setCreatedBy(securityUtils.getCurrentUserLogin());
    }

    @PreUpdate
    void onUpdate(AbstractAuditingEntity entity) {
        entity.setLastModifiedDate(Instant.now());
        entity.setLastModifiedBy(securityUtils.getCurrentUserLogin());
    }
}
