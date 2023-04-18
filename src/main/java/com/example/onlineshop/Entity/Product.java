package com.example.onlineshop.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String organization;

    @Min(1)
    private Integer price;

    @NotBlank
    @Column(name = "quantity_in_stock")
    private String quantityInStock;

    @NotBlank
    private String discount;

    @ElementCollection(fetch = FetchType.EAGER)
    List<String> reviews;

    @ElementCollection(fetch = FetchType.EAGER)
    List<String> keyword;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "characteristic_table")
    Map<String, String> characteristicTable;

    private Double grade;
}
