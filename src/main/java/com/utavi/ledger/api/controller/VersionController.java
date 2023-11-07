
package com.utavi.ledger.api.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/version")
@RestController
public class VersionController {

    private final String appVersion;

    public VersionController(@Value("${application.version}") String appVersion) {
        this.appVersion = appVersion;
    }

    //todo wujek uzywane przez utavi-user-service. zwraca wersje
    @GetMapping
    public String getVersion() {
        return appVersion;
    }
}
