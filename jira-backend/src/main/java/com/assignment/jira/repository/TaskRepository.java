package com.assignment.jira.repository;

import com.assignment.jira.entity.Task;
import com.assignment.jira.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    
    List<Task> findByProjectId(Long projectId);

    Page<Task> findByAssignedUserId(Long userId, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    Page<Task> searchTasks(@Param("keyword") String keyword, Pageable pageable);

    long countByProjectIdAndStatus(Long projectId, TaskStatus status);
    
    long countByProjectId(Long projectId);
}
