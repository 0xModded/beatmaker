import com.formdev.flatlaf.FlatDarkLaf;
import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class SimpleBeatMaker extends JFrame {
    private final JToggleButton[][] pads = new JToggleButton[5][5];
    private final JButton startBtn = styledButton("▶");
    private final JButton stopBtn  = styledButton("■");
    private final JButton clearBtn = styledButton("⟳");
    private final JSlider[] knobs = new JSlider[4];
    private final String[] knobLabels = {"Volume", "Pitch", "Pan", "Depth"};

    private Sequencer sequencer;
    private Sequence  sequence;
    private Track     track;

    public SimpleBeatMaker() {
        super("5×5 Beat Maker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 420);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(24,24,28));

        // Pads grid
        JPanel grid = new JPanel(new GridLayout(5, 5, 10, 10));
        grid.setBackground(new Color(20,20,24));
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                pads[row][col] = makePad();
                grid.add(pads[row][col]);
            }
        }
        grid.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        add(grid, BorderLayout.CENTER);

        // Knobs (sliders) above grid
        JPanel knobsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 28, 0));
knobsPanel.setBackground(new Color(24,24,28));
for (int i = 0; i < knobLabels.length; i++) {
    knobsPanel.add(styledKnob(knobLabels[i], i));
}
add(knobsPanel, BorderLayout.NORTH);


        // Transport panel below grid
        JPanel transport = new JPanel();
        transport.setBackground(new Color(24,24,28));
        transport.add(startBtn);
        transport.add(stopBtn);
        transport.add(clearBtn);
        add(transport, BorderLayout.SOUTH);

        // MIDI setup
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track    = sequence.createTrack();
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.setTempoInBPM(120);
        } catch(Exception ex) { ex.printStackTrace(); }

        // Button actions
        startBtn.addActionListener(e -> buildAndStart());
        stopBtn.addActionListener(e -> { if (sequencer.isRunning()) sequencer.stop(); });
        clearBtn.addActionListener(e -> {
    for (var row : pads)
        for (var pad : row)
            pad.setSelected(false);
    for (var knob : knobs)
        if (knob != null) knob.setValue(64); // Reset slider to midpoint
    if (sequencer.isRunning())
        sequencer.stop();
});

}

    // Custom pad with FlatLaf accent color
    private JToggleButton makePad() {
        JToggleButton pad = new JToggleButton();
        pad.setPreferredSize(new Dimension(54, 54));
        pad.setFocusPainted(false);
        pad.setBorder(BorderFactory.createLineBorder(new Color(40,40,48), 2, true));
        pad.setBackground(new Color(38,46,60));
        pad.setOpaque(true);
        pad.setContentAreaFilled(true);
        pad.setMargin(new Insets(0,0,0,0));
        pad.addChangeListener(e -> {
            if (pad.isSelected()) {
                pad.setBackground(new Color(130, 230, 255));
            } else {
                pad.setBackground(new Color(38,46,60));
            }
        });
        return pad;
    }

    // Styled transport buttons
    private static JButton styledButton(String label) {
        JButton btn = new JButton(label);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Dialog", Font.BOLD, 20));
        btn.setBackground(new Color(30,30,40));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(90,100,110),2,true));
        btn.setPreferredSize(new Dimension(54, 38));
        return btn;
    }

    // Fake rotary knob as vertical slider
   private JComponent styledKnob(String label, int idx) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false); // lets background show through

    knobs[idx] = new JSlider(JSlider.VERTICAL, 0, 127, 64);
    knobs[idx].setPreferredSize(new Dimension(30, 84));
    knobs[idx].setMaximumSize(new Dimension(30, 84));
    knobs[idx].setAlignmentX(Component.CENTER_ALIGNMENT);
    knobs[idx].setToolTipText(label);

    JLabel l = new JLabel(label, SwingConstants.CENTER);
    l.setForeground(Color.WHITE); // ensure it's visible on dark
    l.setFont(l.getFont().deriveFont(Font.BOLD, 13f));
    l.setAlignmentX(Component.CENTER_ALIGNMENT);
    l.setBorder(BorderFactory.createEmptyBorder(6,0,0,0)); // space above label

    panel.add(knobs[idx]);
    panel.add(l);
    return panel;
}


    // Sequencer logic
    private void buildAndStart() {
        int volume = knobs[0].getValue(); // 0-127, from Volume slider
        int pitchOffset = knobs[1].getValue() - 64; // -64..+63, from Pitch slider (centered at 0)
        int tempo = knobs[3].getValue() + 60; // [60 BPM to 187 BPM] - adjust as desired
        int[] drumNotes = {35, 38, 42, 46, 49};
        try {
            sequence.deleteTrack(track);
            track = sequence.createTrack();
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (pads[row][col].isSelected()) {
                        int note = drumNotes[row % drumNotes.length] + pitchOffset; // pitch transposing
                        int tick = col;
                        int vel = volume; // use slider for velocity
                        track.add(event(ShortMessage.NOTE_ON, 9, note, vel, tick));
                        track.add(event(ShortMessage.NOTE_OFF, 9, note, vel, tick + 1));            
                    }
                }
            }
            sequencer.setSequence(sequence);
            sequencer.start();
            sequencer.setTempoInBPM(tempo);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private static MidiEvent event(int cmd, int chan, int note, int vel, long tick) throws Exception {
        ShortMessage msg = new ShortMessage();
        msg.setMessage(cmd, chan, note, vel);
        return new MidiEvent(msg, tick);
    }

    public static void main(String[] args) {
        try { FlatDarkLaf.setup(); } catch(Exception ignored){}
        SwingUtilities.invokeLater(() -> new SimpleBeatMaker().setVisible(true));
    }
}
