package xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用DOM生成xml
 */
public class WriteXmlDemo {
    public static void main(String[] args) {
        List<Emp> empList = new ArrayList<>();
        empList.add(new Emp("张三",21));
        empList.add(new Emp("李四",22));
        empList.add(new Emp("王五",23));
        empList.add(new Emp("赵六",24));
        empList.add(new Emp("钱七",25));

        XMLWriter writer = null;

        /**
         * 将empList集合中的员工信息保存到myemp.xml文档中
         */
        try {
            //创建头
            Document document = DocumentHelper.createDocument();
            //根标签，只能创建一次
            Element root = document.addElement("list");
            /*将empList集合中的每个员工信息以<emp>标签保存到<list>标签中
            调用addElement(String name)  增加子标签，并将其返回
            addText(String text)  向当前标签中添加文本信息，
            返回值为当前标签
            addAttribute(String name,String value) 给当前标签
            添加属性，返回值为当前标签*/
            for (Emp emp : empList) {
                //向跟标签<list>中添加子标签<emp>
                Element empEle = root.addElement("e");
                //向跟标签<emp>中添加子标签<name>
                Element nameEle = empEle.addElement("name");
                //增加文本
                nameEle.addText(emp.getName());
                //增加age标签
                String age = String.valueOf(emp.getAge());
                empEle.addElement("age").addText(age);

                //增加属性：id  忘记创建ID了，age顶一下
                empEle.addAttribute("id",emp.getAge()+"");

                FileOutputStream fos = new FileOutputStream("myemp.xml");
                writer = new XMLWriter(fos, OutputFormat.createCompactFormat());
                writer.write(document);
            }
            System.out.println("写出完毕");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
