package be.appfoundry.progressbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //@Bind(R.id.progressButton) ProgressButton progressButton;
    //@Bind(R.id.progressButton2) ProgressButton progressButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*progressButton.setIndeterminate(true);
        progressButton.setAnimationStep(3);
        progressButton.setAnimationDelay(0);

        progressButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        progressButton.startAnimating();
                        //progressButton.setRadius(LayoutUtil.convertDpToPixel(getBaseContext(), 85));
                        break;
                    case MotionEvent.ACTION_UP:
                        progressButton.stopAnimating();
                        //progressButton.setRadius(LayoutUtil.convertDpToPixel(getBaseContext(), 75));
                        break;
                }
                return true;
            }
        });

        progressButton2.setIndeterminate(false);
        progressButton2.setAnimationStep(1);
        progressButton2.setMaxProgress(100);

        progressButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //progressButton2.startAnimating();
                        //progressButton.setRadius(LayoutUtil.convertDpToPixel(getBaseContext(), 85));
                        progressButton2.setProgress(50);
                        break;
                    case MotionEvent.ACTION_UP:
                        //progressButton2.stopAnimating();
                        //progressButton.setRadius(LayoutUtil.convertDpToPixel(getBaseContext(), 75));
                        progressButton2.setProgress(0);
                        break;
                }
                return true;
            }
        });*/

    }
}
