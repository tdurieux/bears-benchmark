package br.com.patiolegal.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.patiolegal.dto.CompanyDTO;
import br.com.patiolegal.dto.ProtocolDTO;
import br.com.patiolegal.dto.SealDTO;
import br.com.patiolegal.dto.SealReportDTO;
import br.com.patiolegal.dto.SealRequestDTO;
import br.com.patiolegal.exception.GenerateProtocolReportException;
import br.com.patiolegal.exception.GenerateSealReportException;
import br.com.patiolegal.exception.NotFoundException;
import br.com.patiolegal.utils.QRCodeUtils;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtils {

    private static final Logger LOG = LogManager.getLogger(ReportUtils.class);
    @Value("classpath:reports/seal.jasper")
    private Resource sealResource;
    @Value("classpath:reports/protocol.jasper")
    private Resource protocolResource;

    public byte[] generateProtocolReport(CompanyDTO company, ProtocolDTO protocol) {

        LOG.info("Dados recebidos na requisicao para geracao de protocolo: " + protocol);

        List<ProtocolDTO> list = new ArrayList<>();
        list.add(protocol);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TITLE.LINE1", company.getLine1());
        parameters.put("TITLE.LINE2", company.getLine2());
        parameters.put("TITLE.LINE3", company.getLine3());
        String imageWithBasePrefix = StringUtils.replace(company.getImage(), "data:image/png;base64,", "");
        InputStream imageIs = new ByteArrayInputStream(Base64.decodeBase64(imageWithBasePrefix.getBytes()));
        parameters.put("TITLE.IMAGE", imageIs);

        try {
            LOG.debug("Preparando para gerar protocolo...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(protocolResource.getInputStream(), parameters,
                    beanColDataSource);
            LOG.debug("Protocolo gerado.");
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            LOG.error("Erro ao gerar protocolo: ", e);
            throw new GenerateProtocolReportException();
        }
    }

    public byte[] generateSealReport(SealRequestDTO request, SealReportDTO sealReportDTO) {
        LOG.info("Dados recebidos na requisicao para geracao de lacres: " + request.toString());
        long amount = (request.getAmount() != null ? request.getAmount() : 1);

        SealDTO sealDto = new SealDTO();
        sealDto.setProtocol(request.getProtocol());
        sealDto.setAuthentication(sealReportDTO.getAuthentication());

        try {
            String qrcodeText = sealReportDTO.stringfy();

            ByteArrayOutputStream qrCodeImage = QRCodeUtils.getQRCodeImage(qrcodeText, 100, 100);
            InputStream imageIs = new ByteArrayInputStream(qrCodeImage.toByteArray());
            sealDto.setImage(ImageIO.read(imageIs));
        } catch (IOException e) {
            LOG.error("Erro ao recuperar imagem:" + e);
        }

        List<SealDTO> seals = new ArrayList<>();
        while (seals.size() < amount) {
            seals.add(sealDto);
        }

        try {
            LOG.debug("Preparando para gerar lacres...");
            Map<String, Object> parameters = new HashMap<>();
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(seals);
            JasperPrint jasperPrint = JasperFillManager.fillReport(sealResource.getInputStream(), parameters,
                    beanColDataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            LOG.error("Erro ao gerar lacres: ", e);
            throw new GenerateSealReportException();
        }
    }

    public ResponseEntity<InputStreamResource> downloadPdfReport(String fileName, InputStream inputStream) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + fileName);

        try {
            return ResponseEntity.ok().headers(headers).contentLength(inputStream.available())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            throw new NotFoundException();
        }
    }

}
