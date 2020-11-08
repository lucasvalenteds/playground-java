package com.playground.java.openl;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openl.rules.context.IRulesRuntimeContext;
import org.openl.rules.context.RulesRuntimeContextFactory;
import org.openl.rules.runtime.RulesEngineFactory;
import org.openl.runtime.IEngineWrapper;
import org.openl.vm.IRuntimeEnv;

class RuntimeContextTest {

    interface CreditRules {
        double getCreditLimitBasedOnIncome(double income);
    }

    private final Path classpath = Paths.get("src", "test", "resources");
    private final RulesEngineFactory<CreditRules> engineFactory = new RulesEngineFactory<>(
        classpath.resolve("credit-rules.xlsx").toString(),
        CreditRules.class
    );
    private final CreditRules rules = ((CreditRules) engineFactory.newInstance());
    private final IRuntimeEnv runtime = ((IEngineWrapper) rules).getRuntimeEnv();
    private final IRulesRuntimeContext context = RulesRuntimeContextFactory.buildRulesRuntimeContext();

    @BeforeEach
    void setUp() {
        runtime.setContext(context);
    }

    @Test
    void testLimitForRegularCustomers() {
        context.setLob("Regular");

        assertEquals(250d, rules.getCreditLimitBasedOnIncome(654.32d));
        assertEquals(480d, rules.getCreditLimitBasedOnIncome(1200d));
        assertEquals(699.99d, rules.getCreditLimitBasedOnIncome(2501.01d));
    }

    @Test
    void testLimitForPremiumCustomers() {
        context.setLob("Premium");

        assertEquals(800d, rules.getCreditLimitBasedOnIncome(3500d));
        assertEquals(1200d, rules.getCreditLimitBasedOnIncome(5432d));
        assertEquals(2600d, rules.getCreditLimitBasedOnIncome(9800d));
    }

    @Test
    void testFallbackLimits() {
        context.setLob("Fallback");

        assertEquals(650d, rules.getCreditLimitBasedOnIncome(2500d));
        assertEquals(4000d, rules.getCreditLimitBasedOnIncome(15000d));
    }
}
