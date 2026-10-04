package com.agenticIde.distributed_lovable.workspace_service.repository;


import com.agenticIde.distributed_lovable.comman_lib.enums.ProjectRole;
import com.agenticIde.distributed_lovable.workspace_service.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("""
      SELECT p as project, pm.projectRole as role
      FROM Project p
      JOIN ProjectMember pm
      ON pm.project.id = p.id
      WHERE pm.id.userId = :userId
      AND p.deletedAt IS NULL
      ORDER BY p.updatedAt DESC
""")
    List<ProjectWithRole> findAllAccessibleByUser(@Param("userId") Long userId);

    @Query("""
      SELECT p FROM Project p
      WHERE p.id = :projectId
       AND p.deletedAt is NULL
       AND EXISTS (
       SELECT 1 FROM ProjectMember pm
       WHERE pm.id.userId = :userId
       AND pm.id.projectId = :projectId
      )
""")
    Optional<Project> findAllAccessibleProjectById(@Param("projectId") Long projectId, @Param("userId") Long userId);


    @Query("""
      SELECT p as project, pm.projectRole as role
      FROM Project p
      JOIN ProjectMember pm
      ON pm.project.id = p.id
      WHERE p.id = :projectId
      AND pm.id.userId = :userId
      AND p.deletedAt IS NULL
""")
    Optional<ProjectWithRole> findAllAccessibleProjectByIdWithRole(@Param("projectId") Long projectId, @Param("userId") Long userId);

    interface ProjectWithRole{
        Project getProject();
        ProjectRole getRole();
    }

    //    List<Project> findByOwnerId(Long userId);
//    List<Project> findByOwnerIdOrderByCreatedAtDesc(Long userId);
//    Project findByIdAndOwnerId(Long id,Long userId);
}
