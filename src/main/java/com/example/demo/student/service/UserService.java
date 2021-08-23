package com.example.demo.student.service;

import com.example.demo.student.model.Book;
import com.example.demo.student.model.User;
import com.example.demo.student.model.dto.BookDto;
import com.example.demo.student.model.dto.UserDto;
import com.example.demo.student.repository.RoleRepository;
import com.example.demo.student.repository.UserRepository;
import com.example.demo.student.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookService bookService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, BookService bookService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookService = bookService;
    }

//    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDto::toDto);
    }


    public void addNewUser(UserDto userDto) {
        userRepository.findUserByEmail(userDto.getEmail()).ifPresent(student -> {
            throw new IllegalStateException("Email is taken");
        });
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRole(roleRepository.findOneByName("ROLE_USER"));
        userRepository.save(UserDto.toEntity(userDto));
    }

    public void addNewAdmin(UserDto userDto) {
        userRepository.findUserByEmail(userDto.getEmail()).ifPresent(student -> {
            throw new IllegalStateException("Email is taken");
        });
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRole(roleRepository.findOneByName("ROLE_ADMIN"));
        userRepository.save(UserDto.toEntity(userDto));
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("Student with id " + userId + " doesn't exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateStudent(Long studentId, UserDto studentDto) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " doesn't exist"));

        if (userRepository.findUserByEmail(studentDto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is taken");
        }
        student.setAge(studentDto.getAge());
        student.setName(studentDto.getName());
        student.setDob(studentDto.getDob());
        student.setRole(studentDto.getRole());
        student.setEmail(studentDto.getEmail());
    }


    public void buyBook(UserDetails userDetails, Long bookId) throws Exception {
        User user = ((CurrentUser) userDetails).getUser();
        Book book = BookDto.toEntity(bookService.getBookById(bookId));
        user.getBookList().add(book);
        userRepository.save(user);
    }


}
