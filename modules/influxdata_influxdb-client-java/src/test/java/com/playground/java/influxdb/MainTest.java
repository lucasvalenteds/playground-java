package com.playground.java.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
class MainTest {

    @Container
    private final GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("quay.io/influxdb/influxdb:2.0.0-beta"))
        .withExposedPorts(9999)
        .waitingFor(new WaitAllStrategy()
            .withStrategy(Wait.forHttp("/ping").forStatusCode(200))
            .withStrategy(Wait.forListeningPort())
        );

    private final Random random = new Random();
    private final Integer flushIntervalInMilliseconds = 1;
    private final String bucket = "noise";

    private InfluxDBClient client;
    private WriteApi writeApi;
    private QueryApi queryApi;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        String organization = "default";
        String username = "username";
        String password = "password";

        this.container.execInContainer(
            "influx", "setup",
            "--org", organization,
            "--bucket", bucket,
            "--username", username,
            "--password", password,
            "--force"
        );

        this.client = InfluxDBClientFactory.create(
            InfluxDBClientOptions.builder()
                .url("http://" + this.container.getHost() + ":" + this.container.getFirstMappedPort())
                .authenticate(username, password.toCharArray())
                .org(organization)
                .bucket(bucket)
                .build()
        );

        this.writeApi = client.getWriteApi(
            WriteOptions.builder()
                .flushInterval(flushIntervalInMilliseconds)
                .batchSize(1)
                .build()
        );

        this.queryApi = client.getQueryApi();
    }

    @AfterEach
    public void afterEach() {
        this.writeApi.close();
        this.client.close();
    }

    @RepeatedTest(5)
    void testWritingAndReadingMeasurements() {
        writeApi.writePoint(createPointFromLocation(createRandomLocation(random)));
        waitDatabaseFlush(flushIntervalInMilliseconds);

        List<FluxTable> locations = queryApi.query(
            String.format("from(bucket:\"%s\") |> range(start: 0)", bucket)
        );

        assertNotEquals(0L, locations.size());
    }

    private Point createPointFromLocation(Location location) {
        return Point.measurement("device-location")
            .addTag("device-id", location.getDeviceId())
            .addField("latitude", location.getLatitude())
            .addField("longitude", location.getLongitude())
            .time(location.getTimestamp(), WritePrecision.S);
    }

    private Location createRandomLocation(Random random) {
        Location location = new Location();
        location.setDeviceId(UUID.randomUUID().toString());
        location.setLatitude(random.nextDouble());
        location.setLongitude(random.nextDouble());
        location.setTimestamp(Instant.now());
        return location;
    }

    private void waitDatabaseFlush(Integer timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException exception) {
            fail("Could not block thread.");
        }
    }
}
