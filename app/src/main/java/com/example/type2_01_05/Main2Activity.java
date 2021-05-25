package com.example.type2_01_05;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;


import java.util.Collection;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    private ExternalTexture texture;
    private MediaPlayer mediaPlayer;
    private CustomArFragment arFragment;
    private Scene scene;
    private ModelRenderable renderable;
    private boolean isImageDetected = false;
    private AnchorNode anchorNode;
    private Button bouton;
    private ImageView fitToScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (CustomArFragment)
                getSupportFragmentManager().findFragmentById(R.id.arFragment);
        fitToScanView = findViewById(R.id.image_view_fit_to_scan);

        scene = arFragment.getArSceneView().getScene();
        bouton=(Button)findViewById(R.id.button);
        bouton.setOnClickListener(this);
        scene.addOnUpdateListener(this::onUpdate);

    }



    private void onUpdate(FrameTime frameTime) {

        if (isImageDetected)
            return;

        Frame frame = arFragment.getArSceneView().getArFrame();

        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);


        for (AugmentedImage image : augmentedImages) {

            switch (image.getTrackingState()) {

                case TRACKING:
                    isImageDetected = true;

                    fitToScanView.setVisibility(View.GONE);

                    if (image.getName().equals("type2.png")) {
                        addVideo(R.raw.pluie);
                        playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());
                        break;
                    } else if (image.getName().equals("desert.png")) {
                        addVideo(R.raw.desert);
                        playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());

                    }
                    else if (image.getName().equals("voiture.png")) {
                        addVideo(R.raw.voiture);
                        playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());

                    }
                    else if (image.getName().equals("jungle.png")) {
                        addVideo(R.raw.jungle);
                        playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());

                    }
                    else if (image.getName().equals("glacier.png")) {
                        addVideo(R.raw.glacier);
                        playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());

                    }else if (image.getName().equals("venise.png")) {
                        addVideo(R.raw.venise);
                        playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());

                    }
                    break;

                case STOPPED:
                    augmentedImages.remove(image);
                    break;
            }


        }
    }
    private void addVideo(int video) {

        texture = new ExternalTexture();

        mediaPlayer = MediaPlayer.create(this, video);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);

        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("video_screen.sfb"))
                .build()
                .thenAccept(modelRenderable -> {
                    modelRenderable.getMaterial().setExternalTexture("videoTexture",
                            texture);
                    modelRenderable.getMaterial().setFloat4("keyColor",
                            new Color(0.01843f, 1f, 0.098f));

                    renderable = modelRenderable;
                });

    }


    private void playVideo(Anchor anchor, float extentX, float extentZ) {
        mediaPlayer.start();

        anchorNode = new AnchorNode(anchor);

        texture.getSurfaceTexture().setOnFrameAvailableListener(surfaceTexture -> {
            anchorNode.setRenderable(renderable);
            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
        });

        anchorNode.setWorldScale(new Vector3(extentX, 1f, extentZ));

        scene.addChild(anchorNode);

    }


    @Override
    public void onClick(View v) {
        if (isImageDetected == true) {
            arFragment.onDestroy();
            mediaPlayer.reset();
        }
        Intent activite2= new Intent(Main2Activity.this, MainActivity.class);
        startActivity(activite2);
        Animatoo.animateFade(this);

    }
}
