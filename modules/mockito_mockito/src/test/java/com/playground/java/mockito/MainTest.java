package com.playground.java.mockito;

import com.playground.java.mockito.Client;
import com.playground.java.mockito.Tax;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MainTest {

    @Nested
    class Mocks {

        @DisplayName("It can be used to define behavior of a contract without implement it")
        @Test
        void testMocksDefinedBehavior() {
            Tax fakeUnknownTax = mock(Tax.class);

            when(fakeUnknownTax.applyTaxOnValue(100)).thenReturn(50.0);

            assertThat(fakeUnknownTax.applyTaxOnValue(100)).isEqualTo(50.0);
        }

        @DisplayName("It return the primitive value in case the behavior mocked is not called")
        @Test
        void testMockBehaviorNotUsed() {
            Tax fakeUnknownTax = mock(Tax.class);

            when(fakeUnknownTax.applyTaxOnValue(1)).thenReturn(2.0);

            double defaultDoubleValue = 0.0;
            assertThat(fakeUnknownTax.applyTaxOnValue(101)).isEqualTo(defaultDoubleValue);
        }

        @DisplayName("The method thenAnswer receives a callback to handle the mocked method return")
        @Test
        void testMockThenReturnAndThenAnswer() {
            List participantsList = mock(List.class);

            when(participantsList.get(0)).thenAnswer(invocation -> {
                assertThat(invocation.getArguments()).hasSize(1).contains(0);
                return "Hello World";
            });

            assertThat(participantsList.get(0)).isEqualTo("Hello World");
        }

        @DisplayName("It is possible to verify how many times a method was called")
        @Test
        void testCheckTimesTheMockedMethodWasCalled() {
            Tax fakeTax = mock(Tax.class);

            when(fakeTax.applyTaxOnValue(0.42)).thenReturn(42.0);

            assertThat(fakeTax.applyTaxOnValue(0.42)).isEqualTo(42.0);
            verify(fakeTax, times(1)).applyTaxOnValue(0.42);
        }

    }

    @Nested
    class Spys {

        @DisplayName("Spies can be used to copy 'real' instances behavior")
        @Test
        void testSpyCanBeUsedToChangeRealObjectBehavior() {
            List<Client> clients = new ArrayList<>(2);
            clients.add(new Client("Dave"));
            clients.add(new Client("Barbie"));

            List<Client> clientsSpied = spy(clients);
            assertThat(clients.get(0).getName()).isEqualTo("Dave");

            clients.set(0, null);
            assertThat(clientsSpied.get(0)).isNull();
        }

    }
}
