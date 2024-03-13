package com.colsubsidio.pricesapi.infrastructure.security;

import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.GsonUtils;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.Resultado;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final LogsManager log;
    private final EnvironmentService environmentService;


    public ApiKeyAuthFilter(
            LogsManager log,
            EnvironmentService environmentService)
    {
        this.log = log;
        this.environmentService = environmentService;
    }

    private Authentication createAuthentication() {
        return new ApiKeyAuthenticationToken("apikey", Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_USER")));
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        this.log.info("ApiKeyFilter - URL -- ",request.getMethod() + " -- " + request.getRequestURI());
        this.log.info("ApiKeyAuthFilter activo");
        String requestApiKey = request.getHeader("ApiKey");

        boolean sonCredencialesValidas = this.environmentService.getApiKey().equals(requestApiKey);
        boolean esH2Console = request.getRequestURI().contains("/h2-console");

        if (sonCredencialesValidas || esH2Console) {
            this.log.info("sonCredencialesValidas", "Se crea autenticación");
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("trigger", (Object)null, (Collection)null);
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        } else {
            var resultado = Resultado.instance(HttpStatus.UNAUTHORIZED, false, null);
            String jsonResponse = GsonUtils.serialize(resultado);
            this.log.info("noSonCredencialesValidas", "Se niega petición");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(jsonResponse);
        }
    }
}
