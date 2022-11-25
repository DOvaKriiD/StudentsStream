import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Students {
    private List <Student> StudentList;

     public Students(String xmlPath){
         StudentList = new ArrayList<>();
         this.readFromXml(xmlPath);
     }

    private void readFromXml(String xmlPath) {
        List<Student> students = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            dbf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlPath));
            doc.getDocumentElement().normalize();

            NodeList institutionList = doc.getElementsByTagName("educational_institution");
            for (int temp = 0; temp < institutionList.getLength(); temp++) {

                Node institution = institutionList.item(temp);
                if (institution.getNodeType() == Node.ELEMENT_NODE) {
                    Element institutionElem = (Element) institution;
                    NodeList studentList = institutionElem.getElementsByTagName("student");
                    for (int std = 0; std < studentList.getLength(); std++){
                        Node studentNode = studentList.item(std);
                        if (studentNode.getNodeType() == Node.ELEMENT_NODE){
                            Element studentElem = (Element) studentNode;

                            String full_name = studentElem.getElementsByTagName("full_name").item(0).getTextContent();
                            String id = studentElem.getAttribute("id");
                            String educational_institution = institutionElem.getAttribute("title");
                            Student tempStudent = new Student(full_name,id,educational_institution);

                            NodeList gradeList = studentElem.getElementsByTagName("grade");
                            for (int grd = 0; grd < gradeList.getLength(); grd++){
                                Node gradeNode = gradeList.item(grd);
                                if (gradeNode.getNodeType() == Node.ELEMENT_NODE){
                                    Element gradeElem = (Element) gradeNode;
                                    tempStudent.addGrade(gradeElem.getAttribute("subject"),gradeElem.getTextContent());
                                }
                            }

                            this.StudentList.add(tempStudent);
                        }
                    }

                }
            }



        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void PrintAlphabeticalNames(){
         this.StudentList.stream()
                 .sorted(Comparator.comparing(s -> s.full_name))
                 .forEach(s -> System.out.println(s.full_name));
    }

    // выводит студентов, у которых все экзамены больше X
    public void PrintMoreThanGrade(int X){
        this.StudentList.stream()
                .filter(student -> {
                    for(int i = 0;i< student.grade.size();i++){
                        if (Integer.parseInt(student.grade.get(i)) <= X) return false;
                    }
                    return true;
                })
                .forEach(s -> System.out.println(s.full_name));
    }
}
