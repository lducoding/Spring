package study.datajpa.entity.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Optional<Member> byId = memberRepository.findById(savedMember.getId());
        Member findMember = byId.get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        //then
        // 단건조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("123123");

        // 리스트 조회 검증
        int size = memberRepository.findAll().size();
        assertThat(size).isEqualTo(2);

        // 카운트 검증
        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long deletedCOunt = memberRepository.count();
        assertThat(deletedCOunt).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA",10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();
        for (String s : result) {
            assertThat(s).isEqualTo("AAA");
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA",10);
        memberRepository.save(member1);
        member1.setTeam(team);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println(dto);
        }
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    public void returnType() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aaa = memberRepository.findByUsername("AAA");
        System.out.println(aaa);
    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("membeer1", 10));
        memberRepository.save(new Member("membeer2", 10));
        memberRepository.save(new Member("membeer3", 10));
        memberRepository.save(new Member("membeer4", 10));
        memberRepository.save(new Member("membeer5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when

        // Page 와 Slice
        // Slice 는 limit에 하나 더 추가해서 불러온다. 위 예에서는 3개니깐 4개 불러
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), "TTT"));


        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3); // 불러온 페이지 수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 페이지 수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호 가져옴 (총 2페이지 중 1페이지)
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 갯수
        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지 인지?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는지
    }

    @Test
    public void testBulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("membeer1", 10));
        memberRepository.save(new Member("membeer2", 19));
        memberRepository.save(new Member("membeer3", 20));
        memberRepository.save(new Member("membeer4", 21));
        memberRepository.save(new Member("membeer5", 40));

        //when
        int bulkAgePlus = memberRepository.bulkAgePlus(20);
//        em.clear();

        List<Member> members = memberRepository.findByUsername("membeer5");
        System.out.println(members.get(0));

        //then
        assertThat(bulkAgePlus).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member: "+ member.getUsername());
            System.out.println("member.Team: "+ member.getTeam());
        }

        //then
    }

    @Test
    public void queryHint() throws Exception {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush();

        //when

        //then
    }

    @Test
    public void lock() throws Exception {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findLockByUsername("member1");
    }
}