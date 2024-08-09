package com.sparta.msa_exam.order.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto implements Serializable {
    private Long orderId;
    private List<Integer> productIds;
}
