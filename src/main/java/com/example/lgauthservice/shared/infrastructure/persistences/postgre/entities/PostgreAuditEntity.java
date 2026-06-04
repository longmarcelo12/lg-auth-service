package com.example.lgauthservice.shared.infrastructure.persistences.postgre.entities;//package com.msb.de.pipeline.shared.infrastructure.persistences.postgre.entities;
//
//import com.msb.de.pipeline.shared.domain.entities.BaseEntity;
//import jakarta.persistence.Column;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.MappedSuperclass;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
//import lombok.Getter;
//import org.springframework.data.annotation.CreatedBy;
//import org.springframework.data.annotation.LastModifiedBy;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.io.Serial;
//import java.io.Serializable;
//import java.time.Instant;
//
//
//@Getter
//@MappedSuperclass
//@EntityListeners({AuditingEntityListener.class})
//public abstract class PostgreAuditEntity extends BaseEntity implements Serializable {
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    @Column(name = "CREATED_TIME", nullable = false, updatable = false)
//    private Instant createdTime;
//
//    @Column(name = "UPDATED_TIME", nullable = false)
//    private Instant updatedTime;
//
//    @CreatedBy
//    @Column(name = "CREATED_BY")
//    private String createdBy;
//
//    @LastModifiedBy
//    @Column(name = "UPDATED_BY")
//    private String updatedBy;
//
//    @PrePersist
//    public void onPrePersist() {
//        this.setCreatedTime(Instant.now());
//        this.setUpdatedTime(Instant.now());
//    }
//
//    @PreUpdate
//    public void onPreUpdate() {
//        this.setUpdatedTime(Instant.now());
//    }
//
//    public void setCreatedTime(Instant createdTime) {
//        this.createdTime = createdTime;
//    }
//
//    public void setUpdatedTime(Instant updatedTime) {
//        this.updatedTime = updatedTime;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public void setUpdatedBy(String updatedBy) {
//        this.updatedBy = updatedBy;
//    }
//
//    @Override
//    public Long getId() {
//        return null;
//    }
//}
