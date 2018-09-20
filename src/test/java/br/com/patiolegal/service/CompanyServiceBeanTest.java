package br.com.patiolegal.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.patiolegal.domain.Company;
import br.com.patiolegal.dto.CompanyDTO;
import br.com.patiolegal.dto.CompanyDTO.CompanyDTOBuilder;
import br.com.patiolegal.exception.CompanyNotFoundException;
import br.com.patiolegal.repository.CompanyRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CompanyService.class, CompanyServiceBean.class })
public class CompanyServiceBeanTest {

    @Autowired
    private CompanyService companyService;

    @MockBean
    private CompanyRepository companyRepository;

    @Test
    public void shouldTestInstanceService() {
        Assert.assertNotNull(companyService);
    }
    
    @Test(expected = CompanyNotFoundException.class)
    public void shouldValidateWithoudResults() {
        BDDMockito.given(companyRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.empty());
        companyService.findById("123");
    }
    
    @Test
    public void shouldValidateWithResults() {
        Company company = new Company();
        company.setCity("Maringá");
        BDDMockito.given(companyRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(company));
        CompanyDTO actual = companyService.findById("123");
        CompanyDTO expected = new CompanyDTOBuilder().withCity("Maringá").build();
        Assert.assertEquals(expected, actual);
    }

}
