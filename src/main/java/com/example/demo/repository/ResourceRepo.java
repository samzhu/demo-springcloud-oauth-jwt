package com.example.demo.repository;

import com.example.demo.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepo extends JpaRepository<Resource, String> {

    @Query("SELECT r FROM Resource r, OauthClientResourceMap rm WHERE r.resourceId = rm.resourceId and rm.clientId = :clientId")
    List<Resource> findByOauthClientId(@Param("clientId") String clientId);
}
