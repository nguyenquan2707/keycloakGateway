package com.sergio.auth.backend.resources.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class SecurityConfig {

    /**
     * For the backend-resources, I indicate that all the endpoints are protected.
     * To request any endpoint, the OAuth2 protocol is necessary, using the server configured and with the given scope.
     * Thus, a JWT will be used to communicate between the backend-resources and backend-auth when backend-resources
     * needs to validate the authentication of a request.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.mvcMatcher("/**")
                .authorizeRequests()
                .mvcMatchers("/**")
                .hasRole("user")
//                .access("hasAuthority('SCOPE_message.read')")

                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(source -> new CustomJwtConfigure().convert(source));
        return http.build();
    }

    public static class CustomJwtConfigure implements Converter<Jwt, JwtAuthenticationToken> {
        private static final String REALM = "realm_access";
        private static final String ROLES = "roles";

        @Override
        public JwtAuthenticationToken convert(Jwt jwt) {
            var tokenAttributes = jwt.getClaims();
            var jsonObject = (JSONObject) tokenAttributes.get(REALM);
            var roles = (JSONArray) jsonObject.get(ROLES);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            return new JwtAuthenticationToken(jwt, grantedAuthorities);
        }
    }
}
