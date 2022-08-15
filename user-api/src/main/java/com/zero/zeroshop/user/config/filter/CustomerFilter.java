package com.zero.zeroshop.user.config.filter;

import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.domain.domain.common.UserVo;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.service.CustomerService;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
public class CustomerFilter implements Filter {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");
        if (!provider.validateToken(token)) {
            throw new ServletException("Invalid Access");
        }
        UserVo userVo = provider.getUserVo(token);
        Customer customer =
                customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail()).orElseThrow(
                () -> new ServletException("Invalid Access"));

        chain.doFilter(request, response);
    }

}
