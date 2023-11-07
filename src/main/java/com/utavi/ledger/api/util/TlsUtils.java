package com.utavi.ledger.api.util;

import java.util.Map;
import java.util.Properties;

public final class TlsUtils {

	public static Properties makeCAServerTlsProperties(final byte[] crtBytes, final String name) {
		return Builder.newInstance()
				.setPemBytes(crtBytes)
				.setHostnameOverride(name)
				.setCustomProperty("allowAllHostNames", "true")
				.build();
	}

	private static class Builder {

		private final Properties properties;

		private Builder() {
			this.properties = new Properties();
			this.properties.put("sslProvider", "openSSL");
			this.properties.put("negotiationType", "TLS");
		}

		static Builder newInstance() {
			return new Builder();
		}

		Builder setPemBytes(final byte[] pemBytes) {
			this.properties.put("pemBytes", pemBytes);
			return this;
		}

		Builder setHostnameOverride(final String name) {
			this.properties.put("hostnameOverride", name);
			this.properties.put("ssl-target-name-override", name);
			return this;
		}

		Builder setClientCertBytes(final byte[] certBytes) {
			this.properties.put("clientCertBytes", certBytes);
			return this;
		}

		Builder setClientKeyBytes(final byte[] keyBytes) {
			this.properties.put("clientKeyBytes", keyBytes);
			return this;
		}

		Builder setCustomProperty(final String key, final Object value) {
			this.properties.put(key, value);
			return this;
		}

		public Properties build() {
			return this.properties;
		}
	}

}
