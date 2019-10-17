import com.panicbuying.PanicbuyingServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试类
 *
 * @author: zhaozhonghui
 * @create: 2019-04-28 15:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PanicbuyingServiceApplication.class)
public class MyTest {
    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void test1(){
        System.out.println("HelloWorld");
    }

    @Test
    public void test2(){
        Date date = new Date(1558669502L);
        System.out.println(date);
    }


}
