package ru.netology.service;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class FormTest {

    private String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String planningDate = generateDate(7);

    @Test
    void shouldSubmitRequest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue("Казань");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] input").setValue(planningDate);
        form.$("[data-test-id=name] .input__control").setValue("Федор Федор-Федор");
        form.$("[data-test-id=phone] .input__control").setValue("+78787878787");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$(".button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldNotSubmitRequest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue("Moscow");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] input").setValue(planningDate);
        form.$("[data-test-id=name] .input__control").setValue("Fred");
        form.$("[data-test-id=phone] .input__control").setValue("7878787878");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$(".button").find(exactText("Забронировать")).click();
        form.$("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotSubmitEmptyRequest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue("");
        form.$("[data-test-id=name] .input__control").setValue("");
        form.$("[data-test-id=phone] .input__control").setValue("");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$(".button").find(exactText("Забронировать")).click();
        form.$("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
    @Test
    void shouldNotSubmitEmptyCheckboxRequest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue("Казань");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] input").setValue(planningDate);
        form.$("[data-test-id=name] .input__control").setValue("Федор Федор-Федор");
        form.$("[data-test-id=phone] .input__control").setValue("+78787878787");
        form.$$(".button").find(exactText("Забронировать")).click();
        form.$("[data-test-id=agreement] .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
