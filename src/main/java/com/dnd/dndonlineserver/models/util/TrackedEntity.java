package com.dnd.dndonlineserver.models.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@EntityListeners(TrackedEntityListener.class)
@Getter
@Setter
public class TrackedEntity {
    @Column(name = "creator_id")
    private Long creatorId;
    @Column(name = "creator")
    private String creatorName;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "modifier_id")
    private Long modifierId;
    @Column(name = "modifier")
    private String modifierName;
    @Column(name = "mut_time")
    private Date mutTime;
}
