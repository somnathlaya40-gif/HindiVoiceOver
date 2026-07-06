package com.example.hindivoiceover;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private EditText scriptInput;
    private RadioGroup voiceSelection;
    private SeekBar speedControl;
    private SeekBar pitchControl;
    private Button generateButton;
    private Button downloadButton;
    private TextToSpeech textToSpeech;
    private int selectedVoice = 0;

    // 12 Professional Male Voices
    private String[] voices = {
        "Deep Narrator",           // 0
        "Serious Anchor",          // 1
        "Dramatic Voice",          // 2
        "Smooth Narrator",         // 3
        "Powerful Bass",           // 4
        "Clear & Crisp",           // 5
        "Emotional",               // 6
        "Energy Packed",           // 7
        "Gentle Storyteller",      // 8
        "Royal/Majestic",          // 9
        "Street Smart",            // 10
        "Documentary Pro"          // 11
    };

    private float[] pitchValues = {
        0.8f, 1.0f, 1.2f, 0.9f, 0.7f, 1.1f,
        1.3f, 1.5f, 0.85f, 0.75f, 1.25f, 1.0f
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        scriptInput = findViewById(R.id.scriptInput);
        voiceSelection = findViewById(R.id.voiceSelection);
        speedControl = findViewById(R.id.speedControl);
        pitchControl = findViewById(R.id.pitchControl);
        generateButton = findViewById(R.id.generateButton);
        downloadButton = findViewById(R.id.downloadButton);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, this);

        // Set listeners
        generateButton.setOnClickListener(v -> generateVoice());
        downloadButton.setOnClickListener(v -> downloadAudio());

        // Voice selection listener
        voiceSelection.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < voiceSelection.getChildCount(); i++) {
                if (voiceSelection.getChildAt(i).getId() == checkedId) {
                    selectedVoice = i;
                    break;
                }
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("hi", "IN"));
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Hindi language not supported", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ready to generate!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateVoice() {
        String script = scriptInput.getText().toString().trim();

        if (script.isEmpty()) {
            Toast.makeText(this, "Please enter script", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get speed
        float speed = speedControl.getProgress() / 50.0f;
        if (speed < 0.5f) speed = 0.5f;
        if (speed > 2.0f) speed = 2.0f;

        // Get pitch for selected voice
        float pitch = pitchValues[selectedVoice];
        float controlPitch = 1.0f + (pitchControl.getProgress() / 100.0f);
        pitch = pitch * controlPitch;

        // Set pitch and speed
        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);

        // Speak
        textToSpeech.speak(script, TextToSpeech.QUEUE_FLUSH, null);
        Toast.makeText(this, "Generating: " + voices[selectedVoice], Toast.LENGTH_SHORT).show();
    }

    private void downloadAudio() {
        Toast.makeText(this, "Download feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
