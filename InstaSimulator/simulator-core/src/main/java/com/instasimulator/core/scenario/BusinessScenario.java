package com.instasimulator.core.scenario;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.core.call.ApiCall;

import java.util.List;

/**
 * A business scenario orchestrating a sequence of API calls.
 */
public interface BusinessScenario {

    String name();

    String description();

    List<ApiCall> steps();

    /**
     * Optional hook before the first step.
     */
    default void beforeScenario(SimulationContext context) {
        context.setCurrentScenario(name());
    }

    /**
     * Optional hook after the last step.
     */
    default void afterScenario(SimulationContext context, ScenarioResult result) {
        // no-op
    }
}
