package com.dnd.dndonlineserver.services;

import com.dnd.dndonlineserver.controllers.request_forms.AuthForm;
import com.dnd.dndonlineserver.controllers.request_forms.FriendsListFilter;
import com.dnd.dndonlineserver.controllers.request_forms.ProfileUpdateForm;
import com.dnd.dndonlineserver.controllers.response_forms.ResponseForm;
import com.dnd.dndonlineserver.dao.TagRepository;
import com.dnd.dndonlineserver.dao.UserRepository;
import com.dnd.dndonlineserver.dao.impl.base.SearchResult;
import com.dnd.dndonlineserver.models.AppUser;
import com.dnd.dndonlineserver.models.Tag;
import com.dnd.dndonlineserver.models.light.UserLight;
import com.dnd.dndonlineserver.models.util.SecurityUser;
import com.google.firebase.auth.FirebaseToken;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional
    public AppUser findAndRefreshUserByFirebaseUid(final String firebaseUid, final AuthForm authForm) {
        final AppUser user = userRepository.findByFirebaseUid(firebaseUid);
        if (user != null) {
            user.setPhotoUrl(authForm.getPhotoUrl());
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public AppUser signUp(final FirebaseToken firebaseToken, final AuthForm authForm) {
        final AppUser user = new AppUser(firebaseToken);
        user.setName(authForm.getName());
        user.setPhotoUrl(authForm.getPhotoUrl());
        return userRepository.save(user);
    }

    @Transactional
    public ResponseForm updateProfile(final ProfileUpdateForm profileUpdateForm) {
        AppUser currentLoggedInUser = SecurityUser.getCurrentLoggedInUser();
        if (StringUtils.isNotEmpty(profileUpdateForm.getNickName())) {
            if (userRepository.existsByNicknameAndIdNot(profileUpdateForm.getNickName(), currentLoggedInUser.getId())) {
                return ResponseForm.failure("Nickname must be unique");
            }
            currentLoggedInUser.setNickname(profileUpdateForm.getNickName());
        }
        if (StringUtils.isNotEmpty(profileUpdateForm.getName())) {
            currentLoggedInUser.setName(profileUpdateForm.getName());
        }
        userRepository.save(currentLoggedInUser);
        if (profileUpdateForm.getTags() != null) {
            for (String tagName : profileUpdateForm.getTags()) {
                final Tag tag = new Tag();
                tag.setUserId(currentLoggedInUser.getId());
                tag.setTag(tagName);
                tagRepository.save(tag);
            }
        }
        if (profileUpdateForm.getTagIdsToBeRemoved() != null) {
            tagRepository.bulkDelete(profileUpdateForm.getTagIdsToBeRemoved());
        }
        refreshLoggedInUser();
        return ResponseForm.success();
    }

    @Transactional(readOnly = true)
    public AppUser read(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional(readOnly = true)
    public SearchResult<UserLight> searchUsers(final FriendsListFilter listFilter) throws Exception {
        listFilter.setSearcherId(SecurityUser.getCurrentLoggedInUser().getId());
        return userRepository.searchUsers(listFilter);
    }

    @Transactional
    public void addContact(final Long userId) {
        final AppUser currentLoggedInUser = SecurityUser.getCurrentLoggedInUser();
        final List<UserLight> contacts = currentLoggedInUser.getContacts();
        if (contacts.stream().noneMatch(record -> record.getId().equals(userId))) {
            final UserLight contact = new UserLight();
            contact.setId(userId);
            contacts.add(contact);
            userRepository.save(currentLoggedInUser);
            refreshLoggedInUser();
        }
    }
    @Transactional
    public void removeContact(final Long userId) {
        final AppUser currentLoggedInUser = SecurityUser.getCurrentLoggedInUser();
        final List<UserLight> contacts = currentLoggedInUser.getContacts();
        if (contacts.stream().anyMatch(record -> record.getId().equals(userId))) {
            currentLoggedInUser.setContacts(contacts.stream().filter(contact -> !contact.getId().equals(userId)).collect(Collectors.toList()));
            userRepository.save(currentLoggedInUser);
            refreshLoggedInUser();
        }
    }
    private void refreshLoggedInUser() {
        final SecurityUser securityUser = SecurityUser.get();
        securityUser.setUser(userRepository.findById(securityUser.getAppUser().getId()).get());
    }

    @Transactional(readOnly = true)
    public UserLight readLight(Long userId) {
        return userRepository.readObject(UserLight.class, userId);
    }
}
