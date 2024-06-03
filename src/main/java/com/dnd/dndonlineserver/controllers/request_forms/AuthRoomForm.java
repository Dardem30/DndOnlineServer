package com.dnd.dndonlineserver.controllers.request_forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRoomForm {
    private Long roomId;
    private String password;
}
