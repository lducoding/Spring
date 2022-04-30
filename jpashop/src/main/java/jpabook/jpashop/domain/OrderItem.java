package jpabook.jpashop.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격
    private int count; // 주문 수량
}
