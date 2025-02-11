package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "title"),
                @UniqueConstraint(columnNames = "isbn")
        })
@Getter
@Setter
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 20)
    private String isbn;

    @NotNull
    private Integer published_year;

    @NotNull
    private Boolean available = true;

    public Books() {}

    public Books(String title, String isbn, Integer published_year, Boolean available) {
        this.title = title;
        this.isbn = isbn;
        this.published_year = published_year;
        this.available = available;
    }


}


