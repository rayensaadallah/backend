package com.alpha.backend;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.alpha.backend.Dto.BlogDto;
import com.alpha.backend.Dto.EntityMapper;
import com.alpha.backend.Dto.UserDto;
import com.alpha.backend.Services.BlogServices;
import com.alpha.backend.Services.UserService;
import com.alpha.backend.entity.ApplicationUser;
import com.alpha.backend.entity.Blog;
import com.alpha.backend.repository.BlogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@SpringBootTest
public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityMapper<ApplicationUser, UserDto> userEntityMapper;

    @Mock
    private EntityMapper<Blog, BlogDto> blogEntityMapper;

    @InjectMocks
    private BlogServices blogService; // Assuming BlogService is the class name

    @Test
    void getAllBlogsTest() {
        // Setup mock environment
        Blog blog = new Blog(); // Assume Blog is a valid entity
        when(blogRepository.findAll()).thenReturn(Arrays.asList(blog));
        when(blogEntityMapper.fromBasic(any(Blog.class), eq(BlogDto.class))).thenReturn(new BlogDto());

        // Execute the service call
        List<BlogDto> result = blogService.getAllBlogs();

        // Validate the results
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(blogRepository).findAll();
    }

    @Test
    void myBlogsTest() {
        // Mock dependencies
        ApplicationUser currentUser = new ApplicationUser(); // Mocked user
        Blog blog = new Blog(); // Mocked blog entity
        blog.setUser(currentUser);
        List<Blog> blogList = Collections.singletonList(blog);

        when(userService.getUserByToken()).thenReturn(new UserDto());
        when(userEntityMapper.fromDTO(any(UserDto.class), eq(ApplicationUser.class))).thenReturn(currentUser);
        when(blogRepository.getBlogsByUser(currentUser)).thenReturn(blogList);
        when(blogEntityMapper.fromBasic(any(Blog.class), eq(BlogDto.class))).thenReturn(new BlogDto());

        // Invoke the method
        List<BlogDto> result = blogService.myblogs();

        // Assertions
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // Assuming one blog returned for simplicity
        verify(blogRepository).getBlogsByUser(currentUser);
    }


    @Test
    void removeMyBlog_BlogDoesNotExist_ThrowsEntityNotFoundException() {
        when(blogRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Exception expectation
        Exception exception = assertThrows(EntityNotFoundException.class, () -> blogService.removemyBlog(1));
        String expectedMessage = "Blog not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
