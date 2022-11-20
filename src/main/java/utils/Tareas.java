package utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Tareas {
  public static void tareaRecomendaciones() {
    try {
      JobDetail myJob = JobBuilder.newJob(EnvioRecomendaciones.class).withIdentity("myjob", "mygroup").build();
      Trigger myTrigger = TriggerBuilder.newTrigger().withIdentity("mytrigger", "mygroup").startNow()
              .withSchedule(CronScheduleBuilder.cronSchedule("0 52 22  29 6 ? 2022")).build();
      Scheduler myScheduler = new StdSchedulerFactory().getScheduler();
      myScheduler.start();
      myScheduler.scheduleJob(myJob, myTrigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  public static void tareaGenerarHistorial() {
    try {
      JobDetail myJob = JobBuilder.newJob(GeneracionHistorial.class).withIdentity("myjob", "mygroup").build();
      Trigger myTrigger = TriggerBuilder.newTrigger().withIdentity("mytrigger", "mygroup").startNow()
          .withSchedule(CronScheduleBuilder.cronSchedule("0 52 22  29 6 ? 2022")).build();
      Scheduler myScheduler = new StdSchedulerFactory().getScheduler();
      myScheduler.start();
      myScheduler.scheduleJob(myJob, myTrigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }
}
