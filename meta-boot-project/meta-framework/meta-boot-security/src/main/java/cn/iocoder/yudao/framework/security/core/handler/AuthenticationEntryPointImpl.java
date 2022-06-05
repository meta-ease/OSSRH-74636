package cn.iocoder.yudao.framework.security.core.handler;

import io.github.meta.ease.common.pojo.CommonResult;
import io.github.meta.ease.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.github.meta.ease.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;


/**
 * 访问一个需要认证的 URL 资源，但是此时自己尚未认证（登录）的情况下，返回 {@link io.github.meta.ease.common.exception.enums.GlobalErrorCodeConstants#UNAUTHORIZED} 错误码，从而使前端重定向到登录页
 * <p>
 * 补充：Spring Security 通过 {@link org.springframework.security.web.access.ExceptionTranslationFilter#sendStartAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, org.springframework.security.core.AuthenticationException)} 方法，调用当前类
 *
 * @author ruoyi
 */
@Slf4j
@SuppressWarnings("JavadocReference") // 忽略文档引用报错
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), e);
        // 返回 401
        ServletUtils.writeJSON(response, CommonResult.error(UNAUTHORIZED));
    }
}
