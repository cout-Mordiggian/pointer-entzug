# Werkzeuge: Beispieldateien

Diese Dateien werden vom Kurs-Runner **nicht** kompiliert. Sie sind Vorlagen
zum Abschreiben, wenn du dein erstes richtiges Projekt aufsetzt.

- `pom.xml.vorlage`: Maven-Projekt mit Java 21 und JUnit 6 (Stand 2026;
  mit JUnit 5 sieht alles gleich aus)
- `build.gradle.kts.vorlage` und `settings.gradle.kts.vorlage`: dasselbe mit Gradle
- `BeispielTest.java`: wie ein JUnit-Test aussieht

Die Endung `.vorlage` hat einen Grund: Hießen die Dateien `pom.xml` bzw.
`build.gradle.kts`, würden IntelliJ und VS Code diesen Ordner als eigenes
Maven- oder Gradle-Projekt importieren, und die Einrichtung des Kurses aus
`SETUP.md` griffe nicht mehr. **Beim Kopieren in ein eigenes Projekt benennst
du sie zurück**, also ohne `.vorlage`.

## Ein Maven-Projekt in einer Minute

Die Befehle sind für Bash, unter Windows also für Git Bash, und laufen hier
im Ordner `werkzeuge`. Ohne Bash geht es genauso von Hand: die Ordner im
Explorer anlegen, die Dateien hineinkopieren, `pom.xml.vorlage` in `pom.xml`
umbenennen, in `Konto.java` oben `package de.beispiel;` ergänzen und dann im
Ordner `mein-projekt` `mvn test` ausführen. Oder den Ordner in IntelliJ bzw.
VS Code öffnen und die Tests dort starten.

```bash
mkdir -p ~/mein-projekt/src/main/java/de/beispiel
mkdir -p ~/mein-projekt/src/test/java/de/beispiel
cp pom.xml.vorlage ~/mein-projekt/pom.xml
cp BeispielTest.java ~/mein-projekt/src/test/java/de/beispiel/
# Konto aus Kapitel 5 dazulegen, mit "package de.beispiel;" als erster Zeile:
(echo 'package de.beispiel;'; cat ../../05-oop-klassen-und-objekte/loesungen/Konto.java) \
    > ~/mein-projekt/src/main/java/de/beispiel/Konto.java
cd ~/mein-projekt
mvn test
```

## Dasselbe mit Gradle

Statt `pom.xml` kommen die beiden Gradle-Vorlagen ins Projekt. Die Befehle
laufen wieder hier im Ordner `werkzeuge` und legen einen eigenen Ordner
`mein-gradle-projekt` an:

```bash
mkdir -p ~/mein-gradle-projekt/src/main/java/de/beispiel
mkdir -p ~/mein-gradle-projekt/src/test/java/de/beispiel
cp build.gradle.kts.vorlage ~/mein-gradle-projekt/build.gradle.kts
cp settings.gradle.kts.vorlage ~/mein-gradle-projekt/settings.gradle.kts
cp BeispielTest.java ~/mein-gradle-projekt/src/test/java/de/beispiel/
(echo 'package de.beispiel;'; cat ../../05-oop-klassen-und-objekte/loesungen/Konto.java) \
    > ~/mein-gradle-projekt/src/main/java/de/beispiel/Konto.java
cd ~/mein-gradle-projekt
gradle wrapper     # einmalig, braucht ein installiertes Gradle ab 8.2; erzeugt ./gradlew
./gradlew test
```

`./gradlew run` braucht zusätzlich eine Klasse mit `main`, die du in
`build.gradle.kts` unter `mainClass` einträgst.

## Versionen und Generator

Die Versionsnummern in den Vorlagen veralten: Die jeweils aktuelle findest du
auf <https://central.sonatype.com> (Suche nach `junit-jupiter`).

Ein Maven-Projekt legt auch der Generator an (lädt beim ersten Mal einiges
nach):

```bash
mvn archetype:generate -DgroupId=de.beispiel -DartifactId=mein-projekt \
    -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
