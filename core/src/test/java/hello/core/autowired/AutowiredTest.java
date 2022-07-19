package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void autowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    static class TestBean {

        // Member가 빈에 등록이 안되어 있어서
        // 호출이 안됨
        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            System.out.println("member = " + noBean1);
        }

        // 호출은 되나 null임
        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("member = " + noBean2);
        }

        // 호출이 되고 Optional.empty 임
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("member = " + noBean3);
        }
    }
}
