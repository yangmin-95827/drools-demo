package org.example.validateZipcode;

import org.example.module.Address;
import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.CommandFactory;

import java.util.ArrayList;

public class DroolsValidateZipcodeTest {

    @Test
    public void validateZipcode(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("validateZipcode-test-session");

        Person person = new Person(20,"tom");
        person.setAddresses(new ArrayList<>());
        person.getAddresses().add(new Address("北京","000010"));
        person.getAddresses().add(new Address("上海","23920W"));

        kieSession.insert(person);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
