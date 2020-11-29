package org.example.matches;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsMatchesTest {

    @Test
    public void matchesTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("matches-test-session");

        Person person= new Person( 19 , "tom" );

        session.insert(person);

        session.fireAllRules();
        session.dispose();
    }
}
