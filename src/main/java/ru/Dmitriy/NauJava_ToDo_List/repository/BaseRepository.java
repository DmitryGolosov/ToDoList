package ru.Dmitriy.NauJava_ToDo_List.repository;

import ru.Dmitriy.NauJava_ToDo_List.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    List<T> findAllByOrderByIdAsc();

    List<T> findAllByOrderByIdDesc();

    List<T> findAllByOrderByCreatedAtDesc();

    List<T> findAllByOrderByCreatedAtAsc();

    List<T> findByActiveTrue();

    List<T> findByActiveFalse();

    List<T> findByActiveTrue(Sort sort);

    Page<T> findByActiveTrue(Pageable pageable);

    List<T> findByCreatedAtAfter(LocalDateTime date);

    List<T> findByCreatedAtBefore(LocalDateTime date);

    List<T> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT e FROM #{#entityName} e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<T> findByNameContaining(@Param("name") String name);

    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.active = true")
    Long countActive();

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.active = false WHERE e.id = :id")
    int deactivateById(@Param("id") ID id);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.active = true WHERE e.id = :id")
    int activateById(@Param("id") ID id);

    default T findByIdOrThrow(ID id) {
        return this.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }

    default T findByIdOrThrow(ID id, String message) {
        return this.findById(id)
                .orElseThrow(() -> new RuntimeException(message));
    }

    default List<T> findAllActiveSorted() {
        return this.findByActiveTrue(Sort.by("createdAt").descending());
    }

    default T updateIfExists(ID id, java.util.function.Consumer<T> updater) {
        return this.findById(id).map(entity -> {
            updater.accept(entity);
            return this.save(entity);
        }).orElse(null);
    }

    default void safeDeactivate(ID id) {
        T entity = this.findByIdOrThrow(id);
        entity.deactivate();
        this.save(entity);
    }

    static <T> void validateEntity(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
    }

    static String getRepositoryInfo() {
        return "BaseRepository - generic repository for all entities";
    }
}
