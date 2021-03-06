


package net.sf.freecol.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.sf.freecol.common.model.Settlement.SettlementType;
import net.sf.freecol.common.util.RandomChoice;



public class IndianNationType extends NationType {

    public static enum SettlementNumber { LOW, AVERAGE, HIGH }
    public static enum AggressionLevel { LOW, AVERAGE, HIGH }

    
    private SettlementNumber numberOfSettlements;

    
    private AggressionLevel aggression;

    
    private List<RandomChoice<UnitType>> skills = 
        new ArrayList<RandomChoice<UnitType>>();

    
    private List<String> regions = new ArrayList<String>();

    
    public boolean isEuropean() {
        return false;
    }

    
    public boolean isREF() {
        return false;
    }
    
    public final SettlementNumber getNumberOfSettlements() {
        return numberOfSettlements;
    }

    
    public final void setNumberOfSettlements(final SettlementNumber newNumberOfSettlements) {
        this.numberOfSettlements = newNumberOfSettlements;
    }

    
    public final AggressionLevel getAggression() {
        return aggression;
    }

    
    public final void setAggression(final AggressionLevel newAggression) {
        this.aggression = newAggression;
    }

    
    public final String getSettlementTypeKey() {
        switch (getTypeOfSettlement()) {
        case INCA_CITY:
        case AZTEC_CITY:
            return "settlementType.city";
        case INDIAN_VILLAGE:
            return "settlementType.village";
        case INDIAN_CAMP:
        default:
            return "settlementType.camp";
        }
    }

    
    public List<String> getRegionNames() {
        return regions;
    }

    
    
    
    

    
    public List<RandomChoice<UnitType>> getSkills() {
        return skills;
    }

    public void readAttributes(XMLStreamReader in, Specification specification)
            throws XMLStreamException {

        String valueString = in.getAttributeValue(null, "number-of-settlements").toUpperCase();
        numberOfSettlements = Enum.valueOf(SettlementNumber.class, valueString);

        valueString = in.getAttributeValue(null, "aggression").toUpperCase();
        aggression = Enum.valueOf(AggressionLevel.class, valueString);

        valueString = in.getAttributeValue(null, "type-of-settlement").toUpperCase();
        setTypeOfSettlement(Enum.valueOf(SettlementType.class, valueString));
        setSettlementRadius(getAttribute(in, "settlementRadius", getSettlementRadius()));
        setCapitalRadius(getAttribute(in, "capitalRadius", getCapitalRadius()));
    }

    public void readChildren(XMLStreamReader in, Specification specification)
            throws XMLStreamException {
        while (in.nextTag() != XMLStreamConstants.END_ELEMENT) {
            String childName = in.getLocalName();
            if ("skill".equals(childName)) {
                UnitType unitType = specification.getUnitType(in.getAttributeValue(null, "id"));
                int probability = getAttribute(in, "probability", 0);
                skills.add(new RandomChoice<UnitType>(unitType, probability));
                in.nextTag(); 
            } else if (Region.getXMLElementTagName().equals(childName)) {
                regions.add(in.getAttributeValue(null, "id"));
                in.nextTag(); 
            } else {
                super.readChild(in, specification);
            }
        }

        
        Collections.sort(skills, new Comparator<RandomChoice<UnitType>>() {
                public int compare(RandomChoice<UnitType> choice1, RandomChoice<UnitType> choice2) {
                    return choice2.getProbability() - choice1.getProbability();
                }
            });


    }

}
