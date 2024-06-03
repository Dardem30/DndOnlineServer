package com.dnd.dndonlineserver.controllers;

import com.dnd.dndonlineserver.controllers.request_forms.FriendsListFilter;
import com.dnd.dndonlineserver.controllers.request_forms.ProfileUpdateForm;
import com.dnd.dndonlineserver.controllers.response_forms.ResponseForm;
import com.dnd.dndonlineserver.models.util.SecurityUser;
import com.dnd.dndonlineserver.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "user/")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "updateProfile")
    public ResponseForm updateProfile(@RequestBody ProfileUpdateForm profileUpdateForm) {
        return userService.updateProfile(profileUpdateForm);
    }
    @GetMapping("home")
    public ResponseForm home() {
        return ResponseForm.successWithResult("Home", SecurityUser.getCurrentLoggedInUser());
    }
    @PostMapping("searchFriends")
    public ResponseForm searchFriends(@RequestBody final FriendsListFilter listFilter) throws Exception {
        return ResponseForm.successWithResult(userService.searchUsers(listFilter));
    }
    @PostMapping("addContact")
    public ResponseForm addContact(@RequestParam final Long userId) {
        userService.addContact(userId);
        return ResponseForm.success();
    }
    @PostMapping("removeContact")
    public ResponseForm removeContact(@RequestParam final Long userId) {
        userService.removeContact(userId);
        return ResponseForm.success();
    }
}
