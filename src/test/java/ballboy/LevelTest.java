package ballboy;

import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.behaviour.FloatingCloudBehaviourStrategy;
import javafx.application.Platform;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.Semaphore;

import static org.junit.Assert.*;
public class LevelTest {
    @BeforeAll
    public static void setupJavaFx() throws InterruptedException {
        Semaphore available = new Semaphore(0, true);
        Platform.startup(available::release);
        available.acquire();
    }

    @AfterAll
    public static void cleanupJavaFx() {
        Platform.exit();
    }
    @Test
    public void writerNotNullTest() {
        App.main(null);

    }
}
