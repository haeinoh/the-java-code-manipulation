package org.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
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

            //javapoet : 새로운 소스 코드 생성 유틸리티
            TypeElement typeElement = (TypeElement) element;
            // poet
            ClassName className = ClassName.get(typeElement);

            // 메모리 상에 객체로만 클래스를 정의한 것이다.
            MethodSpec pullOut = MethodSpec.methodBuilder("pullOut")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", "Rabbit!")
                    .build();

            TypeSpec magicMoja = TypeSpec.classBuilder("MagicMoja")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(className) //매직이라는 annotation이 붙어있는 클래스이름을 구현하는 implements
                    .addMethod(pullOut) // magicmoja안에 pullout 메소드를 넣음
                    .build();

            // Filer 인터페이스 : 소스 코드, 클래스 코 및 리소스를 생성할 수 있는 인터페이스
            Filer filer = processingEnv.getFiler();

            // javafile 생성
            try {
                JavaFile.builder(className.packageName(), magicMoja)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR " + e);
            }
        }

       return true;
    }
}
