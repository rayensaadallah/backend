package com.alpha.backend.Controller;

import com.alpha.backend.Dto.BlogDto;
import com.alpha.backend.Dto.UserDto;
import com.alpha.backend.Services.BlogServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/blog")
@CrossOrigin("*")
public class BlogController {
    private BlogServices blogServices;

    @GetMapping("/all")
    public List<BlogDto> getAllBlogs() {
        return blogServices.getAllBlogs();
    }

    @PostMapping("/add")
    public ResponseEntity<BlogDto> addblog(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "images", required = false) MultipartFile image
    ) throws IOException {
        BlogDto blogDto= new BlogDto();
        blogDto.setDescription(description);
        blogDto.setTitle(title);
        blogDto.setImages(Collections.singletonList(image.getBytes()));
        BlogDto savedBlog = blogServices.addBlog(blogDto);
        return new ResponseEntity<>(savedBlog, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDto> getBlogInfo(@PathVariable Integer id) {
        BlogDto blogDto = blogServices.getBlogInfo(id);
        return new ResponseEntity<>(blogDto, HttpStatus.OK);
    }

    @GetMapping("/myblogs")
    public ResponseEntity<List<BlogDto>> getMyBlogs() {
        List<BlogDto> myBlogs = blogServices.myblogs();
        return new ResponseEntity<>(myBlogs, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")// delete blog with conditions
    public ResponseEntity<String> removeBlog(@PathVariable Integer id) {
        blogServices.removemyBlog(id);
        return new ResponseEntity<>("Blog deleted successfully", HttpStatus.OK);
    }
}
