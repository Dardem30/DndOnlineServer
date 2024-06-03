package com.dnd.dndonlineserver.dao.impl.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBy {
    private String property;
    private SortDirection direction;

}
