package com.dnd.dndonlineserver.models.util;

import com.dnd.dndonlineserver.IConstants;
import com.dnd.dndonlineserver.models.AppUser;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class TrackedEntityListener {

    @PrePersist
    public void setCreatedOn(TrackedEntity trackable) {
        final Date now = new Date();
        trackable.setCreateTime(now);
        trackable.setMutTime(now);
        AppUser currentLoggedInUser = SecurityUser.currentLoggedInUser();
        if (currentLoggedInUser != null) {
            trackable.setCreatorId(currentLoggedInUser.getId());
            trackable.setModifierId(currentLoggedInUser.getId());
            trackable.setCreatorName(currentLoggedInUser.getName());
            trackable.setModifierName(currentLoggedInUser.getName());
        } else {
            trackable.setCreatorName(IConstants.SYSTEM_USER);
            trackable.setModifierName(IConstants.SYSTEM_USER);
        }
    }

    @PreUpdate
    public void setUpdatedOn(TrackedEntity trackable) {
        trackable.setMutTime(new Date());
        AppUser currentLoggedInUser = SecurityUser.currentLoggedInUser();
        if (currentLoggedInUser != null) {
            trackable.setModifierId(currentLoggedInUser.getId());
            trackable.setModifierName(currentLoggedInUser.getName());
        } else {
            trackable.setModifierName(IConstants.SYSTEM_USER);
        }
    }
}
