package hellojpa;

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
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "Street", "1000"));

            member.getFavoriteFoods().add("이마트피자");
            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족방");

            member.getAddressHistory().add(new Address("old1", "Street", "1000"));
            member.getAddressHistory().add(new Address("old2", "Street", "1000"));
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("===============================");
            Member member1 = em.find(Member.class, member.getId());

//            member1.getFavoriteFoods().remove("족방");
//            member1.getFavoriteFoods().add("족23");

            member1.getAddressHistory().remove(new Address("old1", "Street", "1000"));
            member1.getAddressHistory().add(new Address("newCity", "Street", "1000"));


            tx.commit();
        } catch (Exception e) {
            em.close();
        }
        emf.close();
    }
}
