package br.com.fiap.Calendario2.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.Calendario2.service.TokenJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationFilter  extends OncePerRequestFilter{


    @Autowired
    TokenJwtService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                var token = getTokenFromHeader(request);
                if (token != null){
                    var conta = service.validate(token);
                    Authentication auth = new UsernamePasswordAuthenticationToken(conta, null, conta.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

                filterChain.doFilter(request, response);

    }
    

    private String getTokenFromHeader(HttpServletRequest request){
        var header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ") || header.isEmpty()){
            return null;
        }

        return header.replace("Bearer ", "");
    }
    

}
