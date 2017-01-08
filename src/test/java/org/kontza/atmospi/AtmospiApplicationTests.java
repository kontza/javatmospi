package org.kontza.atmospi;

import java.util.HashMap;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = AtmospiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AtmospiApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(AtmospiApplicationTests.class.getName());

    // These values are from the log.sqlite -file in this repo.
    private final String latestDeviceLabel = "28-000003ea01f5";
    private final long latestTimestamp = 1477576501000L;
    private final double latestTemperature = 8.94;
    private final double alphaTemperature = 21.5;
    private final double omegaTemperature = 21.5;
    private final String onlyDeviceId = "1";
    private final long alpha = 1460767801000L;
    private final long omega = 1460770501000L;
    private final int rangeCount = 10;
    private final int totalCount = 55078;
    private final HashMap<String, Long> parms = new HashMap<>();

    @Autowired
    private AtmospiSettings settings;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void settingsLoaded() {
        assertNotNull(settings);
    }

    @Test
    public void verifySettings() {
        final ResponseEntity<Settings> entity;
        entity = restTemplate.getForEntity("/settings", Settings.class);
        logger.info("Entity status code: {}", entity.getStatusCodeValue());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getLatestTemperature() {
        final ResponseEntity<String> entity;
        entity = restTemplate.getForEntity("/data/latest/temperature", String.class);
        logger.info("Latest temp: {}", entity.getBody());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONObject o = new JSONObject(entity.getBody());
        Set<String> keys = o.keySet();
        assertTrue(keys.contains(latestDeviceLabel));
        JSONArray array = o.optJSONArray(latestDeviceLabel);
        // The first item is the timestamp.
        assertEquals(array.getLong(0), latestTimestamp);
        // The second item is the temperature in centigrade.
        assertEquals(array.getDouble(1), latestTemperature, 0);
    }

    @Test
    public void getTemperatureDevices() {
        final ResponseEntity<String> entity;
        entity = restTemplate.getForEntity("/data/devices/temperature", String.class);
        logger.info("Devices: {}", entity.getBody());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONObject o = new JSONObject(entity.getBody());
        Set<String> keys = o.keySet();
        assertTrue(keys.contains(onlyDeviceId));
        String label = o.getString(onlyDeviceId);
        assertEquals(latestDeviceLabel, label);
    }

    @Test
    public void getRangedTemperatures() {
        parms.put("rangeMin", alpha);
        parms.put("rangeMax", omega);
        String urlTemplate = "/data/device/1/temperature/?range_min={rangeMin}&range_max={rangeMax}";
        final ResponseEntity<String> entity;
        entity = restTemplate.exchange(urlTemplate, HttpMethod.GET, null, String.class, parms);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONArray o = new JSONArray(entity.getBody());
        logger.info("Temperatures: {}", o.toString());
        assertEquals(rangeCount, o.length());
        JSONArray array = o.getJSONArray(0);
        // The first item is the timestamp.
        assertEquals(array.getLong(0), alpha);
        // The second item is the temperature in centigrade.
        assertEquals(array.getDouble(1), alphaTemperature, 0);
        // Check the last item.
        array = o.getJSONArray(o.length() - 1);
        // The first item is the timestamp.
        assertEquals(array.getLong(0), omega);
        // The second item is the temperature in centigrade.
        assertEquals(array.getDouble(1), omegaTemperature, 0);
    }

    @Test
    public void getTemperatures() {
        final ResponseEntity<String> entity;
        entity = restTemplate.exchange("/data/device/1/temperature", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONArray o = new JSONArray(entity.getBody());
        logger.info("Temperatures count: {}", o.length());
        assertTrue(o.length() >= totalCount);
    }
}
