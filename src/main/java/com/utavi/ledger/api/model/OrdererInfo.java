package com.utavi.ledger.api.model;

import com.utavi.ledger.api.dto.OrdererInfoDto;
import com.utavi.ledger.api.service.manage.NetworkNodeConfigManager.NodeConfigWrapper;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.modelmapper.PropertyMap;

@Entity
@Table(name = "lc_orderer_info")
public class OrdererInfo extends BaseOrganizationComponent {

	public OrdererInfo() {
	}

	public OrdererInfo(final NodeConfigWrapper wrapper) {
		super(wrapper.getName(), wrapper.getLocation(), wrapper.getOrgInfo(), wrapper.getTlsCertificate());
	}

	@ManyToMany(mappedBy = "channelOrderers")
	private Set<ChannelDetails> ordererChannels;

	public Set<ChannelDetails> getOrdererChannels() {
		return this.ordererChannels;
	}

	public static PropertyMap<OrdererInfo, OrdererInfoDto> mappedFields = new PropertyMap<OrdererInfo, OrdererInfoDto>() {
		protected void configure() {
			map().setLocation(this.source.getLocation());
			map().setOrdererName(this.source.getName());
		}
	};

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("orgName", getOrgName())
				.append("location", getLocation())
				.append("id", getId())
				.append("name", getName())
				.toString();
	}
}
