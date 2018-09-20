package br.com.patiolegal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import br.com.patiolegal.domain.ChassisState;
import br.com.patiolegal.domain.EngineState;
import br.com.patiolegal.dto.ProtocolDTO.ProtocolDTOBuilder;

public class ProtocolDTOTest {

    @Test
    public void shouldBuilderWithPart() {
        String part = "Part";

        ProtocolDTO dto = new ProtocolDTOBuilder().withPart(part).build();

        Assert.assertEquals(dto.getPart(), part);

    }

    @Test
    public void shouldBuilderWithProtocol() {
        String protocol = "PROTOCOLO20180522152256";

        ProtocolDTO dto = new ProtocolDTOBuilder().withProtocol(protocol).build();

        Assert.assertEquals(dto.getProtocol(), protocol);
    }

    @Test
    public void shouldBuilderWithDate() {
        LocalDate date = LocalDate.now();

        ProtocolDTO dto = new ProtocolDTOBuilder().withDate(date).build();

        Assert.assertEquals(dto.getDate(), date);
    }

    @Test
    public void shouldBuilderWithDateTimeIn() {
        LocalDateTime dateTimeIn = LocalDateTime.now();

        ProtocolDTO dto = new ProtocolDTOBuilder().withDateTimeIn(dateTimeIn).build();

        Assert.assertEquals(dto.getDateTimeIn(), dateTimeIn);

    }

    @Test
    public void shouldBuilderWithDateTimeOut() {
        LocalDateTime dateTimeOut = LocalDateTime.of(2018, 11, 22, 18, 0);

        ProtocolDTO dto = new ProtocolDTOBuilder().withDateTimeOut(dateTimeOut).build();

        Assert.assertEquals(dto.getDateTimeOut(), dateTimeOut);
    }

    @Test
    public void shouldBuilderWithPoliceInvestigation() {
        String policeInvestigation = "Investigação Policial";

        ProtocolDTO dto = new ProtocolDTOBuilder().withPoliceInvestigation(policeInvestigation).build();

        Assert.assertEquals(dto.getPoliceInvestigation(), policeInvestigation);
    }

    @Test
    public void shouldBuilderWithEventBulletin() {
        String eventBulletin = "B.O";

        ProtocolDTO dto = new ProtocolDTOBuilder().withEventBulletin(eventBulletin).build();

        Assert.assertEquals(dto.getEventBulletin(), eventBulletin);
    }

    @Test
    public void shouldBuilderWithTaxId() {
        String taxId = "08154878526";

        ProtocolDTO dto = new ProtocolDTOBuilder().withTaxId(taxId).build();

        Assert.assertEquals(dto.getTaxId(), taxId);
    }

    @Test
    public void shouldBuilderWithName() {
        String name = "Bolota";

        ProtocolDTO dto = new ProtocolDTOBuilder().withName(name).build();

        Assert.assertEquals(dto.getName(), name);
    }

    @Test
    public void shouldBuilderWithTheyRenamed() {
        String theyRenamed = "45487894451259";

        ProtocolDTO dto = new ProtocolDTOBuilder().withTheyRenamed(theyRenamed).build();

        Assert.assertEquals(dto.getTheyRenamed(), theyRenamed);
    }

    @Test
    public void shouldBuilderWithOwnerName() {
        String ownerName = "Josélito";

        ProtocolDTO dto = new ProtocolDTOBuilder().withOwnerName(ownerName).build();

        Assert.assertEquals(dto.getOwnerName(), ownerName);
    }

    @Test
    public void shouldBuilderWithOwnerTaxIdentifier() {
        String ownerTaxIdentifier = "João";

        ProtocolDTO dto = new ProtocolDTOBuilder().withOwnerTaxIdentifier(ownerTaxIdentifier).build();

        Assert.assertEquals(dto.getOwnerTaxIdentifier(), ownerTaxIdentifier);
    }

    @Test
    public void shouldBuilderWithBrand() {
        String brand = "Ford";

        ProtocolDTO dto = new ProtocolDTOBuilder().withBrand(brand).build();

        Assert.assertEquals(dto.getBrand(), brand);
    }

    @Test
    public void shouldBuilderWithModel() {
        String model = "KA";

        ProtocolDTO dto = new ProtocolDTOBuilder().withModel(model).build();

        Assert.assertEquals(dto.getModel(), model);
    }

    @Test
    public void shouldBuilderWithCategory() {
        String category = "sei lá";

        ProtocolDTO dto = new ProtocolDTOBuilder().withCategory(category).build();

        Assert.assertEquals(dto.getCategory(), category);
    }

    @Test
    public void shouldBuilderWithColor() {
        String color = "azul";

        ProtocolDTO dto = new ProtocolDTOBuilder().withColor(color).build();

        Assert.assertEquals(dto.getColor(), color);
    }

    @Test
    public void shouldBuilderWithFuel() {
        String fuel = "Flex";

        ProtocolDTO dto = new ProtocolDTOBuilder().withFuel(fuel).build();

        Assert.assertEquals(dto.getFuel(), fuel);
    }

    @Test
    public void shouldBuilderWithYearFactory() {
        Integer yearFactory = 2015;

        ProtocolDTO dto = new ProtocolDTOBuilder().withYearFactory(yearFactory).build();

        Assert.assertEquals(dto.getYearFactory(), yearFactory);
    }

    @Test
    public void shouldBuilderWithYearModel() {
        Integer yearModel = 2016;

        ProtocolDTO dto = new ProtocolDTOBuilder().withYearModel(yearModel).build();

        Assert.assertEquals(dto.getYearModel(), yearModel);
    }

    @Test
    public void shouldBuilderWithSportingPlate() {
        String sportingPlate = "AAS-9858";

        ProtocolDTO dto = new ProtocolDTOBuilder().withSportingPlate(sportingPlate).build();

        Assert.assertEquals(dto.getSportingPlate(), sportingPlate);
    }

    @Test
    public void shouldBuilderWithOriginalPlate() {
        String originalPlate = "AAS-8858";

        ProtocolDTO dto = new ProtocolDTOBuilder().withOriginalPlate(originalPlate).build();

        Assert.assertEquals(dto.getOriginalPlate(), originalPlate);
    }

    @Test
    public void shouldBuilderWithChassisState() {
        ChassisState chassisState = ChassisState.ADULTERADO;

        ProtocolDTO dto = new ProtocolDTOBuilder().withChassisState(chassisState).build();

        Assert.assertEquals(dto.getChassisState(), chassisState);
    }

    @Test
    public void shouldBuilderWithChassis() {
        String chassis = "ASDHR6587GLOTRDF1SD";

        ProtocolDTO dto = new ProtocolDTOBuilder().withChassis(chassis).build();

        Assert.assertEquals(dto.getChassis(), chassis);
    }

    @Test
    public void shouldBuilderWithMotorState() {
        EngineState motorState = EngineState.ADULTERADO;

        ProtocolDTO dto = new ProtocolDTOBuilder().withMotorState(motorState).build();

        Assert.assertEquals(dto.getMotorState(), motorState);
    }

    @Test
    public void shouldBuilderWithMotor() {
        String motor = "V8";

        ProtocolDTO dto = new ProtocolDTOBuilder().withMotor(motor).build();

        Assert.assertEquals(dto.getMotor(), motor);
    }

    @Test
    public void shouldBuilderWithInsured() {
        Boolean insured = true;

        ProtocolDTO dto = new ProtocolDTOBuilder().withInsured(insured).build();

        Assert.assertEquals(dto.getInsured(), insured);
    }

    @Test
    public void shouldBuilderWithFinanced() {
        Boolean financed = false;

        ProtocolDTO dto = new ProtocolDTOBuilder().withFinanced(financed).build();

        Assert.assertEquals(dto.getFinanced(), financed);
    }

    @Test
    public void shouldBuilderWithStolen() {
        Boolean stolen = true;

        ProtocolDTO dto = new ProtocolDTOBuilder().withStolen(stolen).build();

        Assert.assertEquals(dto.getStolen(), stolen);
    }

    @Test
    public void shouldBuilderWithDrugTrafficking() {
        Boolean drugTrafficking = false;

        ProtocolDTO dto = new ProtocolDTOBuilder().withDrugTrafficking(drugTrafficking).build();

        Assert.assertEquals(dto.getDrugTrafficking(), drugTrafficking);
    }

    @Test
    public void shouldBuilderWithMoneyLaundry() {
        Boolean moneyLaundry = true;

        ProtocolDTO dto = new ProtocolDTOBuilder().withMoneyLaundry(moneyLaundry).build();

        Assert.assertEquals(dto.getMoneyLaundry(), moneyLaundry);
    }

    @Test
    public void shouldBuilderWithPerquisite() {
        Boolean perquisite = true;

        ProtocolDTO dto = new ProtocolDTOBuilder().withPerquisite(perquisite).build();

        Assert.assertEquals(dto.getPerquisite(), perquisite);
    }

    @Test
    public void shouldBuilderWithPapillaryExpertise() {
        Boolean papillaryExpertise = true;

        ProtocolDTO dto = new ProtocolDTOBuilder().withPapillaryExpertise(papillaryExpertise).build();

        Assert.assertEquals(dto.getPapillaryExpertise(), papillaryExpertise);
    }

    @Test
    public void shouldBuilderWithOwnerIntimate() {
        Boolean ownerIntimate = false;

        ProtocolDTO dto = new ProtocolDTOBuilder().withOwnerIntimate(ownerIntimate).build();

        Assert.assertEquals(dto.getOwnerIntimate(), ownerIntimate);
    }

    @Test
    public void shouldBuilderWithAuthorizedAlienation() {
        Boolean authorizedAlienation = false;

        ProtocolDTO dto = new ProtocolDTOBuilder().withAuthorizedAlienation(authorizedAlienation).build();

        Assert.assertEquals(dto.getAuthorizedAlienation(), authorizedAlienation);
    }

    @Test
    public void shouldBuilderWithDebits() {
        Boolean debits = true;

        ProtocolDTO dto = new ProtocolDTOBuilder().withDebits(debits).build();

        Assert.assertEquals(dto.getDebits(), debits);
    }

    @Test
    public void shouldBuilderWithShed() {
        String shed = "Barracão fundos";

        ProtocolDTO dto = new ProtocolDTOBuilder().withShed(shed).build();

        Assert.assertEquals(dto.getShed(), shed);
    }

    @Test
    public void shouldBuilderWithRow() {
        String row = "8";

        ProtocolDTO dto = new ProtocolDTOBuilder().withRow(row).build();

        Assert.assertEquals(dto.getRow(), row);
    }

    @Test
    public void shouldBuilderWithColumn() {
        String column = "F";

        ProtocolDTO dto = new ProtocolDTOBuilder().withColumn(column).build();

        Assert.assertEquals(dto.getColumn(), column);
    }

    @Test
    public void shouldBuilderWithFloor() {
        String floor = "G";

        ProtocolDTO dto = new ProtocolDTOBuilder().withFloor(floor).build();

        Assert.assertEquals(dto.getFloor(), floor);
    }

    @Test
    public void shouldBuilderWithAccountableIn() {
        String accountableIn = "JOÃO";

        ProtocolDTO dto = new ProtocolDTOBuilder().withAccountableIn(accountableIn).build();

        Assert.assertEquals(dto.getAccountableIn(), accountableIn);
    }

    @Test
    public void shouldBuilderWithAccountableOut() {
        String accountableOut = "JOÃO";

        ProtocolDTO dto = new ProtocolDTOBuilder().withAccountableOut(accountableOut).build();

        Assert.assertEquals(dto.getAccountableOut(), accountableOut);
    }

    @Test
    public void shouldBuilderWithAuthentication() {
        String authentication = "89asdf89sdftn2nh65d23sdyyy31";

        ProtocolDTO dto = new ProtocolDTOBuilder().withAuthentication(authentication).build();

        Assert.assertEquals(dto.getAuthentication(), authentication);
    }

    @Test
    public void shouldBuilderWithAmountSeals() {
        Integer amountSeals = 5;

        ProtocolDTO dto = new ProtocolDTOBuilder().withAmountSeals(amountSeals).build();

        Assert.assertEquals(dto.getAmountSeals(), amountSeals);
    }

}
