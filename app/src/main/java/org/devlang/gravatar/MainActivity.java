package org.devlang.gravatar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView ivGravatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GravatarManager.setFileCachePath(getCacheDir().getAbsolutePath());

        Intent intent = getIntent();
        final int account = intent.getIntExtra("account", 0);

        ivGravatar = findViewById(R.id.iv_gravatar);
        ivGravatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("account", account + 1);
                startActivity(intent);
            }
        });

        Gravatar gravatar = new Gravatar.Builder(account + "@devlang.org")
                .setDefaultImage(Gravatar.IMAGE_MONSTERID)
                .setSize(500)
                .build();
        GravatarRequest gravatarRequest = GravatarRequest.obtain(gravatar, new GravatarCallBack() {
            @Override
            public void success(GravatarRequest request, GravatarResponse response) {
                Bitmap bitmap = response.getBitmap();
                ivGravatar.setImageBitmap(bitmap);
            }

            @Override
            public void error(GravatarRequest request, String code, String erroMsg) {
                Log.d("cmf", code + " : " + erroMsg);
            }
        });
        GravatarManager.enqueue(gravatarRequest);
    }
}
