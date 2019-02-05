package com.rooma.scraper.helper;

public class BerlinDistricts {
    public static String[] getNames() {
        return new String[]{
                "mitte",
                "moabit",
                "hansaviertel",
                "tiergarten",
                "wedding",
                "gesundbrunen",
                "friedrichshain",
                "kreuzberg",
                "prenzlauer berg",
                "prenzlauerberg",
                "prenzlauer",
                "weißensee",
                "blankenburg",
                "heinersdorf",
                "karow",
                "stadtrandsiedlung",
                "pankow",
                "blankenfelde",
                "buch",
                "französisch buchholz",
                "niederschönhausen",
                "niederschoenhausen",
                "rosenthal",
                "wilhelmsruh",
                "charlottenburg",
                "wilmersdorf",
                "schmargendorf",
                "grunewald",
                "charlottenburg-nord",
                "halensee",
                "spandau",
                "haselhorst",
                "siemensstadt",
                "staaken",
                "gatow",
                "kladow",
                "hakenfelde",
                "falkenhagener feid",
                "wilhelmstadt",
                "steglitz",
                "lichterfelde",
                "lankwitz",
                "zehlendorf",
                "dahlem",
                "nikolassee",
                "wannsee",
                "schöneberg",
                "schoeneberg",
                "friedenau",
                "tempelhof",
                "mariendorf",
                "marienfelde",
                "lichtenrade",
                "neukölln",
                "neukoelln",
                "britz",
                "buckow",
                "rudow",
                "gropiusstadt",
                "alt-treptow",
                "plänterwald",
                "plaenterwald",
                "baumschulenweg",
                "johannisthal",
                "niederschöneweide",
                "altglienicke",
                "adlershof",
                "bohnsdorf",
                "oberschöneweide",
                "oberschoeneweide",
                "köpenick",
                "koepenick",
                "friedrichshagen",
                "rahnsdorf",
                "grünau",
                "gruenau",
                "müggelheim",
                "mueggelheim",
                "schmöckwitz",
                "marzahn",
                "biesdorf",
                "kaulsdorf",
                "mahlsdorf",
                "hellesdorf",
                "friedrichsfelde",
                "karlshorst",
                "lichtenberg",
                "falkenberg",
                "malchow",
                "wartenberg",
                "neu-hohenschönhausen",
                "neu-hohenschoenhausen",
                "alt-hohenschönhausen",
                "alt-hohenschoenhausen",
                "fennpfuhl",
                "rummelsburg",
                "reinickendorf",
                "tegel",
                "konradshöhe",
                "konradshoehe",
                "heiligensee",
                "frohnau",
                "hermsdorf",
                "waidmannslust",
                "lübars",
                "luebars",
                "wittenau",
                "märkisches viertel",
                "maerkisches viertel",
                "borsigwalde"
        };
    }

    public static String convertCommonMisspelling(String foundDistrict) {
        switch (foundDistrict.toLowerCase()) {
            case "prenzlauer":
            case "prenzlauerberg":
                return "prenzlauer berg";
            case "neukoelln":
                return "neukölln";
            default:
                return foundDistrict.toLowerCase();
        }
    }
}