package com.yqq.touchtest.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import com.yqq.touchtest.R;


public class TransitionFromActivity extends Activity {

    private Button go;
    private ViewGroup viewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitionfrom);

//        if(savedInstanceState!=null){
//            getFragmentManager().beginTransaction().add(R.id.container,new Fragment()).commit();
//        }

        viewGroup = (ViewGroup) findViewById(R.id.scene);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent it = new Intent(TransitionFromActivity.this,TransitionToActivity.class);
                //startActivity(it);
                goScene();
            }
        });

        //setupWindowAnimations();
    }

    private void goScene() {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(2000);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator());
        Slide slide = new Slide();
        slide.setDuration(2000);
        slide.setInterpolator(new BounceInterpolator());
        Fade fadeOut = new Fade(Fade.OUT);
        fadeOut.setDuration(2000);
        Fade fadeIn = new Fade(Fade.IN);
        TransitionSet transition = new TransitionSet();
        transition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        transition.addTransition(fadeOut).addTransition(changeBounds).addTransition(fadeIn);
        TransitionManager.go(Scene.getSceneForLayout(viewGroup,R.layout.activity_transition_to,this),transition);
    }

    private void setupWindowAnimations() {
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        getWindow().setEnterTransition(fade);

        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }
}
