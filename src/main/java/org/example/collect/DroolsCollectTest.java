package org.example.collect;

import org.example.module.Address;
import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsCollectTest {

    @Test
    public void collectTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("collectTest-test-session");

        session.insert(new Person(11,"tom1",new Address("成都","0000012")));
        session.insert(new Person(12,"tom2",new Address("上海","0000013")));
        session.insert(new Person(13,"tom3",new Address("北京","0000010")));
        session.insert(new Person(14,"tom4",new Address("南京","0000011")));
        session.insert(new Person(15,"jerry",new Address("北京","0000010")));
        session.insert(new Person(16,"jerry",new Address("北京","0000010")));

        session.fireAllRules();
        session.dispose();

    }

}
