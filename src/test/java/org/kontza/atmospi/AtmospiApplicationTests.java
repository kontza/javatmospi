package org.kontza.atmospi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
}
