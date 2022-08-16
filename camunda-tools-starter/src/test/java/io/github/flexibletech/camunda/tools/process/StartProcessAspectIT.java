package io.github.flexibletech.camunda.tools.process;

import io.github.flexibletech.camunda.tools.Application;
import io.github.flexibletech.camunda.tools.config.TestCamundaConfig;
import io.github.flexibletech.camunda.tools.process.start.StartProcessAspect;
import io.github.flexibletech.camunda.tools.values.beans.ApplicationTestService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;


@Import(TestCamundaConfig.class)
@ExtendWith(ProcessEngineExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class StartProcessAspectIT {
    @SpyBean
    private StartProcessAspect startProcessAspect;

    @Autowired
    private ApplicationTestService applicationTestService;

    @Test
    @Deployment(resources = "test_flow.bpmn")
    public void shouldCreateStartProcessAspectForService() {
        applicationTestService.testApplication();

        Assertions.assertTrue(AopUtils.isAopProxy(applicationTestService));
        Assertions.assertTrue(AopUtils.isCglibProxy(applicationTestService));
        Assertions.assertEquals(AopProxyUtils.ultimateTargetClass(applicationTestService), ApplicationTestService.class);

        Mockito.verify(startProcessAspect, Mockito.times(1))
                .execute(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

}
