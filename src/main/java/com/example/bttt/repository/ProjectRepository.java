package com.example.bttt.repository;

import com.example.bttt.entity.Project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
    SELECT DISTINCT p FROM Project p 
    JOIN p.members m 
    WHERE m.user.id = :userId
""")
List<Project> findProjectsByUserId(@Param("userId") Long userId);
}
