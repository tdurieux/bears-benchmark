package br.com.patiolegal.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.patiolegal.domain.User;
import br.com.patiolegal.dto.LoginRequestDTO;
import br.com.patiolegal.dto.LoginResponseDTO;
import br.com.patiolegal.dto.LoginResponseDTO.LoginResponseBuilder;
import br.com.patiolegal.exception.AccessDeniedException;
import br.com.patiolegal.repository.UserRepository;

@Service
public class LoginServiceBean implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        String login = StringUtils.upperCase(request.getUsername());

        User user = userRepository.findByUsername(request.getUsername());

        if (user == null || !StringUtils.equals(user.getPassword(), request.getPassword())) {
            throw new AccessDeniedException();
        }

        return new LoginResponseBuilder().withAccessToken("DSADSA").withProfile("ADMIN").withUsername(login).build();
    }

}
