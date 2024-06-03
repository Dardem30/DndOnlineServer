package com.dnd.dndonlineserver.dao.impl.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseHibernateDaoImpl implements BaseHibernateDao{
    @PersistenceContext
    private EntityManager session;

    public <T> SearchResult<T> resolvePredicates(final Class<T> clazz,
                                                 final ListFilter searchCriteria,
                                                 final CriteriaFunction<T> criteriaFunction) throws Exception {
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(clazz);
        final Root<T> root = query.from(clazz);
        final List<Predicate> predicates = criteriaFunction.apply(root, builder);
        if (searchCriteria.getOrderBy() != null) {
            final OrderBy ob = searchCriteria.getOrderBy();
            if (SortDirection.DESC.equals(ob.getDirection())) {
                query.orderBy(builder.desc(root.get(ob.getProperty())));
            } else {
                query.orderBy(builder.asc(root.get(ob.getProperty())));
            }
        }
        Predicate[] varargOfPredicates = predicates.toArray(new Predicate[predicates.size()]);
        final List<T> searchResult = session.createQuery(query.select(root).where(varargOfPredicates))
                .setFirstResult(searchCriteria.getStart())
                .setMaxResults(searchCriteria.getLimit())
                .getResultList();

        final CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        final Root<T> countRoot = countQuery.from(clazz);
        final List<Predicate> countPredicates = criteriaFunction.apply(countRoot, builder);
        varargOfPredicates = countPredicates.toArray(new Predicate[countPredicates.size()]);
        final Long count = session.createQuery(countQuery.select(builder.count(countRoot)).where(varargOfPredicates)).getSingleResult();

        final SearchResult<T> result = new SearchResult<>();
        result.setResult(searchResult);
        result.setTotalNumberFound(count.intValue());
        return result;
    }
    public <T> List<T> findEntities(final Class<T> clazz, final CriteriaFunction<T> criteriaFunction) throws Exception {
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(clazz);
        final Root<T> root = query.from(clazz);
        final List<Predicate> predicates = criteriaFunction.apply(root, builder);
        final Predicate[] varargOfPredicates = predicates.toArray(new Predicate[predicates.size()]);
        return session.createQuery(
                query.select(root).where(varargOfPredicates)
        ).getResultList();
    }
    public <T> T findEntity(final Class<T> clazz, final CriteriaFunction<T> criteriaFunction) throws Exception {
        try {
            final CriteriaBuilder builder = session.getCriteriaBuilder();
            final CriteriaQuery<T> query = builder.createQuery(clazz);
            final Root<T> root = query.from(clazz);
            final List<Predicate> predicates = criteriaFunction.apply(root, builder);
            final Predicate[] varargOfPredicates = predicates.toArray(new Predicate[predicates.size()]);
            return session.createQuery(
                    query.select(root).where(varargOfPredicates)
            ).getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
    }

    @Override
    public <T> T readObject(Class<T> tClass, Long identifier) {
        return session.find(tClass, identifier);
    }
}
