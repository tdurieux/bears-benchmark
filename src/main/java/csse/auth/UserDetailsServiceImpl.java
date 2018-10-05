package csse.auth;

import csse.users.ApplicationUser;
import csse.users.UserService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service	// It has to be annotated with @Service.
public class UserDetailsServiceImpl implements UserDetailsService{

    private UserService service;

    public UserDetailsServiceImpl(UserService userservice) {
        this.service = userservice;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser user = service.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String authority: user.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return new User(user.getUsername(), user.getPassword(), true, true, true,
                true, authorities);
    }
    
}