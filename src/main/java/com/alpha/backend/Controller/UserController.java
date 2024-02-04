package com.alpha.backend.Controller;


import com.alpha.backend.Dto.UserDto;
import com.alpha.backend.Services.UserService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    UserService userService;
    @GetMapping("/hey")
    @PreAuthorize("hasAnyRole('USER')")
    public String getAnonymous() {
        String ok="Im User";
        return ok;
    }
    @GetMapping("/list-user")
    @PreAuthorize("hasAnyRole('USER')")
    public List<UserDto> getusers() {
        return userService.getAllUsersByRoles("USER");
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER')")
    public UserDto getUserInfo() {
        return userService.getUserByToken();
    }
    @DeleteMapping("/deleteMyAccount")
    @PreAuthorize("hasAnyRole('USER')")
    public void removeUser() {
        userService.removeMyaccount();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserDto> updateUser(
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "image", required = false) MultipartFile image
    ) throws IOException {
        UserDto updatedUser = new UserDto();
        updatedUser.setFirstname(firstName);
        updatedUser.setLastname(lastName);
        updatedUser.setPhoneNumber(phoneNumber);
        if (image != null && !image.isEmpty()) {
            updatedUser.setImage(image.getBytes());
        }
        UserDto result = userService.update(updatedUser);
        return ResponseEntity.ok(result);
    }
}