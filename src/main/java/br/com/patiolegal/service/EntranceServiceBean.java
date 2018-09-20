package br.com.patiolegal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import br.com.patiolegal.domain.ChassisState;
import br.com.patiolegal.domain.EngineState;
import br.com.patiolegal.domain.Entrance;
import br.com.patiolegal.domain.Location;
import br.com.patiolegal.domain.Part;
import br.com.patiolegal.domain.Police;
import br.com.patiolegal.domain.Protocol;
import br.com.patiolegal.domain.QProtocol;
import br.com.patiolegal.domain.Shed;
import br.com.patiolegal.domain.Vehicle;
import br.com.patiolegal.dto.ProtocolDTO;
import br.com.patiolegal.dto.ProtocolDTO.ProtocolDTOBuilder;
import br.com.patiolegal.dto.ProtocolRequestDTO;
import br.com.patiolegal.dto.SearchEntranceRequestDTO;
import br.com.patiolegal.dto.SearchEntranceResponseDTO;
import br.com.patiolegal.dto.SearchEntranceResponseDTO.SearchEntranceBuilder;
import br.com.patiolegal.exception.BusinessException;
import br.com.patiolegal.repository.EntranceRepository;
import br.com.patiolegal.repository.PartRepository;
import br.com.patiolegal.repository.ShedRepository;

@Service
public class EntranceServiceBean implements EntranceService {

    private static final Logger LOG = LogManager.getLogger(EntranceServiceBean.class);

    @Autowired
    private EntranceRepository entranceRepository;

    @Autowired
    private ShedRepository shedRepository;

    @Autowired
    private PartRepository partRepository;

    @Override
    public String save(ProtocolRequestDTO request) {

        LOG.info("Dados recebidos na requisição:" + request);

        Protocol protocol = new Protocol();
        Entrance entrance = new Entrance();
        Vehicle vehicle = new Vehicle();
        Police police = new Police();
        Location location = new Location();
        Shed shed;

        LOG.debug("Validando date...");
        validateDate(request);
        LOG.debug("Validando e retornando shed...");
        shed = validateAndReturnShed(request.getShed());
        LOG.debug("Validando e retornando part...");
        Part part = validateAndReturnPart(request.getPart());
        LOG.debug("Validando orinalPlate...");
        validateOriginalPlate(request);
        LOG.debug("Validando chassis...");
        validateChassis(request);
        LOG.debug("Validando location...");
        validateLocation(request);
        LOG.debug("Validando yearFactory...");
        validateYearFactory(request);
        LOG.debug("Validando yearModel...");
        validateYearModel(request);
        LOG.debug("Validando motorState...");
        validateMotorState(request);
        LOG.debug("Validando chassisState...");
        validateChassisState(request);

        vehicle.setOriginalPlate(request.getOriginalPlate());
        vehicle.setSportingPlate(request.getSportingPlate());
        vehicle.setOwnerName(request.getOwnerName());
        vehicle.setOwnerTaxIdentifier(request.getOwnerTaxIdentifier());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setCategory(request.getCategory());
        vehicle.setColor(request.getColor());
        vehicle.setFuel(request.getFuel());
        vehicle.setYearFactory(request.getFactoryYear());
        vehicle.setYearModel(request.getModelYear());
        vehicle.setChassisState(request.getChassisState());
        vehicle.setChassis(request.getChassis());
        vehicle.setEngineState(request.getMotorState());
        vehicle.setEngine(request.getMotor());
        vehicle.setTheyRenamed(request.getTheyRenamed());
        vehicle.setOriginalPlate(request.getOriginalPlate());

        police.setInsured(request.getInsured());
        police.setFinanced(request.getFinanced());
        police.setStolen(request.getStolen());
        police.setDrugTrafficking(request.getDrugTrafficking());
        police.setMoneyLaundry(request.getMoneyLaundry());
        police.setPerquisite(request.getPerquisite());
        police.setPapillaryExpertise(request.getPapillaryExpertise());
        police.setOwnerIntimate(request.getOwnerIntimate());
        police.setAuthorizedAlienation(request.getAuthorizedAlienation());
        police.setDebits(request.getDebits());

        location.setShed(shed);
        location.setRow(request.getRow());
        location.setColumn(request.getColumn());
        location.setFloor(request.getFloor());

        entrance.setVehicle(vehicle);
        entrance.setPolice(police);
        entrance.setLocation(location);
        entrance.setUsername(getUserNameAuthentication());

        protocol.setAccountableIn(getUserNameAuthentication());
        protocol.setDate(request.getDate());
        protocol.setEventBulletin(request.getEventBulletin());
        protocol.setName(request.getName());
        protocol.setPart(request.getPart());
        protocol.setPoliceInvestigation(request.getPoliceInvestigation());
        protocol.setTaxIdentifier(request.getTaxIdentifier());
        protocol.setEntrance(entrance);
        protocol.setArrestOrgan(part);

        LOG.debug("Gerando protocol...");
        protocol.generateProtocol();
        LOG.debug("Protocol gerado.");

        LOG.debug("Gerando authentication...");
        protocol.generateAuthentication();
        LOG.debug("Authentication gerado.");

        LOG.debug("Salvando entrada...");
        entranceRepository.save(protocol);
        LOG.debug("Entrada salva.");

        return protocol.getProtocol();
    }

    @Override
    public List<ProtocolDTO> find() {
        List<Protocol> protocols = entranceRepository.findAll();

        return protocols.stream().map(protocol -> new ProtocolDTOBuilder().withProtocol(protocol.getProtocol()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchEntranceResponseDTO> search(SearchEntranceRequestDTO request) {

        LOG.info("Dados recebidos na requisição: " + request.toString());

        Predicate predicate = createPredicate(request);

        LOG.info("Predicado utilizado para realizar consulta:" + predicate.toString());

        List<Protocol> protocols = (List<Protocol>) entranceRepository.findAll(predicate);

        return protocols.stream().map(protocol -> {
            String sportingPlate = protocol.getEntrance().getVehicle().getSportingPlate();
            String originalPlate = protocol.getEntrance().getVehicle().getOriginalPlate();
            LocalDate entranceDate = protocol.getDate();
            LocalDate exitDate = protocol.getExit() != null ? protocol.getExit().getDate() : null;
            return new SearchEntranceBuilder().withEntranceDate(entranceDate).withExitDate(exitDate)
                    .withProtocol(protocol.getProtocol()).withSportingPlate(sportingPlate)
                    .withOriginalPlate(originalPlate).build();
        }).collect(Collectors.toList());
    }

    private Predicate createPredicate(SearchEntranceRequestDTO request) {

        String protocol = request.getProtocol();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        QProtocol qProtocol = new QProtocol("protocol");
        BooleanExpression expression;

        if (StringUtils.isBlank(protocol) && startDate == null && endDate == null) {

            return qProtocol.date.goe(LocalDate.now()).or(qProtocol.dateTimeIn.goe(LocalDate.now().atStartOfDay()))
                    .or(qProtocol.exit.dateTimeOut.goe(LocalDate.now().atStartOfDay()))
                    .and(qProtocol.date.loe(LocalDate.now()).or(qProtocol.dateTimeIn.loe(LocalDateTime.now()))
                            .or(qProtocol.exit.dateTimeOut.loe(LocalDateTime.now())));
        }

        expression = qProtocol.id.isNotNull();

        if (StringUtils.isNotBlank(protocol)) {
            expression = expression.and(qProtocol.protocol.containsIgnoreCase(protocol));
        }

        if (startDate != null) {
            expression = expression
                    .and(qProtocol.date.goe(startDate).or(qProtocol.dateTimeIn.goe(startDate.atStartOfDay()))
                            .or(qProtocol.exit.dateTimeOut.goe(startDate.atStartOfDay())));
        }
        if (endDate != null) {
            expression = expression
                    .and(qProtocol.date.loe(endDate).or(qProtocol.dateTimeIn.loe(endDate.atTime(23, 59, 59, 999999999)))
                            .or(qProtocol.exit.dateTimeOut.loe(endDate.atTime(23, 59, 59, 999999999))));
        }

        return expression;
    }

    private void validateOriginalPlate(ProtocolRequestDTO request) {
        List<Protocol> protocols = entranceRepository.findOriginalPlateWithoutExit(request.getOriginalPlate());
        if (!CollectionUtils.isEmpty(protocols)) {
            throw new BusinessException("originalPlate", "Veículo já se encontra no pátio");
        }

    }

    private void validateChassis(ProtocolRequestDTO request) {
        List<Protocol> protocols = entranceRepository.findChassisWithoutExit(request.getChassis());
        if (!CollectionUtils.isEmpty(protocols)) {
            throw new BusinessException("chassis", "Veículo já se encontra no pátio");
        }
    }

    private void validateLocation(ProtocolRequestDTO request) {
        List<Protocol> protocols = entranceRepository.findLocationWithoutExit(request.getShed(), request.getRow(),
                request.getFloor(), request.getColumn());
        if (!CollectionUtils.isEmpty(protocols)) {
            throw new BusinessException("location", "Local informado já está preenchido");
        }
    }

    private Shed validateAndReturnShed(String initials) {
        Optional<Shed> shed = shedRepository.findByInitials(initials);

        if (!shed.isPresent()) {
            throw new BusinessException("shed", "Barracão não encontrado");
        }

        return shed.get();
    }

    private Part validateAndReturnPart(String initials) {
        Optional<Part> part = partRepository.findByInitials(initials);

        if (part.isPresent()) {
            return part.get();
        }

        throw new BusinessException("part", "Órgão de apreensão não encontrado");
    }

    private void validateDate(ProtocolRequestDTO request) {
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new BusinessException("date", "Data da entrada não pode ser maior que data atual");
        }
    }

    private void validateYearFactory(ProtocolRequestDTO request) {
        if (request.getFactoryYear() > LocalDate.now().getYear()) {
            throw new BusinessException("yearFactory", "Ano de fabricação não pode ser maior que ano atual");
        }

    }

    private void validateYearModel(ProtocolRequestDTO request) {
        if (request.getModelYear() > (LocalDate.now().getYear() + 1)) {
            throw new BusinessException("yearModel", "Ano do modelo não pode ser maior que ano atual + 1");
        }
    }

    private void validateMotorState(ProtocolRequestDTO request) {

        List<EngineState> states = Arrays.asList(EngineState.values());

        if (!states.contains(request.getMotorState())) {
            throw new BusinessException("motorState", "Estado de motor inválido");
        }

    }

    private void validateChassisState(ProtocolRequestDTO request) {

        List<ChassisState> states = Arrays.asList(ChassisState.values());

        if (!states.contains(request.getChassisState())) {
            throw new BusinessException("chassis", "Estado de chassis inválido");
        }

    }

    private String getUserNameAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
