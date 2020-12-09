package org.example.decisionTable;

import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DroolsDecisionTableTest {

    @Test
    public void decisionTableTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession kieSession = container.newKieSession("decisionTable-test-session");


        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void parseDecisionTable() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("F:\\java_work\\drools-demo\\target\\classes\\org\\decisionTable\\decisionTables.xls");
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String compile = compiler.compile(inputStream, InputType.XLS);
        System.out.println(compile);
    }

}
