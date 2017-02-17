package luo.myapplication.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 数据库操作示例bean
 */

@Table(name = "PersonBean")
public class PersonBean {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
