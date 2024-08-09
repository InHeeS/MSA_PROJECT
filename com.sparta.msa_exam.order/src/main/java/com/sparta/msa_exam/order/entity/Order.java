package com.sparta.msa_exam.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private String name;
    private String createdBy;

    // 부모가 Order 이므로 mappedBy = "order", name = "부모 Id column"
    // 가 Order 엔티티의 productsIds 리스트에 추가되었을 때,
    // CascadeType.ALL로 인해 Order 엔티티를 저장할 때 ProductIds 엔티티도 자동으로 저장됩니다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "products_ids")
    private List<ProductIds> productsIds = new ArrayList<>();

    public void addProduct(ProductIds productIds) {
        this.productsIds.add(productIds);
        productIds.addOrder(this); // 양방향 관계
    }
}
