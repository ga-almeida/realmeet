package br.com.sw2you.realmeet.domain.repository;

import br.com.sw2you.realmeet.domain.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByIdAndActive(Long id, Boolean active);

    Optional<Room> findByNameAndActive(String name, Boolean active);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Room r set r.active = false where r.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Room r set r.name = :name, r.seats = :seats where r.id = :id")
    void update(@Param("id") Long id, @Param("name") String name, @Param("seats") Integer seats);
}
