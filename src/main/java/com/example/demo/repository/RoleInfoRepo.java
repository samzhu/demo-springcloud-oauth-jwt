package com.example.demo.repository;

import com.example.demo.model.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleInfoRepo extends JpaRepository<RoleInfo, String> {

    @Query("SELECT r FROM RoleInfo r, UserRoleMap urm WHERE r.roleId = urm.roleId and urm.uid = :uid")
    List<RoleInfo> findByUserAccountUid(@Param("uid") String uid);
}
