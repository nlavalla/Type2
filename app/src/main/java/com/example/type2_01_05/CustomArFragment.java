package com.example.type2_01_05;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

import java.io.IOException;
import java.io.InputStream;

public class CustomArFragment extends ArFragment {
    private static final String SAMPLE_IMAGE_DATABASE = "type2V6.imgdb";
    private static final String TAG = "AugmentedImageFragment";

    @Override
    protected Config getSessionConfiguration(Session session) {
        getPlaneDiscoveryController().setInstructionView(null);
        Config config = new Config(session);
        // Use setFocusMode to configure auto-focus.
        config.setFocusMode(Config.FocusMode.AUTO);

        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);

        try (InputStream is = getContext().getAssets().open(SAMPLE_IMAGE_DATABASE)) {
            aid = AugmentedImageDatabase.deserialize(session, is);
        } catch (IOException e) {
        }
        config.setAugmentedImageDatabase(aid);
        getArSceneView().setupSession(session);
        return config;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout=(FrameLayout)super.onCreateView(inflater,container,savedInstanceState);
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);
        getArSceneView().getPlaneRenderer().setEnabled(false);

        return frameLayout;
    }


}
