package io.github.flexibletech.camunda.tools.delegate;

import com.google.auto.service.AutoService;
import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.flexibletech.camunda.tools.delegate.Delegate")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DelegateAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : annotatedElements) {
                List<ProcessKeyValue> processKeyValues = findProcessKeyValueAnnotations(element);
                if (processKeyValues.size() != 1) {
                    processingEnv.getMessager()
                            .printMessage(Diagnostic.Kind.ERROR, "Method annotated with Delegate should contain only one process key");
                    return false;
                }
            }
        }
        return true;
    }

    private List<ProcessKeyValue> findProcessKeyValueAnnotations(Element element) {
        ExecutableElement executableElement = (ExecutableElement) element;
        return executableElement.getParameters()
                .stream()
                .flatMap(p -> Arrays.stream(p.getAnnotationsByType(ProcessKeyValue.class)))
                .collect(Collectors.toList());
    }

}
