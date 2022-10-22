import com.z2011.blogadmin.BlogAdminApplication;
import com.z2011.blogadmin.dao.mapper.PermissionMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BlogAdminApplication.class})
public class BlogAdminApplicationTest {
    @Autowired
    private PermissionMapper permissionMapper;

    // 数据库有效
    @Test
    public void a(){
        System.out.println(permissionMapper.selectById(1));
    }
}