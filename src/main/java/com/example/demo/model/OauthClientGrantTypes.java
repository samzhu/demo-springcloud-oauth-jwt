/*
 * This file is generated by jOOQ.
 */
package com.example.demo.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 客戶端可用授權方式
 */
@Data
@Accessors(chain = true)
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.11.5"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "oauth_client_grant_types", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "grant_type"})
}, indexes = {
        @Index(name = "oauth_client_grant_types_uk1", unique = true, columnList = "client_id ASC, grant_type ASC"),
        @Index(name = "PRIMARY", unique = true, columnList = "sid ASC")
})
public class OauthClientGrantTypes implements Serializable {

    private static final long serialVersionUID = 1559059405;
    @Id
    @Column(name = "sid", unique = true, nullable = false, length = 36)
    @NotNull
    @Size(max = 36)
    private String sid;
    @Column(name = "client_id", nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    private String clientId;
    @Column(name = "grant_type", nullable = false, length = 256)
    @NotNull
    @Size(max = 256)
    private String grantType;
    @Column(name = "created_by", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    private String createdBy;
    @Column(name = "created_date", nullable = false)
    @NotNull
    private LocalDateTime createdDate;
    @Column(name = "last_modified_by", length = 100)
    @Size(max = 100)
    private String lastModifiedBy;
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
