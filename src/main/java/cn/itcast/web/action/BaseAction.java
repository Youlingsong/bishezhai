package cn.itcast.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
    protected T model;

    protected Integer rows;
    protected Integer page;

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public BaseAction(){
// 构造子类Action对象 ，获取继承父类型的泛型，然后实例化
        // AreaAction extends BaseAction<Area>
        // 需要获取BaseAction<Area>中的Area，然后实例化
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        // 获取类型第一个泛型参数
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("模型构造失败...");
        }
    }

    // 将分页查询结果数据，压入值栈的方法
    protected void pushPageDataToValueStack(Page<T> pageData) {
        Map<String, Object> result = new HashMap<String, Object>();

        long totalElements = pageData.getTotalElements();
        List<T> content = pageData.getContent();

        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        ActionContext.getContext().getValueStack().push(result);
    }

    //抽取公共压栈的方法，把结果压入值栈
    protected void pushValueToValueStach(Object value) {
        ActionContext.getContext().getValueStack().push(value);
    }

    @Override
    public T getModel() {
        return model;
    }
}
