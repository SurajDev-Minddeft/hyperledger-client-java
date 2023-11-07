package com.utavi.ledger.api.exceptions.handler;

import com.utavi.ledger.api.exceptions.BusinessException;
import com.utavi.ledger.api.exceptions.EntityNotFoundException;
import com.utavi.ledger.api.exceptions.ProposalExceptionBadRequest;
import com.utavi.ledger.api.exceptions.ProposalExceptionInternalServerError;
import com.utavi.ledger.api.exceptions.ProposalExceptionNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({BusinessException.class, ProposalExceptionBadRequest.class})
	public ExceptionResponseBody businessExceptionHandler(final Exception e) {
		return new ExceptionResponseBody(e.getMessage());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({EntityNotFoundException.class, ProposalExceptionNotFound.class})
	public ExceptionResponseBody entityNotFoundExceptionHandler(final Exception e) {
		LOGGER.error("Error:", e);
		return new ExceptionResponseBody(e.getMessage());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class, ProposalExceptionInternalServerError.class})
	public ExceptionResponseBody exceptionHandler(final Exception e) {
		LOGGER.error("Error:", e);
		return new ExceptionResponseBody(e.getMessage());
	}
}
