package br.com.patiolegal.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.patiolegal.domain.Entrance;
import br.com.patiolegal.domain.Location;
import br.com.patiolegal.domain.Shed;

public final class ProtocolGenerator {

	private static final Logger LOG = LogManager.getLogger(ProtocolGenerator.class);
	
	public String generateProtocolNumber(Entrance entrance) {
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmssSSS");
		String now = LocalDate.now().format(formatterDate);
		String uniqueKey = LocalDateTime.now().format(formatterTime);
		Shed shed = entrance.getLocation().getShed();
		String initials = shed.getInitials();
		Location location = entrance.getLocation();
		String row = location.getRow();
		String column = location.getColumn();
		String floor = location.getFloor();
		String protocol = now + initials + row + column + floor + uniqueKey;
		LOG.debug("Retornando protocolo gerado: " + protocol);
		return protocol;
	}

}
