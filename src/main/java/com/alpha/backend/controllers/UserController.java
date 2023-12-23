package com.alpha.backend.controllers;

import com.alpha.backend.Dtos.UserDto;
import com.alpha.backend.models.ApplicationUser;
import com.alpha.backend.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String getAnonymous() {
        String ok="Im User";
        return ok;
    }

    @GetMapping("/list-user")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','USER','ORGANIZER')")// el kol
    public List<UserDto> getusers() {
        return userService.getAllUsersByRoles("USER");

    }
    @GetMapping("/list-admin")
    @PreAuthorize("hasAnyRole('SUPER-ADMIN')")
    public List<UserDto> getAdmins() {
        List<UserDto> list = userService.getAllUsersByRoles("ADMIN");
        return list;
    }
    @GetMapping("/list-organizer")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','ORGANIZER')")
    public List<UserDto> getOrganizers() {
        List<UserDto> list = userService.getAllUsersByRoles("ORGANIZER");
        return list;
    }
    @PutMapping("/upgrade/{username}")
    @PreAuthorize("hasAnyRole('SUPER-ADMIN','ADMIN')")
    public void UserToadmin(@PathVariable("username") String username ) throws RoleNotFoundException {
        userService.userToAdmin(username);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','USER','ORGANIZER')")// el kol
    public UserDto getUserInfo() {
        return userService.getUserByToken();
    }

    @GetMapping("/getUserByUsername/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','USER','ORGANIZER')")// el kol
    public ApplicationUser getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','USER','ORGANIZER')")// el kol
    public ApplicationUser getUserById(@RequestParam("id") Integer id )

    {
        return userService.getByUserId(id);
    }


    @DeleteMapping("/deleteMyAccount")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','USER','ORGANIZER')")// el kol
    public void removeUser() {
        userService.removeMyaccount();
    }


    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER-ADMIN','USER','ORGANIZER')")
    public void updateUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "image", required = false) MultipartFile image
    ) throws IOException {
        UserDto updatedUser = new UserDto();
        updatedUser.setUsername(username);
        updatedUser.setEmail(email);
        updatedUser.setPhoneNumber(phoneNumber);
        updatedUser.setCountry(country);
        updatedUser.setImage(image.getBytes());
        userService.update(updatedUser);
    }
}