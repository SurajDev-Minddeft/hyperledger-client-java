ALTER TABLE lc_org_info ADD COLUMN number_of_ica SMALLINT NOT NULL DEFAULT 1;
ALTER TABLE lc_org_info ADD COLUMN number_of_nodes SMALLINT NOT NULL DEFAULT 1;
ALTER TABLE lc_org_info ADD COLUMN tls BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE lc_channel_peers (
	peer_details_id BIGINT NOT NULL REFERENCES lc_peer_info(id),
	channel_details_id BIGINT NOT NULL REFERENCES lc_channel_info(id),
	CONSTRAINT channel_peers_unique UNIQUE(peer_details_id, channel_details_id)
);

CREATE TABLE lc_channel_orderers (
	orderer_details_id BIGINT REFERENCES lc_orderer_info(id),
	channel_details_id BIGINT REFERENCES lc_channel_info(id),
	CONSTRAINT channel_orderers_unique UNIQUE(orderer_details_id, channel_details_id)
);

INSERT INTO lc_channel_peers(peer_details_id, channel_details_id)
SELECT id, channel_info_id FROM lc_peer_info
ON conflict ON CONSTRAINT channel_peers_unique do nothing;

do
$$
DECLARE
  i record;
BEGIN
  FOR i IN 1..(SELECT COUNT(*) FROM lc_orderer_info) loop
	INSERT INTO lc_channel_orderers(channel_details_id) SELECT id FROM lc_channel_info;
	UPDATE lc_channel_orderers co SET orderer_details_id=oi.id
	FROM lc_orderer_info oi
	WHERE co.orderer_details_id IS NULL AND NOT EXISTS (SELECT 1 FROM lc_channel_orderers lco
		WHERE lco.orderer_details_id = oi.id
	);
  END loop;
END;
$$
;

ALTER TABLE lc_channel_orderers ALTER COLUMN orderer_details_id SET NOT NULL;
ALTER TABLE lc_channel_orderers ALTER COLUMN channel_details_id SET NOT NULL;
