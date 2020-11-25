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
        // 定义只在星期日生效的日历
        session.getCalendars().set("WEEKDAY",l -> {
            WeeklyCalendar calendar = new WeeklyCalendar();
            calendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,true});
            calendar.setDayExcluded(java.util.Calendar.SUNDAY,true);
            return calendar.isTimeIncluded(l);
        });
        // 定义排除周一到周日的日历
        session.getCalendars().set("WEEKDAY_EXCLUDE",l -> {
            WeeklyCalendar calendar = new WeeklyCalendar();
            calendar.setDaysExcluded(new boolean[]{false,false,false,false,false,false,false});
            return calendar.isTimeIncluded(l);
        });

        session.fireAllRules();

        session.dispose();
    }





}
