package ru.Dmitriy.NauJava_ToDo_List.repository;

import ru.Dmitriy.NauJava_ToDo_List.entity.Task;
import ru.Dmitriy.NauJava_ToDo_List.entity.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCompleted(Long userId, boolean completed);

    List<Task> findByUserIdOrderByPriorityDesc(Long userId);

    List<Task> findByPriority(Priority priority);

    List<Task> findByCreatedAtAfter(LocalDateTime date);

    List<Task> findByDescriptionContainingIgnoreCase(String keyword);

    List<Task> findByUserId(Long userId, Sort sort);

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByCompleted(boolean completed, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.priority = :priority AND t.completed = false")
    List<Task> findHighPriorityIncompleteTasks(@Param("priority") Priority priority);

    @Query("SELECT t FROM Task t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Task> findTasksBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Task t WHERE LENGTH(t.description) > :minLength")
    List<Task> findTasksWithLongDescription(@Param("minLength") int minLength);

    @Query("SELECT t FROM Task t WHERE DATE(t.createdAt) = CURRENT_DATE")
    List<Task> findTodayTasks();

    @Query("""
           SELECT
               COUNT(t) as total,
               SUM(CASE WHEN t.completed = true THEN 1 ELSE 0 END) as completed,
               SUM(CASE WHEN t.completed = false THEN 1 ELSE 0 END) as pending
           FROM Task t
           WHERE t.user.id = :userId
           """)
    Object[] getTaskStatistics(@Param("userId") Long userId);

    @Query("""
           SELECT
               t.priority,
               COUNT(t) as count
           FROM Task t
           WHERE t.user.id = :userId
           GROUP BY t.priority
           ORDER BY t.priority DESC
           """)
    List<Object[]> getTasksByPriorityDistribution(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Task t SET t.completed = true, t.completedAt = CURRENT_TIMESTAMP WHERE t.id = :taskId")
    int markTaskAsCompleted(@Param("taskId") Long taskId);

    @Modifying
    @Query("UPDATE Task t SET t.priority = :newPriority WHERE t.description LIKE %:keyword%")
    int updatePriorityByKeyword(
            @Param("keyword") String keyword,
            @Param("newPriority") Priority newPriority);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.completed = true AND t.completedAt < :date")
    int deleteOldCompletedTasks(@Param("date") LocalDateTime date);

    interface TaskSummary {
        Long getId();
        String getDescription();
        Priority getPriority();
        boolean isCompleted();

        default String getShortDescription() {
            return getDescription().length() > 50 ?
                    getDescription().substring(0, 47) + "..." :
                    getDescription();
        }
    }

    @Query("SELECT t.id as id, t.description as description, " +
            "t.priority as priority, t.completed as completed " +
            "FROM Task t WHERE t.user.id = :userId")
    List<TaskSummary> findTaskSummariesByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.completed = false ORDER BY t.priority DESC, t.createdAt ASC")
    List<Task> findActiveTasksByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.completed = false AND t.createdAt < :deadline")
    List<Task> findOverdueTasks(@Param("deadline") LocalDateTime deadline);

    @Query("SELECT t FROM Task t WHERE t.completed = false AND t.priority = 'HIGH'")
    List<Task> findTasksForNotification();

    @Query("SELECT t FROM Task t WHERE " +
            "(:userId IS NULL OR t.user.id = :userId) AND " +
            "(:completed IS NULL OR t.completed = :completed) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:keyword IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> findTasksByCriteria(
            @Param("userId") Long userId,
            @Param("completed") Boolean completed,
            @Param("priority") Priority priority,
            @Param("keyword") String keyword);
}
