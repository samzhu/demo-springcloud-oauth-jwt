package com.example.demo.repository;

import com.example.demo.model.ResourceScop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceScopRepo extends JpaRepository<ResourceScop, String> {

    List<ResourceScop> findByResourceIdIn(@Param("resourceIds") List<String> resourceIds);

    @Query("SELECT s FROM ResourceScop s, RoleScop rs, RoleInfo ri WHERE s.scopId = rs.scopId and rs.roleId = ri.roleId and ri.code in :roleCodeList")
    List<ResourceScop> findByUserRoleCodeList(@Param("roleCodeList") List<String> roleCodeList);
}
