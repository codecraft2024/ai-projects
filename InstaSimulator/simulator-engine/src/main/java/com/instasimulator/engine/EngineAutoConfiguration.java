package com.instasimulator.engine;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.instasimulator.calls.CallsAutoConfiguration;
import com.instasimulator.common.CommonAutoConfiguration;
import com.instasimulator.config.ConfigAutoConfiguration;
import com.instasimulator.core.CoreAutoConfiguration;
import com.instasimulator.scenarios.ScenariosAutoConfiguration;

@Configuration
@ComponentScan("com.instasimulator.engine")
@Import({
        CommonAutoConfiguration.class,
        ConfigAutoConfiguration.class,
        CoreAutoConfiguration.class,
        CallsAutoConfiguration.class,
        ScenariosAutoConfiguration.class
})
public class EngineAutoConfiguration {
}
