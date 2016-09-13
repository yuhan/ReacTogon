/* This class has subclass AutomatedAudioPlayer
 * The class has a responsibility for playing and recording music events derived from MouseHandler, provides a player new MusicStrings at runtime.
 * This class add new bits of a MusicString with the add() method.
 * The newly-added patterns are played immediately.
 * @author: Vu San Ha Huynh
 * References: JFugue library
 */
package controllers;

import gui.Instrument;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import org.jfugue.JFugueException;
import org.jfugue.MusicStringParser;
import org.jfugue.Pattern;
import org.jfugue.Player;
import org.jfugue.StreamingMidiRenderer;

public class StreamingPlayer {
    
    private Sequencer sequencer;
    private StreamingMidiRenderer renderer;
    public MusicStringParser parser;
    public String instrument = "I[PIANO] ";
    public String volume = "X[VOLUME]=11111 ";
    private float baseVolume = 11111;
    private float instrumentVolume = 100;
    public String duration = "s";
    public long realTime = 0;
    public Map<Integer, String> recordedHarmonicTablePattern = new HashMap<>();
    public String recordedStreamingPattern = "";
    public int pitch = 0;
    public boolean isRecording = false;

    
    public String getRecordedStreamingPattern() {
        return this.recordedStreamingPattern;
    }

    public void setInstrument(int instrumentID) {
        this.instrument = "I[" + Instrument.INSTRUMENT_NAMES[instrumentID] + "] ";
    }

    public String getInstrument() {
        return instrument;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getPitch() {
        return pitch;
    }

    public void setBaseVolume(float volume) {
        this.baseVolume = volume;
        updateVolume();
    }

    private void updateVolume() {
        this.volume = "X[VOLUME]=" + (int) (baseVolume * instrumentVolume / 100) + " ";
    }

    public String getBaseVolume() {
        return volume;
    }

    public void setInstrumentVolume(float volume) {
        this.instrumentVolume = volume;
        updateVolume();
    }

    public void setDuration(int duration) {
        if (duration <= 1) {
            this.duration = "o";
        } else if (duration <= 2) {
            this.duration = "x";
        } else if (duration <= 3) {
            this.duration = "t";
        } else if (duration <= 4) {
            this.duration = "s";
        } else if (duration <= 5) {
            this.duration = "i";
        } else if (duration <= 6) {
            this.duration = "q";
        } else if (duration <= 7) {
            this.duration = "h";
        } else {
            this.duration = "w";
        }
    }

    public String getDuration() {
        return duration;
    }

    public void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
        realTime = System.currentTimeMillis();
        if (isRecording) {
            recordedHarmonicTablePattern = new HashMap<>();
            recordedStreamingPattern = "";
        }
    }

    public boolean getIsRecording() {
        return isRecording;
    }

    public void resetTime() {
        realTime = System.currentTimeMillis();
    }

    public StreamingPlayer() {
        try {
            init(MidiSystem.getSequencer());
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED_WITH_EXCEPTION + e.getMessage());
        }
    }

    /*
     * Creates a new StreamingPlayer instance using a Sequencer 
     */
    public StreamingPlayer(Sequencer sequencer) {
        this();
        init(sequencer);
    }

    /*
     * Creates a new StreamingPlayer instance using a Sequencer obtained from
     * the Synthesizer
     */
    public StreamingPlayer(Synthesizer synth) throws MidiUnavailableException {
        this(Player.getSequencerConnectedToSynthesizer(synth));
    }

    private void init(Sequencer sequencer) {
        setSequencer(sequencer);

        parser = new MusicStringParser();
        renderer = new StreamingMidiRenderer();
        parser.addParserListener(renderer);
    }

    private void openSequencer() {
        if (getSequencer() == null) {
            throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED);
        }

        // Open the sequencer, if it is not already open
        if (!getSequencer().isOpen()) {
            try {
                getSequencer().open();
            } catch (MidiUnavailableException e) {
                throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED_WITH_EXCEPTION + e.getMessage());
            }
        }
    }

    /*
     * Closes MIDI resources
     */
    public void close() {
        getSequencer().close();
        try {
            if (MidiSystem.getSynthesizer() != null) {
                MidiSystem.getSynthesizer().close();
            }
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.GENERAL_ERROR + e.getMessage());
        }
        renderer.close();
    }

    private void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    /*
     * Returns the sequencer containing the MIDI data from a pattern that has
     * been parsed.
     */
    public Sequencer getSequencer() {
        return this.sequencer;
    }

    public MusicStringParser getParser() {
        return parser;
    }
    /*
     * Listener for additions to streaming patterns. When a fragment has been
     * added to a pattern, this implementation plays the pattern.
     */
    public void stream(Pattern fragment) {

        parser.parse(fragment);
    }

    /*
     * Listener for additions to streaming patterns. When a fragment has been
     * added to a pattern, this implementation plays the pattern.
     */
    
    public void stream(int note, boolean isPressed) {
        
        note += pitch;
        String fragment;
        if (isPressed) {
            fragment = instrument + volume + "[" + note + "]o-";
            
        } else {
            fragment = instrument + volume + "[" + note + "]-o";
            
        }
        parser.parse(new Pattern(fragment));
        try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                
            }
        /* Record string pattern for convert to midi output
         * The pattern generated by random played note
         */
        if (isRecording && isPressed) {
            synchronized (this) {
                recordedStreamingPattern += instrument + volume + "[" + note + "]/" + (float) (System.currentTimeMillis() - realTime) / 10000 + " ";
                resetTime();
            }
        }
    }
}