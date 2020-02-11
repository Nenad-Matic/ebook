package com.example.nenad.eudzbenik.lekcija5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.nenad.eudzbenik.LekcijaFragment;
import com.example.nenad.eudzbenik.R;

import org.w3c.dom.Text;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class L5F6 extends LekcijaFragment {
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.layout_l5f6, container, false);

        setViewReferences();

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();

        txtNaslov.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in_500));



        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtPr1.setVisibility(View.VISIBLE);
                txtPr1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));
            }
        }, 3400);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtPr2.setVisibility(View.VISIBLE);
                txtPr2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));
            }
        }, 3600);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtPr3.setVisibility(View.VISIBLE);
                txtPr3.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));
            }
        } , 3800);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtPr4.setVisibility(View.VISIBLE);
                txtPr4.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));

            }
        } , 4000);



        // TACNA RECENICA

        txtPr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundControl().playFX(R.raw.money);

                txtPr3.setOnClickListener(null);

                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pokreniShow();
                    }
                }, 250);
            }
        });


        // NETACNE RECENICE
        txtPr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundControl().playFX(R.raw.wood);
            }
        });

        txtPr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundControl().playFX(R.raw.wood);
            }
        });


        txtPr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundControl().playFX(R.raw.wood);
            }
        });

    }

    private SpannableStringBuilder ssb;

    private void pokreniShow() {

        // fade out wrong answers
        txtPr1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_out));
        txtPr2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_out));
        txtPr4.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_out));


        // move right answer to center
        txtPr3.animate().x(txtNaslov.getX()+txtNaslov.getWidth()/2 - txtPr3.getWidth()/2).y(txtNaslov.getY()+txtNaslov.getHeight()+80).setDuration(800).setInterpolator(new AccelerateInterpolator()).scaleX(1.5f).scaleY(1.5f).start();

        // ssb is used to color the important words
        ssb = new SpannableStringBuilder(txtPr3.getText());


        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOkerZuta)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtPr3.setText(ssb);

                txtTrecelice.setVisibility(View.VISIBLE);
                txtTrecelice.setY(txtNaslov.getY()+txtNaslov.getHeight()+80+txtPr3.getHeight()+30);
                txtTrecelice.setTextColor(getResources().getColor(R.color.colorOkerZuta));
                txtTrecelice.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));
            }
        }, 1750);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOkerZuta)), 12, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtPr3.setText(ssb);
            }
        }, 2000);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                soundControl().playSpeech(R.raw.speech_predikat_5_glagol);

                String strGlagol = getString(R.string.l5f6_glagol_u_sluzbi);
                String strToSpan = getString(R.string.l5f6_to_span);
                int pos = strGlagol.indexOf(strToSpan);
                int len = strToSpan.length();

                CalligraphyTypefaceSpan italicSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getActivity().getAssets(), "Philosopher-Italic.ttf"));

                SpannableStringBuilder ssbGlagol = new SpannableStringBuilder(strGlagol);

                ssbGlagol.setSpan(italicSpan, pos, pos+len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtGlagol.setText(ssbGlagol);

                txtGlagol.setVisibility(View.VISIBLE);
                txtGlagol.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));


            }
        }, 3750);


        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                txtPogledaj.setVisibility(View.VISIBLE);
                txtPogledaj.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));

                postaviSwitcher();
            }
        }, 10500);


    }

    String str1, str2, str3, str4, str5, str6, strLiceJednine, strLiceMnozine;
    int current;
    private void postaviSwitcher() {
        current = 3;
        final Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_out);
        fadeOut.setDuration(350);
        final Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in);
        fadeIn.setDuration(350);


        str1 = getString(R.string.l5f6_primer1);
        str2 = getString(R.string.l5f6_primer2);
        str3 = getString(R.string.l5f6_primer3);
        str4 = getString(R.string.l5f6_primer4);
        str5 = getString(R.string.l5f6_primer5);
        str6 = getString(R.string.l5f6_primer6);

        strLiceJednine = getString(R.string.l5f6_lice_jednine);
        strLiceMnozine = getString(R.string.l5f6_lice_mnozine);

        txtPr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (current) {
                    case 1: {

                        current = 0;

                        txtPr3.startAnimation(fadeOut);
                        txtTrecelice.startAnimation(fadeOut);

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtPr3.setText(str2);
                                txtPr3.startAnimation(fadeIn);

                                txtTrecelice.setText("2. "+strLiceJednine);
                                txtTrecelice.startAnimation(fadeIn);
                            }
                        }, 351);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                current = 2;
                            }
                        }, 700);


                        break;
                    }

                    case 2: {
                        current = 0;

                        txtPr3.startAnimation(fadeOut);
                        txtTrecelice.startAnimation(fadeOut);

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtPr3.setText(str3);
                                txtPr3.startAnimation(fadeIn);

                                txtTrecelice.setText("3. "+strLiceJednine);
                                txtTrecelice.startAnimation(fadeIn);
                            }
                        }, 351);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                current = 3;
                            }
                        }, 700);

                        if (btnDalje.getVisibility() != View.VISIBLE) {
                            btnDalje.setVisibility(View.VISIBLE);
                            btnDalje.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in));
                        }

                        break;
                    }

                    case 3: {
                        current = 0;

                        txtPr3.startAnimation(fadeOut);
                        txtTrecelice.startAnimation(fadeOut);

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtPr3.setText(str4);
                                txtPr3.startAnimation(fadeIn);

                                txtTrecelice.setText("1. "+strLiceMnozine);
                                txtTrecelice.startAnimation(fadeIn);
                            }
                        }, 351);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                current = 4;
                            }
                        }, 700);


                        break;
                    }

                    case 4: {
                        current = 0;

                        txtPr3.startAnimation(fadeOut);
                        txtTrecelice.startAnimation(fadeOut);

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtPr3.setText(str5);
                                txtPr3.startAnimation(fadeIn);

                                txtTrecelice.setText("2. "+strLiceMnozine);
                                txtTrecelice.startAnimation(fadeIn);
                            }
                        }, 351);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                current = 5;
                            }
                        }, 700);


                        break;
                    }

                    case 5: {
                        current = 0;

                        txtPr3.startAnimation(fadeOut);
                        txtTrecelice.startAnimation(fadeOut);

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtPr3.setText(str6);
                                txtPr3.startAnimation(fadeIn);

                                txtTrecelice.setText("3. "+strLiceMnozine);
                                txtTrecelice.startAnimation(fadeIn);
                            }
                        }, 351);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                current = 6;
                            }
                        }, 700);


                        break;
                    }

                    case 6: {
                        current = 0;

                        txtPr3.startAnimation(fadeOut);
                        txtTrecelice.startAnimation(fadeOut);

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtPr3.setText(str1);
                                txtPr3.startAnimation(fadeIn);


                                txtTrecelice.setText("1. "+strLiceJednine);
                                txtTrecelice.startAnimation(fadeIn);
                            }
                        }, 351);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                current = 1;
                            }
                        }, 700);


                        break;
                    }
                    default: break;

                }
            }
        });
    }

    TextView txtNaslov, txtPr1, txtPr2, txtPr3, txtPr4, txtTrecelice, txtGlagol, txtPogledaj;

    private void setViewReferences() {
        btnDalje = rootView.findViewById(R.id.btn56dalje);
        btnDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfejs.pokreniSledecu();
            }
        });


        txtNaslov = rootView.findViewById(R.id.txt56naslov);

        txtPr1 = rootView.findViewById(R.id.txt56pr1);
        txtPr2 = rootView.findViewById(R.id.txt56pr2);
        txtPr3 = rootView.findViewById(R.id.txt56pr3);
        txtPr4 = rootView.findViewById(R.id.txt56pr4);

        txtTrecelice = rootView.findViewById(R.id.txt56trecelice);

        txtGlagol = rootView.findViewById(R.id.txt56glagol);

        txtPogledaj = rootView.findViewById(R.id.txt56pogledaj);
    }
}
