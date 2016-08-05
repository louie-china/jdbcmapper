package com.lll.xx;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * 使用HtmlUnit模拟浏览器执行JS来获取网页内容
 * @author 杨尚川
 */
public class HtmlUnitTest {
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        while (true)
        try {
            engine.eval(new FileReader("E:\\csdn.js"));
            Object result = engine.eval("test()");
            System.out.println(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}