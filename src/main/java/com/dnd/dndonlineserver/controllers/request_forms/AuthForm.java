package com.dnd.dndonlineserver.controllers.request_forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthForm {
    private String token;
    private String name;
    private String photoUrl;
}
