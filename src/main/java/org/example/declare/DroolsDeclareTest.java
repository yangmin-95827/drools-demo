package org.example.declare;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactField;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Date;

public class DroolsDeclareTest {

    @Test
    public void declareTest() throws IllegalAccessException, InstantiationException {
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("declare-test-session");
        // 获取DRL定义的类型
        FactType student = kieSession.getKieBase().getFactType("org.declareTest", "Student");
        FactType address = kieSession.getKieBase().getFactType("org.declareTest", "Address");
        // 实例化对象
        Object a = address.newInstance();
        Object nStu = student.newInstance();

        // 设置属性值
        address.set(a,"address","上海");
        student.set(nStu,"className","三班");
        student.set(nStu,"score",91);
        student.set(nStu,"age",18);
        student.set(nStu,"name","李四");
        student.set(nStu,"birthday",new Date());
        student.set(nStu,"address",a);

        kieSession.insert(nStu);

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
