package mail2clients.logger.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import mail2clients.logger.entities.EmailLogger;


class LoggerServiceTest {

	@Test
	void testGetEmailBySerial() {
        LoggerService loggerService = new LoggerService();
        List<EmailLogger> listEmaiLogger = loggerService.getEmailBySerial(null);
		int listSize = listEmaiLogger.size();
		assertEquals(0,listSize,"Esperaba una lista de tamaño cero");
	}

}
