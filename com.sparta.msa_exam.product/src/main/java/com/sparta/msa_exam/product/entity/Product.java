package com.sparta.msa_exam.product.entity;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    @Column(name = "supply_price")
    private Integer supplyPrice;

    private String createdBy;

    public static Product createProduct(ProductRequestDto requestDto, String userId){
        return Product.builder()
            .name(requestDto.getName())
            .supplyPrice(requestDto.getSupplyPrice())
            .createdBy(userId)
            .build();
    }

    // Put method : 통으로 변환
    // Fetch method : 하나씩 변경

    public ProductResponseDto toResponseDto(){
        return new ProductResponseDto(
            this.id, this.name, this.supplyPrice
        );
    }

}
