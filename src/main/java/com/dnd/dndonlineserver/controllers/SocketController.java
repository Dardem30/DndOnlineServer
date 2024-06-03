package com.dnd.dndonlineserver.controllers;

import com.dnd.dndonlineserver.controllers.response_forms.ResponseForm;
import com.dnd.dndonlineserver.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class SocketController {
    private final UserService userService;

    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public ResponseForm sendMessage(@DestinationVariable String roomId, Map<String, Object> message) {
        message.put("user", userService.readLight(((Integer) message.get("userId")).longValue()));
        return ResponseForm.successWithResult(message);
    }
    @MessageMapping("/user/{userId}")
    @SendTo("/topic/user/{userId}")
    public ResponseForm notifyUser(@DestinationVariable Long userId, Map<String, Object> message) {
        message.put("user", userService.readLight(((Integer) message.get("userId")).longValue()));
        return ResponseForm.successWithResult(message);
    }

}