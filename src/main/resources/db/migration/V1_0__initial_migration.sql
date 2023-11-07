--
-- Name: lc_account; Type: TABLE;
--
CREATE TABLE lc_account (
    id BIGSERIAL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    name CHARACTER VARYING(255) NOT NULL UNIQUE,
    update_date TIMESTAMP WITH TIME ZONE,
    account_type INTEGER NOT NULL,
    affiliation CHARACTER VARYING(255) NOT NULL,
    encrypted_enrollment TEXT,
    enrollment_secret CHARACTER VARYING(255),
    fabric_account CHARACTER VARYING(255),
    msp_id CHARACTER VARYING(255) NOT NULL,
    organization_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE INDEX lc_account_index ON lc_account(name);
CLUSTER lc_account USING lc_account_index;
ANALYZE lc_account;

--
-- Name: lc_account_memberships; Type: TABLE;
--
CREATE TABLE lc_account_memberships (
    account_id BIGINT NOT NULL,
    memberships_id BIGINT NOT NULL
);

--
-- Name: lc_ca_server_info; Type: TABLE;
--
CREATE TABLE lc_ca_server_info (
    id BIGSERIAL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    name CHARACTER VARYING(255) UNIQUE NOT NULL,
    update_date TIMESTAMP WITH TIME ZONE,
    location CHARACTER VARYING(255) NOT NULL,
    tls_certificate CHARACTER VARYING(2000) NOT NULL,
    org_type CHARACTER VARYING(255) NOT NULL,
    organization_id BIGINT NOT NULL
);

--
-- Name: lc_channel_info; Type: TABLE;
--
CREATE TABLE lc_channel_info (
    id BIGSERIAL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    channel_name CHARACTER VARYING(255) NOT NULL UNIQUE,
    update_date TIMESTAMP WITH TIME ZONE,
    api_key CHARACTER VARYING(255) NOT NULL UNIQUE,
    channel_hex BYTEA,
    serialized_version CHARACTER VARYING(255),
    silo_name CHARACTER VARYING(255) UNIQUE
);

--
-- Name: lc_membership; Type: TABLE;
--
CREATE TABLE lc_membership (
    id BIGSERIAL,
    silo_membership_status CHARACTER VARYING(255) NOT NULL,
    account_id BIGINT NOT NULL,
    channel_info_id BIGINT NOT NULL
);

--
-- Name: lc_network_info; Type: TABLE;
--
CREATE TABLE lc_network_info (
    id BIGSERIAL,
    bootstrap_date TIMESTAMP WITH TIME ZONE NOT NULL,
    version_hash CHARACTER VARYING(255) NOT NULL UNIQUE
);

--
-- Name: lc_orderer_info; Type: TABLE;
--
CREATE TABLE lc_orderer_info (
    id BIGSERIAL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    name CHARACTER VARYING(255) NOT NULL UNIQUE,
    update_date TIMESTAMP WITH TIME ZONE,
    location CHARACTER VARYING(255) NOT NULL,
    tls_certificate CHARACTER VARYING(2000) NOT NULL,
    organization_id BIGINT NOT NULL
);

--
-- Name: lc_org_info; Type: TABLE;
--
CREATE TABLE lc_org_info (
    id BIGSERIAL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    name CHARACTER VARYING(255) NOT NULL UNIQUE,
    update_date TIMESTAMP WITH TIME ZONE,
    root_affiliation CHARACTER VARYING(255) NOT NULL,
    msp_id CHARACTER VARYING(255) NOT NULL,
    org_type CHARACTER VARYING(255) NOT NULL
);

--
-- Name: lc_peer_info; Type: TABLE;
--
CREATE TABLE lc_peer_info (
    id BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    name CHARACTER VARYING(255) NOT NULL UNIQUE,
    update_date TIMESTAMP WITH TIME ZONE,
    location CHARACTER VARYING(255) NOT NULL,
    tls_certificate CHARACTER VARYING(2000) NOT NULL,
    organization_id BIGINT NOT NULL,
    channel_info_id BIGINT
);

--
-- Name: lc_chaincode_info; Type: TABLE;
--
CREATE TABLE lc_chaincode_info (
    id BIGSERIAL PRIMARY KEY,
    lang CHARACTER VARYING(255) NOT NULL,
    name CHARACTER VARYING(255) NOT NULL,
    path CHARACTER VARYING(255) NOT NULL,
    version CHARACTER VARYING(255) NOT NULL,
    UNIQUE(name, version)
);

CREATE TABLE lc_external_chaincode_info (
    id BIGSERIAL PRIMARY KEY,
    chaincode_info_id BIGSERIAL REFERENCES lc_chaincode_info(id) ON DELETE CASCADE,
    package_id CHARACTER VARYING(500) UNIQUE NOT NULL,
    name CHARACTER VARYING(255) UNIQUE NOT NULL
);

--
-- Name: lc_chaincode_upgrade_log; Type: TABLE;
--
CREATE TABLE lc_chaincode_upgrade_log (
    id BIGSERIAL,
    completed_at TIMESTAMP WITH TIME ZONE,
    deploy_type CHARACTER VARYING(255) NOT NULL,
    deploy_version CHARACTER VARYING(255) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    upgrade_status CHARACTER VARYING(255) NOT NULL,
    external_chaincode_info_id BIGSERIAL NOT NULL REFERENCES lc_external_chaincode_info(id) ON DELETE CASCADE,
    channel_info_id BIGINT NOT NULL,
    peer_info_id  BIGSERIAL REFERENCES lc_peer_info(id) ON DELETE CASCADE
);

--
-- Name: lc_account lc_account_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_account ADD CONSTRAINT lc_account_pkey PRIMARY KEY (id);

--
-- Name: lc_ca_server_info lc_ca_server_info_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_ca_server_info ADD CONSTRAINT lc_ca_server_info_pkey PRIMARY KEY (id);

--
-- Name: lc_chaincode_upgrade_log lc_chaincode_upgrade_log_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_chaincode_upgrade_log ADD CONSTRAINT lc_chaincode_upgrade_log_pkey PRIMARY KEY (id);

--
-- Name: lc_channel_info lc_channel_info_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_channel_info ADD CONSTRAINT lc_channel_info_pkey PRIMARY KEY (id);

--
-- Name: lc_membership lc_membership_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_membership ADD CONSTRAINT lc_membership_pkey PRIMARY KEY (id);

--
-- Name: lc_network_info lc_network_info_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_network_info ADD CONSTRAINT lc_network_info_pkey PRIMARY KEY (id);

--
-- Name: lc_orderer_info lc_orderer_info_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_orderer_info ADD CONSTRAINT lc_orderer_info_pkey PRIMARY KEY (id);

--
-- Name: lc_org_info lc_org_info_pkey; Type: CONSTRAINT;
--
ALTER TABLE ONLY lc_org_info ADD CONSTRAINT lc_org_info_pkey PRIMARY KEY (id);

--
-- Name: lc_membership fk10nsy857f34r2k3fbxa451aon; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_membership ADD CONSTRAINT fk10nsy857f34r2k3fbxa451aon FOREIGN KEY (channel_info_id) REFERENCES lc_channel_info(id);

--
-- Name: lc_account fkae1bux8g1f9yagpw0x5gstl60; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_account ADD CONSTRAINT fkae1bux8g1f9yagpw0x5gstl60 FOREIGN KEY (organization_id) REFERENCES lc_org_info(id);

--
-- Name: lc_membership fkco2ythhhc8fnp1tevmvo6sbsj; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_membership ADD CONSTRAINT fkco2ythhhc8fnp1tevmvo6sbsj FOREIGN KEY (account_id) REFERENCES lc_account(id);

--
-- Name: lc_peer_info fkfko80c8nvi71lqqxi09k5yx6x; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_peer_info ADD CONSTRAINT fkfko80c8nvi71lqqxi09k5yx6x FOREIGN KEY (channel_info_id) REFERENCES lc_channel_info(id);

--
-- Name: lc_account_memberships fkfxmrnm6w6htsjjrsoh40cueu4; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_account_memberships ADD CONSTRAINT fkfxmrnm6w6htsjjrsoh40cueu4 FOREIGN KEY (account_id) REFERENCES lc_account(id);

--
-- Name: lc_chaincode_upgrade_log fkhnf1dx9qjsnam3pua8bptltmm; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_chaincode_upgrade_log ADD CONSTRAINT fkhnf1dx9qjsnam3pua8bptltmm FOREIGN KEY (channel_info_id) REFERENCES lc_channel_info(id);

--
-- Name: lc_account_memberships fkm0rt3h2nc9lo4ob05ugiw0sf4; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_account_memberships ADD CONSTRAINT fkm0rt3h2nc9lo4ob05ugiw0sf4 FOREIGN KEY (memberships_id) REFERENCES lc_membership(id);

--
-- Name: lc_peer_info fkm7x7t7twcn7u1s7gp872aiakv; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_peer_info ADD CONSTRAINT fkm7x7t7twcn7u1s7gp872aiakv FOREIGN KEY (organization_id) REFERENCES lc_org_info(id);

--
-- Name: lc_ca_server_info fko38lmwf864rn8tt065pbv78uv; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_ca_server_info ADD CONSTRAINT fko38lmwf864rn8tt065pbv78uv FOREIGN KEY (organization_id) REFERENCES lc_org_info(id);

--
-- Name: lc_orderer_info fkt2f4fulkspabc1q2wcmfy88gx; Type: FK CONSTRAINT;
--
ALTER TABLE ONLY lc_orderer_info ADD CONSTRAINT fkt2f4fulkspabc1q2wcmfy88gx FOREIGN KEY (organization_id) REFERENCES lc_org_info(id);
