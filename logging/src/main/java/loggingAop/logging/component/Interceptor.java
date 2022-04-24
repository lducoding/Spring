package loggingAop.logging.component;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
public class Interceptor implements HandlerInterceptor {

    // request가 들어오고 controller에 넘어가기 직전에 처리
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("url controller 직전 : {}", request.getRequestURI());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    // Controller 요청을 모두 마무리 하고 view로 렌더링 하기 전에 처리
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("response status 컨트롤러 직후 : {}", response.getStatus());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // 요청 모두 마무리되고 view에 렌더링 해주면 처리
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("마무리");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
