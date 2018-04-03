package cn.itcast.dao;

import cn.itcast.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeacherDao extends JpaRepository<Teacher,Integer>,JpaSpecificationExecutor<Teacher>{
    @Query("delete from Teacher where type=2")
    @Modifying
    void deleteByBy();

    Teacher findByUsername(String username);
}
