package com.example.coursecomrade;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MeetCreatorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_creators);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.baseline_arrow_back_24));
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Set toolbar title
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Meet the Creators");

        // Profile button
        ImageButton buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(v -> {
            // Profile options logic
        });

        LinearLayout githubLinksContainer = findViewById(R.id.githubLinksContainer);
        githubLinksContainer.setPadding(0, 0, 0, 0);
        githubLinksContainer.removeAllViews();

        String[] githubNames = {
                "Matthew's GitHub",
                "Shane's GitHub",
                "Abram's GitHub",
                "Geo's GitHub"
        };

        String[] githubUrls = {
                "https://github.com/matthewthomasbeck",
                "https://github.com/boyndasouth",
                "https://github.com/ksaii",
                "https://github.com/DarthPeanut"
        };

        int[] backgroundColors = {
                R.color.tan_shade_1,
                R.color.tan_shade_2,
                R.color.tan_shade_3
        };

        for (int i = 0; i < githubNames.length; i++) {
            TextView githubLink = new TextView(this);
            githubLink.setText(githubNames[i]);
            githubLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            githubLink.setTextColor(getResources().getColor(android.R.color.white));
            githubLink.setBackgroundColor(getResources().getColor(backgroundColors[i % backgroundColors.length]));
            githubLink.setClickable(true);
            githubLink.setFocusable(true);

            githubLink.setPadding(32, 32, 32, 32);
            githubLink.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    250
            );
            params.setMargins(0, 0, 0, 0);
            githubLink.setLayoutParams(params);

            int finalI = i;
            githubLink.setOnClickListener(v -> openGitHub(githubUrls[finalI]));

            githubLinksContainer.addView(githubLink);
        }
    }

    private void openGitHub(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
