package com.dnd.dndonlineserver.dao.impl;

import com.dnd.dndonlineserver.controllers.request_forms.FriendsListFilter;
import com.dnd.dndonlineserver.dao.UserDA;
import com.dnd.dndonlineserver.dao.impl.base.BaseHibernateDaoImpl;
import com.dnd.dndonlineserver.dao.impl.base.SearchResult;
import com.dnd.dndonlineserver.models.light.UserLight;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class UserDAImpl extends BaseHibernateDaoImpl implements UserDA {

    @Override
    public SearchResult<UserLight> searchUsers(FriendsListFilter friendsListFilter) throws Exception {
        return resolvePredicates(UserLight.class, friendsListFilter, (root, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotEmpty(friendsListFilter.getNickName())) {
                predicates.add(builder.like(root.get("nickName"), "%" + friendsListFilter.getNickName() + "%"));
            }
            if (friendsListFilter.getSearcherId() != null) {
                predicates.add(builder.notEqual(root.get("id"), friendsListFilter.getSearcherId()));
            }
            return predicates;
        });
    }
}
