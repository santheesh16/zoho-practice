package com.zoho.booktickets.service;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itextpdf.text.log.SysoCounter;
import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.model.User;
import com.zoho.booktickets.constant.Constant;

public class LoadXMLFile extends HttpServlet {

    public static final String xmlFilePath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/BookMyTicket-1/resources/createtable.xml";

    public static List<String> xmlToQuery() {
        List<String> createQuery = new ArrayList<String>();
        try {
            File fXmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("table-type");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                StringBuilder builder = new StringBuilder("CREATE TYPE ");
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("\nCurrent Table :" + eElement.getAttribute("name"));
                    builder.append(eElement.getAttribute("name") + " AS ENUM (");
                    for (int i = 0; i < eElement.getElementsByTagName("column").getLength(); i++) {
                        builder.append("'" + eElement.getElementsByTagName("value").item(i).getTextContent() + "' ");
                        if (i != eElement.getElementsByTagName("column").getLength() - 1) {
                            builder.append(", ");
                        } else {
                            builder.append(");");
                        }
                    }

                }
                createQuery.add(builder.toString());
            }
            nList = doc.getElementsByTagName("table");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    builder.append(eElement.getAttribute("name") + " (");
                    for (int i = 0; i < eElement.getElementsByTagName("column").getLength(); i++) {
                        builder.append(eElement.getElementsByTagName("col-name").item(i).getTextContent() + " ");
                        builder.append(eElement.getElementsByTagName("type").item(i).getTextContent() + " ");
                        if (eElement.getElementsByTagName("size").item(i).getTextContent() != "") {
                            builder.append("(" + eElement.getElementsByTagName("size").item(i).getTextContent() + ") ");
                        }
                        if (eElement.getElementsByTagName("state").item(i).getTextContent() != "") {
                            builder.append(eElement.getElementsByTagName("state").item(i).getTextContent());
                        }
                        if (eElement.getElementsByTagName("constraints").item(i).getTextContent() != "") {
                            builder.append(
                                    ", " + eElement.getElementsByTagName("constraints").item(i).getTextContent());
                        }

                        if (i != eElement.getElementsByTagName("column").getLength() - 1) {
                            builder.append(", ");
                        } else {
                            builder.append(");");
                        }
                    }
                }
                createQuery.add(builder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return createQuery;
    }

    public static List<String> copyTableQuery() throws Exception {
        List<String> copyQuery = new ArrayList<String>();
        File fXmlFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nSrcList = doc.getElementsByTagName("table");
        NodeList nList = doc.getElementsByTagName("link-table");

        for (int temp = 0; temp < nSrcList.getLength(); temp++) {
            Node nNode = nSrcList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                for (int temp1 = 0; temp1 < nList.getLength(); temp1++) {
                    Node nSubNode = nList.item(temp1);
                    StringBuilder builder = null;

                    if (nSubNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eSubElement = (Element) nSubNode;
                        String source = eSubElement.getAttribute("source");
                        if (eElement.getAttribute("name").equals(source)) {
                            for (int k = 0; k < eSubElement.getElementsByTagName("target").getLength(); k++) {
                                String target =  eSubElement.getElementsByTagName("target").item(k).getTextContent();
                                builder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+ target +" (LIKE "+ source +" INCLUDING ALL);");
                                builder.append("INSERT INTO "+ target +" SELECT * FROM "+ source+";");
                                if (builder != null) {
                                    copyQuery.add(builder.toString());
                                }
                            }

                        }
                    }

                }
            }

        }
        return copyQuery;
    }

    public static void sendlinkTable() throws Exception {
        Map<String, List<String>> srcTarget = new HashMap<String, List<String>>();
        String source = null;
        File fXmlFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("link-table");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            List<String> linkTables = new ArrayList<String>();
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("\nCurrent Source :" + eElement.getAttribute("source"));
                source = eElement.getAttribute("source");

                for (int i = 0; i < eElement.getElementsByTagName("target").getLength(); i++) {

                    linkTables.add(eElement.getElementsByTagName("target").item(i).getTextContent());
                }
            }
            srcTarget.put(source, linkTables);
        }
        new PortService().post(Constant.PORT_URL+"/xml", JsonUtil.objectToString(srcTarget));
    }
    
    public void init() throws ServletException {
        List<String> queries = xmlToQuery();
        try {
            for (String query : queries) {
                try (PreparedStatement pstmt = DatabaseConnection.getConnect().prepareStatement(query)) {
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            queries.addAll(copyTableQuery());
            System.out.println(queries);
            sendlinkTable();
            new UserService().checkMovieOrAdd(new User("admin", "Santheesh", "7904188021", "santheesh62@gmail.com", "Male"));
            new PortService().post(Constant.PORT_URL+"/dbcheck", JsonUtil.objectToString(queries));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}