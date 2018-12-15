package com.rooma.scraper;

public class BerlinDistricts {
    public static String[] getBerlinDistricts() {
        return new String[]{"mitte", "moabit", "hansaviertel", "tiergarten", "wedding", "gesundbrunen",
                "friedrichshain", "kreuzberg",
                "prenzlauer berg", "prenzlauerberg", "prenzlauer", "weißensee", "blankenburg", "heinersdorf", "karow", "stadtrandsiedlung", "pankow", "blankenfelde", "buch", "französisch buchholz", "niederschönhausen", "rosenthal", "wilhelmsruh",
                "charlottenburg", "wilmersdorf", "schmargendorf", "grunewald", "charlottenburg-nord", "halensee",
                "spandau", "haselhorst", "siemensstadt", "staaken", "gatow", "kladow", "hakenfelde", "falkenhagener feid", "wilhelmstadt",
                "steglitz", "lichterfelde", "lankwitz", "zehlendorf", "dahlem", "nikolassee", "wannsee",
                "schöneberg", "friedenau", "tempelhof", "mariendorf", "marienfelde", "lichtenrade",
                "neukölln", "neukoelln", "britz", "buckow", "rudow", "gropiusstadt",
                "alt-Treptow", "plänterwald", "baumschulenweg", "johannisthal", "niederschöneweide", "altglienicke", "adlershof", "bohnsdorf", "oberschöneweide", "köpenick", "friedrichshagen", "rahnsdorf", "grünau", "müggelheim", "schmöckwitz",
                "marzahn", "biesdorf", "kaulsdorf", "mahlsdorf", "hellesdorf",
                "friedrichsfelde", "karlshorst", "lichtenberg","falkenberg", "malchow", "wartenberg", "neu-hohenschönhausen", "alt-hohenschönhausen", "fennpfuhl", "rummelsburg",
                "reinickendorf", "tegel", "konradshöhe", "heiligensee", "frohnau", "hermsdorf", "waidmannslust", "lübars", "wittenau", "märkisches viertel", "borsigwalde"};
    }

    static String convertCommonMisspelling(String foundDistrict) {
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
