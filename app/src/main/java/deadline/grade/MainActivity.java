package deadline.grade;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private GradeProgressView gradeProgressView;
    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gradeProgressView = (GradeProgressView) findViewById(R.id.gradeProgressView);
        mEditText = (EditText) findViewById(R.id.et);
        mButton = (Button) findViewById(R.id.bt);


//        gradeProgressView.setProgress(100);                 //without animation
//        gradeProgressView.setProgressWidthAnimation(100);   // with animation
//        gradeProgressView.setBackgroundColor(Color.LTGRAY); //set background color
//        gradeProgressView.setProgressColor(Color.WHITE);    //set progress and pointer color
//        gradeProgressView.setLineWidth(60);
//        gradeProgressView.setGapWidth(30);
//        gradeProgressView.setOutLineWidth(5);
//        gradeProgressView.setOnProgressChangeListener(new GradeProgressView.OnProgressChangeListener() {
//            @Override
//            public void onProgressChanged(GradeProgressView gradeProgressView, int progress) {
//                Log.i("GradeProgressView test", "on progress changed: " + progress);
//            }
//        });




        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = 0;
                try {
                    progress = Integer.parseInt(mEditText.getText().toString());
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }

                gradeProgressView.setProgressWidthAnimation(progress);
            }
        });
    }


}
