package bgu.spl.mics;
import bgu.spl.mics.Subscriber;
import static org.junit.jupiter.api.Assertions.*;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    private MessageBroker broker;

    @BeforeEach
    public void setUp(){broker = MessageBrokerImpl.getInstance(); }

    @Test
    public void testregister(){
        setUp();
        Subscriber ran = new Subscriber("ran") {
            protected void initialize() {
            }
        };
        broker.register(ran);
        broker.unregister(ran);
    }
    @AfterEach
    public void tearDown(){}
}
