package ru.Dmitriy.NauJava_ToDo_List.repository;

import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    List<User> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")

    List<User> findByNameContaining(@Param("name") String name);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    List<User> findAllByOrderByLastNameAsc();

    List<User> findAllByOrderByCreatedAtDesc();

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.createdAt >= :date")
    List<User> findUsersCreatedAfter(@Param("date") java.time.LocalDateTime date);

    default List<User> findByCreatedAtAfter(LocalDateTime date) {
        return findUsersCreatedAfter(date);
    }

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :emailPart, '%'))")
    List<User> findUsersByEmailContaining(@Param("emailPart") String emailPart);

    @Query("SELECT DISTINCT u FROM User u JOIN u.tasks t WHERE t.priority = :priority")
    List<User> findUsersWithTasksByPriority(@Param("priority") ru.Dmitriy.NauJava_ToDo_List.entity.Priority priority);

    @Query("SELECT DISTINCT u FROM User u JOIN u.tasks t WHERE t.completed = false")
    List<User> findUsersWithIncompleteTasks();

    @Query("SELECT COUNT(t) FROM User u JOIN u.tasks t WHERE u.id = :userId")
    Long countTasksByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE SIZE(u.tasks) > :minTasks")
    List<User> findUsersWithMoreThanTasks(@Param("minTasks") int minTasks);

    @Query(value = "SELECT * FROM users WHERE first_name ILIKE %:name%",
            nativeQuery = true)
    List<User> findUsersByNameNative(@Param("name") String name);

    @Query(value = """
           SELECT u.* FROM users u
           LEFT JOIN tasks t ON u.id = t.user_id 
           GROUP BY u.id 
           ORDER BY COUNT(t.id) DESC 
           LIMIT :limit
           """,
            nativeQuery = true)
    List<User> findTopUsersByTaskCount(@Param("limit") int limit);

    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.id = :userId")
    int updateUserEmail(@Param("userId") Long userId, @Param("newEmail") String newEmail);

    @Modifying
    @Query("DELETE FROM User u WHERE u.tasks IS EMPTY")
    int deleteUsersWithoutTasks();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = false WHERE u.id = :userId")
    void deactivateById(@Param("userId") Long userId);

    List<User> findByActiveTrue();

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countActive();

    default User findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.active = false AND u.lastLogin < :date")
    void deleteInactiveOlderThan(@Param("date") LocalDateTime date);

}
