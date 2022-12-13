package io.github.flexibletech.camunda.tools.process.start;

import com.google.auto.service.AutoService;
import io.github.flexibletech.camunda.tools.common.AbstractAnnotationProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.flexibletech.camunda.tools.process.start.StartProcess")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class StartProcessAnnotationProcessor extends AbstractAnnotationProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            var annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            for (var element : annotatedElements) {
                if (!isAnnotatedMethodReturnValue(element)) {
                    processingEnv.getMessager()
                            .printMessage(Diagnostic.Kind.ERROR, "Method annotated with StartProcess should return value");
                    return false;
                }
            }
            var groupedAnnotatedElements = annotatedElements
                    .stream()
                    .collect(Collectors.groupingBy(this::classNameFrom));

            for (var entry : groupedAnnotatedElements.entrySet()) {
                if (entry.getValue().size() > 1) {
                    processingEnv.getMessager()
                            .printMessage(Diagnostic.Kind.ERROR,
                                    String.format("There can be only one annotation StartProcess for a class %s", entry.getKey()));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAnnotatedMethodReturnValue(Element element) {
        var executableElement = (ExecutableElement) element;
        var voidTypeKind = executableElement.getReturnType().getKind();

        return voidTypeKind != TypeKind.VOID;
    }
}
