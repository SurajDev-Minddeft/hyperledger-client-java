package com.utavi.ledger.api.model;

import com.utavi.ledger.api.exceptions.BusinessException;
import com.utavi.ledger.api.repository.GenericRepository;

public final class EntityUtils {

	public static <T extends GenericRepository> void throwIfExists(final T t, final String name) {
		t.findByName(name).ifPresent(entity -> {
			throw new BusinessException(String.format("%s with name %s does exist.", entity.getClass().getTypeName(), name));
		});
	}

}
