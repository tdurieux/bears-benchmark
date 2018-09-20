package br.com.patiolegal.service;

import br.com.patiolegal.dto.LoginRequestDTO;
import br.com.patiolegal.dto.LoginResponseDTO;

public interface LoginService {

    LoginResponseDTO login(LoginRequestDTO request);

}
