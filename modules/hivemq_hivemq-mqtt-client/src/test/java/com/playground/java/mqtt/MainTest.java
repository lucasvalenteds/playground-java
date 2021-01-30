package com.playground.java.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.reactor.Mqtt5ReactorClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class MainTest {

    @Container
    public static final GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("eclipse-mosquitto:1.6"))
        .withExposedPorts(1883)
        .waitingFor(Wait.forListeningPort());

    private static final String MQTT_TOPIC = "topic";

    private static final MqttQos MQTT_QUALITY_OF_SERVICE = MqttQos.AT_LEAST_ONCE;

    private Mqtt5ReactorClient client;

    @BeforeEach
    public void beforeEach() {
        client = Mqtt5ReactorClient.from(
            Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(container.getHost())
                .serverPort(container.getFirstMappedPort())
                .build()
        );

        StepVerifier.create(client.connect())
            .assertNext(it -> assertEquals(Mqtt5ConnAckReasonCode.SUCCESS, it.getReasonCode()))
            .verifyComplete();
    }

    @AfterEach
    public void afterEach() {
        StepVerifier.create(client.disconnect())
            .verifyComplete();
    }

    @DisplayName("Publishing messages to a topic")
    @RepeatedTest(5)
    @Timeout(5)
    void testPublishingMessages() {
        Flux<String> subscription = Flux.just(1, 2, 3)
            .map(number -> "Number " + number)
            .map(payload ->
                Mqtt5Publish.builder()
                    .topic(MQTT_TOPIC)
                    .qos(MQTT_QUALITY_OF_SERVICE)
                    .payload(ByteBuffer.wrap(payload.getBytes()))
                    .build()
            )
            .delayElements(Duration.ofMillis(500))
            .flatMap(message -> client.publish(Mono.just(message)))
            .flatMap(this::payloadToString);

        StepVerifier.create(subscription)
            .assertNext(it -> assertEquals("Number 1", it))
            .assertNext(it -> assertEquals("Number 2", it))
            .assertNext(it -> assertEquals("Number 3", it))
            .verifyComplete();
    }

    @DisplayName("Consuming messages published to a topic")
    @RepeatedTest(5)
    @Timeout(5)
    void testConsumingMessages() {
        publishRandomMessageInBackground(Flux.just("a", "b", "c")).subscribe();

        Flux<String> subscription = client.subscribePublishesWith()
            .topicFilter(MQTT_TOPIC)
            .qos(MQTT_QUALITY_OF_SERVICE)
            .applySubscribe()
            .share()
            .flatMap(this::payloadToString)
            .take(3);

        StepVerifier.create(subscription)
            .assertNext(it -> assertEquals("A", it))
            .assertNext(it -> assertEquals("B", it))
            .assertNext(it -> assertEquals("C", it))
            .verifyComplete();
    }

    private Flux<Mqtt5PublishResult> publishRandomMessageInBackground(Flux<String> messages) {
        return Flux.from(messages)
            .map(String::toUpperCase)
            .map(it ->
                Mqtt5Publish.builder()
                    .topic(MQTT_TOPIC)
                    .payload(ByteBuffer.wrap(it.getBytes()))
                    .build()
            )
            .delayElements(Duration.ofMillis(500))
            .flatMap(it -> client.publish(Mono.just(it)))
            .subscribeOn(Schedulers.fromExecutor(Executors.newSingleThreadExecutor()));
    }

    private Flux<String> payloadToString(Mqtt5PublishResult publish) {
        return Flux.just(publish)
            .map(Mqtt5PublishResult::getPublish)
            .map(Mqtt5Publish::getPayloadAsBytes)
            .map(String::new);
    }

    private Flux<String> payloadToString(Mqtt5Publish publish) {
        return Flux.just(publish)
            .map(Mqtt5Publish::getPayloadAsBytes)
            .map(String::new);
    }
}
