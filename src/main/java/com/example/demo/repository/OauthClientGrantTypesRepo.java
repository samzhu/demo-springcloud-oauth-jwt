package com.example.demo.repository;

import com.example.demo.model.OauthClientGrantTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientGrantTypesRepo extends JpaRepository<OauthClientGrantTypes, String> {

    List<OauthClientGrantTypes> findByClientId(String clientId);
}
