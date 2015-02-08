package be.kdg.services;

import be.kdg.model.Territory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 6/2/2015.
 */
public class TerritorieService {

    private Set<Territory> territories = new HashSet<Territory>();


    public Set<Territory> getTerritories () {

        /////////////
        //    NORTH AMERICA
        ////////////

        Territory alaska = new Territory();
        alaska.setGameId(1);
        Territory northwestTerritory = new Territory();
        northwestTerritory.setGameId(2);
        Territory greenland = new Territory();
        greenland.setGameId(3);
        Territory alberta = new Territory();
        alberta.setGameId(4);
        Territory ontario = new Territory();
        ontario.setGameId(5);
        Territory quebec = new Territory();
        quebec.setGameId(6);
        Territory westernUnitedStates = new Territory();
        westernUnitedStates.setGameId(7);
        Territory easternUnitedStates = new Territory();
        easternUnitedStates.setGameId(8);
        Territory centralAmerica = new Territory();
        centralAmerica.setGameId(9);


        /////////////
        //    SOUTH AMERICA
        ////////////

        Territory venezuela = new Territory();
        venezuela.setGameId(10);
        Territory peru = new Territory();
        peru.setGameId(11);
        Territory brazil = new Territory();
        brazil.setGameId(12);
        Territory argentina = new Territory();
        argentina.setGameId(13);

        /////////////
        //    EUROPE
        ////////////

        Territory iceland = new Territory();
        iceland.setGameId(14);
        Territory greatBritain = new Territory();
        greatBritain.setGameId(15);
        Territory scandinavia = new Territory();
        scandinavia.setGameId(16);
        Territory ukraine = new Territory();
        ukraine.setGameId(17);
        Territory northernEurope = new Territory();
        northernEurope.setGameId(18);
        Territory westernEurope = new Territory();
        westernEurope.setGameId(19);
        Territory southernEurope = new Territory();
        southernEurope.setGameId(20);

        /////////////
        //    AFRICA
        ////////////

        Territory northAfrica = new Territory();
        northAfrica.setGameId(21);
        Territory egypt = new Territory();
        egypt.setGameId(22);
        Territory congo = new Territory();
        congo.setGameId(23);
        Territory eastAfrica = new Territory();
        eastAfrica.setGameId(24);
        Territory southAfrica = new Territory();
        southAfrica.setGameId(25);
        Territory madagascar = new Territory();
        madagascar.setGameId(26);


        /////////////
        //    ASIA
        ////////////

        Territory ural = new Territory();
        ural.setGameId(27);
        Territory siberia = new Territory();
        siberia.setGameId(28);
        Territory yakutsk = new Territory();
        yakutsk.setGameId(29);
        Territory kamchatka = new Territory();
        kamchatka.setGameId(30);
        Territory irkutsk = new Territory();
        irkutsk.setGameId(31);
        Territory afghanistan = new Territory();
        afghanistan.setGameId(32);
        Territory mongolia = new Territory();
        mongolia.setGameId(33);
        Territory japan = new Territory();
        japan.setGameId(34);
        Territory china = new Territory();
        china.setGameId(35);
        Territory middleEast = new Territory();
        middleEast.setGameId(36);
        Territory india = new Territory();
        india.setGameId(37);
        Territory siam = new Territory();
        siam.setGameId(38);

        /////////////
        //    AUSTRALIA
        ////////////

        Territory indonesia = new Territory();
        indonesia.setGameId(39);
        Territory newGuinea = new Territory();
        newGuinea.setGameId(40);
        Territory westernAustralia = new Territory();
        westernAustralia.setGameId(41);
        Territory easternAustralia = new Territory();
        easternAustralia.setGameId(42);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////

        return territories;
    }
}

