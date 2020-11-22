package org.example.example;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.Calendar;
import org.quartz.impl.calendar.WeeklyCalendar;

public class DroolsCalendarsTest {

    @Test
    public void calendarsTest(){
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("calendar-test-session");
        session.getCalendars().set("WEEKDAY",WEEKDAY);
        session.getCalendars().set("WEEKDAY_EXCLUDE",WEEKDAY_EXCLUDE);

        session.fireAllRules();

        session.dispose();
    }

    // 包含周四
    public static Calendar WEEKDAY  = l -> {
        WeeklyCalendar calendar = new WeeklyCalendar();
        calendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,true});
        calendar.setDayExcluded(java.util.Calendar.SUNDAY,true);
        return calendar.isTimeIncluded(l);
    };

    public static Calendar WEEKDAY_EXCLUDE = l -> {
        WeeklyCalendar calendar = new WeeklyCalendar();
        calendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,false});
        return calendar.isTimeIncluded(l);
    };




}
