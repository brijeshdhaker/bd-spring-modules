package org.examples.sb.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@ConfigurationProperties("kafka")
public class KafkaCustomProperties {
    private String transactionTopic;
}
