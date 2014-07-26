package com.maogousoft.logisticsmobile.driver.utils;

public interface RecorderAndPlaybackInterface {
	boolean startRecording();

	boolean stopRecording();

	boolean startPlayback();

	boolean pausePlayback();

	boolean resumePlayback();

	boolean stopPlayback();

	boolean isPlaying();

	void recordingComplete(String filePath);

	void playbackComplete();

	void release();
}
