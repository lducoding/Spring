package jpabook.jpashop.domain2;

import jpabook.jpashop.domain2.item.Item;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;
}
