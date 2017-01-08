package org.kontza.atmospi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juharuotsalainen
 */
@RestController
public class AtmospiSettingsController {

    private Logger logger = LoggerFactory.getLogger(AtmospiSettingsController.class.getName());

    @Autowired
    private AtmospiSettings settings;

    @RequestMapping("/settings")
    public Settings settings() {
        Settings retVal = new Settings(settings.getRangeSeconds(), settings.getPrecision(), settings.getTemperatureUnit());
        return retVal;
    }
}
