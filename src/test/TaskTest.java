package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    Task task;

    @BeforeEach
    void createTask() {
        task = new Task("Task1", "Description task1", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 01, 00, 00, 00));
    }

    @Test
    void getNameTest() {
        assertEquals("Task1", task.getName(), "Имя не соответствует");
    }

    @Test
    void getDescriptionTest() {
        assertEquals("Description task1", task.getDescription(),
                "Описание не соответствует");
    }

    @Test
    void getIdNumberTest() {
        assertEquals(1, task.getIdNumber(), "ID номер не соответствует");
    }

    @Test
    void getStatusTest() {
        assertEquals(Status.NEW, task.getStatus(), "Тип задачи не соответствует");
    }

    @Test
    void getEngTimeTest() {
        assertEquals(LocalDateTime.of(2022, 9, 01, 01, 00, 00), task.getEndTime(),
                "Время окончания выполнения задачи рассчитано не верно");
    }

    @Test
    void getStartTimeTest() {
        assertEquals(LocalDateTime.of(2022, 9, 01, 00, 00, 00),
                task.getStartTime(), "Время начала выполнения задачи не верно");
    }

    @Test
    void getDurationTest() {
        assertEquals(Duration.ofMinutes(60), task.getDuration(), "Длительность выполнения задачи не верна");
    }

    @Test
    void testToString() {
        String expectedString = "1,TASK,Task1,NEW,Description task1,PT1H,2022-09-01T00:00";
        String actualString = task.toString();
        assertEquals(expectedString, actualString, "Вывод не соответствует ожидаемому");
    }
}