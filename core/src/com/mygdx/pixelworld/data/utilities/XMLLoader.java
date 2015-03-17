package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.items.armors.ArmorStats;
import com.mygdx.pixelworld.data.items.weapons.PlayerWeaponStats;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XMLLoader {

    private static String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        if (textVal == null) Logger.log("XMLLoader.getTextValue()", "Error: XML tag '" + tagName + "' not found.");
        return textVal;
    }

    private static int getIntValue(Element ele, String tagName) {
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    public static PlayerWeaponStats retrieveWeapon(GameClasses playerClass, int requiredRank) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("core/assets/Weapons/weapons.xml");
            Element docEle = dom.getDocumentElement();
            NodeList nl = docEle.getElementsByTagName("Weapon");

            if (nl != null && nl.getLength() > 0) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element el = (Element) nl.item(i);

                    String name = getTextValue(el, "Name");
                    int rank = getIntValue(el, "Rank");
                    int damage = getIntValue(el, "Damage");
                    int range = getIntValue(el, "Range");
                    int speed = getIntValue(el, "Speed");
                    float rotationAngle = getFloatValue(el, "Rotation");
                    String type = getTextValue(el, "Class");
                    if (playerClass.toString().equalsIgnoreCase(type) && rank == requiredRank)
                        return new PlayerWeaponStats(playerClass, name, damage, range, speed, rotationAngle);
                }
            }
        } catch (ParserConfigurationException pce) {
            Logger.log("XMLLoader.retrieve()", "Parser Error");
        } catch (IOException ioe) {
            Logger.log("XMLLoader.retrieve()", "IOExc");
        } catch (org.xml.sax.SAXException e) {
            Logger.log("XMLLoader.retrieve()", "SAX Error");
        }
        Logger.log("XMLLoader.retrieve()", "Weapon not found. Class:" + playerClass.toString() + " and rank " + requiredRank);
        return null;
    }

    private static float getFloatValue(Element el, String tagName) {
        return Float.parseFloat(getTextValue(el, tagName));
    }


    public static ArmorStats retrieveArmor(GameClasses playerClass, int requiredRank) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("core/assets/Armors/armors.xml");
            Element docEle = dom.getDocumentElement();
            NodeList nl = docEle.getElementsByTagName("Armor");

            if (nl != null && nl.getLength() > 0) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element el = (Element) nl.item(i);

                    String name = getTextValue(el, "Name");
                    int rank = getIntValue(el, "Rank");
                    int defense = getIntValue(el, "Defense");
                    String type = getTextValue(el, "Class");
                    if (playerClass.toString().equalsIgnoreCase(type) && rank == requiredRank)
                        return new ArmorStats(name, defense, playerClass);
                }
            }
        } catch (ParserConfigurationException pce) {
            Logger.log("XMLLoader.retrieve()", "Parser Error");
        } catch (IOException ioe) {
            Logger.log("XMLLoader.retrieve()", "IOExc");
        } catch (org.xml.sax.SAXException e) {
            Logger.log("XMLLoader.retrieve()", "SAX Error");
        }
        Logger.log("XMLLoader.retrieve()", "Armor not found. Class:" + playerClass.toString() + " and rank " + requiredRank);
        return null;
    }
}
