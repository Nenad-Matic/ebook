package com.example.nenad.eudzbenik.lekcija1;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nenad.eudzbenik.LekcijaFragmentInterfejs;
import com.example.nenad.eudzbenik.R;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class Lekcija1Fragment1 extends Lekcija1Fragment {

    private extendedSpannableStringBuilder eSSB;
    private TextView tekstic, brojZvezdica, textView3;
    private ImageView zvezdica;
    private Button dugmeFragmentDalje;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_lekcija1_fragment1, container, false);

        // postavljanje dugmeta za dalje iz fragmenta, poziva interfejs u Lekcija1Activity

        dugmeFragmentDalje = rootView.findViewById(R.id.buttonFragment1Next);
        dugmeFragmentDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfejs.pokreniSledecu();
            }
        });
        dugmeFragmentDalje.setVisibility(View.INVISIBLE);

        tekstic = rootView.findViewById(R.id.tekstic1);

       // TODO zameniti hardkodovanje JSON tekstom, resursima ili bilo cime

        String pocetniTekst = getResources().getString(R.string.lekcija1_tekst1_klik);

        int[] pozicijaTacnih = {1, 3, 10, 12, 13, 20, 24, 25, 26, 28, 33, 36, 43, 45};


        eSSB = new extendedSpannableStringBuilder(pocetniTekst, pozicijaTacnih);

        eSSB.postaviSpanove();
        CalligraphyTypefaceSpan philosopherSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getActivity().getAssets(), "Philosopher-Regular.ttf"));

        eSSB.setSpan(philosopherSpan, 0, pocetniTekst.length(), 0);
        tekstic.setText(eSSB);

        tekstic.setHighlightColor(Color.TRANSPARENT);
        tekstic.setMovementMethod(LinkMovementMethod.getInstance());

        brojZvezdica = rootView.findViewById(R.id.textViewBrojZvezdica);
        brojZvezdica.setText(eSSB.getBrojTacnih() + "/" + eSSB.getPozicijaTacnihReci().length);

        zvezdica = rootView.findViewById(R.id.imageViewZvezdica);

        tekstic.setVisibility(View.INVISIBLE);
        zvezdica.setVisibility(View.INVISIBLE);
        brojZvezdica.setVisibility(View.INVISIBLE);

        handler = new Handler();
        Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                tekstic.setVisibility(View.VISIBLE);
                zvezdica.setVisibility(View.VISIBLE);
                brojZvezdica.setVisibility(View.VISIBLE);

                Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(400);
                tekstic.startAnimation(fadeIn);

            }
        };
        handler.postDelayed(runn1, 2249);

        return rootView;
    }


    // Unutarnje klase potrebne za prvu lekciju:
    // extendedSpannableStringBuilder
    // i
    // clickableSpanWithPosition

    protected class extendedSpannableStringBuilder extends SpannableStringBuilder {

        /* variables */

        private final int[] pozicijaTacnihReci;

        private int brojTacnih;
        private int brojNetacnih;
        private int brojPreostalih;

        public int[] getPozicijaTacnihReci() {
            return pozicijaTacnihReci;
        }
        public int getBrojTacnih() {
            return brojTacnih;
        }
        public int getBrojNetacnih () {
            return brojNetacnih;
        }

        /* constructor */


        public extendedSpannableStringBuilder(CharSequence text, int[] pozicijaTacnihReci) {
            super(text);
            this.pozicijaTacnihReci = pozicijaTacnihReci;
            this.brojTacnih = 0;
            this.brojNetacnih = 0;
            this.brojPreostalih = pozicijaTacnihReci.length;
            // todo ako bude interfejs drzacInformacija.getInstance().postaviBrojUkupno(pozicijaTacnihReci.length);
        }

        /* methods */



        public int kolikoPreostalih () {
            return brojPreostalih;
        }
        public int kolikoUkupnoZaKliknuti () {
            return pozicijaTacnihReci.length;
        }

    /*    dobijam redni broj reci od clickableSpana
    u extended span stringu imam poziciju tacnih reci.
    onda u ovoj funkciji prodjem kroz taj niz i pogledam da li je pogodjena tacna rec.
        */

        public boolean jesamPogodio(int pozicijaReci) {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    dugmeFragmentDalje.setVisibility(View.VISIBLE);
                    Animation animacijaDugmenceta = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_shake_once);
                    dugmeFragmentDalje.startAnimation(animacijaDugmenceta);
                }
            };
            //TODO optimizacija optimizovati ovu metodu
            for(int i=0; i<pozicijaTacnihReci.length; i++) {
                if (pozicijaReci == pozicijaTacnihReci[i]) {
                    brojTacnih++;
                    brojPreostalih--;
                    if (brojTacnih == getPozicijaTacnihReci().length) {
                        //pronasao sve tacne
                        handler.postDelayed(runnable, 500);
                        cuvarPodataka1.getInstance().setBrojGresaka11(getBrojNetacnih());
                    }
                    //todo interfejs drzacInformacija.getInstance().povecajPogodjene();
                    return true;}
            }
            brojNetacnih++;
            return false;
        }

        public extendedSpannableStringBuilder postaviSpanove () {

            int pozicijaPocetkaReci = 0;
            int pozicijaKrajaReci;
            int redniBrojSpana = 1;

            // provera da li string pocinje recju
            boolean inAWord;
            if ( Character.isLetter(this.charAt(0) ) )
                inAWord = true;
            else inAWord = false;

            for (int i = 0; i<this.length(); i++) {

                if (Character.isLetter(this.charAt(i))) {
                    // trenutno je slovo
                    if (inAWord) {
                        //u reci sam pa produzavam dalje
                        continue;
                    }
                    else {
                        //nasao sam slovo a nisam u reci. Dakle pocetak nove reci.
                        inAWord = true;
                        pozicijaPocetkaReci = i;

                    }

                }
                else {
                    // trenutno nije slovo
                    if (inAWord) {
                        // stigao do kraja reci + 1
                        inAWord = false;
                        pozicijaKrajaReci = i;
                        this.setSpan(new clickableSpanWithPosition(redniBrojSpana, pozicijaPocetkaReci, this),
                                pozicijaPocetkaReci, pozicijaKrajaReci, 0);
                        redniBrojSpana++; // postavio sam span, sledeci span ce biti uvecan za jedan redni broj.

                        continue;
                    }
                    // nije slovo i nisam u reci.
                    else continue;
                }

            }


            return this;
        }

    }

    private class clickableSpanWithPosition extends ClickableSpan {

        // variables

        private int redniBr;
        private int mestoSlova;
        private boolean vecKliknut;
        private extendedSpannableStringBuilder eSSB;

        // constructor

        public clickableSpanWithPosition(int redniBrojSpana, int mestoPrvogSlovaReci, extendedSpannableStringBuilder eSSB) {
            this.redniBr = redniBrojSpana;
            this.mestoSlova = mestoPrvogSlovaReci;
            this.eSSB = eSSB;
            this.vecKliknut = false;

        }

        // methods

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);


            // nem pojma kako ovo radi. Ali radi.
        }

        @Override
        public void onClick(View viewT) {


            // view je textview

            if (vecKliknut == false) {

                vecKliknut = true;

                final TextView teksVju = (TextView) viewT;

                // eSSB.jesamTacnoKliknuo?

                // jesam:
                if (eSSB.jesamPogodio(redniBr) == true) {

                    eSSB.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.holoGreenDark)),
                            mestoSlova, mestoSlova + 1, 0);
                    char velikoSlovo = Character.toUpperCase(eSSB.charAt(mestoSlova));
                    eSSB.replace(mestoSlova, mestoSlova + 1, String.valueOf(velikoSlovo));

                    soundControl().playFX(R.raw.money);

                    Animation animacijaZvezdice = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_pulse_once);
                    animacijaZvezdice.setInterpolator(new AccelerateDecelerateInterpolator());
                    animacijaZvezdice.setRepeatCount(4);
                    zvezdica.startAnimation(animacijaZvezdice);
                }

                // nisam:
                else {

                    eSSB.setSpan(new ForegroundColorSpan(Color.rgb(200, 50, 50)), mestoSlova, mestoSlova + 1, 0);

                    soundControl().playFX(R.raw.wood);

                }
                teksVju.setText(eSSB);



            } else return;

            TextView brojZvezdica = rootView.findViewById(R.id.textViewBrojZvezdica);
            brojZvezdica.setText(eSSB.getBrojTacnih() + "/" + eSSB.getPozicijaTacnihReci().length);


        }


    }
}
