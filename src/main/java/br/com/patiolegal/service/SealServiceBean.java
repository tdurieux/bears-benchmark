package br.com.patiolegal.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.patiolegal.domain.Configuration;
import br.com.patiolegal.domain.Location;
import br.com.patiolegal.domain.Protocol;
import br.com.patiolegal.domain.Seal;
import br.com.patiolegal.dto.FileIdentifierDTO;
import br.com.patiolegal.dto.SealReportDTO;
import br.com.patiolegal.dto.SealReportDTO.SealReportBuilder;
import br.com.patiolegal.dto.SealRequestDTO;
import br.com.patiolegal.exception.BusinessException;
import br.com.patiolegal.exception.ConfigurationNotFoundException;
import br.com.patiolegal.exception.ProtocolNotFoundException;
import br.com.patiolegal.exception.SealNotFoundException;
import br.com.patiolegal.reports.ReportUtils;
import br.com.patiolegal.repository.ConfigurationRepository;
import br.com.patiolegal.repository.ProtocolRepository;
import br.com.patiolegal.repository.SealRepository;

@Service
public class SealServiceBean implements SealService {

    private static final Logger LOG = LogManager.getLogger(SealServiceBean.class);
    private static final String KEY_PRINT_SEAL_LIMIT = "print.seal.limit";
    @Autowired
    private ReportUtils reportUtils;
    @Autowired
    private ProtocolRepository protocolRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private SealRepository sealRepository;

    private void validatePrintSealLimit(Protocol protocol, Integer amountRequired) {

        LOG.debug("Validando quantidade de lacres...");
        LOG.debug("Quantidade requerida: " + amountRequired);

        Configuration configuration = findConfigurationByKey(KEY_PRINT_SEAL_LIMIT);
        Integer limitPrintConfig = new Integer(configuration.getValue());
        LOG.debug("Quantidade máxima permitida : " + limitPrintConfig);

        Integer amountSealsPrinted = protocol.getAmountSeals();
        LOG.debug("Quantidade ja impressa de lacres : " + amountSealsPrinted);

        if ((amountRequired + amountSealsPrinted) > limitPrintConfig) {
            StringBuilder message = new StringBuilder();
            message.append("Excedido valor máximo de impressões.");
            message.append("\n");
            message.append("Valor configurado: ");
            message.append(limitPrintConfig);
            message.append("\n");
            message.append("Total impresso: ");
            message.append(amountSealsPrinted);
            message.append("\n");
            message.append("Solicitado: ");
            message.append(amountRequired);
            throw new BusinessException(KEY_PRINT_SEAL_LIMIT, String.valueOf(message));
        }

    }

    private Protocol findProtocol(String protocolRequest) {
        Optional<Protocol> protocol = protocolRepository.findByProtocol(protocolRequest);
        if (protocol.isPresent()) {
            return protocol.get();
        }
        LOG.error("Protocolo não encontrado em base de dados: " + protocolRequest);
        throw new ProtocolNotFoundException();
    }

    private Configuration findConfigurationByKey(String key) {
        Optional<Configuration> configuration = configurationRepository.findByKey(key);
        if (configuration.isPresent()) {
            return configuration.get();
        }
        LOG.error("Configuração não encontrada: " + KEY_PRINT_SEAL_LIMIT);
        throw new ConfigurationNotFoundException();
    }

    private Seal findSealById(String id) {
        Optional<Seal> seal = sealRepository.findById(id);
        if (seal.isPresent()) {
            return seal.get();
        }
        throw new SealNotFoundException();
    }

    private InputStream getInputStreamSeal(Seal seal) {
        Binary file = seal.getFile();
        return new ByteArrayInputStream(file.getData());
    }

    private String getUserNameAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private byte[] generateSealReport(SealRequestDTO request, Protocol protocol, Seal seal) {

        Location location = protocol.getEntrance().getLocation();

        SealReportDTO sealReportDTO = new SealReportBuilder().withLocation(location.stringfy())
                .withAuthentication(seal.getAuthentication()).withProtocol(protocol.getProtocol()).build();

        LOG.debug("Criando file para o lacre gerado...");
        return reportUtils.generateSealReport(request, sealReportDTO);

    }

    private void saveSeal(Seal seal) {
        LOG.debug("Salvando lacres...");
        sealRepository.save(seal);
        LOG.debug("Salvo com sucesso.");
    }

    private void saveProtocol(Protocol protocol) {
        LOG.debug("Salvando protocolo...");
        protocolRepository.save(protocol);
    }

    @Override
    public FileIdentifierDTO generateSeal(SealRequestDTO request) {
        LOG.debug("Dados recebidos na requisição: " + request);

        Protocol protocol = findProtocol(request.getProtocol());

        validatePrintSealLimit(protocol, request.getAmount());

        Seal seal = new Seal();
        seal.generateAuthentication();
        seal.setAmount(request.getAmount());
        seal.setReason(request.getReason());
        String username = getUserNameAuthentication();
        seal.setUsername(username);

        byte[] file = generateSealReport(request, protocol, seal);

        seal.setFile(new Binary(BsonBinarySubType.BINARY, file));

        saveSeal(seal);

        protocol.addSeal(seal);

        saveProtocol(protocol);

        LOG.debug("Lacre gerado com sucesso.");
        return new FileIdentifierDTO(seal.getId());
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadSeal(String id) {
        Seal seal = findSealById(id);
        InputStream inputStream = getInputStreamSeal(seal);
        return reportUtils.downloadPdfReport("lacre.pdf", inputStream);
    }

}
