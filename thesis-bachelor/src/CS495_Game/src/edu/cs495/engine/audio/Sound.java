/**
 * 
 */
package edu.cs495.engine.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/** A Sound clip object used to handle sound
 * Clips must be initialized via {@link #init()}
 * 
 * @version February 2019
 * @author Spencer Imbleau
 */
public class Sound {

	/** The path for the sound file */
	private String path;

	/** The clip for this sound */
	private Clip clip;

	/** The gain control of our clip */
	private FloatControl gainControl;

	/**
	 * Initializes a sound object
	 * 
	 * @param path : the path for this sound clip
	 */
	public Sound(String path) {
		this.path = path;
		this.clip = null;
		this.gainControl = null;
	}

	/**
	 * Loads and initializes the sound clip
	 * 
	 * @throws IOException                   if the file is unreachable
	 * @throws UnsupportedAudioFileException If the audio stream is unreadable
	 * @throws LineUnavailableException      if the input line is unavailable to be
	 *                                       opened
	 */
	public void init() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		// Read source of audio clip
		InputStream audioSrc = Sound.class.getResourceAsStream(path);
		InputStream bufferedIn = new BufferedInputStream(audioSrc);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
		AudioFormat baseFormat = audioInputStream.getFormat();
		AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, // Encoding
				baseFormat.getSampleRate(), // Sample rate
				16, // Sample size (bits)
				baseFormat.getChannels(), // Channels
				baseFormat.getChannels() * 2, // Frame Size
				baseFormat.getSampleRate(), // Frame rate
				false); // Big Endian = no
		AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);

		clip = AudioSystem.getClip();
		clip.open(decodedAudioInputStream);

		gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	}

	/** Play the sound clip if it loaded correctly */
	public void play() {
		if (clip == null) {
			return;
		}

		stop();
		clip.setFramePosition(0);
		while (!clip.isRunning()) {
			clip.start();
		}
	}

	/** Stop the sound clip if it was playing */
	public void stop() {
		if (clip.isRunning()) {
			clip.stop();
		}
	}

	/** Stop any sound clip and close the line of system resources for the clip */
	public void close() {
		stop();
		clip.drain();
		clip.close();
	}

	/** Sets the clip to loop continuously and play if it wasn't already */
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		play();
	}

	/**
	 * Sets the level of gain/loss to the clip's volume
	 * 
	 * @param volume : the level of gain or loss (in decibels)
	 */
	public void setVolume(float volume) {
		gainControl.setValue(volume);
	}

	/**
	 * Return whether the clip is playing
	 * 
	 * @return true if the clip is currently sounding, false otherwise
	 */
	public boolean isPlaying() {
		return clip.isRunning();
	}
}
