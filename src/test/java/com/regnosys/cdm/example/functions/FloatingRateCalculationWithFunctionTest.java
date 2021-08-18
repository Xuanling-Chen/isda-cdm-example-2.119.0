package com.regnosys.cdm.example.functions;

import com.google.inject.Inject;
import com.regnosys.cdm.example.AbstractExampleTest;
import org.junit.jupiter.api.Test;

class FloatingRateCalculationWithFunctionTest extends AbstractExampleTest {

    @Inject
    private FloatingRateCalculationWithFunction floatingRateCalculationWithFunction;

    @Test
    void shouldRunWithoutExceptions() {
        floatingRateCalculationWithFunction.run();
    }
}