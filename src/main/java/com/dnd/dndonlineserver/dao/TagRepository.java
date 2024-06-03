package com.dnd.dndonlineserver.dao;

import com.dnd.dndonlineserver.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Tag t WHERE t.id IN ?1")
    void bulkDelete(List<Long> ids);
}
