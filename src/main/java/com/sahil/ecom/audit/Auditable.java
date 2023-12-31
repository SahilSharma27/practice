package com.sahil.ecom.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

    @JsonIgnore
    @Column(name = "DATE_CREATED")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JsonIgnore
    @Column(name = "CREATED_BY")
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @Column(name ="LAST_UPDATED_DATE")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @JsonIgnore
    @Column(name = "UPDATED_BY")
    @LastModifiedBy
    private String modifiedBy;

    public Auditable() {
        // TODO document why this constructor is empty
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
