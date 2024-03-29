package com.alpha.backend.Controller;

import com.alpha.backend.Dto.UserDto;
import com.alpha.backend.Services.UserService;
import com.alpha.backend.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    UserService userService;
    @GetMapping("/list-user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserDto> getusers() {
        return userService.getAllUsersByRoles(Role.USER);
    }
    @GetMapping("/list-admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserDto> getAdmins() {
        List<UserDto> list = userService.getAllUsersByRoles(Role.ADMIN);
        return list;
    }
    @PutMapping("/upgrade/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void UserToadmin(@PathVariable("email") String email ) throws RoleNotFoundException {
        userService.userToAdmin(email);
    }

}
