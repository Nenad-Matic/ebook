package com.example.nenad.eudzbenik.lekcija1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nenad.eudzbenik.LekcijaFragment;
import com.example.nenad.eudzbenik.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Lekcija1Fragment13igra extends LekcijaFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.layout_lekcija1_fragment13_igra, container, false);

        setViewReferences();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Igra mIgra = new Igra();


        btnZapocni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIgra.start();
                txtUputstvo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_out));
                txtUputstvo.setX(-1000);
            }
        });

    }



    Button btnZapocni, btnMeni;
    TextView txtUputstvo, txtCorrectCounter, txtIncorrectCounter;

    private void setViewReferences() {
        btnZapocni = rootView.findViewById(R.id.btn113zapocni);

        btnMeni = rootView.findViewById(R.id.btn113meni);
        btnMeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfejs.pokreniSledecu();
            }
        });

        txtUputstvo = rootView.findViewById(R.id.txt113uputstvo);

        txtCorrectCounter = rootView.findViewById(R.id.txtCorrectCounter);
        txtIncorrectCounter = rootView.findViewById(R.id.txtIncorrectCounter);
    }

    private class Igra {
        Set<Integer> setOfCurrent;
        Counter mCounter;
        Random rnd;

        ArrayList<String> listCorrect, listIncorrect;

        ConstraintLayout rootLayout;



        Igra () {
            mCounter = new Counter();
            setOfCurrent = new HashSet<>();
            rnd = new Random();

            listCorrect = new ArrayList<>(Arrays.asList(getString(R.string.l1_tacni).split(";")));
            listIncorrect = new ArrayList<>(Arrays.asList(getString(R.string.l1_netacni).split(";")));

            rootLayout = (ConstraintLayout) rootView;

        }

        public void start() {
            btnZapocni.setEnabled(false);
            btnZapocni.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_out));
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCounter();
                    txtCorrectCounter.setText("0");
                    txtIncorrectCounter.setText("0/3");
                }
            }, 1000);
        }

        public void end() {

            for (int i : setOfCurrent) {
                rootLayout.removeView(rootLayout.findViewById(i));
            }


            // post feedback

            Toast.makeText(getActivity(), "Игра завршена. Скупио си: " + mCounter.getCorrect() + " тачних.", Toast.LENGTH_LONG).show();


            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnZapocni.setEnabled(true);
                    btnZapocni.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));
                    mCounter.reset();
                }
            }, 2000);

        }

        private void setCounter() {


            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (rnd.nextBoolean())
                        makeNewItem();
                }

                @Override
                public void onFinish() {
                    if (mCounter.isBellow())
                        setCounter();

                }
            }.start();

        }



        private void makeNewItem() {

            final View newItem = makeItemOnTop(rootLayout);

            int multiplier = rnd.nextInt(6);


            final ObjectAnimator animation = ObjectAnimator.ofFloat(newItem, "translationY", 500f);
            animation.setDuration(8000 + (3-multiplier)*1000);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewFellDown(newItem);
                }
            });
            animation.start();

            newItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag().toString().equals("correct")) {
                        // TODO add sound
                        soundControl().playFX(R.raw.money);
                        mCounter.addCorrect();
                    }
                    else {
                        // TODO add sound
                        soundControl().playFX(R.raw.wood);
                        mCounter.addIncorrect();
                    }

                    rootLayout.removeView(v);
                    animation.getListeners().remove(0);
                    animation.cancel();
                    setOfCurrent.remove(v.getId());
                }
            });

        }

        // item passed the screen
        // if it was incorrect increment the counter
        // remove the view
        private void viewFellDown (View view) {
            if (view.getTag().toString() == "correct") {
                mCounter.addIncorrect();
            }

            setOfCurrent.remove(view.getId());
            rootLayout.removeView(view);
        }

        // create a new view
        // set a new ID and add it to currently shown views
        // set text
        // set position
        private View makeItemOnTop (ConstraintLayout root) {

            ConstraintLayout consItem = (ConstraintLayout) LayoutInflater.from(getActivity()).inflate(R.layout.balloon_text, root, false);

            root.addView(consItem);

            int ID = View.generateViewId();
            consItem.setId(ID);
            setOfCurrent.add(ID);


            TextView itemText = consItem.findViewById(R.id.textView);

            if (rnd.nextBoolean()) {
                itemText.setText(listCorrect.get(rnd.nextInt(listCorrect.size())));
                consItem.setTag("correct");
            }
            else {
                itemText.setText(listIncorrect.get(rnd.nextInt(listIncorrect.size())));
                consItem.setTag("incorrect");
            }
            int rndPlace = rnd.nextInt(root.getWidth() - 400);
            consItem.setX(100+ rndPlace);
            consItem.setY(-200);

            return consItem;
        }


        private class Counter {
            private int incorrect;
            private int correct;
            private static final int LIMIT = 3;
            Counter () {
                incorrect = 0;
                correct = 0;
            }

            public void addIncorrect () {
                incorrect++;

                if (incorrect >= LIMIT) {
                    end();
                }

                txtIncorrectCounter.setText(incorrect + "/" + LIMIT);
            }
            public void reset() {
                incorrect = 0;
                correct = 0;
            }

            public boolean isBellow() {
                return incorrect < LIMIT;
            }

            public void addCorrect() {
                correct++;
                txtCorrectCounter.setText("" + correct);
            }

            public int getCorrect() {
                return correct;
            }
        }

    }
}
