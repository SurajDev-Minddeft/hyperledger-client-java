package com.utavi.ledger.api.controller.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

	private final ModelMapper modelMapper;

	public MapperUtil(final ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public <T, R> R mapToType(final T type, final Class<R> cls) {
		return this.modelMapper.map(type, cls);
	}

}
