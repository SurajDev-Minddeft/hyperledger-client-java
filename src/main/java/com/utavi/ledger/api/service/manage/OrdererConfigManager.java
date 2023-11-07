package com.utavi.ledger.api.service.manage;

import com.utavi.ledger.api.model.OrdererInfo;
import com.utavi.ledger.api.repository.OrdererDetailsRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component("ordererConfigManager")
public class OrdererConfigManager extends NetworkNodeConfigManager<OrdererInfo> {

    public OrdererConfigManager(final OrdererDetailsRepository repository) {
        super(repository);
    }
    @Override
    public void save(Set<NodeConfigWrapper> nodeConfigWrappers) {
        Set<OrdererInfo> orderers = nodeConfigWrappers
            .stream()
            .map(OrdererInfo::new)
            .collect(toSet());
        this.repository.saveAll(orderers);
    }

}
