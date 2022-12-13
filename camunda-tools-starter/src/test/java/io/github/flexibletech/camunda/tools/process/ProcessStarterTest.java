package io.github.flexibletech.camunda.tools.process;

import io.github.flexibletech.camunda.tools.process.start.ProcessStarter;
import io.github.flexibletech.camunda.tools.process.start.StartProcess;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.beans.ApplicationTestService;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.annotation.AnnotationUtils;

@ExtendWith(MockitoExtension.class)
public class ProcessStarterTest {
    @Mock
    private RuntimeService runtimeService;

    @InjectMocks
    private ProcessStarter processStarter;

    @Test
    public void shouldDontStartProcess() {
        var startProcessMethod = ReflectionUtils.findMethod("testApplication", ApplicationTestService.class);
        var startProcessAnnotation = AnnotationUtils.findAnnotation(startProcessMethod, StartProcess.class);

        processStarter.startProcess(startProcessAnnotation, null);

        Mockito.verify(runtimeService, Mockito.times(0)).createProcessInstanceByKey(ArgumentMatchers.anyString());
    }
}
