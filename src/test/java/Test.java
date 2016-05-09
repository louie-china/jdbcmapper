import com.louie.mapper.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
@Table(name = "test")
public class Test extends BaseEO {
    @Id
    @Generate
    private int id;
    @Column(name = "name")
    private String name;
    @Column
    private String logs;

    private String email;
    @Transient
    private String none;


    public static void main(String[] args) {

        Test test = new Test();
        test.setId(0);
        test.setName("louie");
        test.setEmail("louieluo@foxmail.com");
        test.setLogs("this is a 'mapper' for spring-jdbc");
        test.setNone("this is a test");
        System.out.println(test.selectSQL(null));
        System.out.println(test.updateSQL(null));
        System.out.println(test.insertSQL());

//        JdbcTemplate jdbcTemplate = new JdbcTemplate(null);//This is an example of a JDBC call, so there is no data source
//        List<Test> tests = jdbcTemplate.query(test.selectSQL("name=? and logs=?"), new Object[]{"louie", "test"}, test);//This is an example of a query with parameters
//        int count = jdbcTemplate.update(test.insertSQL());//This is an example of an insert
//        count=jdbcTemplate.update(test.updateSQL(null));//This is a default update
//        count=jdbcTemplate.update(test.updateSQL("name=? or logs=?"),new Object[]{"louie","this is a 'mapper' for spring-jdbc"});
    }


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

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }
}
