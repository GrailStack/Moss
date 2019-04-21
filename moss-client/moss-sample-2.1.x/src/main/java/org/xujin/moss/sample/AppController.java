package org.xujin.moss.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/app")
public class AppController {

    private static Logger logger = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/add")
    public String addCity() {
        logger.info("我是Info日志");
        logger.debug("我是debug日志");
        return "";
    }


}
