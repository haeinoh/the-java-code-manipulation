package org.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // type으로 한정되도 애노테이션은 interface, class, enum 3개에서 사용할 수 있다.
@Retention(RetentionPolicy.SOURCE) // bytecode에서 필요없다. 오로지 소스레벨에서만 필요함
public @interface Magic {

}
