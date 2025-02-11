package com.example.demo.controller;

import com.example.demo.model.Books;
import com.example.demo.model.User;
import com.example.demo.security.MyUser;
import com.example.demo.security.MyUserDetailService;
import com.example.demo.security.MyUserRepository;
import com.example.demo.service.UserService;
import com.example.demo.webtoken.JwtService;
import com.example.demo.webtoken.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
//@Tag(name = "User Controller", description = "Operations related to user management")

public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

  //  @Operation(summary = "Create a new user", description = "This endpoint allows creating a new user")
    @PostMapping("/register")
    public MyUser createUser(@RequestBody MyUser user) {
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

   // @Operation(summary = "Authenticate and get JWT token", description = "This endpoint authenticates a user and provides a JWT token for further requests")
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody Login login) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.username(), login.password()
        ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(myUserDetailService.loadUserByUsername(login.username()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

 //   @Operation(summary = "Get users with pagination", description = "This endpoint fetches users with pagination support")
    @GetMapping("/userPaging")
    public Page<MyUser> getUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return myUserDetailService.getAllUsers(pageable);
    }

   // @Operation(summary = "Get the all user", description = "This endpoint fetches users")
    @GetMapping("/allUser")
    public List<MyUser> getAllUsers(){

        return myUserDetailService.getAllUsers();
    }

    //@Operation(summary = "Get the user by id", description = "This endpoint fetches user by id")
    @GetMapping("/getUserById/{id}")
    public MyUser getUserById(@PathVariable Long id){
        return myUserDetailService.getUserById(id);
    }

    //@Operation(summary = "Add the user", description = "This endpoint adding the new user")
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){
        User addedUser =  userService.addUser(user);
        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }


    //@Operation(summary = "Update the user", description = "This endpoint updating the user")
    @PutMapping("updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    //@Operation(summary = "Delete the user by id", description = "This endpoint deleting the user by id")
    @DeleteMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return "User deleted successfully with ID: " + id;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


}

