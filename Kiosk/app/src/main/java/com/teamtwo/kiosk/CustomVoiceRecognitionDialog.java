package com.teamtwo.kiosk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

import android.os.AsyncTask;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CustomVoiceRecognitionDialog extends Dialog {

    private TextView menuNameTextView;
    private TextView menuPriceTextView;
    private TextView textView;
    private Button startButton;
    private ImageButton closeButton;
    private Context context;
    private SpeechRecognizer mRecognizer;
    private Intent intent;

    private TextToSpeech tts;

    public CustomVoiceRecognitionDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_voice_dialog);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View addMenuLayout = inflater.inflate(R.layout.add_menu, null, false);

        menuNameTextView = addMenuLayout.findViewById(R.id.user_menu_name);
        menuPriceTextView = addMenuLayout.findViewById(R.id.user_menu_price);

        textView = findViewById(R.id.dialogTextView);
        startButton = findViewById(R.id.dialogStartButton);
        new ServerRequestTask().execute();
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(context);

        tts=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        //팝업창 닫는 코드
        textView = findViewById(R.id.dialogTextView);
        startButton = findViewById(R.id.dialogStartButton);
        closeButton = findViewById(R.id.voice_recognition_esc_btn); // 닫기 버튼 가져오기

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 다이얼로그 닫기
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // 말하기 시작할 준비가되면 호출
            Toast.makeText(context.getApplicationContext(),"음성인식 시작",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            // 말하기 시작했을 때 호출
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // 입력받는 소리의 크기를 알려줌
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // 말을 시작하고 인식이 된 단어를 buffer에 담음
        }

        @Override
        public void onEndOfSpeech() {
            // 말하기를 중지하면 호출
        }

        @Override
        public void onError(int error) {
            // 네트워크 또는 인식 오류가 발생했을 때 호출
//            String message;
//
//            switch (error) {
//                case SpeechRecognizer.ERROR_AUDIO:
//                    message = "오디오 에러";
//                    break;
//                case SpeechRecognizer.ERROR_CLIENT:
//                    message = "클라이언트 에러";
//                    break;
//                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                    message = "퍼미션 없음";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK:
//                    message = "네트워크 에러";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                    message = "네트웍 타임아웃";
//                    break;
//                case SpeechRecognizer.ERROR_NO_MATCH:
//                    message = "찾을 수 없음";
//                    break;
//                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                    message = "RECOGNIZER 가 바쁨";
//                    break;
//                case SpeechRecognizer.ERROR_SERVER:
//                    message = "서버가 이상함";
//                    break;
//                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                    message = "말하는 시간초과";
//                    break;
//                default:
//                    message = "알 수 없는 오류임";
//                    break;
//            }
//
//            Toast.makeText(context.getApplicationContext(), "에러 발생 : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 인식 결과가 준비되면 호출
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


            //json --> 바꿔서 검색 하고 장바구니에 추가
            String search = matches.get(0);


            String searchedMenuString = "";
            for(int i = 0; i < matches.size() ; i++){
                textView.setText(matches.get(i));
                searchedMenuString += matches.get(i);
            }

            String[] rs = new String[matches.size()];
            matches.toArray(rs);
            FuncVoiceOrderCheck(rs[0]);
            mRecognizer.startListening(intent);

            mRecognizer.stopListening(); // 음성인식 중지

            for(int i = 0; i < MenuForUser.menyList.size(); i++) {
                String menuNameFromMenyListString = "";
                String menuTextFromMenyListString = "";
                String imageURL = "";
                String price_menu= "";
                try {
                    menuNameFromMenyListString = MenuForUser.menyList.get(i).getString("name");
                    menuTextFromMenyListString = MenuForUser.menyList.get(i).getString("text");
                    imageURL = MenuForUser.menyList.get(i).getString("imageURL");
                    price_menu = MenuForUser.menyList.get(i).getString("price");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View addMenuLayout = inflater.inflate(R.layout.add_menu, null, false);
                View detail_selecting_window_activity = inflater.inflate(R.layout.detail_seleting_window_activity, null, false);
                TextView menuNameTextView = addMenuLayout.findViewById(R.id.user_menu_name);
                TextView menuPriceTextView = addMenuLayout.findViewById(R.id.user_menu_price);
                TextView menuDetailTextView = addMenuLayout.findViewById(R.id.user_menu_detail);
                Button orderingBtn_temp = detail_selecting_window_activity.findViewById(R.id.orderingBtn_temp);


                LinearLayout inner = addMenuLayout.findViewById(R.id.middle_inner);
                inner.setLayoutParams(new LinearLayout.LayoutParams(
                        430,
                        1016
                ));
                inner.setOrientation(LinearLayout.VERTICAL);

                ImageView menuImageView = new ImageView(getContext());
                menuImageView.setId(View.generateViewId());
                menuImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                menuImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        670
                ));
                Glide.with(getContext())
                        .load(imageURL)
                        .into(menuImageView);

                inner.addView(menuImageView, 0);
                if (searchedMenuString.equals(menuNameFromMenyListString)){


                    Log.v("확인 URL : ", imageURL);
                    SelectedMenuDetailActivity selectedMenuDetailActivity = new SelectedMenuDetailActivity(getContext());
                    selectedMenuDetailActivity.show();

                    menuNameTextView.setText(menuNameFromMenyListString);
                    menuPriceTextView.setText(price_menu);

                    selectedMenuDetailActivity.setDetailPopupImage(imageURL);
                    selectedMenuDetailActivity.setDetail_popup_name(menuNameFromMenyListString);
                    selectedMenuDetailActivity.setDetail_popup_script(menuTextFromMenyListString);

                    Button addToCart = selectedMenuDetailActivity.findViewById(R.id.orderingBtn_temp);
                    TextView count_num = selectedMenuDetailActivity.findViewById(R.id.count_num);

                    addToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String selectedMenuName = menuNameTextView.getText().toString();
                            Integer selectedMenuPrice = Integer.parseInt(menuPriceTextView.getText().toString());
                            Integer selectedMenuCnt = Integer.parseInt(count_num.getText().toString());

                            Log.v("name: ",selectedMenuName);
                            Log.v("cnt: ",selectedMenuCnt.toString());
                            boolean addCartStatus = ShoppingCartDialog.addToCart(selectedMenuName, new int[]{selectedMenuPrice, selectedMenuCnt});
                            if (addCartStatus == false) {
                                int curCnt = ShoppingCartDialog.addInfo.get(selectedMenuName)[1];
                                String message = "10개 이상 장바구니에 추가할 수 없습니다. (현재 " + curCnt + "개 담겨있습니다.)";
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            selectedMenuDetailActivity.dismiss();
                        }
                    });
                    break;
                }
            }
            dismiss();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // 부분 인식 결과를 사용할 수 있을 때 호출
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // 향후 이벤트를 추가하기 위해 예약
        }
    };

    public class ServerRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // 서버 URL 설정
                URL url = new URL("https://sm-kiosk.kro.kr/menu/name");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // POST 요청 설정
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                // JSON 데이터 생성
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", "a");
                jsonObject.put("password", "1234");
                jsonObject.put("name", "search");

                // JSON 데이터 전송
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                // 응답 처리
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 서버 응답을 처리
                    // ...
                }
                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //입력된 음성 메세지 확인 후 동작 처리
    private void FuncVoiceOrderCheck(String VoiceMsg){
        if(VoiceMsg.length()<1)return;

        VoiceMsg=VoiceMsg.replace(" ","");//공백제거

        boolean menuFound = false; // Flag

        for (int i = 0; i < MenuForUser.menyList.size(); i++) {
            String dishName = null;
            try {
                dishName = MenuForUser.menyList.get(i).optString("name");
                Log.v("메뉴리스트가 널값인지 체크", dishName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (!dishName.isEmpty()) { // Check if dishName is not empty
                Boolean isMenu = VoiceMsg.indexOf(dishName) > -1;

                if (isMenu) {
                    menuFound = true;
                    mRecognizer.stopListening();
                    FuncVoiceOut("갯수를 선택하시고 장바구니에서 결제해 주세요.");// 음성 출력
                }
            }
        }

        if (!menuFound) {
            mRecognizer.stopListening();
            FuncVoiceOut("그런 메뉴는 없어요~ 다시 주문해 주세요.");
        }




//        if(VoiceMsg.indexOf("김치찌개")>-1 || VoiceMsg.indexOf("김치")>-1){
//            //Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
//            //이름과 일치할 때 shopping cart 에 주문 추가
//            FuncVoiceOut("주문이 완료 되었 습니다.");// 음성 출력
//        }
//
//        if(VoiceMsg.indexOf("취소")>-1 || VoiceMsg.indexOf("삭제")>-1){
//            FuncVoiceOut("메뉴를 취소 할까요?");// 음성 출력
//        }
//
//        if(VoiceMsg.indexOf("사랑해")>-1 || VoiceMsg.indexOf("이스터 에그")>-1){
//            FuncVoiceOut("이스터 에그! 더욱 노력 하는 1943이 되겠 습니다.");
//        }
    }
    //음성 메세지 출력용
    private void FuncVoiceOut(String OutMsg){
        if(OutMsg.length()<1)return;

        tts.setPitch(1.0f);//목소리 톤1.0
        tts.setSpeechRate(1.0f);//목소리 속도
        tts.speak(OutMsg,TextToSpeech.QUEUE_FLUSH,null);

        //어플이 종료할때는 완전히 제거

    }
}