package com.dnd.dndonlineserver.controllers.request_forms;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileUpdateForm {
    private String name;
    private String nickName;
    private List<String> tags;
    private List<Long> tagIdsToBeRemoved;
}
