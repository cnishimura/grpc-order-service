package com.hacom.order.smpp;


import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.hacom.order.metrics.OrderMetricsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.type.Address;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class SmppClient {


    public void sendSms(String phoneNumber, String message) {
        DefaultSmppClient client = new DefaultSmppClient();
        SmppSessionConfiguration config = new SmppSessionConfiguration();
        config.setType(SmppBindType.TRANSCEIVER);
        config.setHost("localhost");
        config.setPort(2776);
        config.setSystemId("test");
        config.setPassword("test");
        config.setSystemType("cp");
        config.getLoggingOptions().setLogBytes(true);
        config.setName("client-session");

        try {
            SmppSession session = client.bind(config);

            SubmitSm submit = new SubmitSm();
            submit.setSourceAddress(new Address((byte)0x03, (byte)0x00, "SENDER"));
            submit.setDestAddress(new Address((byte)0x01, (byte)0x01, phoneNumber));
            submit.setShortMessage(message.getBytes());

            SubmitSmResp response = session.submit(submit, 10000);
            log.info("Message submitted, messageId: " + response.getMessageId());

            session.unbind(5000);
            session.destroy();
            client.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
