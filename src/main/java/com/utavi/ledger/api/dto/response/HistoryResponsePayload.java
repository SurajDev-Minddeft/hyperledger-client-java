package com.utavi.ledger.api.dto.response;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class HistoryResponsePayload {

	private String key;
	private List<HistoryEntryDto> historyEntries;

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public List<HistoryEntryDto> getHistoryEntries() {
		return this.historyEntries;
	}

	public void setHistoryEntries(final List<HistoryEntryDto> historyEntries) {
		this.historyEntries = historyEntries;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("key", this.key)
				.append("historyEntries", this.historyEntries)
				.toString();
	}
}
