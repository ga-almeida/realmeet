package br.com.sw2you.realmeet.domain.repository;

import br.com.sw2you.realmeet.domain.entity.Allocation;
import java.time.OffsetDateTime;
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
}
