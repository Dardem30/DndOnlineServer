package com.dnd.dndonlineserver.services;

import com.dnd.dndonlineserver.controllers.request_forms.AuthRoomForm;
import com.dnd.dndonlineserver.controllers.response_forms.ResponseForm;
import com.dnd.dndonlineserver.dao.RoomRepository;
import com.dnd.dndonlineserver.models.AppUser;
import com.dnd.dndonlineserver.models.Room;
import com.dnd.dndonlineserver.models.light.UserLight;
import com.dnd.dndonlineserver.models.util.SecurityUser;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
    private final Log log = LogFactory.getLog(getClass());
    private final RoomRepository roomRepository;
    private final JitsiService jitsiService;

    @Transactional
    public void createRoom(final Room room) {
        roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<Room> listRoom() {
        return roomRepository.findAllByClosed(false);
    }

    @Transactional
    public ResponseForm accessConversation(final AuthRoomForm authRoomForm) {
        final Optional<Room> optionalRoom = roomRepository.findById(authRoomForm.getRoomId());
        if (optionalRoom.isPresent()) {
            final Room room = optionalRoom.get();
            if (room.getParticipants().size() < 6 && room.getParticipants().size() < room.getMaxPeople()
                    && (room.getPassword() == null || (authRoomForm.getPassword() != null && authRoomForm.getPassword().equals(room.getPassword())))) {
                if (!join(room)) {
                    return ResponseForm.failure("You already joined!");
                }
                return ResponseForm.successWithResult(jitsiService.generateToken(room));
            }
        }
        return ResponseForm.failure("Failed to access room");
    }

    @Transactional
    public boolean join(Room room) {
        AppUser currentLoggedInUser = SecurityUser.currentLoggedInUser();
        final UserLight user = roomRepository.readObject(UserLight.class, currentLoggedInUser.getId());
        if (room.getParticipants().contains(user)) {
            log.warn("User [" + user.getId() + "] already joined the room [" + room.getId() + "]");
            return false;
        }
        room.setModifierId(currentLoggedInUser.getId());
        room.setModifierName(currentLoggedInUser.getName());
        room.getParticipants().add(user);
        roomRepository.save(room);
        return true;
    }

    @Transactional
    public void disconnect(Long roomId) {
        final Room room = roomRepository.readObject(Room.class, roomId);
        final UserLight user = roomRepository.readObject(UserLight.class, SecurityUser.getCurrentLoggedInUser().getId());
        room.getParticipants().remove(user);
        roomRepository.save(room);
    }

    @Transactional
    public void cleanUpEmptyRooms() {
        roomRepository.cleanUpEmptyRooms(LocalDateTime.now().minusMinutes(2),LocalDateTime.now().minusHours(6));
    }
}
