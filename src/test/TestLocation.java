import com.ecmoho.data.LocationCount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by meidejing on 2016/8/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestLocation {
    @Resource(name = "locationCount")
    private LocationCount locationCount;
    @Test
    public void test1(){
        locationCount.updatelocationCount();
    }
}
