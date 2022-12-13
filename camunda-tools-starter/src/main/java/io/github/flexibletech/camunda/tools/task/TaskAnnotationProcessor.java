package io.github.flexibletech.camunda.tools.task;

import com.google.auto.service.AutoService;
import io.github.flexibletech.camunda.tools.common.AbstractAnnotationProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "io.github.flexibletech.camunda.tools.task.user.UserTask",
        "io.github.flexibletech.camunda.tools.task.receive.ReceiveTask"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class TaskAnnotationProcessor extends AbstractAnnotationProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            var annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            boolean isProcessKeyValueExists = checkProcessKeyValueAnnotation(annotatedElements);
            if (!isProcessKeyValueExists) return false;
        }
        return true;
    }
}
