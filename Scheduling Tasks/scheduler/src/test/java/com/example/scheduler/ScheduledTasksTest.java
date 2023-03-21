package com.example.scheduler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduledTasksTest {
    @SpyBean
    private ScheduledTasks scheduledTasks;

    @Test
    void reportCurrentTime() {
        await().atMost(10, TimeUnit.SECONDS).untilAsserted(()->{
            verify(scheduledTasks, atLeast(2)).reportCurrentTime();
        });
    }
}