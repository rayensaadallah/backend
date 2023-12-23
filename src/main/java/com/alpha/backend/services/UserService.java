package com.alpha.backend.services;


import com.alpha.backend.Dtos.EntityMapper;
import com.alpha.backend.Dtos.UserDto;
import com.alpha.backend.models.ApplicationUser;
import com.alpha.backend.models.Role;
import com.alpha.backend.repository.RoleRepository;
import com.alpha.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private EntityMapper<ApplicationUser, UserDto> mapper;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("In the user details service");

        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }

    public UserDto getUserByToken() {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder
                .getContext()
                .getAuthentication();
        Jwt jwt = jwtAuthenticationToken.getToken();
        String username = jwt.getClaimAsString("sub");
        Optional<ApplicationUser> user = userRepository.findByUsername(username);
        ApplicationUser applicationUser = user.orElseThrow(() -> new UsernameNotFoundException("User not found for username: " + username));
        return mapper.fromBasic(applicationUser, UserDto.class);
    }

    public void userToAdmin(String  username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            Optional<Role> managerRole = roleRepository.findByAuthority("ADMIN");

            if (managerRole.isPresent()) {
                Set<Role> authorities = new HashSet<>();
                authorities.add(managerRole.get());

                user.setAuthorities(authorities);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Admin role not found.");
            }
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
    public List<UserDto> getAllUsersByRoles(String s) {
        List<ApplicationUser> usersWithUserRole = userRepository.findByAuthorities_Authority(s);

        List<UserDto> userDtos = new ArrayList<>();
        for (ApplicationUser user : usersWithUserRole) {
            userDtos.add(mapper.fromBasic(user, UserDto.class));
        }

        return userDtos;
    }
    public void removeMyaccount() {
        UserDto userDto = getUserByToken();
        Optional<ApplicationUser> existingUserOptional = userRepository.findByUsername(userDto.getUsername());

        existingUserOptional.ifPresentOrElse(
                existingUser -> userRepository.delete(existingUser),
                () -> {
                    throw new EntityNotFoundException("User not found for username: " + userDto.getUsername());
                }
        );
    }

    @Transactional
    public UserDto update(UserDto dto) {
        ApplicationUser userupdated = mapper.fromDTO(dto, ApplicationUser.class);
        userupdated.setId(getUserByToken().getUserId());
        userRepository.save(userupdated);
        return dto;
    }

    public ApplicationUser getByUserId(Integer id) {
        ApplicationUser user=userRepository.getById(id);
        return user;
    }

    public ApplicationUser getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
