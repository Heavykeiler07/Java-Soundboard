# 🎵 Java Soundboard

Ein einfaches, plattformübergreifendes Soundboard in Java!  
Spiele Audiodateien blitzschnell per Tastenbelegung ab – wahlweise über eine intuitive grafische Oberfläche **oder** global mit Tastenkombinationen.

---

## 🚀 Features

- **Bis zu 10 belegbare Tasten:** Weise deinen Lieblingssounds schnell Tastenkürzel zu
- **Unterstützt aktuell `.wav`-Dateien** (weitere Formate in Planung)
- **Komfortable Steuerung:**  
  - Über die moderne GUI  
  - *Oder* global per Tastenkürzel (dank [JNativeHook](https://github.com/kwhat/jnativehook))
- **Konfiguration speichern:** Deine Sound-Zuordnung bleibt erhalten
- **Clips verwalten:** Einfache Zuordnung, Verschiebung oder Löschung direkt im Programm

---

## ⚠️ Bekannte Einschränkungen

- Aktuell werden nur **`.wav`-Dateien** unterstützt.
- Auf manchen Systemen können für globale Tastenerkennung **Sonderrechte** oder zusätzliche Einstellungen nötig sein.

---

## 📥 Installation & Nutzung

1. **Java 11 oder neuer installieren**
2. **Projekt klonen:**  
   ```bash
   git clone https://github.com/Heavykeiler07/Java-Soundboard.git
   ```
3. **Abhängigkeiten:**  
   Stelle sicher, dass [JNativeHook](https://github.com/kwhat/jnativehook) eingebunden ist (als JAR oder via Maven/Gradle).
4. **Starten:**  
   - Kompiliere und starte das Projekt wie gewohnt in deiner IDE oder per Konsole.
5. **Bedienung:**  
   - Clips hinzufügen, löschen, verschieben – alles direkt in der GUI  
   - Mit `Ctrl + Ziffer` spielst du den Sound auch global ab

---

## 💡 Hinweise & Mitmachen

> **Hinweis:**  
> Dies ist mein erstes veröffentlichtes Repository.  
> **Pull Requests** und **Vorschläge** sind herzlich willkommen!  
> Für Bugs oder Feature-Ideen: bitte ein **Issue** eröffnen.

---

## 📄 Lizenz

Dieses Projekt steht unter der [MIT-Lizenz](LICENSE).

---

**Autor:**  
[Heavykeiler07](https://github.com/Heavykeiler07)
