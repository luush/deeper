package org.lszjaf.deeper.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lszjaf.deeper.baseBean.DeeperInterfaceXMLConifgInfo;
import org.lszjaf.deeper.common.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Joybana
 * @date 2018-12-06
 */
public class XMLUtils {
    private final static Logger logger = LoggerFactory.getLogger(XMLUtils.class);
    public static String filePath;

    private static Map<String, String> fileMap;

    static {
        fileMap = Collections.synchronizedMap(new HashMap<String, String>(16, 0.75F));

    }

    public static void main(String[] args) {
    }


    public static void initXML(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new ExceptionInInitializerError("Deeper can't obtain database xml config message_1,path is " +
                    "null or empty ,initXML failed!");
        }

        URL p = XMLUtils.class.getResource(SystemConstants.SLASH);//get the Class files's Path
        if (p.getFile().startsWith(SystemConstants.SLASH)) {
            path = p.getFile().replaceFirst(SystemConstants.SLASH, SystemConstants.EMPTY) + path.substring(path.lastIndexOf(SystemConstants.SLASH));
            filePath = path;
        }

        File file = new File(path);
        if (file == null) {
            throw new ExceptionInInitializerError("Deeper can't obtain database xml config message_2,initXML failed!");
        }
        File[] files = file.listFiles();
        if (files == null) {
            throw new ExceptionInInitializerError("Deeper can't obtain database xml config message_3,initXML failed!");
        }
        for (File f : files) {
            SAXReader saxReader = new SAXReader();
            Document document = null;
            try {
                document = saxReader.read(f);
                Element root = document.getRootElement();
                fileMap.put(root.attribute(SystemConstants.PACKAGE).getText(), f.getPath());
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static DeeperInterfaceXMLConifgInfo parseXml(String className, String methodName) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            String path = fileMap.get(className);
            if (path == null) {
                return null;
            }
            document = saxReader.read(path);
            Element element = document.getRootElement();
            List<Element> list = element.elements();
            for (Element e : list) {
                String id = e.attribute(SystemConstants.ID).getStringValue();
                if (!id.equals(methodName)) {
                    continue;
                }
                String eName = e.getName();
                DeeperInterfaceXMLConifgInfo deeperInterfaceXMLConifgInfo = new DeeperInterfaceXMLConifgInfo();
                deeperInterfaceXMLConifgInfo.setMethodId(id);
                if(SystemConstants.SELECT.equals(eName)){//just select has resultType!
                    deeperInterfaceXMLConifgInfo.setReturnType(e.attribute(SystemConstants.RESULT_TYPE).getText());
                }
                deeperInterfaceXMLConifgInfo.setSql(e.getTextTrim());
                deeperInterfaceXMLConifgInfo.setParamsType(e.attribute(SystemConstants.PARAMS_TYPE).getText().split(SystemConstants.COMMA));
                deeperInterfaceXMLConifgInfo.setJdbcType(eName);
                return deeperInterfaceXMLConifgInfo;
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        XMLUtils.filePath = filePath;
    }
}
