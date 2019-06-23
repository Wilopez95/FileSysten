/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlmanager;

import javax.xml.transform.TransformerException;

/**
 *
 * @author Wilson
 */
public class XmlManager {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TransformerException {
        int id=0;
        xmlSingletonManager.getInstance();
        int acceso = xmlSingletonManager.getInstance().Login("Armando95@gmail.com","Armando");
        
        if(acceso != 0){
            System.out.println("Acceso permitido al usuario con el id " + acceso);
            id = acceso;
        }else{
            System.out.println("Acceso denegado");
        }
        
        
        // Crear nuevo usuario
       //xmlSingletonManager.getInstance().SingUp("tuche@gmail.com","Pepegrillo");
       
       //crear un nuevo archivo
        //xmlSingletonManager.getInstance().creararchivo(id, "root/montaña", "nose.txt", "somethin");
       
       //crear una nueva carpeta
       //xmlSingletonManager.getInstance().crearcarpeta(id,"root", "fotos");
       
       //borrar un archivo o una carpeta
       //xmlSingletonManager.getInstance().delete(id, "root/montaña/nose.txt");
        
        //Actualizar un archivo
        //xmlSingletonManager.getInstance().updatefile(id, "root/montaña/nose.txt", "nose.txt", "vamo a ver si sirve");
        
        //Compartir un archivo
        //xmlSingletonManager.getInstance().SharedFile(id, , "Wilopez95@gmail.com");
        
        //Obtener lista de archivos
        //xmlSingletonManager.getInstance().getDOM(id,"root");
        
        //optener los datos de un archivo.
        //System.out.println(xmlSingletonManager.getInstance().getFileData(id,"root/montaña/nose.txt" ));
       
        
        //Copiararchivos
        //xmlSingletonManager.getInstance().CopyFile(id, "root/montaña/nose.txt", "root/rio");
        
        //Mover archivos
        //xmlSingletonManager.getInstance().MoveFile(id, "root/montaña/nose.txt", "root/rio");
        
        
    }
    
}
