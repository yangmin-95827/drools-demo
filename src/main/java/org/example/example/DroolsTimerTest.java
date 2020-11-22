package org.example.example;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsTimerTest {

    @Test
    public void timerTest() throws InterruptedException {
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("timer-test-session");

        new Thread(kieSession::fireUntilHalt).start();

//        ((Runnable)kieSession::fireUntilHalt).run();
//        new Thread(((Runnable)kieSession::fireUntilHalt)).start();

        for (int i = 0; i < 60; i++) {
            Thread.sleep(1000);
        }
        kieSession.halt();
        kieSession.dispose();
    }

}
