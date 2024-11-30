package com.fashionstore.fashion_store_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDto {
    private String code;
    private String name;
    private double fee;
}
