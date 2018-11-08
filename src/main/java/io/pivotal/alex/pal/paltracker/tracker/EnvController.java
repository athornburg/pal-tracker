package io.pivotal.alex.pal.paltracker.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private final String port;
    private final String memoryLimit;
    private final String cfInstanceIndex;
    private final String cfInstanceAddr;

    private static final String PORT_KEY = "PORT";
    private static final String MEMORY_LIMIT_KEY = "MEMORY_LIMIT";
    private static final String CF_INSTANCE_INDEX_KEY = "CF_INSTANCE_INDEX";
    private static final String CF_INSTANCE_ADDR = "CF_INSTANCE_ADDR";

    public EnvController(
            @Value("${" + PORT_KEY + ":NOT SET}") String port,
            @Value("${" + MEMORY_LIMIT_KEY + ":NOT SET}") String memoryLimit,
            @Value("${" + CF_INSTANCE_INDEX_KEY + ":NOT SET}") String cfInstanceIndex,
            @Value("${" + CF_INSTANCE_ADDR + ":NOT SET}") String cfInstanceAddr) {
        this.port = port;
        this.memoryLimit = memoryLimit;
        this.cfInstanceIndex = cfInstanceIndex;
        this.cfInstanceAddr = cfInstanceAddr;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map<String, String> env = new HashMap<>();
        env.put(PORT_KEY, port);
        env.put(MEMORY_LIMIT_KEY, memoryLimit);
        env.put(CF_INSTANCE_INDEX_KEY, cfInstanceIndex);
        env.put(CF_INSTANCE_ADDR, cfInstanceAddr);

        return env;
    }
}
