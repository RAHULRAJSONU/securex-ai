package io.github.rahulrajsonu.securexai.data.entity;

import io.github.rahulrajsonu.securexai.data.security.SignedUserHelper;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractPersistable implements Persistable<UUID>, Serializable {

    @Serial
    private static final long serialVersionUID = 2535090450811888936L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @TenantId
    private String tenantId;

    @CreatedDate
    @Column(name = "CreatedOn")
    private LocalDateTime createdOn;

    @CreatedBy
    @Column(name = "CreatedBy", length = 50)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "UpdatedOn")
    private LocalDateTime updatedOn;

    @LastModifiedBy
    @Column(name = "UpdatedBy", length = 50)
    private String updatedBy;

    @Column(name = "DeletedOn")
    private LocalDateTime deletedOn;

    @Column(name = "DeletedBy", length = 50)
    private String deletedBy;

    @Column(name = "isDeleted", length = 50)
    private Boolean isDeleted = false;

    @PreUpdate
    @PrePersist
    public void beforeAnyUpdate() {
        if (isDeleted != null && isDeleted) {

            if (deletedBy == null) {
                deletedBy = SignedUserHelper.userId();
            }

            if (getDeletedOn() == null) {
                deletedOn = LocalDateTime.now();
            }
        }
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractPersistable rhs = (AbstractPersistable) obj;
        return null != this.id && this.id.equals(rhs.id);
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += (this.id == null) ? 0 : this.id.hashCode() * 31;
        return hashCode;
    }
}