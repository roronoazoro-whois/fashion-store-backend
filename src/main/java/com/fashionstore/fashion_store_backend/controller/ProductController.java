package com.fashionstore.fashion_store_backend.controller;

import com.fashionstore.fashion_store_backend.dto.ProductCreateDto;
import com.fashionstore.fashion_store_backend.dto.ProductImagesCreateDto;
import com.fashionstore.fashion_store_backend.dto.ProductResponseDto;
import com.fashionstore.fashion_store_backend.response.ApiResponse;
import com.fashionstore.fashion_store_backend.service.ImageService;
import com.fashionstore.fashion_store_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    // API tạo mới sản phẩm
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductCreateDto productDTO) {
        try {
            // Gọi service để tạo mới sản phẩm
            Long product = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Tạo sản phẩm thành công", true, product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }

    // API tạo ảnh cho sản phẩm
    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse> createProductImages(@PathVariable Long productId, @RequestBody ProductImagesCreateDto requestDto) {
        try {
            // Gọi service để tạo ảnh
            imageService.createProductImages(productId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Tạo ảnh cho sản phẩm thành công", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{productId}/images")
    public ResponseEntity<ApiResponse> updateProductImages(@PathVariable Long productId, @RequestBody ProductImagesCreateDto requestDto) {
        try {
            // Gọi service để cập nhật lại hình ảnh
            imageService.updateProductImages(productId, requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Cập nhật ảnh cho sản phẩm thành công", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }


    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> softDeleteProduct(@PathVariable Long productId) {
        try {
            productService.softDeleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Xóa sản phẩm thành công", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            // Gọi service để lấy thông tin sản phẩm
            ProductResponseDto productResponseDto = productService.getProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Lấy thông tin sản phẩm thành công", true, productResponseDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductCreateDto productDTO) {
        try {
            // Gọi service để cập nhật sản phẩm
            Long updateProductId = productService.updateProduct(productId, productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Cập nhật sản phẩm thành công", true, updateProductId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }


}
