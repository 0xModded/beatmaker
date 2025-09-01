# 5x5 Java Beat Maker

A simple, modern 5×5 beat sequencer built with Java Swing and FlatLaf for a sleek dark interface.  
Toggle pads, control playback, and experiment with tempo, pitch, and loudness—all in a hardware-inspired UI!

---

## Features

- **5x5 pad grid:** Click to activate/deactivate steps and assemble drum patterns.
- **Transport controls:** Play, Stop, and Reset (Clear) buttons.
- **4 labeled slider knobs:**
  - **Volume:** Controls overall beat loudness.
  - **Pitch:** Transposes drum sounds up or down.
  - **Pan:** (Reserved for future use)
  - **Depth (Tempo):** Sets tempo in BPM.
- Modern dark theme with FlatLaf.

---

## Requirements

- Java JDK 17 or later (<https://adoptium.net/>)
- FlatLaf (already included but you can download from [here](https://search.maven.org/remotecontent?filepath=com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar))
- Windows, Mac, or Linux

---

## Installation & Running

1. **Clone this repository:**
git clone https://github.com/0xModded/beatmaker/

2. **Place FlatLaf JAR in `lib/` folder:**
- Download [flatlaf-3.4.1.jar](https://search.maven.org/remotecontent?filepath=com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar)
- Place it in `lib/` (create the folder if needed).

3. **Compile:**
javac -cp "lib/flatlaf-3.4.1.jar" -d bin src/SimpleBeatMaker.java

4. **Run:**
- Windows:
  ```
  java -cp "lib/flatlaf-3.4.1.jar;bin" SimpleBeatMaker
  ```
- Mac/Linux:
  ```
  java -cp "lib/flatlaf-3.4.1.jar:bin" SimpleBeatMaker
  ```

---

## How to Use

- **Click** any pad to turn it on/off (blue = on).
- **Sliders (`knob` controls):**
- **Volume:** Sets drum hit loudness.
- **Pitch:** Moves all drums higher/lower.
- **Pan:** (future)
- **Tempo:** Adjust playback speed.
- **Transport:**
- ▶️ Play: Start playback (loops)
- ◼️ Stop: Halts playback
- ⟳ Reset: Clears all pads and resets knobs

---

## Troubleshooting

- **No sound?**  
Check your computer’s MIDI settings and audio output.
- **javac not found?**  
Make sure JDK is installed and on your `PATH`.
- **FlatLaf error?**  
Confirm the jar is in `lib/` and included in the classpath when you compile/run.

---

## Credits

Created by josh.

---


