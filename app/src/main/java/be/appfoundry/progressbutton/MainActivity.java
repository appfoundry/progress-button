package be.appfoundry.progressbutton;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.progressButton) ProgressButton progressButton;
    @Bind(R.id.progressButton2) ProgressButton progressButton2;

    private float progress;
    private Observable<Long> interval;
    private Subscriber<Long> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressButton.setIndeterminate(true);
        progressButton.setAnimationStep(3);
        progressButton.setAnimationDelay(0);

        progressButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        progressButton.startAnimating();
                        break;
                    case MotionEvent.ACTION_UP:
                        progressButton.stopAnimating();
                        break;
                }
                return true;
            }
        });

        progressButton2.setIndeterminate(false);
        progressButton2.setAnimationStep(1);
        progressButton2.setMaxProgress(10);

        progressButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        progress = 0;
                        progressButton2.setProgress(progress);
                        progressButton2.setColor(Color.parseColor("#0000EE"));

                        if (subscriber != null) subscriber.unsubscribe();
                        interval = Observable.interval(1, TimeUnit.SECONDS);
                        subscriber = new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                unsubscribe();
                                progressButton2.setColor(Color.parseColor("#117700"));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("ProgressButton", e.getMessage());
                            }

                            @Override
                            public void onNext(Long aLong) {
                                progress += progressButton2.getAnimationStep();
                                progressButton2.setProgress(progress);
                                if (progress >= progressButton2.getMaxProgress()) {
                                    onCompleted();
                                }
                            }
                        };

                        interval.subscribeOn(Schedulers.newThread())
                                .startWith(0l)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(subscriber);
                        break;
                }
                return true;
            }
        });

    }
}
