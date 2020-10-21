package bgu.spl.mics;
import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private Inventory Inve;

    @BeforeEach
    public void setUp(){Inve = Inventory.getInstance(); }

    @Test
    public void testload(){
        setUp();
        Inve.load(new String[]{"item1", "item2"});
        assertTrue(Inve.getItem("item1"));
        assertFalse(Inve.getItem("item1"));
        assertFalse(Inve.getItem(""));
        assertFalse(Inve.getItem(null));
    }
    @AfterEach
    public void tearDown(){}
}