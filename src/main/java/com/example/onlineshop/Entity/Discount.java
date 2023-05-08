package com.example.onlineshop.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

    private Double discountPrice;

    private LocalDateTime endDiscount;
}
