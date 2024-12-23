import React, {useEffect, useRef, useState} from 'react';
import {
  Modal,
  StyleSheet,
  View,
  SafeAreaView,
  Platform,
  ImageSourcePropType,
  requireNativeComponent,
} from 'react-native';

interface NativeVideoViewProps {
  source: string; // Native expects a plain string
}

const NativeVideoView =
  requireNativeComponent<NativeVideoViewProps>('ReactVideoView');


  console.log("🚀 ~ NativeVideoView:", NativeVideoView)

interface VideoPlayer {
  videoUri: string;
  isVisible: boolean;
  onClose: Function;
  onLoad: Function;
  loadingIconColor?: string;
  playIcon?: ImageSourcePropType;
  playIconColor?: string;
  pauseIcon?: ImageSourcePropType;
  pauseIconColor?: string;
  backIcon?: ImageSourcePropType;
  backIconColor?: string;
  volumeIcon?: ImageSourcePropType;
  volumeIconColor?: string;
}

export const VideoPlayer = (props: any) => {
  return (
    <Modal animationType="slide" transparent={false}>
      <SafeAreaView style={styles.safeArea}>
        <View style={styles.container}>
          <NativeVideoView
            source={
              'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4'
            }
          />
        </View>
      </SafeAreaView>
    </Modal>
  );
};

const styles = StyleSheet.create({
  safeArea: {flex: 1},
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  fullscreenVideo: {
    position: 'absolute',
    top: 0,
    left: 0,
    bottom: 0,
    right: 0,
    elevation: 10,
  },
  closeButton: {
    position: 'absolute',
    marginTop: Platform.OS === 'ios' ? 40 : 20,
    right: 20,
    backgroundColor: 'black',
    padding: 10,
    borderRadius: 5,
    zIndex: 20,
  },
  text: {
    color: 'white',
    fontWeight: 'bold',
  },
  loadingIndicator: {
    position: 'absolute',
    alignSelf: 'center',
  },
});
