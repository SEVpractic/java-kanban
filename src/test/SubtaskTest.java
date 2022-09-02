package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    Subtask subtask;

    @BeforeEach
    void createSubtask() {
        subtask = new Subtask("Subtask1", "Description subtask1", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 01, 00, 00, 00), 2);
    }

    @Test
    void getEpicsID() {
        assertEquals(2, subtask.getEpicsID(), "Не совпадает номер эпика");
    }

    @Test
    void testToString1() {
        String expectedString = "1,SUBTASK,Subtask1,NEW,Description subtask1,PT1H,2022-09-01T00:00,2";
        String actualString = subtask.toString();
        assertEquals(expectedString, actualString, "Вывод не соответствует ожидаемому");
    }
}