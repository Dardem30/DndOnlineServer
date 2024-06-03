package com.dnd.dndonlineserver.dao.impl.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListFilter {
    private OrderBy orderBy;
    private int start;
    private int limit;
}
