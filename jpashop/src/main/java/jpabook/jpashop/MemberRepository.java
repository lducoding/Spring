package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // 객체 리턴안하는 이유는 커맨드와 쿼리를 분리하라!
        // 저장을 일으키면 사이드 이펙트 일으킬 수 있는 커맨드 성으로 리턴값 거의 안만들고 아이디 정도만 리턴
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
