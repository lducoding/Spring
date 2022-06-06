package jpabook.jpashop;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // jpa는 이 팩토리 기반으로 돌아가고 이건 db당 하나를 만들어야 함
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // 고객의 요청이오면 이 매니저를 만들고 작업
        EntityManager em = emf.createEntityManager();
        // 트랜잭션 안에서 실행해야 한다.
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {

            Order order = new Order();
            order.addOrderItem(new OrderItem());

            tx.commit();
        } catch (Exception e) {
            em.close();
        }
        emf.close();
    }
}
