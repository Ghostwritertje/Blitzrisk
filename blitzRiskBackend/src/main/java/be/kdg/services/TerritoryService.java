package be.kdg.services;

import be.kdg.dao.TerritoryDao;
import be.kdg.model.Territory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 6/2/2015.
 */

@Service("territoryService")
public class TerritoryService {

    @Autowired
    private TerritoryDao territoryDao;

    @Transactional
    public void removeTerritory (Territory territory){
        //Set<Territory> territories = territory.getPlayer().getTerritories();
        territoryDao.removeTerritory(territory);
    }

    @Transactional
    public Territory updateTerritory(Territory territory) {
        territoryDao.updateTerritory(territory);
        return territory;
    }

    @Transactional
    public Territory saveTerritory(Territory territory) {
        territoryDao.saveTerritory(territory);
        return territory;
    }


    @Transactional
    public Territory getTerritory(int territoryId) {
        return territoryDao.getTerritoryById(territoryId);
    }

    public Set<Territory> getTerritories () {

        Set<Territory> territories = new HashSet<Territory>();

        /////////////
        //    NORTH AMERICA
        ////////////

        Territory alaska = new Territory();
        alaska.setGameKey(1);
        Territory northwestTerritory = new Territory();
        northwestTerritory.setGameKey(2);
        Territory greenland = new Territory();
        greenland.setGameKey(3);
        Territory alberta = new Territory();
        alberta.setGameKey(4);
        Territory ontario = new Territory();
        ontario.setGameKey(5);
        Territory quebec = new Territory();
        quebec.setGameKey(6);
        Territory westernUnitedStates = new Territory();
        westernUnitedStates.setGameKey(7);
        Territory easternUnitedStates = new Territory();
        easternUnitedStates.setGameKey(8);
        Territory centralAmerica = new Territory();
        centralAmerica.setGameKey(9);


        /////////////
        //    SOUTH AMERICA
        ////////////

        Territory venezuela = new Territory();
        venezuela.setGameKey(10);
        Territory peru = new Territory();
        peru.setGameKey(11);
        Territory brazil = new Territory();
        brazil.setGameKey(12);
        Territory argentina = new Territory();
        argentina.setGameKey(13);

        /////////////
        //    EUROPE
        ////////////

        Territory iceland = new Territory();
        iceland.setGameKey(14);
        Territory greatBritain = new Territory();
        greatBritain.setGameKey(15);
        Territory scandinavia = new Territory();
        scandinavia.setGameKey(16);
        Territory ukraine = new Territory();
        ukraine.setGameKey(17);
        Territory northernEurope = new Territory();
        northernEurope.setGameKey(18);
        Territory westernEurope = new Territory();
        westernEurope.setGameKey(19);
        Territory southernEurope = new Territory();
        southernEurope.setGameKey(20);

        /////////////
        //    AFRICA
        ////////////

        Territory northAfrica = new Territory();
        northAfrica.setGameKey(21);
        Territory egypt = new Territory();
        egypt.setGameKey(22);
        Territory congo = new Territory();
        congo.setGameKey(23);
        Territory eastAfrica = new Territory();
        eastAfrica.setGameKey(24);
        Territory southAfrica = new Territory();
        southAfrica.setGameKey(25);
        Territory madagascar = new Territory();
        madagascar.setGameKey(26);


        /////////////
        //    ASIA
        ////////////

        Territory ural = new Territory();
        ural.setGameKey(27);
        Territory siberia = new Territory();
        siberia.setGameKey(28);
        Territory yakutsk = new Territory();
        yakutsk.setGameKey(29);
        Territory kamchatka = new Territory();
        kamchatka.setGameKey(30);
        Territory irkutsk = new Territory();
        irkutsk.setGameKey(31);
        Territory afghanistan = new Territory();
        afghanistan.setGameKey(32);
        Territory mongolia = new Territory();
        mongolia.setGameKey(33);
        Territory japan = new Territory();
        japan.setGameKey(34);
        Territory china = new Territory();
        china.setGameKey(35);
        Territory middleEast = new Territory();
        middleEast.setGameKey(36);
        Territory india = new Territory();
        india.setGameKey(37);
        Territory siam = new Territory();
        siam.setGameKey(38);

        /////////////
        //    AUSTRALIA
        ////////////

        Territory indonesia = new Territory();
        indonesia.setGameKey(39);
        Territory newGuinea = new Territory();
        newGuinea.setGameKey(40);
        Territory westernAustralia = new Territory();
        westernAustralia.setGameKey(41);
        Territory easternAustralia = new Territory();
        easternAustralia.setGameKey(42);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////
        //////// SET BORDER TERRITORIES
        ////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////
        //    NORTH AMERICA
        ////////////

        alaska.addNeighbour(alberta);
        alaska.addNeighbour(northwestTerritory);
        alaska.addNeighbour(kamchatka);

        northwestTerritory.addNeighbour(alaska);
        northwestTerritory.addNeighbour(alberta);
        northwestTerritory.addNeighbour(ontario);
        northwestTerritory.addNeighbour(greenland);

        greenland.addNeighbour(iceland);
        greenland.addNeighbour(quebec);
        greenland.addNeighbour(ontario);
        greenland.addNeighbour(northwestTerritory);

        alberta.addNeighbour(alaska);
        alberta.addNeighbour(northwestTerritory);
        alberta.addNeighbour(ontario);
        alberta.addNeighbour(westernUnitedStates);

        ontario.addNeighbour(northwestTerritory);
        ontario.addNeighbour(greenland);
        ontario.addNeighbour(quebec);
        ontario.addNeighbour(easternUnitedStates);
        ontario.addNeighbour(westernUnitedStates);
        ontario.addNeighbour(alberta);

        quebec.addNeighbour(greenland);
        quebec.addNeighbour(ontario);
        quebec.addNeighbour(easternUnitedStates);

        westernUnitedStates.addNeighbour(easternUnitedStates);
        westernUnitedStates.addNeighbour(alberta);
        westernUnitedStates.addNeighbour(ontario);
        westernUnitedStates.addNeighbour(centralAmerica);

        easternUnitedStates.addNeighbour(westernUnitedStates);
        easternUnitedStates.addNeighbour(ontario);
        easternUnitedStates.addNeighbour(quebec);
        easternUnitedStates.addNeighbour(centralAmerica);

        centralAmerica.addNeighbour(venezuela);
        centralAmerica.addNeighbour(easternUnitedStates);
        centralAmerica.addNeighbour(westernUnitedStates);

        /////////////
        //    SOUTH AMERICA
        ////////////

        venezuela.addNeighbour(centralAmerica);
        venezuela.addNeighbour(peru);
        venezuela.addNeighbour(brazil);

        peru.addNeighbour(venezuela);
        peru.addNeighbour(brazil);
        peru.addNeighbour(argentina);

        brazil.addNeighbour(argentina);
        brazil.addNeighbour(venezuela);
        brazil.addNeighbour(peru);
        brazil.addNeighbour(northAfrica);

        argentina.addNeighbour(peru);
        argentina.addNeighbour(brazil);

        /////////////
        //    EUROPE
        ////////////

        iceland.addNeighbour(greenland);
        iceland.addNeighbour(greatBritain);
        iceland.addNeighbour(scandinavia);

        greatBritain.addNeighbour(iceland);
        greatBritain.addNeighbour(scandinavia);
        greatBritain.addNeighbour(westernEurope);
        greatBritain.addNeighbour(northernEurope);

        scandinavia.addNeighbour(iceland);
        scandinavia.addNeighbour(greatBritain);
        scandinavia.addNeighbour(northernEurope);
        scandinavia.addNeighbour(ukraine);

        southernEurope.addNeighbour(northernEurope);
        southernEurope.addNeighbour(westernEurope);
        southernEurope.addNeighbour(ukraine);
        southernEurope.addNeighbour(egypt);
        southernEurope.addNeighbour(northAfrica);

        westernEurope.addNeighbour(northAfrica);
        westernEurope.addNeighbour(southernEurope);
        westernEurope.addNeighbour(northernEurope);
        westernEurope.addNeighbour(greatBritain);

        northernEurope.addNeighbour(greatBritain);
        northernEurope.addNeighbour(westernEurope);
        northernEurope.addNeighbour(southernEurope);
        northernEurope.addNeighbour(ukraine);
        northernEurope.addNeighbour(scandinavia);

        ukraine.addNeighbour(scandinavia);
        ukraine.addNeighbour(northernEurope);
        ukraine.addNeighbour(southernEurope);
        ukraine.addNeighbour(ural);
        ukraine.addNeighbour(afghanistan);
        ukraine.addNeighbour(middleEast);

        /////////////
        //    AFRICA
        ////////////

        northAfrica.addNeighbour(brazil);
        northAfrica.addNeighbour(westernEurope);
        northAfrica.addNeighbour(southernEurope);
        northAfrica.addNeighbour(egypt);
        northAfrica.addNeighbour(eastAfrica);
        northAfrica.addNeighbour(congo);

        egypt.addNeighbour(southernEurope);
        egypt.addNeighbour(northAfrica);
        egypt.addNeighbour(middleEast);
        egypt.addNeighbour(eastAfrica);

        congo.addNeighbour(northAfrica);
        congo.addNeighbour(eastAfrica);
        congo.addNeighbour(southAfrica);

        eastAfrica.addNeighbour(middleEast);
        eastAfrica.addNeighbour(madagascar);
        eastAfrica.addNeighbour(southAfrica);
        eastAfrica.addNeighbour(congo);
        eastAfrica.addNeighbour(northAfrica);
        eastAfrica.addNeighbour(egypt);

        southAfrica.addNeighbour(madagascar);
        southAfrica.addNeighbour(eastAfrica);
        southAfrica.addNeighbour(congo);

        madagascar.addNeighbour(eastAfrica);
        madagascar.addNeighbour(southAfrica);

        /////////////
        //    ASIA
        ////////////

        ural.addNeighbour(ukraine);
        ural.addNeighbour(afghanistan);
        ural.addNeighbour(china);
        ural.addNeighbour(siberia);

        siberia.addNeighbour(ural);
        siberia.addNeighbour(china);
        siberia.addNeighbour(mongolia);
        siberia.addNeighbour(irkutsk);
        siberia.addNeighbour(yakutsk);

        yakutsk.addNeighbour(siberia);
        yakutsk.addNeighbour(kamchatka);
        yakutsk.addNeighbour(irkutsk);

        kamchatka.addNeighbour(alaska);
        kamchatka.addNeighbour(japan);
        kamchatka.addNeighbour(mongolia);
        kamchatka.addNeighbour(irkutsk);
        kamchatka.addNeighbour(yakutsk);

        irkutsk.addNeighbour(siberia);
        irkutsk.addNeighbour(yakutsk);
        irkutsk.addNeighbour(kamchatka);
        irkutsk.addNeighbour(mongolia);

        mongolia.addNeighbour(siberia);
        mongolia.addNeighbour(irkutsk);
        mongolia.addNeighbour(kamchatka);
        mongolia.addNeighbour(japan);
        mongolia.addNeighbour(china);

        afghanistan.addNeighbour(ukraine);
        afghanistan.addNeighbour(ural);
        afghanistan.addNeighbour(china);
        afghanistan.addNeighbour(india);
        afghanistan.addNeighbour(middleEast);

        china.addNeighbour(afghanistan);
        china.addNeighbour(ural);
        china.addNeighbour(siberia);
        china.addNeighbour(mongolia);
        china.addNeighbour(siam);
        china.addNeighbour(india);

        japan.addNeighbour(kamchatka);
        japan.addNeighbour(mongolia);

        middleEast.addNeighbour(southernEurope);
        middleEast.addNeighbour(ukraine);
        middleEast.addNeighbour(afghanistan);
        middleEast.addNeighbour(india);
        middleEast.addNeighbour(eastAfrica);
        middleEast.addNeighbour(egypt);

        india.addNeighbour(middleEast);
        india.addNeighbour(afghanistan);
        india.addNeighbour(china);
        india.addNeighbour(siam);

        siam.addNeighbour(india);
        siam.addNeighbour(china);
        siam.addNeighbour(indonesia);

        /////////////
        //    AUSTRALIA
        ////////////

        indonesia.addNeighbour(siam);
        indonesia.addNeighbour(newGuinea);
        indonesia.addNeighbour(westernAustralia);

        newGuinea.addNeighbour(easternAustralia);
        newGuinea.addNeighbour(westernAustralia);
        newGuinea.addNeighbour(indonesia);

        westernAustralia.addNeighbour(easternAustralia);
        westernAustralia.addNeighbour(newGuinea);
        westernAustralia.addNeighbour(indonesia);

        easternAustralia.addNeighbour(westernAustralia);
        easternAustralia.addNeighbour(newGuinea);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////
        //////// FILLING SET
        ////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        territories.add(alaska);
        territories.add(alberta);
        territories.add(centralAmerica);
        territories.add(easternUnitedStates);
        territories.add(greenland);
        territories.add(northwestTerritory);
        territories.add(ontario);
        territories.add(quebec);
        territories.add(westernUnitedStates);

        territories.add(argentina);
        territories.add(brazil);
        territories.add(peru);
        territories.add(venezuela);

        territories.add(greatBritain);
        territories.add(iceland);
        territories.add(northernEurope);
        territories.add(scandinavia);
        territories.add(southernEurope);
        territories.add(ukraine);
        territories.add(westernEurope);

        territories.add(congo);
        territories.add(eastAfrica);
        territories.add(egypt);
        territories.add(madagascar);
        territories.add(northAfrica);
        territories.add(southAfrica);

        territories.add(afghanistan);
        territories.add(china);
        territories.add(india);
        territories.add(irkutsk);
        territories.add(japan);
        territories.add(kamchatka);
        territories.add(middleEast);
        territories.add(mongolia);
        territories.add(siam);
        territories.add(siberia);
        territories.add(ural);
        territories.add(yakutsk);

        territories.add(easternAustralia);
        territories.add(indonesia);
        territories.add(newGuinea);
        territories.add(westernAustralia);

        return territories;
    }
}

