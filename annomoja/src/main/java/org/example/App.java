package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Moja moja = new MagicMoja(); // MagicMoja : annotation processor를 통해 생성할 클래스
        System.out.println(moja.pullOut());
    }
}
