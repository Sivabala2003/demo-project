package com.example.demo.controller;
import com.example.demo.model.BorrowRecord;
import com.example.demo.service.BorrowRecordService;
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
@RequestMapping("/api/borrow")
//@Tag(name = "Borrow Record Controller", description = "Operations related to borrow records management")
public class BorrowRecordController {

    @Autowired
    BorrowRecordService borrowRecordService;

   // @Operation(summary = "Get paginated list of borrow records", description = "Fetch borrow records with pagination support")
    @GetMapping("/borrowPaging")
    public Page<BorrowRecord> getRecord(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return borrowRecordService.getBorrowRecord(pageable);
    }

    //@Operation(summary = "Get all borrow records", description = "Fetch all borrow records without pagination")
    @GetMapping("/allRecord")
    public List<BorrowRecord> getAllRecord(){

        return borrowRecordService.getAllRecord();
    }

    //@Operation(summary = "Get borrow record by ID", description = "Fetch a specific borrow record by its ID")
    @GetMapping("/getRecordById/{id}")
    public BorrowRecord getRecordById(@PathVariable Long id){
        return borrowRecordService.getRecordById(id);
    }

    //@Operation(summary = "Add a new borrow record", description = "This endpoint allows creating a new borrow record")
    @PostMapping("/addRecord")
    public ResponseEntity<BorrowRecord> addBorrowRecord(@Valid @RequestBody BorrowRecord borrowRecord){
        BorrowRecord addedBorrowRecord =  borrowRecordService.addBorrowRecord(borrowRecord);
        return new ResponseEntity<>(addedBorrowRecord, HttpStatus.OK);
    }

    //@Operation(summary = "Update a borrow record", description = "This endpoint updates an existing borrow record by its ID")
    @PutMapping("updateRecord/{id}")
    public ResponseEntity<BorrowRecord> updateBorrowRecord(@PathVariable Long id, @RequestBody BorrowRecord borrowRecordDetails) {
        BorrowRecord updatedBorrowRecord = borrowRecordService.updateBorrowRecord(id, borrowRecordDetails);
        return ResponseEntity.ok(updatedBorrowRecord);
    }

    //@Operation(summary = "Delete a borrow record", description = "This endpoint deletes a borrow record by its ID")
    @DeleteMapping("deleteRecord/{id}")
    public String deleteBorrowRecord(@PathVariable Long id) {
        try {
            borrowRecordService.deleteBorrowRecord(id);
            return "record deleted successfully with ID: " + id;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
