package br.com.patiolegal.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.patiolegal.domain.Exit;
import br.com.patiolegal.domain.Protocol;
import br.com.patiolegal.dto.ExitRequestDTO;
import br.com.patiolegal.exception.BusinessException;
import br.com.patiolegal.exception.ProtocolNotFoundException;
import br.com.patiolegal.repository.EntranceRepository;

@Service
public class ExitServiceBean implements ExitService {

    private static final Logger LOG = LogManager.getLogger(ExitServiceBean.class);

    @Autowired
    private EntranceRepository repository;

    @Override
    public void exit(ExitRequestDTO request) {
        LOG.debug("Dados recebidos na requisição: " + request);

        List<Protocol> protocols = repository.findByProtocol(request.getProtocol());
        if (protocols.isEmpty()) {
            throw new ProtocolNotFoundException();
        }

        LOG.debug("Protocolo encontrado. Iniciando processo de saida...");

        Protocol protocol = protocols.get(0);
        if (protocol.getExit() != null) {
            throw new BusinessException("exit", "Protocolo já baixado.");
        }

        LOG.debug("Validando date...");
        validateDate(request, protocol);

        Exit exit = new Exit(request.getDate(), request.getTaxIdentifier(), getUserNameAuthentication());
        protocol.setAccountableOut(getUserNameAuthentication());
        protocol.setExit(exit);

        LOG.debug("Efetuando saída.");
        repository.save(protocol);
        LOG.debug("Saída efetuada.");
    }

    private void validateDate(ExitRequestDTO request, Protocol protocol) {
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new BusinessException("date", "Data de saida não pode ser maior que data atual");
        }
        if (request.getDate().isBefore(protocol.getDate())) {
            throw new BusinessException("date", "Data de saida não pode ser menor que data entrada");
        }

    }

    private String getUserNameAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
