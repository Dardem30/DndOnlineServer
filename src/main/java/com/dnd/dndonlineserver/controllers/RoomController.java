package com.dnd.dndonlineserver.controllers;

import com.dnd.dndonlineserver.controllers.request_forms.AuthRoomForm;
import com.dnd.dndonlineserver.controllers.response_forms.ResponseForm;
import com.dnd.dndonlineserver.models.Room;
import com.dnd.dndonlineserver.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("room/")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("list")
    public ResponseForm list() {
        return ResponseForm.successWithResult(roomService.listRoom());
    }
    @PostMapping("create")
    public ResponseForm create(@RequestBody Room room) {
        roomService.createRoom(room);
        return ResponseForm.successWithResult(room.getId());
    }
    @PostMapping("accessConversation")
    public ResponseForm accessConversation(@RequestBody AuthRoomForm authRoomForm) {
        return roomService.accessConversation(authRoomForm);
    }
    @DeleteMapping("disconnect")
    public ResponseForm disconnect(@RequestParam Long roomId) {
        roomService.disconnect(roomId);
        return ResponseForm.success();
    }
}
