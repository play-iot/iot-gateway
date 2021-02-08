package io.github.zero88.qwe.iot.connector.bacnet.internal.ack;

import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public final class NoAck extends AcknowledgementService {

    @Override
    public byte getChoiceId() {
        return 0;
    }

    @Override
    public void write(ByteQueue queue) {

    }

}
