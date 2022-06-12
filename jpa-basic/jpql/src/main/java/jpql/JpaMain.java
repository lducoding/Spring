package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
            Team team = new Team();
            team.setName("티이임");
            em.persist(team);

            Member member = new Member();
            member.setUsername("merlin");
            member.setAge(12);
            member.setTeam(team);
            em.persist(member);

            List<Team> resultList = em.createQuery("select t from Member m left join m.team t on t.name = '티이임'", Team.class)
                    .getResultList();

            for (Team member1 : resultList) {
                System.out.println(member1.getName());
            }

            tx.commit();
        } catch (Exception e) {
            em.close();
        }
        emf.close();
    }
}
