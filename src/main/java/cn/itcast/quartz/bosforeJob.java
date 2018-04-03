package cn.itcast.quartz;

import cn.itcast.dao.TeacherDao;
import cn.itcast.service.TeacherService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class bosforeJob implements Job {
    @Autowired
    public TeacherService teacherService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("我开始删人了，闲杂狗逼们闪开");
       teacherService.deleteByBy();
    }
}
