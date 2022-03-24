package de.hsbhv.myquiz.game

data class Question(val text: String, val answers: List<String>)

class QuestionGenerator {
    companion object {
        // The first answers from the questions in the repository are always correct
        private val questionRepository = mutableListOf(
            Question(
                text = "Wie heißt die Hauptstadt der Slowakei?",
                answers = listOf("Bratislava", "Prag", "Sofia", "Ljubljana")
            ),
            Question(
                text = "Wie viele Zähne hat ein erwachsener Mensch normalerweise?",
                answers = listOf("32", "30", "26", "36")
            ),
            Question(
                text = "Wer wählt den Bundespräsidenten?",
                answers = listOf("Bundesversammlung", "Bundesrat", "Bundestag", "Bundeskanzler")
            ),
            Question(
                text = "Wofür steht die Abkürzung KGaA?",
                answers = listOf(
                    "Kommanditgesellschaft auf Aktien",
                    "Kreditgesellschaft auf Aktien",
                    "Kardinalgesellschaft auf Aktien",
                    "Kompetenzgesellschaft auf Aktien"
                )
            ),
            Question(
                text = "Wie hoch ist die Mehrwertsteuer in Deutschland? (Vor der Senkung durch das Konjukturpaket)?",
                answers = listOf("19 Prozent", "18 Prozent", "17 Prozent", "16 Prozent")
            ),
            Question(
                text = "Welches Land ist kein ständiges Mitglied im Sicherheitsrat der Vereinten Nationen?",
                answers = listOf("Deutschland", "China", "Russland", "USA")
            ),
            Question(
                text = "Welches dieser Tiere hält keinen Winterschlaf?",
                answers = listOf("Eichhörnchen", "Fledermaus", "Igel", "Siebenschläfer")
            ),
            Question(
                text = "Wie heißt die Hauptstadt von Thüringen?",
                answers = listOf("Erfurt", "Dresden", "Magdeburg", "Potsdam")
            ),
            Question(
                text = "In welcher Einheit wird der elektrische Widerstand gemessen?",
                answers = listOf("Ohm", "Volt", "Ampere", "Watt")
            ),
            Question(
                text = "Was ist ein Oxymoron?",
                answers = listOf(
                    "Ein innerer Widerspruch",
                    "Ein Versfuß",
                    "Eine Wiederholung",
                    "Eine Frageform"
                )
            ),
            Question(
                text = "Wofür steht das „L“ im Sender RTL?",
                answers = listOf("Luxenbourg", "London", "Liechtenstein", "Los Angeles")
            ),
            Question(
                text = "Wo fanden die Olympischen Spiele 1996 statt?",
                answers = listOf("Atlanta", "Turin", "Barcelona", "Los Angeles")
            ),
            Question(
                text = "Wie heißt die Hauptstadt von Äthiopien?",
                answers = listOf("Addis Abeba", "Harare", "Nairobi", "Mogadischu")
            ),
            Question(
                text = "Wer war von 1981 bis 1995 französischer Präsident?",
                answers = listOf(
                    "Francois Mitterand",
                    "Valéry Giscard d’Estaing",
                    "Charles de Gaulle",
                    "Jaques Chirac"
                )
            ),
            Question(
                text = "Was soll Cäsar gesagt haben, als er den Rubikon überquerte?",
                answers = listOf(
                    "alea iacta est",
                    "divide et empera",
                    "veni vidi vici",
                    "et tu, brute"
                )
            ),
            Question(
                text = "Wer ist Rekordtorschütze der Bundesliga?",
                answers = listOf(
                    "Gerd Müller",
                    "Manfred Burgsmüller",
                    "Jupp Heynckes",
                    "Klaus Fischer"
                )
            ),
            Question(
                text = "Wie viele Oscars gewann der Film „Titaic“?",
                answers = listOf("11", "12", "13", "10")
            ),
            Question(
                text = "Teneriffa, Gran Canaria und Fuerteventura gehören zu den…?",
                answers = listOf("Kanarischen Inseln", "Balearen", "Azoren", "Karibische Inseln")
            ),
            Question(
                text = "Wer gilt als Verfasser der amerikanischen Unabhängigkeits-erklärung?",
                answers = listOf(
                    "Thomas Jefferson",
                    "Benjamin Franklin",
                    "George Washington",
                    "John Adams"
                )
            ),
            Question(
                text = "Welche Adresse ist mit Sherlock Holmes verbunden?",
                answers = listOf(
                    "221b Baker Street",
                    "Abbey Road 42",
                    "Downing Street 10",
                    "Princess Street 7"
                )
            ),
            Question(
                text = "Wie lautet das chemische Symbol für Blei?",
                answers = listOf("Pb", "Bl", "Be", "Pt")
            ),
            Question(
                text = "Wie viele Planeten hat unser Sonnensystem?",
                answers = listOf("8", "9", "10", "11")
            ),
            Question(
                text = "In welchem Meer liegt die Insel Hawaii?",
                answers = listOf(
                    "Pazifischer Ozean",
                    "Karibisches Meer",
                    "Atlantischer Ozean",
                    "Indischer Ozean"
                )
            ),
            Question(
                text = "Welchen Namen trägt die Universität Frankfurt am Main?",
                answers = listOf(
                    "Johann Wolfgang von Goethe",
                    "Heinrich Heine",
                    "Friedrich Schiller",
                    "Bertolt Brecht"
                )
            ),
            Question(
                text = "Wer verbreitete das heliozentrische Weltbild?",
                answers = listOf(
                    "Nikolaus Kopernikus",
                    "Leonardo da Vinci",
                    "Aristoteles",
                    "Galileo Galilei"
                )
            ),
            Question(
                text = "An welchem Datum fiel die Berliner Mauer?",
                answers = listOf(
                    "9. November 1989",
                    "2. November 1990",
                    "3. Oktober 1990",
                    "8. Oktober 1989"
                )
            ),
            Question(
                text = "Was ist ein Sonett?",
                answers = listOf(
                    "Eine Gedichtsform",
                    "Ein Pilz",
                    "Ein Musikinstrument",
                    "Eine Stichwaffe"
                )
            ),
            Question(
                text = "Wie heißt die Schicht der Atmosphäre, die der Erde am nächsten ist?",
                answers = listOf("Troposphäre", "Stratosphäre", "Mesosphäre", "Thermosphäre")
            ),
            Question(
                text = "Wer spielt die Hauptrolle im Film „Einer flog über das Kuckucksnest?",
                answers = listOf("Jack Nicholson", "Robert de Niro", "Mel Gibson", "Tom Hanks")
            )
        )
        fun get(numOfQuestions: Int) : List<Question>{
            questionRepository.shuffle()
            return questionRepository.subList(0, numOfQuestions)
        }
    }
}