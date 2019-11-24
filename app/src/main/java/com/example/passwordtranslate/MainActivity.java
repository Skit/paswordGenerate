package com.example.passwordtranslate;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends Activity {

    private TextView resultTextVew;
    private TextView result_generate;
    private EditText sourceTextView;
    private View copyButton;
    private PasswordsHelper helper;
    private ImageView quality;
    private TextView qualityText;
    private CompoundButton passUpperCase;
    private CompoundButton passSpecialChars;
    private CompoundButton passNuber;
    private SeekBar passwordCharCount;
    private TextView passwordCharCountText;
    private Button button;
    private int password_length;
    private int minPasswordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new PasswordsHelper(getResources().getStringArray(R.array.russian_lower), getResources().getStringArray(R.array.english_lower));

        resultTextVew = findViewById(R.id.result_text);
        passwordCharCountText = findViewById(R.id.password_count_char_text);
        sourceTextView = findViewById(R.id.source_text);
        result_generate = findViewById(R.id.result_generate);
        copyButton = findViewById(R.id.button_copy);
        quality = findViewById(R.id.quality);
        qualityText = findViewById(R.id.quality_text);
        passwordCharCount = findViewById(R.id.seek_level);
        passUpperCase = findViewById(R.id.pass_uppercase);
        passSpecialChars = findViewById(R.id.pass_symbols);
        passNuber = findViewById(R.id.pass_numbers);
        button = findViewById(R.id.button);

        copyButton.setEnabled(false);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(
                        ClipData.newPlainText(MainActivity.this.getString(R.string.clipboard_title),
                        resultTextVew.getText()));

                Toast.makeText(MainActivity.this, R.string.clipboard_title, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        password_length = minPasswordLength = getResources().getInteger(R.integer.min_password_length);
        passwordCharCountText.setText(
                initPasswordText(R.plurals.start_counter, minPasswordLength, minPasswordLength
         ));

        sourceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String convertedPassword = helper.convert(charSequence);
                resultTextVew.setText(convertedPassword);
                copyButton.setEnabled(charSequence.length() > 0);

                int quality = helper.getQuality(convertedPassword) * 1000;
                MainActivity.this.quality.setImageLevel(quality < 10000 ? quality : 10000);
                qualityText.setText(getQualityPasswordText(quality));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordCharCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passwordCharCountText.setText(String.valueOf(i));
                password_length = i + minPasswordLength;
                String pluralsPhrase;

                if (i > 0) {
                    pluralsPhrase = initPasswordText(R.plurals.counter, password_length, minPasswordLength, i, password_length);
                }
                else {
                    pluralsPhrase = initPasswordText(R.plurals.start_counter, password_length, minPasswordLength);
                }

                passwordCharCountText.setText(pluralsPhrase);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                StringBuilder characters = new StringBuilder();
                characters.append(getResources().getString(R.string.lower));

                if (passUpperCase.isChecked())
                    characters.append(getResources().getString(R.string.upper));

                if (passSpecialChars.isChecked())
                    characters.append(getResources().getString(R.string.special));

                if (passNuber.isChecked())
                    characters.append(getResources().getString(R.string.number));

                for(int i = 0; i < password_length; i++)
                    sb.append(characters.charAt(random.nextInt(characters.length())));

                result_generate.setText(sb.toString());
            }
        });
    }

    String initPasswordText(int plurals, int quantity, Object... formatArgs) {
        return getResources().getQuantityString(plurals, quantity, formatArgs);
    }

    String getQualityPasswordText(int quality) {
        String[] qualities = getResources().getStringArray(R.array.qualities);
        String qualityTextResult;
        if (quality > 0 && quality < 4000)
            qualityTextResult = qualities[1];
        else if (quality >= 4000 && quality < 8000)
            qualityTextResult = qualities[2];
        else if (quality >= 8000)
            qualityTextResult = qualities[3];
        else
            qualityTextResult = qualities[0];

        return  qualityTextResult;
    }
}
