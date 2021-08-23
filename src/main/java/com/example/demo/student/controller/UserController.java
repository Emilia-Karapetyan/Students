package com.example.demo.student.controller;

import com.example.demo.student.model.dto.UserDto;
import com.example.demo.student.service.UserService;
import com.example.demo.student.util.ContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ContentResponse<UserDto> getUsers(Pageable pageable) {
        Page<UserDto> userDtos = userService.getUsers(pageable);
        return new ContentResponse<>(userDtos.getTotalElements(), userDtos.getContent());
    }

    @PostMapping()
    public void registerNewUser(@RequestBody @Valid UserDto user) {
        userService.addNewUser(user);
    }

    @PostMapping("/admin")
    public void registerNewAdmin(@RequestBody @Valid UserDto user) {
        userService.addNewAdmin(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable("id") Long userId, @RequestBody UserDto userDto) {
        userService.updateStudent(userId, userDto);
    }

    @PostMapping("/{id}")
    public void buyBook(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long bookId) throws Exception {
        if (bookId == null) {
            throw new IllegalStateException("BookId cannot ba null");
        }
        userService.buyBook(userDetails, bookId);
    }
}
