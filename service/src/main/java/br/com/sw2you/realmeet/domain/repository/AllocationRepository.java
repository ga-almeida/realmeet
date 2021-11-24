package br.com.sw2you.realmeet.domain.repository;

import br.com.sw2you.realmeet.domain.entity.Allocation;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Allocation a set a.subject = :subject, a.startAt = :startAt, endAt = :endAt where a.id = :id")
    void update(
        @Param("id") Long id,
        @Param("subject") String subject,
        @Param("startAt") OffsetDateTime startAt,
        @Param("endAt") OffsetDateTime endAt
    );

    @Query(
        "select a from Allocation a where " +
        "(:employeeEmail is null or a.employee.email = :employeeEmail) and " +
        "(:roomId is null or a.room.id = :roomId) and " +
        "(:startAt is null or a.startAt >= :startAt) and " +
        "(:endAt is null or a.endAt <= :endAt)"
    )
    List<Allocation> findAllWithFilters(
        @Param("employeeEmail") String employeeEmail,
        @Param("roomId") Long roomId,
        @Param("startAt") OffsetDateTime startAt,
        @Param("endAt") OffsetDateTime endAt
    );
}
