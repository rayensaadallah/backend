package com.alpha.backend.Services;


import com.alpha.backend.Dto.EntityMapper;
import com.alpha.backend.Dto.UserDto;
import com.alpha.backend.entity.ApplicationUser;
import com.alpha.backend.entity.Role;
import com.alpha.backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private EntityMapper<ApplicationUser, UserDto> mapper;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserDto getUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof ApplicationUser) {
                ApplicationUser applicationUser = (ApplicationUser) principal;
                return mapper.fromBasic(applicationUser, UserDto.class);
            } else {
                throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
            }
        }
        throw new IllegalStateException("No authenticated user found in the security context");
    }

    public void userToAdmin(String  email) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmail(email).orElse(null);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
    public List<UserDto> getAllUsersByRoles(Role role) {
        List<ApplicationUser> usersWithUserRole = userRepository.findByRole(role);
        List<UserDto> userDtos = new ArrayList<>();
        for (ApplicationUser user : usersWithUserRole) {
            userDtos.add(mapper.fromBasic(user, UserDto.class));
        }
        return userDtos;
    }
    public void removeMyaccount() {
        UserDto userDto = getUserByToken();
        Optional<ApplicationUser> existingUserOptional = userRepository.findByEmail(userDto.getEmail());

        existingUserOptional.ifPresentOrElse(
                existingUser -> userRepository.delete(existingUser),
                () -> {
                    throw new EntityNotFoundException("User not found for email : " + userDto.getEmail());
                }
        );
    }
    @Transactional
    public UserDto update(UserDto dto) {
        ApplicationUser existingUser = userRepository.findById(getUserByToken().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Update only the fields that should be changed
        if (dto.getFirstname() != null) {
            existingUser.setFirstname(dto.getFirstname());
        }
        if (dto.getLastname() != null) {
            existingUser.setLastname(dto.getLastname());
        }
        if (dto.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getImage() != null) {
            existingUser.setImage(dto.getImage());
        }
        // Save the updated user back to the database
        userRepository.save(existingUser);

        // Return the updated DTO (consider mapping the updated entity back to DTO)
        return mapper.fromBasic(existingUser, UserDto.class);
    }


}

