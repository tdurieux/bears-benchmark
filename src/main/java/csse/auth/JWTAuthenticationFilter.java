package csse.auth;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import csse.users.ApplicationUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static csse.auth.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Autowired
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            ApplicationUser user = new ObjectMapper()
                    .readValue(request.getInputStream(), ApplicationUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    	
    	// Add token to header
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + generateJWTToken(authResult));
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write("{\"" + HEADER_STRING + "\":\"Bearer "  + generateJWTToken(authResult)+"\"}");
//        ApplicationUser u = new ApplicationUser();
//        
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		Date date = new Date();
//		String d=dateFormat.format(date);
//        
//        u.setlastLogin(d);
        
    }

    /**
     *
     * Returns a String in the form of the JWT token template (in the form of header.payload.signature).
     * The default spring security authentication is used to validate the username and password.
     * the JWT.create() builder is use to create the token.
     * User roles are taken from the authentication parameter and added as claims to the JWT token
     * so that they can be used in role-based UI routing in the frontend application
     *
     * @param authentication the spring framework's Authentication object which holds information about the user
     * @return JWT token
     */
	
    private String generateJWTToken(Authentication authentication){

        ArrayList<String> arr =  new ArrayList<>();
        for(GrantedAuthority authority: ((User)authentication.getPrincipal()).getAuthorities()){
            arr.add(authority.getAuthority());
        }
        String[] authorities = new String[arr.size()];
        authorities = arr.toArray(authorities);

        return JWT.create()
                .withSubject(((User) authentication.getPrincipal()).getUsername())
                .withArrayClaim("roles", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }
}