package org.kontza.atmospi;

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
    private final int latestTimestamp = 1477576501;
    private final double latestTemperature = 8.94;

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
}
