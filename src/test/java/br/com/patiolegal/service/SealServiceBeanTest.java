package br.com.patiolegal.service;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.bson.types.Binary;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.patiolegal.domain.Configuration;
import br.com.patiolegal.domain.Entrance;
import br.com.patiolegal.domain.Location;
import br.com.patiolegal.domain.Protocol;
import br.com.patiolegal.domain.Seal;
import br.com.patiolegal.dto.ConfigurationStub;
import br.com.patiolegal.dto.FileIdentifierDTO;
import br.com.patiolegal.dto.SealRequestDTO;
import br.com.patiolegal.dto.SealRequestDTOStub;
import br.com.patiolegal.exception.BusinessException;
import br.com.patiolegal.exception.ProtocolNotFoundException;
import br.com.patiolegal.exception.SealNotFoundException;
import br.com.patiolegal.reports.ReportUtils;
import br.com.patiolegal.repository.ConfigurationRepository;
import br.com.patiolegal.repository.ProtocolRepository;
import br.com.patiolegal.repository.SealRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SealService.class, SealServiceBean.class, ReportUtils.class })
public class SealServiceBeanTest {

    @Autowired
    private SealService sealService;

    @MockBean
    private Protocol protocol;

    @MockBean
    private Location location;

    @MockBean
    private Entrance entrance;

    @MockBean
    private Seal seal;

    @MockBean
    private ProtocolRepository protocolRepository;

    @MockBean
    private ConfigurationRepository configurationRepository;

    @MockBean
    private SealRepository sealRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shoudValidatePrintSealLimitWithLargerAmount() {
        expectedException.expect(BusinessException.class);
        expectedException.expectMessage("Excedida quantidade de impressões. (Limite máximo: 10)");
        SealRequestDTO request = new SealRequestDTOStub();
        mockValidateForFail();
        sealService.generateSeal(request);
    }

    @Test
    @WithMockUser("customUsername")
    public void shoudValidatePrintSealLimitWithSmallerAmount() {
        SealRequestDTO request = new SealRequestDTOStub();
        mockValidateForSuccess();
        sealService.generateSeal(request);
    }

    @Test(expected = SealNotFoundException.class)
    public void shoudValidateDownloadWithoutSeal() {
        BDDMockito.given(sealRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        sealService.downloadSeal("456");
    }

    @Test
    public void shoudValidateDownloadWithSeal() {
        byte[] data = new byte[] { (byte) 130, (byte) 140 };
        Binary file = BDDMockito.mock(Binary.class);
        BDDMockito.given(seal.getFile()).willReturn(file);
        BDDMockito.given(file.getData()).willReturn(data);
        BDDMockito.given(sealRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(seal));
        ResponseEntity<InputStreamResource> downloadSeal = sealService.downloadSeal("456");
        assertNotNull(downloadSeal);
    }

    @Test
    @WithMockUser("customUsername")
    public void shoudValidateGenerateSealWithProtocol() {
        SealRequestDTO request = new SealRequestDTOStub();
        mockValidateForSuccess();
        FileIdentifierDTO fileIdentifierDTO = sealService.generateSeal(request);
        assertNotNull(fileIdentifierDTO);
    }

    @Test(expected = ProtocolNotFoundException.class)
    public void shoudValidateGenerateSealWithoutProtocol() {
        SealRequestDTO request = new SealRequestDTOStub();
        BDDMockito.given(protocolRepository.findByProtocol(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        sealService.generateSeal(request);
    }

    private void mockValidateForSuccess() {
        BDDMockito.given(protocol.getAmountSeals()).willReturn(5);
        BDDMockito.given(protocol.getEntrance()).willReturn(entrance);
        BDDMockito.given(protocol.getEntrance().getLocation()).willReturn(location);
        Configuration configuration = new ConfigurationStub("10");
        BDDMockito.given(configurationRepository.findByKey(ArgumentMatchers.anyString()))
                .willReturn(Optional.of(configuration));
        BDDMockito.given(protocolRepository.findByProtocol(ArgumentMatchers.anyString()))
                .willReturn(Optional.of(protocol));
    }

    private void mockValidateForFail() {
        BDDMockito.given(protocol.getAmountSeals()).willReturn(10);
        Configuration configuration = new ConfigurationStub("10");
        BDDMockito.given(configurationRepository.findByKey(ArgumentMatchers.anyString()))
                .willReturn(Optional.of(configuration));
        BDDMockito.given(protocolRepository.findByProtocol(ArgumentMatchers.anyString()))
                .willReturn(Optional.of(protocol));
    }

}
