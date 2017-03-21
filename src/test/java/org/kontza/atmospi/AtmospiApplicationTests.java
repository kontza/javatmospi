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
    private final String onlyDeviceLabel = "Kasvihuone";
    private final long onlyDeviceId = 1;
    private final HashMap<String, Long> parms = new HashMap<>();

    @Autowired
    private AtmospiSettings settings;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TemperatureRepository temperatureRepository;

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
        Temperature t = temperatureRepository.findTop1ByDeviceIdOrderByTimestampDesc(onlyDeviceId);
        final ResponseEntity<String> entity;
        entity = restTemplate.getForEntity("/data/latest/temperature", String.class);
        logger.info("Latest temp: {}", entity.getBody());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONObject o = new JSONObject(entity.getBody());
        Set<String> keys = o.keySet();
        assertTrue(keys.contains(onlyDeviceLabel));
        logger.info(">>> KEYS: {}", keys);
        JSONArray array = o.optJSONArray(onlyDeviceLabel);
        // The first item is the timestamp.
        assertEquals(1000*t.getTimestamp(), array.getLong(0));
        // The second item is the temperature in centigrade.
        Double d = Double.parseDouble(array.getString(1));
        logger.info(">>> Converted: {}", d);
        logger.info(">>> Expected : {}", t.getC());
        assertEquals(t.getC(), d, 0.001);
    }

    @Test
    public void getTemperatureDevices() {
        final ResponseEntity<String> entity;
        entity = restTemplate.getForEntity("/data/devices/temperature", String.class);
        logger.info("Devices: {}", entity.getBody());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONObject o = new JSONObject(entity.getBody());
        Set<String> keys = o.keySet();
        String onlyDeviceIdAsString = String.format("%d", onlyDeviceId);
        assertTrue(keys.contains(onlyDeviceIdAsString));
        String label = o.getString(onlyDeviceIdAsString);
        assertEquals(onlyDeviceLabel, label);
    }

    @Test
    public void getRangedTemperatures() {
        Temperature alphaTemp = temperatureRepository.findTop1ByDeviceIdOrderByTimestampAsc(onlyDeviceId);
        Temperature omegaTemp = temperatureRepository.findTop1ByDeviceIdOrderByTimestampDesc(onlyDeviceId);
        parms.put("rangeMin", alphaTemp.getTimestamp());
        parms.put("rangeMax", omegaTemp.getTimestamp());
        String urlTemplate = "/data/device/1/temperature/?range_min={rangeMin}&range_max={rangeMax}";
        final ResponseEntity<String> entity;
        entity = restTemplate.exchange(urlTemplate, HttpMethod.GET, null, String.class, parms);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONArray o = new JSONArray(entity.getBody());
        logger.info("Temperatures: {}", o.toString());
        assertEquals(temperatureRepository.countByDeviceId(onlyDeviceId), o.length());
        JSONArray array = o.getJSONArray(0);
        // The first item is the timestamp.
        assertEquals(array.getLong(0), 1000 * alphaTemp.getTimestamp());
        // The second item is the temperature in centigrade.
        assertEquals(alphaTemp.getC(), array.getDouble(1), 0.001);
        // Check the last item.
        array = o.getJSONArray(o.length() - 1);
        // The first item is the timestamp.
        assertEquals(array.getLong(0), 1000 * omegaTemp.getTimestamp());
        // The second item is the temperature in centigrade.
        assertEquals(omegaTemp.getC(), array.getDouble(1), 0.001);
    }

    @Test
    public void getTemperatures() {
        final ResponseEntity<String> entity;
        entity = restTemplate.exchange("/data/device/1/temperature", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        JSONArray o = new JSONArray(entity.getBody());
        logger.info("Temperatures count: {}", o.length());
        assertTrue(o.length() >= temperatureRepository.countByDeviceId(onlyDeviceId));
    }
}
