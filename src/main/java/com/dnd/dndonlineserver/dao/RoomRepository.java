package com.dnd.dndonlineserver.dao;

import com.dnd.dndonlineserver.dao.impl.base.BaseHibernateDao;
import com.dnd.dndonlineserver.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, BaseHibernateDao {
    List<Room> findAllByClosed(boolean closed);
    @Modifying
    @Query(value = "delete from rooms WHERE ((id NOT IN (SELECT room_id FROM room_participant GROUP BY room_id) and mut_time < ?1) OR mut_time < ?2) and eternal = '0'", nativeQuery = true)
    void cleanUpEmptyRooms(LocalDateTime timeToJoinAtLeastOneParticipant, LocalDateTime maxTimeExistingLobby);
}
