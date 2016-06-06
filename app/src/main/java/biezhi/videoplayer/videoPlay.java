package biezhi.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.rey.material.widget.Spinner;

public class videoPlay extends AppCompatActivity {

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initClass();
    }
    private void initClass()
    {
        spinner = (Spinner)findViewById(R.id.video_quality_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.quality,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
