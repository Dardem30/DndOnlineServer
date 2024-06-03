package com.dnd.dndonlineserver.dao.impl.base;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

@FunctionalInterface
public interface CriteriaFunction<T> {
    List<Predicate> apply(Root<T> root, CriteriaBuilder builder) throws Exception;
}
