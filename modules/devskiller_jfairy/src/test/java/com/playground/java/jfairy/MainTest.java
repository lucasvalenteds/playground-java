package com.playground.java.jfairy;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.payment.CreditCard;
import io.codearte.jfairy.producer.person.Address;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.text.TextProducer;
import org.joda.time.DateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MainTest {

    @Nested
    class English {

        private final Locale locale = Locale.forLanguageTag("en-US");
        private final Fairy fairy = Fairy.create(locale);

        @DisplayName("It can provide person name, age, sex and birthday")
        @Test
        void testPerson() {
            Person person = fairy.person();

            assertThat(person.getFirstName()).isInstanceOf(String.class);
            assertThat(person.getLastName()).isInstanceOf(String.class);
            assertThat(person.getMiddleName()).isInstanceOf(String.class);
            assertThat(person.getFullName()).isInstanceOf(String.class);
            assertThat(person.getAge()).isGreaterThan(0);
            assertThat(person.getSex()).isInstanceOfAny(Person.Sex.class);
            assertThat(person.getDateOfBirth()).isInstanceOfAny(DateTime.class);
        }

        @DisplayName("It can provide personal documents and contact information")
        @Test
        void testPersonContact() {
            Person person = fairy.person();

            assertThat(person.getEmail()).isInstanceOf(String.class);
            assertThat(person.getPassword()).isInstanceOf(String.class);
            assertThat(person.getPassportNumber()).isInstanceOf(String.class);
            assertThat(person.getTelephoneNumber()).containsPattern("[0-9]");
            assertThat(person.getNationalIdentityCardNumber()).isInstanceOf(String.class);
            assertThat(person.getNationalIdentificationNumber()).isInstanceOf(String.class);
        }

        @DisplayName("It can provide a splittable complete address")
        @Test
        void testAddress() {
            Person person = fairy.person();
            Address address = person.getAddress();

            assertThat(address.getCity()).isInstanceOf(String.class);
            assertThat(address.getPostalCode()).isInstanceOf(String.class);
            assertThat(address.getStreet()).isInstanceOf(String.class);
            assertThat(address.getStreetNumber()).isInstanceOf(String.class);
            assertThat(address.getApartmentNumber()).isInstanceOf(String.class);
            assertThat(address.getAddressLine1()).isInstanceOf(String.class);
            assertThat(address.getAddressLine2()).isInstanceOf(String.class);
        }

        @DisplayName("It can provide company information")
        @Test
        void testCompany() {
            Company company = fairy.company();
            assertThat(company.getName()).isInstanceOf(String.class);
            assertThat(company.getEmail()).isInstanceOf(String.class);
            assertThat(company.getVatIdentificationNumber()).isInstanceOf(String.class);
            assertThat(company.getDomain()).isInstanceOf(String.class);
            assertThat(company.getUrl()).isInstanceOf(String.class);
        }

        @DisplayName("It can provide credit card information")
        @Test
        void testCreditCar() {
            CreditCard creditCard = fairy.creditCard();

            assertThat(creditCard.getVendor()).isInstanceOf(String.class);
            assertThat(creditCard.getExpiryDate()).isInstanceOf(DateTime.class);
            assertThat(creditCard.getExpiryDateAsString()).isInstanceOf(String.class);
        }

        @DisplayName("It can produce N words, sentences and paragraphs with M characters")
        @Test
        void testParagraph() {
            TextProducer textProducer = fairy.textProducer();

            assertThat(textProducer.paragraph()).isInstanceOf(String.class);
            assertThat(textProducer.paragraph(3)).contains(" ");

            assertThat(textProducer.sentence()).isInstanceOf(String.class);
            assertThat(textProducer.sentence(10)).contains(" ");
        }

        @DisplayName("It can provide Lorem Ipsum text")
        @Test
        void testParagraphLoremIpsum() {
            TextProducer textProducer = fairy.textProducer();

            assertThat(textProducer.loremIpsum()).isInstanceOf(String.class);
        }
    }

    @Nested
    class Portuguese {

        @DisplayName("It does not support portuguese")
        @Test
        void testIllegalArgumentException() {
            assertThatThrownBy(() -> Fairy.create(Locale.forLanguageTag("pt-br")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith("File jfairy_pt.yml was not found on classpath");
        }

    }

}
