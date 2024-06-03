package com.dnd.dndonlineserver.controllers.request_forms;

import com.dnd.dndonlineserver.dao.impl.base.ListFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendsListFilter extends ListFilter {
    private String nickName;
    private Long searcherId;
}
