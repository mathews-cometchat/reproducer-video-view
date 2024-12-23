package com.reproducerapp;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class ReactVideoViewManager extends SimpleViewManager<VideoView> {
    public static final String REACT_CLASS = "ReactVideoView";
    private static final String TAG = "RNViewManager";

    ReactApplicationContext reactContext;

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }


    @NonNull
    @Override
    protected VideoView createViewInstance(@NonNull ThemedReactContext reactContext) {
        VideoView videoView = new VideoView(reactContext);
        MediaController mediaController = new MediaController(reactContext);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        Log.d(TAG, "VideoView Id when creating: " + videoView.getId());

        return videoView;
    }

    @ReactProp(name = "source")
    public void setVideoSource(VideoView videoView, @NonNull String videoUrl) {
        // Setup listeners
        videoView.setOnPreparedListener(mp -> {
            Log.d(TAG, "Video is ready");
            mp.start(); // Start playback when ready
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "Error occurred: what=" + what + " extra=" + extra);
            return true; // Return true to consume the error
        });

        videoView.setOnCompletionListener(mp -> {
            Log.d(TAG, "Playback completed");
        });
        Log.d(TAG, "VideoView Id when adding URI: " + videoView.getId());
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                Uri videoUri = Uri.parse(videoUrl);
                Log.d(TAG, "Setting video URI after delay");
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
                if (videoView.hasFocus()) {
                    Log.d(TAG, "The VideoView successfully received focus");
                } else {
                    Log.d(TAG, "The VideoView did not receive focus");
                }
            } catch (Exception e) {
                Log.e(TAG, "Invalid video source: " + videoUrl, e);
            }
        });
    }


    @Override
    public void onDropViewInstance(@NonNull VideoView videoView) {
        if (videoView.hasFocus()) {
            Log.d(TAG, "DROPPING VideoView successfully received focus");
        } else {
            Log.d(TAG, "DROPPING VideoView did not receive focus");
        }
        VideoView v2 = new VideoView(videoView.getContext());

        Log.d(TAG, "Dropping video VIEW ID: " + videoView.getId() + " " + v2.getId());
        super.onDropViewInstance(videoView);

        // Retrieve the URI that was set
        Uri currentUri = (Uri) videoView.getTag();
        if (currentUri != null) {
            Log.d(TAG, "VideoView removed. Last video URI: " + currentUri.toString());
        } else {
            Log.d(TAG, "VideoView removed. No URI was set.");
        }

        // Perform cleanup
        videoView.stopPlayback();
        videoView.setOnPreparedListener(null);
        videoView.setOnErrorListener(null);
        videoView.setOnCompletionListener(null);
    }
}
