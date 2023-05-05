package com.example.onlineshop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purcshase_product")
public class PurchaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private Long productId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    private Double price;

    @JsonIgnore
    private String organizationName;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> purcshaseKeyword;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> purcshaseCharacteristicTable;

    @JsonIgnore
    @OneToMany(mappedBy = "purchaseProduct", fetch = FetchType.EAGER)
    private List<HistoryOrders> historyOrders;

    public PurchaseProduct(Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.organizationName = product.getOrganization().getName();
        this.purcshaseKeyword = new ArrayList<>(product.getKeyword());
        this.purcshaseCharacteristicTable = new HashMap<>(product.getCharacteristicTable());
    }
}
