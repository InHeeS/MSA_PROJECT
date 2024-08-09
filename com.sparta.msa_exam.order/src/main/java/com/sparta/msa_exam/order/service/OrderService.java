package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.client.ProductResponseDto;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.UpdateOrderRequestDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.ProductIds;
import com.sparta.msa_exam.order.repository.OrderRepository;
import com.sparta.msa_exam.order.repository.ProductIdsRepository;
import feign.FeignException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.dfa.DFA;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductIdsRepository productIdsRepository;
    private final ProductClient productClient;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, String userId) {

        List<ProductIds> productIdsList = new ArrayList<>();

        Order order = Order.builder()
            .name(requestDto.getName())
            .createdBy(userId)
            .productsIds(productIdsList) // "this.productsIds" is null error solve check
            .build();

        for (Integer productId : requestDto.getProductIds()) {
            try{
                ResponseEntity<ProductResponseDto> response =
                    productClient.getProductById(productId.longValue());

                log.info("Find Product ID : {}, Name : {}",
                    response.getBody().getProductId(),
                    response.getBody().getName());

                ProductIds productIds = ProductIds.builder()
                    .productId(productId.longValue())
                    .order(order)
                    .build();
                log.info("ProductIds productId is {}", productIds.getProductId());
//                productIdsList.add(productIds);
                order.addProduct(productIds);

            } catch (FeignException.NotFound e){
                log.error("Error occurred while fetching product details for product ID {}: {}", productId, e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching product details", e);
            }
        }

        orderRepository.save(order);
//        productIdsRepository.saveAll(productIdsList);

        return toResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long orderId, UpdateOrderRequestDto requestDto, String userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
        // feignClient 로 해당 상품이 존재하는지 확인 로직 필요
        ResponseEntity<ProductResponseDto> response = productClient.getProductById(requestDto.getProductId());
        log.info("Find Product ID : {}, Name : {}",
            response.getBody().getProductId(),
            response.getBody().getName());

        ProductIds productIds = new ProductIds(requestDto.getProductId(), order);
        productIdsRepository.save(productIds);

        order.addProduct(productIds);

        return toResponseDto(order);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return toResponseDto(order);
    }

    private OrderResponseDto toResponseDto(Order order){
        List<Integer> productIds = order.getProductsIds()
            .stream()
            .map(p -> p.getProductId().intValue())
            .collect(Collectors.toList());

        return new OrderResponseDto(
            order.getId(),
            productIds
        );
    }
}
