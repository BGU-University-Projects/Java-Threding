package bgu.spl.mics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest extends Future {
    private String s = "check";
    private Future future;
    @BeforeEach
    public void setUp(){
        this.future = new Future();
    }

    @Test
    public void testget() {
        assertNull(future);
        future.resolve(s);
        assertTrue(future.get()==s);
    }

    @Test
    public void testisDone() {
        assertFalse(future.isDone());
        future.resolve(s);
        assertTrue(future.isDone());
    }
    @Test
    public void testgetTime() {
        assertNull(future);
        future.resolve(s);
        assertTrue(future.get (10, TimeUnit.SECONDS) == s);
    }
    @AfterEach
    public void tearDown() {}
}
