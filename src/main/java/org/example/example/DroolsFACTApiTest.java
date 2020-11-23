package org.example.example;

import org.example.module.Car;
import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

public class DroolsFACTApiTest {

    @Test
    public void FACTApiTest(){
        // 创建KieServices
        KieServices services = KieServices.Factory.get();
        // 获取KieContainer
        KieContainer container = services.getKieClasspathContainer();
        // 创建session
        KieSession kieSession = container.newKieSession("all-rules");
        // 设置会话分组焦点
        kieSession.getAgenda().getAgendaGroup("factHandler").setFocus();

        Car car = new Car(100,new Person(67));

        FactHandle insert = kieSession.insert(car);
        System.out.println(insert.toExternalForm());
        kieSession.fireAllRules();
        car.getPerson().setAge(59);

        kieSession.update(insert,car);

        kieSession.getAgenda().getAgendaGroup("factHandler").setFocus();

        kieSession.fireAllRules();

        kieSession.dispose();



    }

}
