package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.delivery.data.DataGenerator.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        UserInfo userInfo = Registration.generateUser("Ru");
        $("[data-test-id=city] input").setValue(userInfo.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String deliveryDate = DataGenerator.generateDate(4);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id=name] input").setValue(userInfo.getName());
        $("[data-test-id=phone] input").setValue(userInfo.getPhone());
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(text("Успешно! Встреча успешно запланирована на " + deliveryDate),
                Duration.ofSeconds(15)).shouldBe(visible);
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String redeliveryDate = DataGenerator.generateDate(9);
        $("[data-test-id=date] input").setValue(redeliveryDate);
        $$("[class=button__text]").find(exactText("Запланировать")).click();
        $("[data-test-id=replan-notification]").shouldBe(text("Необходимо подтверждение" +
                        " У вас уже запланирована встреча на другую дату. Перепланировать?"),
                Duration.ofSeconds(15)).shouldBe(visible);
        $$("[class=button__text]").find(exactText("Перепланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(text("Успешно! Встреча успешно запланирована на " + redeliveryDate),
                Duration.ofSeconds(15)).shouldBe(visible);
    }
}
