package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("책책책", 2000, 5);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order order = orderRepository.findOne(orderId);
        //then
        assertEquals("주문 테스트 ORDER 나와야 함", OrderStatus.ORDER , order.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, order.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", (2000*2), order.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 5-orderCount, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("책책책", 2000, 5);

        int orderCount = 10;
        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외 발생해야한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("책책책", 2000, 5);

        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals("상태는 cancel", OrderStatus.CANCEL, order.getStatus());
        assertEquals("주문수량 복구", 5, book.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("이동욱");
        member.setAddress(new Address("시티", "스트리트", "집코드"));
        em.persist(member);
        return member;
    }

    private Book createBook(String bookName, int bookPrice, int bookQuantity) {
        Book book = new Book();
        book.setName(bookName);
        book.setPrice(bookPrice);
        book.setStockQuantity(bookQuantity);
        em.persist(book);
        return book;
    }
}
