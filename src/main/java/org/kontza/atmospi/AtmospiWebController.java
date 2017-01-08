package org.kontza.atmospi;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AtmospiWebController {

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        model.put("subDomain", "");
        return "index";
    }
}
