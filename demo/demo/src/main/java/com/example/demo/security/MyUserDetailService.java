package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private MyUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public Page<MyUser> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<MyUser> getAllUsers(){
        return repository.findAll();
    }

    public MyUser getUserById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("user id" + id + "not found"));
    }

    public MyUser addUser(MyUser user){
        return repository.save(user);
    }


    public MyUser updateUser(Long id, MyUser updatedUserDetails) {
        // Find the existing book by ID
        return repository.findById(id).map(existingUser -> {
            existingUser.setUsername(updatedUserDetails.getUsername());
            existingUser.setPassword(updatedUserDetails.getPassword());
            // Save updated entity
            return repository.save(existingUser);
        }).orElseThrow(() -> new IllegalStateException("user not found with id " + id));
    }

    public void deleteUser(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("user  not found with id: " + id);
        }
    }

}
