package net.finmath.modelling.descriptor.xmlparser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.finmath.modelling.ProductDescriptor;
import net.finmath.modelling.descriptor.InterestRateSwapLegProductDescriptor;
import net.finmath.modelling.descriptor.InterestRateSwapProductDescriptor;
import net.finmath.modelling.descriptor.ScheduleDescriptor;
import net.finmath.time.ScheduleGenerator;
import net.finmath.time.ScheduleGenerator.DaycountConvention;
import net.finmath.time.ScheduleGenerator.Frequency;
import net.finmath.time.ScheduleGenerator.ShortPeriodConvention;
import net.finmath.time.businessdaycalendar.BusinessdayCalendar;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarExcludingTARGETHolidays;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarInterface.DateRollConvention;

/**
 * @author Christian Fries
 * @author Roland Bachl
 *
 */
public class FPMLParser implements XMLParser {
	
	private final String homePartyId;
	private final String discountCurveName;
	
	private BusinessdayCalendar businessdayCalendar = new BusinessdayCalendarExcludingTARGETHolidays();
	private ShortPeriodConvention shortPeriodConvention= ScheduleGenerator.ShortPeriodConvention.LAST;
	
	public FPMLParser(String homePartyId, String discountCurveName, LocalDate referenceDate) {
		super();
		this.homePartyId = homePartyId;
		this.discountCurveName = discountCurveName;
	}

	public ProductDescriptor getProductDescriptor(File file) throws SAXException, IOException, ParserConfigurationException {
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file); 
		doc.getDocumentElement().normalize();
		
		//Check compatibility and assign proper parser
		if(! doc.getDocumentElement().getNodeName().equalsIgnoreCase("dataDocument")) {
			throw new IllegalArgumentException("This parser is meant for XML of type dataDocument, according to FpML 5, but file is "+doc.getDocumentElement().getNodeName()+".");
		}
		
		if(! doc.getDocumentElement().getAttribute("fpmlVersion").split("-")[0].equals("5")) {
			throw new IllegalArgumentException("This parser is meant for FpML of version 5.*, file is version "+ doc.getDocumentElement().getAttribute("fpmlVersion"));
		}
		
		//Isolate trade node
		Element trade = null;
		String tradeName = null;
		
		NodeList tradeWrapper = doc.getElementsByTagName("trade").item(0).getChildNodes();
		for(int index = 0; index < tradeWrapper.getLength(); index++) {
			if(tradeWrapper.item(index).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if(tradeWrapper.item(index).getNodeName().equalsIgnoreCase("tradeHeader")) {
				continue;
			}
			trade = (Element) tradeWrapper.item(index);
			tradeName		= trade.getNodeName().toUpperCase();
			break;
		}

		
		switch (tradeName) {
		case "SWAP" : return getSwapProductDescriptor(trade);
		default: throw new IllegalArgumentException("This FpML parser is not set up to process trades of type "+tradeName+".");
		}
		
	}
	
	
	private ProductDescriptor getSwapProductDescriptor(Element trade) {
		
		InterestRateSwapLegProductDescriptor legReceiver = null;
		InterestRateSwapLegProductDescriptor legPayer = null;
		
		NodeList legs = trade.getElementsByTagName("swapStream");
		for(int legIndex = 0; legIndex < legs.getLength(); legIndex++) {
			Element leg = (Element) legs.item(legIndex);
			
			boolean isPayer = leg.getElementsByTagName("payerPartyReference").item(0).getAttributes().getNamedItem("href").getNodeValue().equals(homePartyId);
			
			if(isPayer) {
				legPayer = getSwapLegProductDescriptor(leg);
			} else {
				legReceiver = getSwapLegProductDescriptor(leg);
			}
		}
		
		return new InterestRateSwapProductDescriptor(legReceiver, legPayer);
	}

	private InterestRateSwapLegProductDescriptor getSwapLegProductDescriptor(Element leg) {
		
		//is this a fixed rate leg?
		boolean isFixed = leg.getElementsByTagName("calculationPeriodDates").item(0).getAttributes().getNamedItem("id").getTextContent().equalsIgnoreCase("fixedCalcPeriodDates");
		
		//get start and end dates of contract
		LocalDate startDate		= LocalDate.parse(((Element) leg.getElementsByTagName("effectiveDate").item(0)).getElementsByTagName("unadjustedDate").item(0).getTextContent());
		LocalDate maturityDate	= LocalDate.parse(((Element) leg.getElementsByTagName("terminationDate").item(0)).getElementsByTagName("unadjustedDate").item(0).getTextContent());
		
		//determine fixing/payment offset if available
		int fixingOffsetDays = 0;
		if(leg.getElementsByTagName("fixingDates").getLength() > 0) {
			fixingOffsetDays = Integer.parseInt(((Element) leg.getElementsByTagName("fixingDates").item(0)).getElementsByTagName("periodMultiplier").item(0).getTextContent());
		}
		int paymentOffsetDays = 0;
		if(leg.getElementsByTagName("paymentDaysOffset").getLength() > 0) {
			paymentOffsetDays = Integer.parseInt(((Element) leg.getElementsByTagName("paymentDaysOffset").item(0)).getElementsByTagName("periodMultiplier").item(0).getTextContent());
		}
		
		//Crop xml date roll convention to match internal format
		String xmlInput = ((Element) leg.getElementsByTagName("calculationPeriodDatesAdjustments").item(0)).getElementsByTagName("businessDayConvention").item(0).getTextContent();
		xmlInput = xmlInput.replaceAll("ING", "");
		DateRollConvention dateRollConvention = DateRollConvention.getEnum(xmlInput);
		
		//get daycount convention
		DaycountConvention daycountConvention = DaycountConvention.getEnum(leg.getElementsByTagName("dayCountFraction").item(0).getTextContent());
		
		//get trade frequency
		Frequency frequency = null;
		Element calcNode = (Element) leg.getElementsByTagName("calculationPeriodFrequency").item(0);
		int multiplier = Integer.parseInt(calcNode.getElementsByTagName("periodMultiplier").item(0).getTextContent());
		switch(calcNode.getElementsByTagName("period").item(0).getTextContent().toUpperCase()) {
		case "D" : if(multiplier == 1) {frequency = Frequency.DAILY;} break;
		case "Y" : if(multiplier == 1) {frequency = Frequency.ANNUAL;} break;
		case "M" : switch(multiplier) {
		case 1 : frequency = Frequency.MONTHLY;
		case 3 : frequency = Frequency.QUARTERLY;
		case 6 : frequency = Frequency.SEMIANNUAL;
		}
		}

		//build schedule
		ScheduleDescriptor schedule = new ScheduleDescriptor(startDate, maturityDate, frequency, daycountConvention, shortPeriodConvention,
				dateRollConvention, businessdayCalendar, fixingOffsetDays, paymentOffsetDays);
		
		// get notional
		double notional = Double.parseDouble(((Element) leg.getElementsByTagName("notionalSchedule").item(0)).getElementsByTagName("initialValue").item(0).getTextContent());
		
		// get fixed rate and forward curve if applicable
		double spread = 0;
		String forwardCurveName = "";
		if(isFixed) {
			spread = Double.parseDouble(((Element) leg.getElementsByTagName("fixedRateSchedule").item(0)).getElementsByTagName("initialValue").item(0).getTextContent());
		} else {
			forwardCurveName = leg.getElementsByTagName("floatingRateIndex").item(0).getTextContent();
		}
		
		return new InterestRateSwapLegProductDescriptor(forwardCurveName, discountCurveName, schedule, notional, spread, false);
	}
	
}
