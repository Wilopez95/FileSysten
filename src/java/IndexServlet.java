/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import xmlmanager.*;

/**
 *
 * @author Wilson
 */
@WebServlet(name = "ServletIndex", urlPatterns = {"/ServletIndex"})
public class IndexServlet extends HttpServlet {
    
    private final xmlSingletonManager manager = xmlSingletonManager.getInstance();
    //private int id=0;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected String mostrarDIR(int id, String path, int cont){
        
        String tree="";
        String temporal = "";
        String[] paths = path.split("/");
        ArrayList<String> DOM = new ArrayList<String>();
        
        if(cont==-1) return "<ul id=\"myUL\">"+mostrarDIR(id, path, cont+1)+"</ul>";
        
        if(cont==paths.length) return "";
        
        else{
            
            temporal+=paths[0];
            
            if(paths.length>1){
                for (int i = 1; i < cont+1; i++) {
                    temporal+="/"+paths[i];
                }
            }
            
            DOM = manager.getDOM(id, temporal);
            
            tree+=  "<li>\n" +
                    "  <img  align=\"middle\" src=\"rsc/img_avatar2.png\"\n" +
                    "  width=\"5%\" height=\"auto\"\n" +
                    "  alt=\"Avatar\" class=\"avatar\"/>\n" +
                    "  <a class=\"folderh\" href=\"ServletIndex?path="+temporal+"\"><b>"+paths[cont]+"</b>\n" +
                    "  </a>\n"+"<ul class=\"nested active\">";
            
            if(cont<paths.length-1) cont++;
            
            for (int i = 0; i < DOM.size(); i++) {
                if(!DOM.get(i).contains(".txt")){
                    if(paths[cont].equals(DOM.get(i)))
                        tree+=mostrarDIR(id, path, cont);
                    else{
                        tree+=  "<li>\n" +
                                "  <img  align=\"middle\" src=\"rsc/img_avatar2.png\"\n" +
                                "  width=\"5%\" height=\"auto\"\n" +
                                "  alt=\"Avatar\" class=\"avatar\"/>\n" +
                                "  <a class=\"folderh\" href=\"ServletIndex?path="+temporal+"/"+DOM.get(i)+"\"><b>"+DOM.get(i)+"</b>\n" +
                                "  </a>\n" +
                                "</li>\n";
                    }
                }
            }
            
            tree+="</ul></li>";
            
            return tree;
        }
    }
    
    protected String mostrarFILES(int id, String path){
        
        ArrayList<String> DOM = new ArrayList<String>();
        String[] paths = path.split("/");
        
        if(paths[paths.length-1].contains(".txt")){
            path=paths[0];
            if(paths.length>1){
                for (int i = 1; i < paths.length-1; i++) {
                    path+="/"+paths[i];
                }
            }
        }
        
        DOM = manager.getDOM(id, path);
        
        String files="";
        
        for (int i = 0; i < DOM.size(); i++) {
            if(DOM.get(i).contains(".txt")){
                files+= "<li>\n" +
                        "  <img  align=\"middle\" src=\"rsc/Files.png\"\n" +
                        "  width=\"15%\" height=\"auto\"\n" +
                        "  alt=\"Avatar\" class=\"avatar\"/>\n" +
                        "  <a class=\"folderh\" href=\"ServletIndex?path="+path+"/"+DOM.get(i)+"\"><b>"+DOM.get(i)+"</b>\n" +
                        "  </a>\n" +
                        "</li>\n";
            }
           
        }
        
        return files;
        
    }
    
    protected String infoFILE(int id, String path){
        
        String[] info;
        
        if(path.contains(".txt")){
            info = manager.getFileData(id, path).split("%");
            path=   "<h3>Nombre: "+info[0]+"</h3>\n" +
                    "<p style=\"text-align: justify;\">Info: "+info[2]+"</p>\n" +
                    "<h5>Fecha: "+info[1]+"</h5>";
            return path;
        }
        
        return  "<h3>Nombre:</h3>\n" +
                "<p style=\"text-align: justify;\">Info:</p>\n" +
                "<h5>Fecha:</h5>";
        
    }
    
    protected void Crear(int id, String path, String info){
        String[] infos = info.split(":");
        
        try {
            if(infos.length>1) manager.creararchivo(id, path, infos[0]+".txt", infos[1]);
            else manager.crearcarpeta(id, path, infos[0]);
        } catch (TransformerException ex) {
            //;
        }
    }
    
    protected String Borrar(int id, String path){
        
        String[] paths = path.split("/");
        
        try {
            manager.delete(id, path);
            path=paths[0];
            if(paths.length>1){
                for (int i = 1; i < paths.length-1; i++) {
                    path+="/"+paths[i];
                }

            }
            return path;
        } catch (TransformerException ex) {
            //;
        }
        return path;
    }
    
    protected void Copiar(int id, String path, String fin){
        try {
            manager.CopyFile(id, path, fin);
        } catch (TransformerException ex) {
            //;
        }
    }
    
    protected void Mover(int id, String path, String fin){
        try {
            manager.MoveFile(id, path, fin);
        } catch (TransformerException ex) {
            //;
        }
    }
    
    protected void Update(int id, String path, String info){
        String[] infos = info.split(":");
        String[] paths = path.split("/");
        
        try {
            if(infos.length>1) manager.updatefile(id, path, infos[0]+".txt", infos[1]);
            else manager.updatefile(id, path, paths[paths.length-1], infos[0]);
        } catch (TransformerException ex) {
            //
        }
    }
    
    protected void Share(int id, String path, String email){
        try {
            manager.SharedFile(id, path, email);
        } catch (TransformerException ex) {
            //
        }
    }
    
    protected String inFILE(String path){
        if(path.contains(".txt")) return "disabled";
        return "";
    }
    
    protected String iFILE(String path){
        if(!path.contains(".txt")) return "disabled";
        return "";
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session=request.getSession(false);
        int id= (int)session.getAttribute("id");
        String path = request.getParameter("path");
        String path2 = request.getParameter("path2");
        String fileo = request.getParameter("fileo");
        if(fileo!=null){
            switch(fileo){
                case "Crear":
                    this.Crear(id, path, path2);
                    break;
                    
                case "Borrar":
                    path=this.Borrar(id, path);
                    break;
                    
                case "Copiar":
                    this.Copiar(id, path, path2);
                    break;
                 
                case "Mover":
                    this.Mover(id, path, path2);
                    break;
                    
                case "Update":
                    this.Update(id, path, path2);
                    break;
                
                case "Share":
                    this.Share(id, path, path2);
                    break;
                    
                case "":
                    break;
            }
            fileo="";
        }
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "    \n" +
                        "    <style>\n" +
                        "        html {\n" +
                        "        overflow: scroll;\n" +
                        "        overflow-x: hidden;\n" +
                        "        }\n" +
                        "        ::-webkit-scrollbar {\n" +
                        "        width: 0px; /* remove scrollbar space /\n" +
                        "        background: transparent; / optional: just make scrollbar invisible /\n" +
                        "        }       \n" +
                        "    </style>\n" +
                        "    \n" +
                        "    <head>\n" +
                        "        <link rel=\"shortcut icon\" href=\"rsc/favicon.ico\"/>\n" +
                        "        <title>Drive</title>\n" +
                        "        <meta charset=\"UTF-8\">\n" +
                        "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "        <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\n" +
                        "        <link rel=\"stylesheet\" type=\"text/css\" href=\"barra.css\"> \n" +
                        "        <link rel=\"stylesheet\" type=\"text/css\" href=\"treeview.css\"> \n" +
                        "        <link rel=\"stylesheet\" type=\"text/css\" href=\"Files.css\"> \n" +
                        "    </head>\n" +
                        "   \n" +
                        "        \n" +
                        "    <body class=\"ulb\">\n" +
                        "        \n" +
                        "        <form method=\"GET\" action=\"ServletIndex\">\n" +
                        "        \n"+
                        "        <div class=\"imgcontainer\">\n" +
                        "            <img src=\"rsc/img_avatar2.png\" width=\"5%\" alt=\"Avatar\" class=\"avatar\">\n" +
                        "        </div>\n" +
                        "        \n" +
                        "        <ul class=\"ulb\">\n" +
                        "            <li class=\"lib\"><a class=\"ab\" href=\"ServletIndex?path=root\">\n" +
                        "                <img  align=\"middle\" src=\"rsc/img_avatar2.png\"\n" +
                        "                width=\"15%\" height=\"auto\"\n" +
                        "                alt=\"Avatar\" class=\"avatar\"/><b>Drive</b></a>\n" +
                        "            </li>\n" +
                        "            <li class=\"lib\"><a class=\"ab\" href=\"ServletIndex?path=shared\">\n" +
                        "                <img  align=\"middle\" src=\"rsc/img_avatar2.png\"\n" +
                        "                width=\"15%\" height=\"auto\"\n" +
                        "                alt=\"Avatar\" class=\"avatar\"/><b>Shared</b></a>\n" +
                        "            </li>\n" +
                        "            <li class=\"lib\"><a class=\"ab\" href=\"Login.html\">\n" +
                        "                    <img  align=\"middle\" src=\"rsc/Config.png\"\n" +
                        "                width=\"15%\" height=\"auto\"\n" +
                        "                alt=\"Avatar\" class=\"avatar\"/><b>Salir</b></a></li>\n" +
                        "        </ul>\n" +
                        "        \n" +
                        "        <div class=\"ulb\" style=\"display: flex; height: 450px\">\n" +
                        "            <div class=\"ulb\" style=\"width:30%\">\n");
            //CODIGO DINAMICO DE TREEVIEW
            out.println(this.mostrarDIR(id, path, -1));
                        
            out.println("                </div>\n" +
                        "\n" +
                        "            <div align=\"middle\" style=\"width:40%; float: right\">\n");
                        out.println("<div><input name=\"path\" value=\""+path+"\" type=\"hidden\"></div>\n");
                        out.println("<div><input type=\"text\" placeholder=\""+path+"\" name=\"path2\"></div>\n");
            out.println("                <div style=\"padding: 14px 20px;\n" +
                        "                    background-color: #f44336;\">\n" +
                        "                    <input "+this.inFILE(path)+" type=\"radio\"\n" +
                        "                    name=\"fileo\" value=\"Borrar\">Borrar</input>\n" +
                        "                    <input "+this.inFILE(path)+" type=\"radio\"\n" +
                        "                    name=\"fileo\" value=\"Crear\">Crear</input>\n" +
                        "                    <input "+this.inFILE(path)+" type=\"radio\"\n" +
                        "                    name=\"fileo\" value=\"Subir\">Subir</input>\n" +        
                        "                    <button "+this.inFILE(path)+" type=\"submit\" style=\"float: right; width: 30%\"\n" +
                        "                        name=\"final\" class=\"signupbtn\">Enviar</button>\n" +
                        "                </div>\n" +
                        "                <div style=\"height: 300px; overflow-y: scroll\">\n" +
                        "                    <ul class=\"ulf\">\n");
            //CODIGO DINAMICO DE FILES
            out.println(this.mostrarFILES(id, path));
            
            out.println("                    </ul>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "\n" +
                        "            <div class=\"ulb\" style=\"width:30%; float: right\">\n" +
                        "                <div style=\"padding: 14px 20px;\n" +
                        "                    background-color: #f44336; \">\n" +
                        "                    <div>\n" +
                        "                        <input "+this.iFILE(path)+" type=\"radio\"\n" +
                        "                        name=\"fileo\" value=\"Borrar\">Borrar</input>\n" +
                        "                        <input "+this.iFILE(path)+" type=\"radio\"\n" +
                        "                        name=\"fileo\" value=\"Copiar\">Copiar</input>\n" +
                        "                        <input "+this.iFILE(path)+" type=\"radio\"\n" +
                        "                        name=\"fileo\" value=\"Mover\">Mover</input>\n" +
                        "                        <input "+this.iFILE(path)+" type=\"radio\"\n" +
                        "                        name=\"fileo\" value=\"Update\">Update</input>\n" +        
                        "                        <input "+this.iFILE(path)+" type=\"radio\"\n" +
                        "                        name=\"fileo\" value=\"Share\">Share</input>\n" +
                        "                        <button "+this.iFILE(path)+" type=\"submit\" style=\"float: right; width: 30%\"\n" +
                        "                            name=\"final\" class=\"signupbtn\">Enviar</button>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "                <div style=\"height: 300px; overflow-y: scroll\">\n");
            //CODIGO DINAMICO DE INFO DE FILE
            out.println(this.infoFILE(id, path));
            out.println("                </div>\n" +
                        "            </div>\n" +
                        "            \n" +
                        "        </div>\n" +
                        "            \n" +
                        "        </form>\n" +
                        "         \n" +
                        "    </body>\n" +
                        "</html>\n" +
                        "");
            //response.sendRedirect("Drive.html");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                String email = request.getParameter("uname");
                String pass = request.getParameter("psw");
                String btnlogin = request.getParameter("btnLogin");
                int id=manager.Login(email, pass);
                if(id!=0){
                    HttpSession session=  request.getSession(true);
                    session.setAttribute("id", id);
                    System.out.println(session.getId()+"-id");
                    processRequest(request, response);
                    
                }else{
                    id=0;
                    response.sendRedirect("Login.html");
                    
                }

            }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
