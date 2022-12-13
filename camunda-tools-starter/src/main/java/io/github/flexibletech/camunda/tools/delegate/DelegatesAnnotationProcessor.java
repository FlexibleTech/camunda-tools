package io.github.flexibletech.camunda.tools.delegate;

import com.google.auto.service.AutoService;
import io.github.flexibletech.camunda.tools.common.AbstractAnnotationProcessor;
import io.github.flexibletech.camunda.tools.process.values.ProcessValues;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.flexibletech.camunda.tools.delegate.Delegates")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DelegatesAnnotationProcessor extends AbstractAnnotationProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            var annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            boolean isProcessKeyValueExists = checkProcessKeyValueAnnotation(annotatedElements);
            if (!isProcessKeyValueExists) return false;
            boolean isProcessValuesExists = checkProcessValuesAnnotation(annotatedElements);
            if (!isProcessValuesExists) return false;
        }
        return true;
    }

    private boolean checkProcessValuesAnnotation(Set<? extends Element> annotatedElements) {
        for (var element : annotatedElements) {
            var processValues = findAnnotationByType(element, ProcessValues.class);
            if (CollectionUtils.isEmpty(processValues)) {
                processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, String.format(
                                "ProcessValues missed for method %s of class %s", element.getSimpleName(), classNameFrom(element)));
                return false;
            }
        }
        return true;
    }
}
