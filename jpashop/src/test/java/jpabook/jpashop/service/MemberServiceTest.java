package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //junit 실행 시 스프링과 함께 실해하겠다
@SpringBootTest // springboot 띄운 상태도 실행할 꺼
@Transactional // 기본적으로 rollback persist만으로는 insert가 되는게 아니라 commit시에 되는 것
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(value = false)
    public void  회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("lee");
        
        //when
        Long joinId = memberService.join(member);

        //then
//        em.flush();
        assertEquals(member, memberRepository.findOne(joinId));
    }

    @Test(expected = IllegalStateException.class)
    public void  중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("lee");

        Member member2 = new Member();
        member2.setName("lee");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외 발생해야 함");
    }
}