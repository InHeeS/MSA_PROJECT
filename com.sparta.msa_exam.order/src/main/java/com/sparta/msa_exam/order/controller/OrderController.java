package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.UpdateOrderRequestDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 조회 API 의 결과를 캐싱 처리하여 60초 동안은 DB 에서 불러오는 데이터가 아닌
     * 메모리에 캐싱된 데이터가 보여지도록 설정
     * @param requestDto
     * @param userId
     * @param role
     * @return
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
                                                        @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                        @RequestHeader(value = "X-Role", required = true) String role){
        return ResponseEntity.ok().body(orderService.createOrder(requestDto, userId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long orderId,
                                                        @RequestBody UpdateOrderRequestDto requestDto,
                                                        @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                        @RequestHeader(value = "X-Role", required = true) String role){
        return ResponseEntity.ok().body(orderService.updateOrder(orderId, requestDto, userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getOrderById(orderId));
    }
}
