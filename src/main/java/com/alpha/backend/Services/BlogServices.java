package com.alpha.backend.Services;

import com.alpha.backend.Dto.BlogDto;
import com.alpha.backend.Dto.EntityMapper;
import com.alpha.backend.Dto.UserDto;
import com.alpha.backend.entity.ApplicationUser;
import com.alpha.backend.entity.Blog;
import com.alpha.backend.entity.Role;
import com.alpha.backend.repository.BlogRepository;
import com.alpha.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogServices {
    private BlogRepository blogRepository;
    private UserService userService;
    private EntityMapper<ApplicationUser, UserDto> userEntityMapper;

    private final EntityMapper<Blog, BlogDto> blogEntityMapper;

    public List<BlogDto> getAllBlogs(){
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream()
                .map(blog -> blogEntityMapper.fromBasic(blog, BlogDto.class))
                .collect(Collectors.toList());
    }

    public BlogDto addBlog(BlogDto blogDto) {
        Blog blogEntity = blogEntityMapper.fromDTO(blogDto, Blog.class);
        blogEntity.setUser(userEntityMapper.fromDTO(userService.getUserByToken(),ApplicationUser.class));
        Date currentTime = new Date();
        blogEntity.setTimeAdded(currentTime);
        blogRepository.save(blogEntity);
        return blogEntityMapper.fromBasic(blogEntity, BlogDto.class);
    }
    public BlogDto getBlogInfo(Integer id) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if (blogOptional.isPresent()) {
            return blogEntityMapper.fromBasic(blogOptional.get(), BlogDto.class);
        } else {
            throw new EntityNotFoundException("Blog not found: " + id);
        }
    }
    public List<BlogDto> myblogs(){
        List<Blog> blogs = blogRepository.getBlogsByUser(userEntityMapper.fromDTO(userService.getUserByToken(),ApplicationUser.class));
        return blogs.stream()
                .map(blog -> blogEntityMapper.fromBasic(blog, BlogDto.class))
                .collect(Collectors.toList());
    }

    public void removemyBlog(Integer id) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            ApplicationUser authenticatedUser = userEntityMapper.fromDTO(userService.getUserByToken(), ApplicationUser.class);
            if (blog.getUser().equals(authenticatedUser)|| authenticatedUser.getRole() == Role.ADMIN) {
                blogRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this blog.");
            }
        } else {
            throw new EntityNotFoundException("Blog not found: " + id);
        }
    }
}
