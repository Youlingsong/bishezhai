package cn.itcast.service.impl;

import cn.itcast.dao.TeacherDao;
import cn.itcast.domain.Teacher;
import cn.itcast.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherDao teacherDao;
    @Override
    public Page<Teacher> findPageData(Specification specification,Pageable pageable) {
        return teacherDao.findAll(specification,pageable);
    }

    @Override
    public void save(Teacher model) {
        model.setType("1");
        teacherDao.save(model);
    }

    @Override
    public void deleteStandard(int id) {
        teacherDao.delete(id);
    }

    @Override
    public void saves(List<Teacher> teachers) {
        teacherDao.save(teachers);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    @Override
    public void deleteByBy() {
        teacherDao.deleteByBy();
    }

    @Override
    public Teacher findByUsername(String username) {
        return teacherDao.findByUsername(username);
    }
}
