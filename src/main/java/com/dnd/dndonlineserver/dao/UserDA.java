package com.dnd.dndonlineserver.dao;

import com.dnd.dndonlineserver.controllers.request_forms.FriendsListFilter;
import com.dnd.dndonlineserver.dao.impl.base.BaseHibernateDao;
import com.dnd.dndonlineserver.dao.impl.base.SearchResult;
import com.dnd.dndonlineserver.models.light.UserLight;

public interface UserDA extends BaseHibernateDao {
    SearchResult<UserLight> searchUsers(FriendsListFilter friendsListFilter) throws Exception;
}
