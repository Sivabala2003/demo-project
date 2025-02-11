package com.example.demo.controller;

import com.example.demo.model.Author;

import com.example.demo.service.AuthorService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/author")
//@Tag(name = "Author Controller", description = "Operations related to authors management")
public class AuthorController {
    @Autowired
    AuthorService authorService;

    //@Operation(summary = "Get paginated list of authors", description = "Fetch authors with pagination support")
    @GetMapping("/authorPaging")
    public Page<Author> getAuthor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return authorService.getAuthorPaging(pageable);
    }

    //@Operation(summary = "Get all authors", description = "Fetch all authors without pagination")
    @GetMapping("/allAuthor")
    public List<Author> getAllAuthors(){

        return authorService.getAllAuthors();
    }

    //@Operation(summary = "Get author by ID", description = "Fetch a specific author by its ID")
    @GetMapping("/getAuthorById/{id}")
    public Author getAuthorById(@PathVariable Long id){
        return authorService.getAuthorById(id);
    }

    //@Operation(summary = "Add a new author", description = "This endpoint allows creating a new author")
    @PostMapping("/addAuthor")
    public ResponseEntity<Author> addAuthor(@Valid @RequestBody Author author){
        Author addedAuthor =  authorService.addAuthor(author);
        return new ResponseEntity<>(addedAuthor, HttpStatus.OK);
    }

    //@Operation(summary = "Update an author", description = "This endpoint updates an existing author by its ID")
    @PutMapping("updateAuthor/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return ResponseEntity.ok(updatedAuthor);
    }

    //@Operation(summary = "Delete an author", description = "This endpoint deletes an author by its ID")
    @DeleteMapping("deleteAuthor/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            return "Book deleted successfully with ID: " + id;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


}
