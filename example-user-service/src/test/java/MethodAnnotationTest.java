import com.emt.morph.Feedback;
import com.emt.morph.MorphClient;
import com.emt.morph.MorphClientServiceImpl;
import com.emt.morph.UserApiService;
import com.emt.morph.annotation.processor.JerseyAnnotationProcessor;
import com.emt.morph.config.InvocationContextConfig;
import org.junit.Test;

public class MethodAnnotationTest {


    @Test
    public void defaultTest() {
        MorphClient morphClient = new MorphClientServiceImpl(new JerseyAnnotationProcessor(), new InvocationContextConfig(true));
        UserApiService testService = morphClient.morph(UserApiService.class);


        testService.handleMultiPartRequest("hello", new Feedback());
    }
}
