package bgu.spl.mics;
import static org.junit.Assert.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
    private Squad squ;
    @BeforeEach
    public void setUp(){squ = Squad.getInstance(); }

    @Test
    public void testgetAgents()throws InterruptedException{
        setUp();
        Agent a=new Agent();
        a.setName("nadav");
        a.setSerialNumber("001");
        Agent b=new Agent();
        b.setName("ran");
        b.setSerialNumber("002");
        Agent[] list= new Agent[]{a,b};
        squ.load(list);
        assertFalse(squ.getAgents(null));
        List<String> check = new ArrayList<String>();
        assertFalse(squ.getAgents(check));
        check.add("001");
        check.add("002");
        assertTrue(squ.getAgents(check));
        check.remove("002");
        assertFalse(squ.getAgents(check));
        check.add("003");
        assertFalse(squ.getAgents(check));
    fail("Not a good test");
    }
    @Test
    public void testreleaseAgents()throws InterruptedException{
        setUp();
        Agent a=new Agent();
        a.setName("nadav");
        a.setSerialNumber("001");
        Agent b=new Agent();
        b.setName("ran");
        b.setSerialNumber("002");
        Agent[] list= new Agent[]{a,b};        squ.load(list);
        List<String> check = new ArrayList<String>();
        check.add("001");
        squ.releaseAgents(check);
        assertFalse(squ.getAgents(check));
        fail("Not a good test");
    }
    @AfterEach
    public void tearDown(){}
}
