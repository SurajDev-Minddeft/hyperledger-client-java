package com.utavi.ledger.api.model.accounts;

import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.PrivateKey;

public class LocalEnrollment {
    private String privateKeyPem;
    private String certPem;
    public String getPrivateKeyHex() {
        return this.privateKeyPem;
    }

    public String getCert() {
        return this.certPem;
    }

    public LocalEnrollment() {
    }
    public LocalEnrollment(PrivateKey privateKey, String pem) {
        this.privateKeyPem =  Identities.toPemString(privateKey);
        this.certPem = pem;
    }

    public Enrollment toEnrollment() {
        LocalEnrollment localEnrollment = this;
        return new Enrollment() {
            @Override
            public PrivateKey getKey() {
                try {
                    return Identities.readPrivateKey(new StringReader(localEnrollment.getPrivateKeyHex()));
                } catch (IOException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public String getCert() {
                return localEnrollment.getCert();
            }
        };
    }
}
