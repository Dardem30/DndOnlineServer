package com.dnd.dndonlineserver.dao.impl.base;

public interface BaseHibernateDao {
    <T> T readObject(Class<T> tClass, Long identifier);
}
