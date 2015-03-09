package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.weapons.WeaponStats;
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
        if (textVal == null) Logger.log("[XMLLoader.getTextValue()] Error: XML tag '" + tagName + "' not found.");
        return textVal;
    }

    private static int getIntValue(Element ele, String tagName) {
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    public static WeaponStats retrieveWeapon(Class playerClass, int requiredRank) {
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
                    String type = getTextValue(el, "Class");
                    if (playerClass.toString().contains(type) && rank == requiredRank)
                        return new WeaponStats(playerClass, name, rank, damage, range, speed);
                }
            }
        } catch (ParserConfigurationException pce) {
            Logger.log("[XMLLoader.retrieve()] Parser Error");
            pce.printStackTrace();
        } catch (IOException ioe) {
            Logger.log("[XMLLoader.retrieve()] IOExc");
            ioe.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            Logger.log("[XMLLoader.retrieve()] SAX Error");
            e.printStackTrace();
        }
        Logger.log("[XMLLoader.retrieve()] Weapon not found. Class:" + playerClass.toString() + " and rank " + requiredRank);
        return null;
    }
}
