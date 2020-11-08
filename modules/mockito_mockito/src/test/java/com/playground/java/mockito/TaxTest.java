package com.playground.java.mockito;

import com.playground.java.mockito.ICMS;
import com.playground.java.mockito.IPVA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class TaxTest {

    @DisplayName("ICMS doubles the value")
    @Test
    void testICMSDoublesTheValue() {
        assertThat(new ICMS().applyTaxOnValue(100)).isEqualTo(200);
    }

    @DisplayName("IPVA should add 200 to if paid on february")
    @Test
    void testIPVAPaidOnFebruary() {
        assertThat(new IPVA(Month.FEBRUARY).applyTaxOnValue(100)).isEqualTo(300);
    }

    @DisplayName("IPVA should add 150 to value if paid on a month other than february")
    @Test
    void testIPVA() {
        assertThat(new IPVA(Month.JANUARY).applyTaxOnValue(100)).isEqualTo(250);
    }
}
