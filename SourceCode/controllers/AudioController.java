/* This class consist of an array of StreamPlayer object which handle the music events and record functionality.
 * @author: Vu San Ha Huynh
 */
package controllers;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.jfugue.Pattern;


public class AudioController {
    ArrayList<StreamingPlayer> streamingPlayer;

    public AudioController() {
        this.streamingPlayer = new ArrayList();
    }

    public void initializeNewController() {
        streamingPlayer.add(new AutomatedAudioPlayer());
    }

    public void setAudioPlayerOfCurrentTab(int index, int instrumentID) {
        streamingPlayer.add(new AutomatedAudioPlayer());
        streamingPlayer.get(index).setInstrument(instrumentID);
    }

    public StreamingPlayer getAudioPlayerCurrentTab(int index) {
        return streamingPlayer.get(index);
    }

    public void remove(int index) {
        streamingPlayer.remove(index);
    }

    public void setVolume(int volume) {
        for (StreamingPlayer sp : streamingPlayer) {
            sp.setBaseVolume(volume);
        }
    }

    public void startRecording() {
        for (StreamingPlayer sp : streamingPlayer) {
            sp.setIsRecording(true);
        }
    }

    public void stopRecording() {
        for (StreamingPlayer sp : streamingPlayer) {
            sp.setIsRecording(false);
        }
    }

    public Pattern getRecordedPattern() {
        Pattern pattern = new Pattern();
        int i = 0;
        for (StreamingPlayer sp : streamingPlayer) {
            String recordedHarmonicTablePattern = "";
            Map<Integer, String> recordedPatternOnMap = new TreeMap<>(((AutomatedAudioPlayer) sp).getRecordedHarmonicTablePattern());
            System.out.println(recordedPatternOnMap.values());
            for (String s: recordedPatternOnMap.values()) {
                recordedHarmonicTablePattern += s;
            }
            System.out.println(recordedHarmonicTablePattern);
            System.out.println(sp.getRecordedStreamingPattern());
            if (!"".equals(recordedHarmonicTablePattern)) {
                pattern.add("V" + i + " " + recordedHarmonicTablePattern);
                i++;
            }
            if (!"".equals(sp.getRecordedStreamingPattern())) {
                pattern.add("V" + i + " " + sp.getRecordedStreamingPattern());
                i++;
            }
        }
        return pattern;
    }
}