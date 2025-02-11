package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.Books;
import com.example.demo.model.User;
import com.example.demo.service.BooksService;
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
@RequestMapping("api/books")
//@Tag(name = "Books Controller", description = "Operations related to books management")
public class BooksController {
    @Autowired
    BooksService booksService;

  //  @Operation(summary = "Get paginated list of books", description = "Fetch books with pagination support")
    @GetMapping("/bookPaging")
    public Page<Books> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return booksService.getAllBooks(pageable);
    }

    //@Operation(summary = "Get all books", description = "Fetch all books without pagination")
    @GetMapping("/allBook")
    public List<Books> getAllBooks(){

        return booksService.getAllBooks();
    }

    //@Operation(summary = "Get book by ID", description = "Fetch a specific book by its ID")
    @GetMapping("/getBookById/{id}")
    public Books getBookById(@PathVariable Long id){
        return booksService.getBookById(id);
    }

    //@Operation(summary = "Add books", description = "Adding the books")
    @PostMapping("/addBooks")
    public ResponseEntity<Books> addBooks(@Valid @RequestBody Books books){
        Books addedBooks =  booksService.addBooks(books);
        return new ResponseEntity<>(addedBooks, HttpStatus.OK);}

    //@Operation(summary = "Update the book by ID", description = "Update the specific book by ID")
    @PutMapping("updateBook/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable Long id, @RequestBody Books bookDetails) {
        Books updatedBook = booksService.updateBook(id, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    //@Operation(summary = "Delete book by ID", description = "Delete a specific book by its ID")
    @DeleteMapping("deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        try {
            booksService.deleteBook(id);
            return "Book deleted successfully with ID: " + id;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
