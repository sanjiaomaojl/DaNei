package xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.util.List;

public class ParseXmlDemo {
    public static void main(String[] args) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new FileInputStream("emplist.xml"));
            Element root = doc.getRootElement();

            //获取当前标签的名字
            String rootName = root.getName();
            //获取当前标签下的所有子标签
            List<Element> elements = root.elements();
            //遍历子标签
            for (Element empEle : elements) {
                //获取名字
                Element nameEle = empEle.element("name");
                //获取当前标签中的文本(确定为文本而不是子标签)
                String name = nameEle.getTextTrim();
                System.out.println(name);
                //获取年龄
                int age = Integer.parseInt(empEle.elementTextTrim("age"));

                //获取属性
                Attribute attr = empEle.attribute("id");
                //属性名
                attr.getName();
                //属性值
                attr.getValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
