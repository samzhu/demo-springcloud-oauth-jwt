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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 使用者資料
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
@Table(name = "user_profile", indexes = {
        @Index(name = "email", unique = true, columnList = "email ASC"),
        @Index(name = "PRIMARY", unique = true, columnList = "uid ASC")
})
public class UserProfile implements Serializable {

    private static final long serialVersionUID = -1891922139;
    @Id
    @Column(name = "uid", unique = true, nullable = false, length = 36)
    @NotNull
    @Size(max = 36)
    private String uid;
    @Column(name = "email", unique = true, nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    private String email;
    @Column(name = "username", nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    private String username;
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
