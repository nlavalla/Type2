package com.example.type2_01_05;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;


import java.util.Collection;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private ExternalTexture texture;
    private MediaPlayer mediaPlayer;
    private CustomArFragment arFragment;
    private Scene scene;
    private ModelRenderable renderable;
    private boolean isImageDetected = false;
   private AnchorNode anchorNode;
   private Button bouton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (CustomArFragment)
                getSupportFragmentManager().findFragmentById(R.id.arFragment);

        scene = arFragment.getArSceneView().getScene();

        bouton=(Button)findViewById(R.id.button);
        bouton.setOnTouchListener(this);
        scene.addOnUpdateListener(this::onUpdate);

    }
    public boolean onTouch(View v,MotionEvent event){
            scene.close();
        scene = arFragment.getArSceneView().getScene();
        scene.addOnUpdateListener(this::onUpdate);

        return true;
    }

    private void onUpdate(FrameTime frameTime) {

        if (isImageDetected)
            return;

        Frame frame = arFragment.getArSceneView().getArFrame();

        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);


        for (AugmentedImage image : augmentedImages) {

            switch (image.getTrackingState()) {

                case TRACKING:

                if (image.getName().equals("type2.png")) {
                    Log.d("MyApp", image.getName());
                    isImageDetected = true;
                    addVideo(R.raw.version2);
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());
                    break;
                } else if (image.getName().equals("type2_2.png")) {
                    isImageDetected = true;
                    Log.d("MyApp", image.getName());
                    addVideo1(R.raw.type2);
                    playVideo(image.createAnchor(image.getCenterPose()), image.getExtentX(), image.getExtentZ());

                }
                break;

                case STOPPED:
                    Log.d("stop", image.getName());
                    augmentedImages.remove(image);
                    break;

                case PAUSED:
                    Log.d("stop", "pause");
                    if (augmentedImages.isEmpty())
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
    private void addVideo1(int video) {

        texture = new ExternalTexture();

        mediaPlayer = MediaPlayer.create(this, video);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);

        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("video_screen2.sfb"))
                .build()
                .thenAccept(modelRenderable -> {
                    modelRenderable.getMaterial().setExternalTexture("videoTexture1",
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


}
