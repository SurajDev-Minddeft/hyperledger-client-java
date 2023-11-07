package com.utavi.ledger.api.util;

import com.utavi.ledger.api.exceptions.EntityNotFoundException;
import java.util.function.Supplier;

public final class UtilFunctions {

	public static TriFunction<String, String, Object, Supplier<? extends RuntimeException>> notFoundBuilder =
			(entity, key, value) -> () -> new EntityNotFoundException(String.format("%s with %s=%s not found.", entity, key, value));
}
