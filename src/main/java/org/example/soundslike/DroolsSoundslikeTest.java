package org.example.soundslike;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsSoundslikeTest {

    @Test
public void soundslikeTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("soundslike-test-session");

        Person person = new Person(20,"fubar");
        kieSession.insert(person);

        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
