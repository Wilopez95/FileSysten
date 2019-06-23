/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlmanager;

import java.io.File;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Wilson
 */
public class xmlSingletonManager {
    private static xmlSingletonManager singleton;
    
    
    //Lista de nodos con los usuarios
    private NodeList data = null;
    
    
    org.w3c.dom.Document document;
    
    private xmlSingletonManager()
    {
        readxmlfile();
    }
    
        
    private static synchronized void createinstance(){
        if(singleton == null){
            singleton =  new xmlSingletonManager();
        }
    }
    
        public static xmlSingletonManager getInstance(){
        if(singleton == null){
            createinstance();
        }
        return singleton;
    }
        
        
        //File fXmlFile = new File();
        private void readxmlfile() {
            try {
               File archivo = new File("C:/Users/piang/Documents/NetBeansProjects/FileSystem/web/data.xml");
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
               document = documentBuilder.parse(archivo);
               document.getDocumentElement().normalize();
               data = document.getElementsByTagName("Usuario");

           } catch (Exception e) {
               e.printStackTrace();
           }
                 
        }
        
        
        //Inicio de sesion//
        public int Login(String user , String pass){
            for (int i = 0; i < data.getLength(); i++) {
                Node nodo = data.item(i);
                Element element = (Element) nodo;
                String id = element.getAttribute("id");
                
                if((element.getElementsByTagName("Correo").item(0).getTextContent().equals(user)) & (element.getElementsByTagName("Contraseña").item(0).getTextContent().endsWith(pass))){
                 return Integer.parseInt(id);
                } 
            }
            return 0;
        }
        
        
        public void SingUp(String email, String pass) throws TransformerConfigurationException, TransformerException{
            Element root = document.getDocumentElement();

            Element newUser = document.createElement("Usuario");
            newUser.setAttribute("id",Integer.toString(data.getLength()+1));
                
            Element Usr_email = document.createElement("Correo");
            Usr_email.appendChild(document.createTextNode(email));
            newUser.appendChild(Usr_email);
                
            Element Usr_pass =  document.createElement("Contraseña");
            Usr_pass.appendChild(document.createTextNode(pass));
            newUser.appendChild(Usr_pass);
                
            Element Usr_space =  document.createElement("Espacio");
            Usr_space.appendChild(document.createTextNode("15"));
            newUser.appendChild(Usr_space);
                
            Element root_folder = document.createElement("root");
            Element folder_name = document.createElement("name");
            folder_name.appendChild(document.createTextNode("root"));
            root_folder.appendChild(folder_name);
            newUser.appendChild(root_folder);
            
            
            Element shared_folder = document.createElement("shared"); 
            Element foldersh_name = document.createElement("name");
            foldersh_name.appendChild(document.createTextNode("shared"));
            shared_folder.appendChild(foldersh_name);
            newUser.appendChild(shared_folder);
            
                
            root.appendChild(newUser);
            
            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("C:/Users/piang/Documents/NetBeansProjects/FileSystem/web/data.xml");
            transformer.transform(source, result);

        }
        
        
        public void creararchivo( int id ,String pName , String name , String text) throws TransformerException{
            
            String[] paths = pName.split("/");
            
            Node nodo = data.item(id-1);
            
            Element element = (Element) nodo;
            Element cElement =null ;
            
            ArrayList<String> DOM = new ArrayList<String>(); 
            DOM = this.getDOM(id, pName);
            
            if(!DOM.contains(name)){
            
                for (int i = 0; i < paths.length; i++) {
                     cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                     element = cElement;
                }


                Element newfile = document.createElement(name);

                //crea el nombre del nuevo archivo
                Element file_name = document.createElement("name");
                file_name.appendChild(document.createTextNode(name));
                newfile.appendChild(file_name);

                //Obtener la fecha actual y darle formato
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //crea la fecha de creacion de un archivo
                Element file_date = document.createElement("date");
                file_date.appendChild(document.createTextNode(dateFormat.format(date)));
                newfile.appendChild(file_date);

                //crea el texto del archivo
                Element file_data = document.createElement("data");
                file_data.appendChild(document.createTextNode(text));
                newfile.appendChild(file_data);


                cElement.appendChild(newfile);


                DOMSource source = new DOMSource(document);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                StreamResult result = new StreamResult("C:/Users/piang/Documents/NetBeansProjects/FileSystem/web/data.xml");
                transformer.transform(source, result); 
            }
        }
        
        
        public void crearcarpeta(int id,String pName, String foldername) throws TransformerException{
            String[] paths = pName.split("/");
            Node nodo = data.item(id-1);
            
            Element element = (Element) nodo;
            Element cElement =null ;
            
            ArrayList<String> DOM = new ArrayList<String>(); 
            DOM = this.getDOM(id, pName);
            
            if(!DOM.contains(foldername)){
                //Element cElement =  (Element) element.getElementsByTagName("root").item(0);

                for (int i = 0; i < paths.length; i++) {
                     cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                     element = cElement;
                }

                Element newfolder = document.createElement(foldername);

                Element folder_name = document.createElement("name");
                folder_name.appendChild(document.createTextNode(foldername));
                newfolder.appendChild(folder_name);

                cElement.appendChild(newfolder);


                DOMSource source = new DOMSource(document);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                StreamResult result = new StreamResult("C:/Users/piang/Documents/NetBeansProjects/FileSystem/web/data.xml");
                transformer.transform(source, result);
            }
        }
        
        
        public void delete(int id,String pName) throws TransformerException{
            String[] paths = pName.split("/");
            Node nodo = data.item(id-1);
            
            Element element = (Element) nodo;
            Element cElement =null ;
            
            //Element cElement =  (Element) element.getElementsByTagName("root").item(0);
            String rname="";
            for (int i = 0; i < paths.length; i++) {
                 cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                 element = cElement;
                 rname=paths[i];
            }
            if(rname=="root" || rname=="shared" ){
                System.out.println("Este elemento no se puede borrar");
            }else{
                 element.getParentNode().removeChild(cElement);
            }
            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("C:/Users/piang/Documents/NetBeansProjects/FileSystem/web/data.xml");
            transformer.transform(source, result); 
            
        }
        public void updatefile(int id, String pname, String name , String texto) throws TransformerException{
            String[] paths = pname.split("/");
            String a = "";
            for (int i = 0; i < paths.length-1; i++) {
                
                if(i<paths.length-2){
                    a+=paths[i]+"/";
                }else{
                    a+=paths[i];
                }
            }
            
            delete(id, pname);
            creararchivo(id, a, name, texto);
        }
        
        public void SharedFile(int id , String pName , String sheredp) throws TransformerException{
            int idShared=0;
            for (int i = 0; i < data.getLength(); i++) {
                Node nodo = data.item(i);
                Element element = (Element) nodo;
                String idSharedstr = element.getAttribute("id");
                
                if((element.getElementsByTagName("Correo").item(0).getTextContent().equals(sheredp))){
                 idShared=Integer.parseInt(idSharedstr);
                }
            }
            if(idShared != 0){
                String[] paths = pName.split("/");
                Node nodo = data.item(id-1);

                Element element = (Element) nodo;
                Element cElement =null ;
                
                //String rname="";
                for (int i = 0; i < paths.length; i++) {
                     cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                     element = cElement;
                }
                
                String name = cElement.getElementsByTagName("name").item(0).getTextContent();
                String data = cElement.getElementsByTagName("data").item(0).getTextContent();
                creararchivo(idShared, "shared/", name, data);
     

        }
    }
        
    public ArrayList<String> getDOM(int id , String pName ){
            String[] paths = pName.split("/");
            ArrayList<String> lista = new ArrayList<String>();
            Node nodo = data.item(id-1);
            
            Element element = (Element) nodo;
            Element cElement =null ;
            
            //Element cElement =  (Element) element.getElementsByTagName("root").item(0);
            
            for (int i = 0; i < paths.length; i++) {
                //System.out.println(paths[i]);
                cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                element = cElement;
            }
            NodeList newnodelist =cElement.getChildNodes();
            for (int i = 0; i < newnodelist.getLength(); i++) {
                Node n = newnodelist.item(i);
                //Element elemento = (Element) n;
                if(n.getNodeName()!="name" && n.getNodeName()!="#text" ){
                    lista.add(n.getNodeName());
                    System.out.println(n.getNodeName());
                }
            }
        return lista;
    }
    
    public String getFileData(int id , String pName){
                String[] paths = pName.split("/");
                Node nodo = data.item(id-1);

                Element element = (Element) nodo;
                Element cElement =null ;
                
                //String rname="";
                for (int i = 0; i < paths.length; i++) {
                     cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                     element = cElement;
                }
                
                String name = cElement.getElementsByTagName("name").item(0).getTextContent();
                String date = cElement.getElementsByTagName("date").item(0).getTextContent();
                String data = cElement.getElementsByTagName("data").item(0).getTextContent();
                return name+"%"+date+"%"+data;
                
    }
    
    
    public void CopyFile(int id , String pName, String bName) throws TransformerException{
                String[] paths = pName.split("/");
                Node nodo = data.item(id-1);

                Element element = (Element) nodo;
                Element cElement =null ;
                
                //String rname="";
                for (int i = 0; i < paths.length; i++) {
                     cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                     element = cElement;
                }
                
                String name = cElement.getElementsByTagName("name").item(0).getTextContent();
                String date = cElement.getElementsByTagName("date").item(0).getTextContent();
                String data = cElement.getElementsByTagName("data").item(0).getTextContent();
                creararchivo(id, bName, name, data);
                
    }
    
        public void MoveFile(int id , String pName, String bName) throws TransformerException{
                String[] paths = pName.split("/");
                Node nodo = data.item(id-1);

                Element element = (Element) nodo;
                Element cElement =null ;
                
                //String rname="";
                for (int i = 0; i < paths.length; i++) {
                     cElement =  (Element) element.getElementsByTagName(paths[i]).item(0);
                     element = cElement;
                }
                
                String name = cElement.getElementsByTagName("name").item(0).getTextContent();
                String date = cElement.getElementsByTagName("date").item(0).getTextContent();
                String data = cElement.getElementsByTagName("data").item(0).getTextContent();
                creararchivo(id, bName, name, data);
                delete(id, pName);
                
    }
    
        
        

    
}
