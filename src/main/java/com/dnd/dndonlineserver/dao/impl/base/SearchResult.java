package com.dnd.dndonlineserver.dao.impl.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResult<T> {
    private List<T> result;
    private int totalNumberFound;
}
