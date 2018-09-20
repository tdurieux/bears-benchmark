package br.com.patiolegal.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.patiolegal.domain.Company;
import br.com.patiolegal.domain.Location;
import br.com.patiolegal.domain.Part;
import br.com.patiolegal.domain.Police;
import br.com.patiolegal.domain.Protocol;
import br.com.patiolegal.domain.ProtocolRecord;
import br.com.patiolegal.domain.Vehicle;
import br.com.patiolegal.dto.CompanyDTO;
import br.com.patiolegal.dto.CompanyDTO.CompanyDTOBuilder;
import br.com.patiolegal.dto.FileIdentifierDTO;
import br.com.patiolegal.dto.ProtocolDTO;
import br.com.patiolegal.dto.ProtocolDTO.ProtocolDTOBuilder;
import br.com.patiolegal.dto.ProtocolRequestDTO;
import br.com.patiolegal.exception.CompanyNotFoundException;
import br.com.patiolegal.exception.ProtocolNotFoundException;
import br.com.patiolegal.exception.ProtocolRecordNotFoundException;
import br.com.patiolegal.reports.ReportUtils;
import br.com.patiolegal.repository.CompanyRepository;
import br.com.patiolegal.repository.ProtocolRecordRepository;
import br.com.patiolegal.repository.ProtocolRepository;

@Service
public class ProtocolServiceBean implements ProtocolService {

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProtocolRecordRepository protocolRecordRepository;

    @Autowired
    private ReportUtils reportUtils;

    @Override
    public FileIdentifierDTO generateProtocol(ProtocolRequestDTO request) {

        Protocol protocol = protocolRepository.findByProtocol(request.getProtocol())
                .orElseThrow(ProtocolNotFoundException::new);

        Company company = companyRepository.findAll().stream().findFirst().orElseThrow(CompanyNotFoundException::new);

        CompanyDTO companyDTO = new CompanyDTOBuilder().withCity(company.getCity()).withImage(company.getImage())
                .withName(company.getName()).withPhone(company.getPhone()).withPostalCode(company.getPostalCode())
                .withPublicPlace(company.getPublicPlace()).withSocialName(company.getSocialName()).build();

        Vehicle vehicle = protocol.getEntrance().getVehicle();
        Police police = protocol.getEntrance().getPolice();
        Location location = protocol.getEntrance().getLocation();
        Part part = protocol.getArrestOrgan();
        ProtocolDTO dto = new ProtocolDTOBuilder().withPart(protocol.getPart()).withProtocol(protocol.getProtocol())
                .withDate(protocol.getDate()).withDateTimeIn(protocol.getDateTimeIn())
                .withDateTimeOut(protocol.getExit() == null ? null : protocol.getExit().getDateTimeOut())
                .withPoliceInvestigation(protocol.getPoliceInvestigation())
                .withEventBulletin(protocol.getEventBulletin()).withTaxId(protocol.getTaxId())
                .withName(protocol.getName()).withTheyRenamed(vehicle.getTheyRenamed())
                .withOwnerName(vehicle.getOwnerName()).withOwnerTaxIdentifier(vehicle.getOwnerTaxIdentifier())
                .withBrand(vehicle.getBrand()).withModel(vehicle.getModel()).withCategory(vehicle.getCategory())
                .withColor(vehicle.getColor()).withFuel(vehicle.getFuel()).withYearFactory(vehicle.getYearFactory())
                .withYearModel(vehicle.getYearModel()).withSportingPlate(vehicle.getSportingPlate())
                .withOriginalPlate(vehicle.getOriginalPlate()).withChassisState(vehicle.getChassisState())
                .withChassis(vehicle.getChassis()).withMotorState(vehicle.getEngineState())
                .withMotor(vehicle.getEngine()).withInsured(police.getInsured()).withFinanced(police.getFinanced())
                .withStolen(police.getStolen()).withDrugTrafficking(police.getDrugTrafficking())
                .withMoneyLaundry(police.getMoneyLaundry()).withPerquisite(police.getPerquisite())
                .withPapillaryExpertise(police.getPapillaryExpertise()).withOwnerIntimate(police.getOwnerIntimate())
                .withAuthorizedAlienation(police.getAuthorizedAlienation()).withDebits(police.getDebits())
                .withShed(location.getShed().getDescription()).withRow(location.getRow())
                .withColumn(location.getColumn()).withFloor(location.getFloor())
                .withAccountableIn(protocol.getAccountableIn()).withAccountableOut(protocol.getAccountableOut())
                .withAuthentication(protocol.getAuthentication()).withArrestOrgan(part.getDescription())
                .withAmountSeals(protocol.getAmountSeals()).build();
        byte[] file = generateProtocolReport(companyDTO, dto);
        ProtocolRecord protocolRecord = new ProtocolRecord();
        String username = getUsername();
        protocolRecord.setUsername(username);
        protocolRecord.setProtocol(protocol);
        protocolRecord.setFile(new Binary(BsonBinarySubType.BINARY, file));

        protocolRecordRepository.save(protocolRecord);
        return new FileIdentifierDTO(protocolRecord.getId());

    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private byte[] generateProtocolReport(CompanyDTO dto, ProtocolDTO protocol) {

        return reportUtils.generateProtocolReport(dto, protocol);
    }

    private InputStream getInputStreamProtocolRecord(ProtocolRecord protocolRecord) {
        Binary file = protocolRecord.getFile();
        return new ByteArrayInputStream(file.getData());
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadPdf(String protocol) {
        ProtocolRecord protocolRecord = protocolRecordRepository.findById(protocol)
                .orElseThrow(ProtocolRecordNotFoundException::new);
        InputStream inputStream = getInputStreamProtocolRecord(protocolRecord);
        return reportUtils.downloadPdfReport("protocol.pdf", inputStream);
    }
}
