package io.github.flexibletech.camunda.tools.common;

import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class AbstractAnnotationProcessor extends AbstractProcessor {

    protected boolean checkProcessKeyValueAnnotation(Set<? extends Element> annotatedElements) {
        for (Element element : annotatedElements) {
            List<ProcessKeyValue> processKeyValues = findProcessKeyValueAnnotations(element);
            if (processKeyValues.size() != 1) {
                processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, String.format(
                                "Method %s of class %s should contain only one process key", element.getSimpleName(), classNameFrom(element)
                        ));
                return false;
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

    protected String classNameFrom(Element element) {
        return ((TypeElement) (element.getEnclosingElement()))
                .getQualifiedName()
                .toString();
    }

}
