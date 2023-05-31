package ba.etf.rma23.projekat.domain

class GameData {

    companion object {
        fun getAll(): List<Game> {
            return arrayListOf(
                Game("World of Warcraft", "PC", "23.11.2004", 4.3, "wow", "T for teen", "Blizzard Entertainment", "Activision-Blizzard", "MMO RPG", "Prepare to lose your life to this game lmao",
                    arrayListOf(
                        UserRating("hater53", 2, 1.0),
                        UserReview("basedgod21", 1, "Lost my wife while playing this game. She didn't die, just left me for another man. Would do it again"),
                        UserRating("blabla47", 7, 3.5),
                        UserReview("idk54", 5, "It's alright ig"),
                        UserReview("wowsucks86", 6, "Runescape is better tbh")
                    )),
                Game("Warhammer 40000 - Space Marine", "PC / Console", "6.9.2011", 3.75, "space_marine", "M for mature", "Relic Entertainment", "THQ", "Hack and slash", "Assume the role of Captain Titus, the biggest badass under the God Emperor's wide gaze",
                    arrayListOf(
                        UserReview("ilovespacemarine", 8, "FOR ULTRAMAR"),
                        UserRating("basedgod21", 4, 4.8),
                        UserReview("killmealready", 3, "I don't even care about the game I just want to perish"),
                        UserRating("idk54", 5, 3.2),
                        UserRating("blabla47", 7, 5.0)
                    )),
                Game("Warhammer 40000 - Mechanicus", "PC", "idk", 4.4, "mechanicus", "T for teen", "some french dev", "idk", "Turn-based strategy", "Crack in digital form", arrayListOf()),
                Game("Dark Messiah of Might and Magic", "PC / Console", "idk", 4.1, "dark_messiah", "M for mature", "some french dev (again)", "idk", "Action RPG", "Really fun and satisfying combat, the story is kinda meh", arrayListOf()),
                Game("Total War: Rome II", "PC", "idk man", 3.6, "rome2", "T for teen", "Creative Assembly", "SEGA", "Turn-based strategy + real-time tactical", "European agression simulator 200BC", arrayListOf()),
                Game("Half-Life", "PC", "1998", 5.0, "half_life", "M for mature", "Valve", "Valve", "FPS", "The best game before Half-Life 2", arrayListOf()),
                Game("Half-Life 2", "PC", "2004", 5.0, "half_life_2", "M for mature", "Valve", "Valve", "FPS", "The best game before Half-Life: Episode 2", arrayListOf()),
                Game("Half-Life 2: Episode 1", "PC", "2005", 5.0, "half_life_2", "M for mature", "Valve", "Valve", "FPS", "It was alright", arrayListOf()),
                Game("Half-Life 2: Episode 2", "PC", "2007", 5.0, "half_life_2", "M for mature", "Valve", "Valve", "FPS", "The pinicle of gaming", arrayListOf()),
                Game("Half-Life 2: Episode 3", "PC", ":(", 5.0, "half_life_2", "M for mature", "Valve", "Valve", "FPS", "First we have to release Portal 2, then we have to release Dota 2", arrayListOf()),
            )
        }

        fun getDetails(title: String): Game? {
            return getAll().find { game -> game.title == title }
        }
    }

}