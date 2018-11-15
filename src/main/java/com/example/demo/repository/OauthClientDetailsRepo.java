package com.example.demo.repository;

import com.example.demo.model.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientDetailsRepo extends JpaRepository<OauthClientDetails, String> {
}
