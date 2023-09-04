package com.example.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.teamtwo.kiosk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddMenuPopUp extends AppCompatActivity {

    private static final String BASE_URL = "https://sm-kiosk.kro.kr/menu/add";
//private static final String BASE_URL = "http://192.168.3.49:4000/menu/add";

    EditText menuNameEditText;
    EditText menuPriceEditText;
    EditText scriptEditText;
    Spinner category1Spinner;

    Button registerButton;
    List<String> itemList;
    ImageButton addImageButton;
    Uri selectedImageUri;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_menu);

        addImageButton = findViewById(R.id.empty_plate);
        addImageButton.setImageResource(R.drawable.empty_plate);

        menuNameEditText = findViewById(R.id.menu_name);
        menuPriceEditText = findViewById(R.id.menu_price);
        scriptEditText = findViewById(R.id.script);
        category1Spinner = findViewById(R.id.category_1_spinner);

        registerButton = findViewById(R.id.register_button);

        itemList = new ArrayList<>();
        itemList.add("안주");
        itemList.add("주류");
        itemList.add("음료");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMenuPopUp.this, R.layout.spinner_list_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category1Spinner.setAdapter(adapter);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent, 1);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri == null) {
                    Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerButton.setBackgroundResource(R.drawable.button_background_clicked);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registerButton.setBackgroundResource(R.drawable.button_back);
                    }
                }, 300);

                String id = LoginActivity.cur_id;
                String password = LoginActivity.cur_pw;
                String name = menuNameEditText.getText().toString();
                String text = scriptEditText.getText().toString();
                int price = -1;
                try {
                    price = Integer.parseInt(menuPriceEditText.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedCategory1 = category1Spinner.getSelectedItem().toString();

                addToServer(id, password, name, text, price, selectedCategory1, selectedImageUri);
            }
        });
    }

    public void addToServer(String id, String password, String name, String text, int price, String selectedCategory1, Uri selectedImageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            File imageFile = new File(getCacheDir(), "image_" + id + ".jpg");
            copyStreamToFile(inputStream, imageFile); // 파일에 스트림을 복사합니다. Utils 클래스에 아래 제공된 copyStreamToFile 메소드를 추가하십시오.

            OkHttpClient client = new OkHttpClient();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("id", id);
            builder.addFormDataPart("password", password);
            builder.addFormDataPart("name", name);
            builder.addFormDataPart("text", text);
            builder.addFormDataPart("price", Integer.toString(price));
            builder.addFormDataPart("category1", selectedCategory1);
            builder.addFormDataPart("imageFile", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                    // Handle failure
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddMenuPopUp.this, "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    AddMenuPopUp.this.onBackPressed();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.d("POST", "Response: " + responseBody);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddMenuPopUp.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        });
                        // Handle success
                    } else {
                        Log.e("POST", "Request failed. Code: " + response.code());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddMenuPopUp.this, "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Handle failure
                    }
//                    AddMenuPopUp.this.onBackPressed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                selectedImage = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                addImageButton.setImageBitmap(selectedImage);
                selectedImageUri = data.getData(); // 선택한 이미지 파일의 Uri 저장
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Utils 클래스에 추가할 메소드:
    public static void copyStreamToFile(InputStream in, File file) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }
}