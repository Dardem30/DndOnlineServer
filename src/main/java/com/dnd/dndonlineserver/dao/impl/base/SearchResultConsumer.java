package com.dnd.dndonlineserver.dao.impl.base;


import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
public interface SearchResultConsumer<T> {
    void apply(CriteriaQuery<Long> query, CriteriaBuilder builder, Root<T> root, EntityManager entityManager, Predicate[] varargOfPredicates, SearchResult<T> result);
}
