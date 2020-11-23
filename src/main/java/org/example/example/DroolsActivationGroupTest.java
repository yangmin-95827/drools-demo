package org.example.example;

import org.example.module.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsActivationGroupTest {


    @Test
    public void activationGroupTes(){
        // 创建KieServices
        KieServices services = KieServices.Factory.get();
        // 获取KieContainer
        KieContainer container = services.getKieClasspathContainer();
        // 创建session
        KieSession session = container.newKieSession("activationgroup-rule");

        Person person = new Person(40);
        // 插入FACT 对象
        session.insert(person);
        // 匹配规则
        session.fireAllRules();
        // 销毁session
        session.dispose();
    }

}
