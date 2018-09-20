package br.com.patiolegal.service;

import java.lang.reflect.Field;
import java.util.Optional;

import org.bson.types.Binary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import br.com.patiolegal.domain.Configuration;
import br.com.patiolegal.domain.Entrance;
import br.com.patiolegal.domain.Location;
import br.com.patiolegal.domain.Protocol;
import br.com.patiolegal.domain.Seal;
import br.com.patiolegal.dto.SealRequestDTO;
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
    private Configuration configuration;

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

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private Authentication authentication;

    @Test(expected = BusinessException.class)
    public void shoudValidatePrintSealLimitWithLargerAmount() {

        SealRequestDTO request = mockSealRequestDTO();
        mockValidateForFail();
        sealService.generateSeal(request);
    }

    @Test
    public void shoudValidatePrintSealLimitWithSmallerAmount() {

        SealRequestDTO request = mockSealRequestDTO();
        mockValidateForSuccess();
        sealService.generateSeal(request);
    }
    
    @Test(expected = SealNotFoundException.class)
    public void shoudValidateDownloadWithoutSeal(){
        BDDMockito.given(sealRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        sealService.downloadSeal("456");
    }
    
    @Test
    public void shoudValidateDownloadWithSeal(){
        byte[] data = new byte[]{(byte)130,(byte)140};
        Binary file = BDDMockito.mock(Binary.class);
        BDDMockito.given(seal.getFile()).willReturn(file);
        BDDMockito.given(file.getData()).willReturn(data);
        BDDMockito.given(sealRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(seal));
        sealService.downloadSeal("456");
    }
    
    @Test
    public void shoudValidateGenerateSealWithProtocol(){
        SealRequestDTO request = mockSealRequestDTO();
        mockValidateForSuccess();
        sealService.generateSeal(request);
    }
    
    @Test(expected = ProtocolNotFoundException.class)
    public void shoudValidateGenerateSealWithoutProtocol(){
        SealRequestDTO request = mockSealRequestDTO();
        BDDMockito.given(protocolRepository.findByProtocol(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        sealService.generateSeal(request);
    }

    private void mockValidateForSuccess(){
        BDDMockito.given(protocol.getAmountSeals()).willReturn(5);
        BDDMockito.given(protocol.getEntrance()).willReturn(entrance);
        BDDMockito.given(protocol.getEntrance().getLocation()).willReturn(location);
        BDDMockito.given(configurationRepository.findByKey(ArgumentMatchers.anyString())).willReturn(Optional.of(configuration));
        BDDMockito.given(configuration.getValue()).willReturn("10");
        BDDMockito.given(protocolRepository.findByProtocol(ArgumentMatchers.anyString())).willReturn(Optional.of(protocol));
        BDDMockito.given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    
    private void mockValidateForFail(){
        BDDMockito.given(protocol.getAmountSeals()).willReturn(10);
        BDDMockito.given(configurationRepository.findByKey(ArgumentMatchers.anyString())).willReturn(Optional.of(configuration));
        BDDMockito.given(configuration.getValue()).willReturn("10");
        BDDMockito.given(protocolRepository.findByProtocol(ArgumentMatchers.anyString())).willReturn(Optional.of(protocol));
    }
    
    private SealRequestDTO mockSealRequestDTO() {

        SealRequestDTO sealRequestDTO = new SealRequestDTO();

        String protocol = "AAAAA";
        Integer amount = 5;
        String reason = "XXX";

        Field fieldProtocol = ReflectionUtils.findField(SealRequestDTO.class, "protocol");
        Field fieldAmount = ReflectionUtils.findField(SealRequestDTO.class, "amount");
        Field fieldReason = ReflectionUtils.findField(SealRequestDTO.class, "reason");

        ReflectionUtils.makeAccessible(fieldProtocol);
        ReflectionUtils.makeAccessible(fieldAmount);
        ReflectionUtils.makeAccessible(fieldReason);

        ReflectionUtils.setField(fieldProtocol, sealRequestDTO, protocol);
        ReflectionUtils.setField(fieldAmount, sealRequestDTO, amount);
        ReflectionUtils.setField(fieldReason, sealRequestDTO, reason);

        return sealRequestDTO;
    }

}
