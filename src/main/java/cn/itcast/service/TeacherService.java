package cn.itcast.service;

import cn.itcast.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TeacherService {
    Page<Teacher> findPageData(Specification specification,Pageable pageable);

    void save(Teacher model);

    void deleteStandard(int id);

    void saves(List<Teacher> teachers);

    List<Teacher> findAll();

    void deleteByBy();

    Teacher findByUsername(String username);
}
