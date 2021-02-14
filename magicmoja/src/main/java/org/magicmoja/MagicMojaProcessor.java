package org.magicmoja;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class) // 프로세서 등록을 위한 manifest 파일을 컴파일 할 때 자동적으로 만들어준다.
public class MagicMojaProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Magic.class.getName());
    }

    // 지원하는 소스 버젼
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    // 소스 코드의 구성요소를 element라고 한다.
    @Override // roundEnv : 애노테이션 프로세스는 라운드 개념으로 작동한다. 각 라운드마다 프로세스가 가지고 있는 element를 찾으면 처리를 시킨다.
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /**
         * true를 return한다는 의미는 해당 애노테이션을 더 이상 처리 하지 않아도 된다라는 뜻이다.
         * 현재 예제는 magic에 특화되어서 true를 리턴하지만, 때에 따라서는 다음 라운드로 넘겨야할 수도 있다.(false 리턴)
         */
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Magic.class);
        for(Element element: elements) {
            // 우리는 element에서 인터페이스만을 원한다.
            // 따라서 element kind가 인터페이스가 아닌 경우에 아래와 같이 처리한다.
            Name elementName = element.getSimpleName();
            if(element.getKind() != ElementKind.INTERFACE) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Magic annotation cannot be used on " + elementName);
            } else { // log
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + elementName);
            }
        }
        return true;
    }
}
