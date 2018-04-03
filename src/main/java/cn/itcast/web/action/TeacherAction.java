package cn.itcast.web.action;

import cn.itcast.domain.Teacher;
import cn.itcast.service.TeacherService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TeacherAction extends BaseAction<Teacher> {
    @Autowired
    TeacherService teacherService;


    public File file;



    List<Teacher> teachers = new ArrayList<Teacher>();

    public void setFile(File file) {
        this.file = file;
    }


    /*属性驱动封装*/
    public  int[] ids;

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Action(value = "teacher_pageQuery", results = { @Result(name = "success", type = "json") })
    public String pageQuery(){
        Specification<Teacher> specification = new Specification<Teacher>() {
            @Override
            public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                ArrayList<Predicate> predicatelist = new ArrayList<>();
                Predicate predicate=null;
                if(StringUtils.isNotBlank(model.getName())){
                    Predicate address= cb.like(root.get("address").as(String.class), "%"+model.getAddress()+"%");
                    predicatelist.add(address);
                }

                if(StringUtils.isNotBlank(model.getName())){
                    Predicate name= cb.equal(root.get("name").as(String.class), model.getName());
                    predicatelist.add(name);
                }

                if(StringUtils.isNotBlank(model.getDepartment())){
                    Predicate department = cb.equal(root.get("department").as(String.class), model.getDepartment());
                    predicatelist.add(department);
                }

                if(StringUtils.isNotBlank(model.getPosition())){
                    Predicate position = cb.equal(root.get("position").as(String.class), model.getPosition());
                    predicatelist.add(position);
                }
                if(predicatelist!=null&&predicatelist.size()>0) {
                    predicate=  cb.and(predicatelist.toArray(new Predicate[0]));
                }

                return predicate;
            }
        };




        Pageable pageable=new PageRequest(page-1,rows,new Sort(new Sort.Order(Sort.Direction.DESC,"id")));
        Subject subject = SecurityUtils.getSubject();
        Teacher teacher = (Teacher)subject.getPrincipal();
        if(teacher.getUsername().equals("admin")) {
            Page<Teacher> pageData = teacherService.findPageData(specification, pageable);
            pushPageDataToValueStack(pageData);
        }else{
            Teacher byUsername = teacherService.findByUsername(teacher.getUsername());
            ArrayList<Teacher> teachers = new ArrayList<>();
            teachers.add(teacher);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", 1L);
            result.put("rows", teachers);
            pushValueToValueStach(result);
        }

        return SUCCESS;
    }


    @Action(value = "teacher_save", results = { @Result(name = "success", type = "redirect",location = "/page/base/teacher.html") })
    public String save(){
      teacherService.save(model);
      return SUCCESS;
    }

    /*删除*/
    @Action(value = "teacher_deleteTeacher",results = {@Result(type = "json")})
    public String delBatch(){

        for(int i=0;i<ids.length;i++){
            teacherService.deleteStandard(ids[i]);
        }
        ActionContext.getContext().getValueStack().push(true);
        return  SUCCESS;
    }


    /*导入excel文件*/
    @Action(value = "area_batchImport")
    public String importArea() throws Exception {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
        for(Row row:sheetAt){
            if(row.getRowNum()==0){
                continue;
            }
            if(row.getCell(0)==null|| StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                continue;
            }
            Teacher teacher = new Teacher();//input  ,value


            teacher.setType("1");
            teacher.setName(row.getCell(0).getStringCellValue());
            teacher.setAddress(row.getCell(1).getStringCellValue());
            if(row.getCell(2)!=null){
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                teacher.setAge(Integer.valueOf(row.getCell(2).getStringCellValue()));
            }

            teacher.setDepartment(row.getCell(3).getStringCellValue());
            teacher.setPosition(row.getCell(4).getStringCellValue());
            teacher.setSex(row.getCell(5).getStringCellValue());
            teacher.setTelephone(row.getCell(6).getStringCellValue());

            if(row.getCell(7)!=null){
                row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
                teacher.setWorkyear(Integer.valueOf(row.getCell(7).getStringCellValue()));
            }

            teacher.setEmail(row.getCell(8).getStringCellValue());

              teachers.add(teacher);

        }
        // 调用业务层

        teacherService.saves(teachers);

        return NONE;
    }


    //导出excel表格
    @Action(value = "teacher_exportTeacher", results = {@Result(type = "json")})
    public String exprotSubArea() throws Exception {
        HSSFWorkbook hwb = new HSSFWorkbook();//第一步，创建一个workbook（一个excel文件）
        HSSFSheet hs = hwb.createSheet("信息管理");//第二步，在workbook中添加一个sheet，对应excel文件中sheet
        HSSFRow hr = hs.createRow((int) 0);//第三部，在sheet中添加表头第0行（相当于解释字段）
        HSSFCellStyle hcs = hwb.createCellStyle();//第四步，设置第0行（表头）居中
        hcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);//创建居中格式

        /**标题设置成为粗体*/
        HSSFFont font =hwb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        /**标题设置字体颜色为红色*/
        short color = HSSFColor.RED.index;
        font.setColor(color);/**设置字体颜色为红色*/
        hcs.setFont(font);/**将font对象赋给样式*/


        //将表头的字段放入数组当中
        String[] excelHeader = {"-姓名-", "-地址-", "-年龄-", "-部门-", "-职位-","-性别-", "-电话-", "-工作年限-", "-邮箱-"};
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell hc = hr.createCell(i);//顺序创建
            hc.setCellValue(excelHeader[i]);//顺序塞入
            hc.setCellStyle(hcs);//居中
            hs.setColumnWidth(i,5000);
        }

        List<Teacher> Blist = teacherService.findAll();//查找全部实体属性字段
        for (int i = 0; i < Blist.size(); i++) {
            hr = hs.createRow(i + 1);//在sheet中自动随 i+1 增加一行（i 是表头）

            Teacher thefuzzyBook = Blist.get(i);

            hr.createCell(0).setCellValue((thefuzzyBook.getName()));
            hr.createCell(1).setCellValue((thefuzzyBook.getAddress()));
            hr.createCell(2).setCellValue((thefuzzyBook.getAge()));
            hr.createCell(3).setCellValue(thefuzzyBook.getDepartment());
            hr.createCell(4).setCellValue(thefuzzyBook.getPosition());

            hr.createCell(5).setCellValue((thefuzzyBook.getSex()));
            hr.createCell(6).setCellValue(thefuzzyBook.getTelephone());
            hr.createCell(7).setCellValue(thefuzzyBook.getWorkyear());
            hr.createCell(8).setCellValue(thefuzzyBook.getEmail());
        }

        FileOutputStream fos = new FileOutputStream("F:/TeacherInformation.xls");//先 new 出文件存放的位置
        hwb.write(fos);//写入

        fos.close();//关闭资源

        pushValueToValueStach(true);
        return SUCCESS;
    }

    @Action(value = "user_login", results = {
            @Result(name="success",type="redirect",location="/page/base/teacher.html"),
            @Result(name="error",type="redirect",location="/index.html")

    })
    public String login() {
        // 用户名和密码 都保存在model中
        // 基于shiro实现登录
        System.out.println(model);
        Subject subject = SecurityUtils.getSubject();
        // 用户名和密码信息
        AuthenticationToken token = new UsernamePasswordToken(
                model.getUsername(), model.getPassword());
        try {
            subject.login(token);
            // 登录成功
            // 将用户信息 保存到 Session
            return SUCCESS;
        } catch (AuthenticationException e) {
            // 登录失败
            e.printStackTrace();
            return ERROR;
        }
    }


}
