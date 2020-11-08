package com.playground.java.jodamoney;

import org.joda.money.CurrencyMismatchException;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MainTest {

    private double five = 5.00;
    private Money fiveBucks = Money.of(CurrencyUnit.USD, five);
    private Money cincoPila = Money.of(CurrencyUnit.of("BRL"), five);

    @DisplayName("It can parse currency unit and value from a String")
    @Test
    void testParseCurrencyAndValue() {
        Money almostFiftyBucks = Money.parse("USD 49.99");

        assertThat(almostFiftyBucks.getCurrencyUnit()).isEqualTo(CurrencyUnit.USD);
        assertThat(almostFiftyBucks.getAmount()).isEqualTo(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_EVEN));
    }

    @DisplayName("It can get countries that use a given currency unit")
    @Test
    void testCountriesByCurrency() {
        assertThat(CurrencyUnit.EUR.getCountryCodes())
                .contains("DE")
                .contains("FR")
                .contains("PT");
    }

    @DisplayName("It represents any currency using Enums and Strings")
    @Test
    void testRepresentsAnyCurrency() {
        assertThat(CurrencyUnit.ofCountry("BR").toString()).isEqualTo("BRL");
        assertThat(CurrencyUnit.of("EUR").toString()).isEqualTo("EUR");
        assertThat(CurrencyUnit.GBP.toString()).isEqualTo("GBP");
    }

    @DisplayName("It converts dollars to reais and vice-versa with a given ratio")
    @Test
    void testConversion() {
        CurrencyUnit currencyUnit = CurrencyUnit.of("BRL");
        BigDecimal convertionRatio = BigDecimal.valueOf(3.50);
        RoundingMode roundingStrategy = RoundingMode.HALF_EVEN;

        Money fiveReais = fiveBucks.convertedTo(currencyUnit, convertionRatio, roundingStrategy);
        BigDecimal fiveReaisDecimal = new BigDecimal(17.50).setScale(2, RoundingMode.HALF_EVEN);

        assertThat(fiveReais.getAmount()).isEqualTo(fiveReaisDecimal);
        assertThat(fiveReais.getCurrencyUnit().getCountryCodes())
                .contains("BR")
                .hasSize(1);
    }

    @DisplayName("It can operate with currencies")
    @Test
    void testRounding() {
        Money pen = Money.of(CurrencyUnit.USD, 1.99);
        Money bag = Money.of(CurrencyUnit.USD, 29.5);
        Money laptop = Money.of(CurrencyUnit.USD, 1249.99);
        Money monitor = Money.of(CurrencyUnit.USD, 347.80);
        Money purchaseSum = Money.total(pen, bag, laptop, monitor);

        assertThat(monitor.plus(Money.of(CurrencyUnit.USD, 0.20))).isEqualTo(Money.of(CurrencyUnit.USD, 348));
        assertThat(laptop.minus(9.99).toString()).isEqualTo("USD 1240.00");
        assertThat(pen.multipliedBy(bag.getAmount(), RoundingMode.HALF_EVEN).toString()).isEqualTo("USD 58.70");
        assertThat(purchaseSum.toString()).isEqualTo("USD 1629.28");
    }

    @DisplayName("It cannot compare money in different currencies")
    @Test
    void testComparison() {
        assertThatThrownBy(() -> fiveBucks.compareTo(cincoPila))
                .isInstanceOf(CurrencyMismatchException.class);
    }

    @DisplayName("It can compare money in the same currency")
    @Test
    void testComparisonSameCurrency() {
        double dollarToReaisRatio = 3.20;
        Money cincoPilaInDollars = cincoPila.convertedTo(
                CurrencyUnit.USD,
                BigDecimal.valueOf(dollarToReaisRatio),
                RoundingMode.UP);

        assertThat(cincoPilaInDollars.compareTo(fiveBucks)).isOne();
        assertThat(cincoPilaInDollars.getAmount())
                .isEqualTo(new BigDecimal(five * dollarToReaisRatio).setScale(2, RoundingMode.HALF_EVEN));
    }

}
