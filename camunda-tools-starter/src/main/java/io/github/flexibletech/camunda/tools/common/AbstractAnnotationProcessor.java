package io.github.flexibletech.camunda.tools.common;

import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class AbstractAnnotationProcessor extends AbstractProcessor {

    protected boolean checkProcessKeyValueAnnotation(Set<? extends Element> annotatedElements) {
        for (var element : annotatedElements) {
            var processKeyValues = findAnnotationByType(element, ProcessKeyValue.class);
            if (processKeyValues.size() != 1) {
                processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, String.format(
                                "Method %s of class %s should contain only one process key", element.getSimpleName(), classNameFrom(element)));
                return false;
            }
        }
        return true;
    }

    protected <T extends Annotation> List<T> findAnnotationByType(Element element, Class<T> clazz) {
        var executableElement = (ExecutableElement) element;
        return executableElement.getParameters()
                .stream()
                .flatMap(p -> Arrays.stream(p.getAnnotationsByType(clazz)))
                .collect(Collectors.toList());
    }

    protected String classNameFrom(Element element) {
        return ((TypeElement) (element.getEnclosingElement()))
                .getQualifiedName()
                .toString();
    }
}
