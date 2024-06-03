package com.dnd.dndonlineserver.dao;

import com.dnd.dndonlineserver.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long>, UserDA {
    AppUser findByFirebaseUid(String firebaseGuid);
    boolean existsByNicknameAndIdNot(String nickname, Long id);
}
