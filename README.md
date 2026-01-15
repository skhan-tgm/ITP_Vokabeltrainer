# ITP_Vokabeltrainer
Vokabeltrainer - ITP Projekt

Willkommen bei unserem Vokabeltrainer! Dieses Programm hilft Schülern dabei, Vokabeln effizient zu lernen, zu verwalten und den Fortschritt im Auge zu behalten.

Was macht das Programm?

Der Vokabeltrainer ist ein Tool, mit dem man eigene Vokabellisten erstellen und diese in verschiedenen Modi abfragen kann.

Die drei Quiz-Modi:

Text-Eingabe: Der klassische Modus. Du bekommst ein Wort angezeigt und musst die Übersetzung exakt eintippen.
Multiple Choice: Du bekommst ein Wort und vier Antwortmöglichkeiten. Nur eine davon ist richtig.
True/False: Das Programm zeigt dir eine Vokabel und eine mögliche Übersetzung. Du musst entscheiden, ob die Paarung stimmt oder nicht.

Wichtige Funktionen
Vokabeln verwalten: Man kann neue Wörter hinzufügen (Deutsch, Englisch, Kategorie) oder Vokabeln aus der Liste löschen.
Automatisches Setup: Wenn du das Programm zum ersten Mal startest und noch keine Liste hast, erstellt der Trainer automatisch einen Satz Basis-Vokabeln (z. B. Unit 1), damit du sofort loslegen kannst.

Statistik & Erfolg: Für jedes Wort wird gespeichert, wie oft du richtig oder falsch gelegen bist. Daraus berechnet das System deine Erfolgsquote als numerischer Wert.

Speichern (Persistenz): Alle Daten werden in der vokabeln.txt mit PrintWriter gespeichert und beim Start mit dem Scanner wieder eingelesen.

Aufbau (MVC)
Wir arbeiten nach dem Model-View-Controller Prinzip:
Model: Speichert die Vokabeln in einem normalen Array (keine ArrayList) und verwaltet die Statistik.
View: Die grafische Oberfläche mit Swing, die zwischen den Modi und der Verwaltung umschaltet.
Controller: Das Bindeglied. Er prüft mit der Logik h:, ob deine Antwort korrekt war und aktualisiert die Daten.
Installation & Start
Projekt in IntelliJ öffnen.
Main-Klasse ausführen.

Die vokabeln.txt wird automatisch im Projektordner angelegt, falls sie noch nicht existiert.
