package com.z2011.blogapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.z2011.blogapi.Service.SysUserService;
import com.z2011.blogapi.Service.TagService;
import com.z2011.blogapi.dao.mapper.ArticleBodyMapper;
import com.z2011.blogapi.dao.mapper.ArticleMapper;
import com.z2011.blogapi.dao.mapper.ArticleTagMapper;
import com.z2011.blogapi.dao.pojo.Article;
import com.z2011.blogapi.dao.pojo.Comment;
import com.z2011.blogapi.utils.JWTUtils;
import com.z2011.blogapi.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = {BlogApiApplication.class})
@Slf4j
public class BlogApiApplicationTests {
    @Autowired
    private ArticleMapper amapper;
    @Autowired
    private SysUserService service;
    @Autowired
    private TagService tagService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ArticleBodyMapper bodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private A a;

    @Test
    //QueryWrapper的使用，时间的转换
    public void contextLoads() {
        System.out.println(amapper.selectById(1));
        Page<Article> articlePage = new Page<>(1, 5);
        QueryWrapper query = new QueryWrapper();
        //order by createtime desc
        query.orderByDesc("create_date");
        //lambda表达式更加方便，有提示比较好
//        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper();
//        lambdaQueryWrapper.orderByDesc(Article::getCreateDate);
        Page<Article> articlePage1 = amapper.selectPage(articlePage, query);

        for (Article record : articlePage1.getRecords()) {
            //新建import org.joda.time.DateTime;参数为string，然后它重写了tostring可以带格式
            //2021-06-18 23:55
            //这是时间戳转时间，那存的时候呢？
            System.out.println(new DateTime(record.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        }
        List<Article> records = articlePage1.getRecords();
        //要转vo，首先要list转单，vo有什么区别呢？时间string、作者string、tag，为什么要tagId，为什么要判断是否需要tag？，懒的多表，


    }

    /**
     * 测试返回字段和拼接sql
     */
    @Test
    public void contextLoads3() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper();
        /* select * from ms_article ORDER BY view_counts desc limit 5 */
        wrapper.orderByDesc(Article::getViewCounts).last("limit " + 10);
        List<Article> articles = amapper.selectList(wrapper);
        for (Article article : articles) {
            System.out.println(article.getViewCounts());
        }

    }

    //为什么要copy
    //string字符串很好用，但有一个问题，他是不可变的，那么想要对自身操作就需要创建新对象，虽然我们jvm对其进行了字符串常量池机制，但这样的操作还是负担太大了，
    //那么在操作字符串自身时就不应该使用string，有两个选择stringBuffer和stringBuilder，我么都知道他们是可变的，但
    //缓存的确加了同步锁，到不如说，它的大多数方法都加了synchronized关键字，所以它其实是线程安全的，也就是说在多线程情况下最好使用这个
    //那stringBuilder有什么用呢，它肯定就不是线程安全的，那它肯定就没有用了，但实际上不是，它是string使用+拼接字符串时默认调用的类，
    // 我们说string是不变的线程安全的，那为什么用builder的append方法不用自己的？
//    我们首先可以看到它没有继承AbstractStringBuilder也没有append方法，那么如果要用基本数据类型常用的+拼接就会不支持，想要让他支持就必须要

    /**
     * 文章归档
     * 2021-02-25
     * 2021-48-22
     * 2018-48-17
     * 2021-35-18
     * 2021-26-18
     * 2021-55-18
     * 获得了时间进行了转换怎么进行分组呢？
     */
    @Test
    public void contextLoads4() {
//        for (Article article : amapper.selectList(null)) {
//            System.out.println(new DateTime(article.getCreateDate()).toString("yyyy-mm-dd"));
//        }
        System.out.println(new DateTime().toDate().getTime());

    }

    /**
     * 登录逻辑：使用jwt对userId生成有期token、存入redis
     * jwt生成的token导到底长什么样
     * 通过1404446129264832513生成的token是由三部分组成
     * eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDgzNzYxNTUsInVzZXJJZCI6MTQwNDQ0NjEyOTI2NDgzMjUxMywiaWF0IjoxNjQ3NDg3MTIyfQ.7etOtzZk58syvsJsNBt30NVG3Ar2dv4o0_O5cG9hpoI
     * 再生成一遍最后一个就会不一样，因为里面加了时间参数，不过它这个日期是在这里设置还是redis设置就不知道
     * 解密返回map对象{exp=1648376155, userId=1404446129264832513, iat=1647487122}，只有userid是我知道的
     * eyJhbGciOiJIUzI1NiJ9.eyLliqDlr4bpu5jorqToh6rluKZJZCI6MTIzLCJleHAiOjE2NDgzNzcyNzUsInVzZXJJZCI6MTQwNDQ0NjEyOTI2NDgzMjUxMywiaWF0IjoxNjQ3NDg4MjQyfQ.fP9IUiBfRleLVQTlige38p84Cy0CSKLccIVVuv2_gvI
     */
    @Test
    public void contextLoads5() {
//        System.out.println(JWTUtils.createToken(1404446129264832513L));
        Map<String, Object> stringObjectMap = JWTUtils.checkToken("eyJhbGciOiJIUzI1NiJ9.eyLliqDlr4bpu5jorqToh6rluKZJZCI6MTIzLCJleHAiOjE2NDgzNzcyNzUsInVzZXJJZCI6MTQwNDQ0NjEyOTI2NDgzMjUxMywiaWF0IjoxNjQ3NDg4MjQyfQ.fP9IUiBfRleLVQTlige38p84Cy0CSKLccIVVuv2_gvI");
        System.out.println(stringObjectMap);
        System.out.println(DigestUtils.md5Hex(123 + "&%&*（（……%…………"));
//        redis使用

//        System.out.println(redisTemplate.hasKey("123"));
//        redisTemplate.boundValueOps("999").set("888");

    }

    /**
     * 记录常用redis使用
     * opsForCluster方法获取的ClusterOperations用于集群操作了，所以没有set
     * 直接设置键对值、时间：redisTemplate.boundValueOps("555").set("77",1, TimeUnit.MINUTES);
     * 间接设置时间redisTemplate.boundValueOps("555").expire(1,TimeUnit.MINUTES);
     * 获取：stringOperations.get()
     */
    @Test
    public void contextLoads6() {
        //通过key判断是否存在
//        System.out.println(redisTemplate.hasKey("123"));
//        //存入key value
//        BoundValueOperations<String, String> stringOperations = redisTemplate.boundValueOps("555");
//        redisTemplate.boundValueOps("999").set("888" );
//        redisTemplate.boundValueOps("555").set("77",1, TimeUnit.MINUTES);
//        redisTemplate.boundValueOps("555").expire(1,TimeUnit.MINUTES);
//        System.out.println(stringOperations.getExpire());
//        System.out.println(stringOperations.get());
        System.out.println(redisTemplate.boundValueOps("123").get());
    }

    /**
     * 注册逻辑
     * 注册是什么？我先完成了登录逻辑，能够让浏览器保存登录状态，login完成的是返回token，在用户登录输入帐号密码后，我对它判断返回token，那/user/currentUser是做什么的？
     * 我一共写了三个接口/login、/user/currentUser、/logout，第一个登录成功后返回token，第二个通过token来返回用户信息、第三个使token过期
     * 用这些来完成登录，那么注册呢？
     * /register注册返回
     * 有的时候我会不明白自己在做什么
     */
    @Test
    public void contextLoads7() throws CloneNotSupportedException {
//        Article article = new Article();
//        article.setId(1L);
//        Article clone = article.clone();
//        System.out.println(article == clone);
//        System.out.println(article.getId().equals(clone.getId()));
//        System.out.println(article.getId() == clone.getId());
//        TagVo tagVo = new TagVo(1L, "12323");
//        System.out.println(tagVo);
//        System.out.println(JSON.toJSONString(tagVo));
//        System.out.println(1231231);


    }

    /**
     * 数组、集合、队列
     * Arrays存储着对数组的方法，除去排序，我们一般会把他们转化成集合来操作会更加方便
     * Arrays.asList：返回由指定数组支持的固定大小的列表，其实就是ArrayList，可以和Collection.toArray相互转换
     * 所以我们用new ArrayList(Arrays.asList(a));来快速初始化ArrayList
     * 集合Collection也没定义什么方法
     * 队列List：
     */
    @Test
    public void test8() {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("123");
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());

        Thread thread1 = new Thread();
        System.out.println(thread1);
        Thread thread2 = new Thread();
        System.out.println(thread2.getId());

    }

    /**
     * mapper.update传入的pojo其属性只要不是null就会在更新时传入，框架这样肯定是为了方便，也的确是定义没有规范好导致的，改回来就可以，
     * 记住不要设置默认值，不然update时哪怕不指定修改的字段也会用默认值自动进行覆盖
     * <p>
     * 来测试下bean
     */
    @Test
    public void test9() {
//        Article article = new Article();
//        System.out.println(article);
        Comment comment = new Comment();
        comment.setCreateDate(123L);
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        System.out.println(commentVo);

    }

    /**
     * 时间的转换
     */
    @Test
    public void test10() {
        val l = System.currentTimeMillis();
        System.out.println(l);
        DateTime dateTime = new DateTime(l);
        System.out.println(dateTime.toString("yyyy-MM-dd hh:mm"));
    }

    /**
     * 存文章
     * 存文章需要的参数我知道、
     */
    @Test
    public void test11() {
        //关于id，直接insert会报错，因为数据库weight设置了not null也没有默认值
        Article article = new Article();
        article.setWeight(0);
        amapper.insert(article);
        System.out.println(article.getId());//1508379307901407233
        //可以看出的确可以获得到程序生成的id，关键在于我还要存别的东西，这样直接存不是有问题？
    }

    @Test
    public void test12() {
        Page<Article> articlePage = new Page<>(2, 5);
        LambdaQueryWrapper<Article> query = new LambdaQueryWrapper();
        query.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage1 = amapper.selectPage(articlePage, query);
        for (Article record : articlePage1.getRecords()) {
            System.out.println(record.getId());
        }
    }

    @Test
    public void test13() throws IOException {
        File file = new File("/home/z2011/桌面/项目/vhr-20180205/blog3/src/main/java/com/z2011/blog3/Service/Impl/ArticleServiceImpl.java");
//        System.out.println(file.getName()+file.isDirectory());
//        File file2 = new File("/home/z2011/桌面/项目/vhr-20180205/blog3/src/main/resources");
//        System.out.println(file2.getName()+file2.isDirectory());
        //对配置文件读取或写入，先来个简单到的，读取
//        FileReader fileReader = new FileReader(file);
////        System.out.println(fileReader.read());
//        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
//        BufferedReader bufferedReader = new BufferedReader(isr);
//        String str;
//        while ((str=bufferedReader.readLine())!=null)
//            System.out.println(str);
//        System.out.println(UUID.randomUUID());//a5777d0b-5d52-4f44-8f0a-f0590339c1bf
//        String str="照片.png";
//        System.out.println(str.substring(str.lastIndexOf(".")+1, str.length()));
//        System.out.println(str.substring(0, 1));
        System.out.println(UUID.randomUUID());

    }

    /**
     * 慢了好多的
     * d41d8cd98f00b204e9800998ecf8427e
     */
    @Test
    public void test14() {
//    System.out.println(DigestUtils.md5Hex(""));
        int[] a = new int[5];
        System.out.println(a.length);
        a[0] = 1;
        for (int i : a) {
            System.out.println(i);
        }

    }
    @Test
    public void test15(int[] numbers,int target) {
        int[] intnum=new int[2];
        for (int i = 0; i < numbers.length-1; i++) {
            if(numbers[i]+numbers[i+1]==target){
                intnum[0]=i;
            }
        }
    }
}