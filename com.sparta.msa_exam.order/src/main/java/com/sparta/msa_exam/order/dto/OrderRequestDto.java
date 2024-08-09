package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.entity.ProductIds;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private String name;
    private List<Integer> productIds; // ex : "productIds : [1,2,3]"
}
