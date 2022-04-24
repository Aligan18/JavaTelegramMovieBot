package classes.java;

import java.util.Map.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.internal.NonNull;
import lombok.SneakyThrows;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;


import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;


import java.util.*;
import java.util.stream.IntStream;


public class MyTelegramBot extends TelegramLongPollingBot {
    DatabaseReference databaseReference;
    @SneakyThrows
    public static void main (String[] args) {

        Thread t=new Thread(new ShowDbChanges());

        t.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyTelegramBot bot = new MyTelegramBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }





    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            handleMessage(update.getMessage());

        }
        else if (update.hasCallbackQuery()) {

            String callback = update.getCallbackQuery().getData();
            System.out.println("Полученный get callback:\n" + callback);
           String[] command =  callback.split("/");


                if (command[0].equals("getMovieInfo")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());

                    getRequestExampleTest(update.getCallbackQuery().getMessage(), "infoById",callback);

                }
                else if (command[0].equals("getTrailer")){
                    getRequestExampleTest(update.getCallbackQuery().getMessage(),"getTrailer" ,  callback);



                }
                else if (command[0].equals("removeLikeMove")){
                    System.out.println(command[1]);
                    String title = command[1] ;
                    System.out.println("TITLE " +title);
                    String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("users/"+chatId);
                    ref.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ObjectMapper mapper = new ObjectMapper();
                            String jsonString = null;
                            Object name = dataSnapshot.getValue();
                            try {
                                jsonString = mapper.writeValueAsString(name);
                                Map<String, Object> map = mapper.readValue(jsonString, new TypeReference<>() {});
                                String key = getFirstLevelKeys(map);
                                ref.child(key).removeValueAsync();
                                System.out.println("keys"+ key);

                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }






//                            System.out.println("name" +name);
//                            String key  = ref.push().getKey();
////
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
                else if (command[0].equals("likeMovie")){

                    String title = command[3] ;
                    String movie_id =command[2];
                    String type = command[1];
                    String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                    System.out.println(title +" "+ movie_id+" "+ type+" " +chatId);


                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("users/"+chatId);
                    ref.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            if (name == null ) {
                                System.out.println("snapshot"+ dataSnapshot +"   name"+ name);
                                String key  = ref.push().getKey();
                                ref.child(key).setValueAsync(new LikeMovies(title,movie_id,type,chatId));
                            }
                            else {
                                System.out.println("snapshot"+ dataSnapshot +"   name"+ name);
                                System.out.println("flag"+ name);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else if (command[0].equals("getOverview")){

                    getRequestExampleTest ( update.getCallbackQuery().getMessage(),"getOverview" ,  callback );


                }


                else{

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }


    public synchronized void  getRequestExampleTest (Message message,String type, String typeAndId ) throws Exception {



            if (type == "\uD83C\uDFAC Популярные фильмы") {
                    String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular?api_key=" + TMDB_KEY + "&language=en-US&page=1";
                    URL url = JsonUtils.createUrl(POPULAR_MOVIES_URL);
                    String resultJson = JsonUtils.parseUrl(url);
                    System.out.println("Полученный JSON:\n" + resultJson);

                    JSONObject [] movie_titles = new JSONObject[10];
                    for (int i = 0; i < 10; i++) {
                        JSONObject movie = JsonUtils.getPopularMovies(resultJson, i);
                        System.out.println(movie.get("original_title"));
                        movie_titles[i] = movie;
                    }

                    sendInlineKeyBoardMessage(movie_titles, message.getChatId().toString(), type);
            }

            else if (type=="\uD83D\uDCFA Популярные сериалы") {

                String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/trending/tv/day?api_key="+ TMDB_KEY +"&language=en-US&page=1";
                URL url = JsonUtils.createUrl(POPULAR_SERIES_URL);
                String resultJson = JsonUtils.parseUrl(url);
                System.out.println("Полученный JSON:\n" + resultJson);

                JSONObject[] series_titles = new JSONObject[10];
                for (int i = 0; i < 10; i++) {
                    JSONObject movie = JsonUtils.getPopularMovies(resultJson, i);
                    System.out.println(movie.get("original_name"));
                    series_titles[i] = movie;
                }

                sendInlineKeyBoardMessage(series_titles, message.getChatId().toString(), type);
            }
            else if (type=="infoById") {
                String[] words = typeAndId.split("/");
                String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/"+words[1]+"/"+words[2]+"?api_key="+TMDB_KEY+"&language=en";
                URL url = JsonUtils.createUrl(POPULAR_SERIES_URL);
                String resultJson = JsonUtils.parseUrl(url);
               JSONObject jsonObject = new JSONObject(JsonUtils.getInfoMovies(resultJson));
                System.out.println("Полученный JSON:\n" + resultJson);
                sendInlineKeyBoardForInfo(jsonObject, message.getChatId().toString(), words[1]);


            }
            else if (type=="getRandom") {
                JSONObject jsonObject ;
                boolean flag= true;
                do {
                    int random_id = getRandomIntegerBetweenRange(2, 6500);
                    String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/movie/" + random_id + "?api_key=" + TMDB_KEY + "&language=en";
                    URL url = JsonUtils.createUrl(POPULAR_SERIES_URL);
                    String resultJson = JsonUtils.parseUrl(url);
                     jsonObject = new JSONObject(JsonUtils.getInfoMovies(resultJson));
                    if(jsonObject.get("status")!="Released"){
                        flag = false;
                    }
                    System.out.println("Полученный JSON:\n" + resultJson);
                }
                while (flag);



                sendInlineKeyBoardForInfo(jsonObject, message.getChatId().toString(), "movie");


            }



            else if (type=="getOverview") {
                String[] words = typeAndId.split("/");
                String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/"+words[1]+"/"+words[2]+"?api_key="+TMDB_KEY+"&language=ru";
                URL url = JsonUtils.createUrl(POPULAR_SERIES_URL);
                String resultJson = JsonUtils.parseUrl(url);
                JSONObject jsonObject = new JSONObject(JsonUtils.getInfoMovies(resultJson));
                System.out.println("Полученный JSON:\n" + resultJson);
               sendMsg(message.getChatId().toString(),  jsonObject.get("overview").toString()); //


            }
            else if (type=="getTrailer") {
//
                String[] words = typeAndId.split("/");
                String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/"+words[1]+"/"+words[2]+"/videos?api_key="+TMDB_KEY+"&language=en-US";
                URL url = JsonUtils.createUrl(POPULAR_SERIES_URL);
                String resultJson = JsonUtils.parseUrl(url);
                JSONObject jsonObject = new JSONObject(JsonUtils.getPopularMovies(resultJson,0));
                System.out.println("Полученный JSON:\n" + resultJson);
                sendMsg(message.getChatId().toString(), "https://www.youtube.com/watch?v="+ (jsonObject.get("key").toString()));


            }
            else if (type=="getSimilar") {
                String[] words = typeAndId.split("/");
                String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/"+words[1]+"/"+words[2]+"?api_key="+TMDB_KEY+"&language=en";
                URL url = JsonUtils.createUrl(POPULAR_SERIES_URL);
                String resultJson = JsonUtils.parseUrl(url);
                JSONObject jsonObject = new JSONObject(JsonUtils.getInfoMovies(resultJson));
                System.out.println("Полученный JSON:\n" + resultJson);
                sendInlineKeyBoardForInfo(jsonObject, message.getChatId().toString(), words[1]);


            }


            if (type == "search") {
                String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/search/multi?api_key="+TMDB_KEY +"&language=en-US&query="+typeAndId +"&page=1&include_adult=false";
                URL url = JsonUtils.createUrl(POPULAR_MOVIES_URL);
                String resultJson = JsonUtils.parseUrl(url);
                System.out.println("Полученный JSON:\n" + resultJson);
//
                JSONObject [] movie_titles = new JSONObject[10];
                for (int i = 0; i < 10; i++) {
                    JSONObject movie = JsonUtils.getPopularMovies(resultJson, i);

                    movie_titles[i] = movie;
                }

                sendInlineKeyBoardMessage(movie_titles, message.getChatId().toString(), " ");
            }



    }

    public static String getFirstLevelKeys(Map<String, Object> map) {
        String key= null ;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            key = entry.getKey();
        }
        return key;
    }

    public static int getRandomIntegerBetweenRange(double min, double max){
        int x = (int) ((int)(Math.random()*((max-min)+1))+min);
        return x;
    }


    @SneakyThrows
    private void handleMessage(Message message){


        if (message.hasText() && message.hasEntities()){

            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e->"bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command =  message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

                switch(command){
                    case "/start":

                        sendMsg(message.getChatId().toString(),"Выберите то что вам нужно");

                        return;
                }
            }
        }
        else if (message.getText().equals("\uD83C\uDFAC Популярные фильмы")) {
            getRequestExampleTest(message,"\uD83C\uDFAC Популярные фильмы","");
        }
        else if (message.getText().equals("\uD83D\uDCFA Популярные сериалы")) {
            getRequestExampleTest(message,"\uD83D\uDCFA Популярные сериалы","");
        }
        else if (message.getText().equals("❤ Понравившиеся фильмы")) {
            getFirebaseRequest(message,"❤ Понравившиеся фильмы");
        }
        else if (message.getText().equals("\uD83D\uDD0E Поиск фильма")) {
          sendMsg(message.getChatId().toString(), "Введите название фильма");
        }
        else if (message.getText().equals("\uD83C\uDFB2 Случайный фильм")) {


            getRequestExampleTest(message,"getRandom"," ");
        }
        else if (message.getText().equals("About")) {
            sendMsg(message.getChatId().toString(),"Автор : Ганиев Алишер"+ "\n" + "https://github.com/Aligan18/JavaTelegramMovieBot.git");
        }

        else {
            getRequestExampleTest(message,"search",message.getText());
        }

    }

    private void getFirebaseRequest(Message message, String type) {


        String chatId = message.getChatId().toString();
        System.out.println(chatId);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/"+chatId);
        System.out.println(ref);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object name = dataSnapshot.getValue();
                System.out.println(" фильмов " + name);
                if (name == null) {

                    System.out.println("нету фильмов ");
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = null;
                    try {
                        jsonString = mapper.writeValueAsString(name);


                        Map<String, Object> map = mapper.readValue(jsonString, new TypeReference<>() {});
                        List<LikeMovies> keys =  getKeys(map);
                        System.out.println(map);
                        System.out.println("keys "+keys+ "name "+name);
                        sendInlineKeyBoardLikeMovies(keys,chatId);
//


                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static List<LikeMovies> getKeys(Map<String, Object> map) {
        List<LikeMovies> keys = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getValue() instanceof Map) {
                Map<String, Object> nested = (Map<String, Object>) entry.getValue();
                System.out.println("map   "+nested.get("title"));
                LikeMovies key = new LikeMovies(nested.get("title").toString(), nested.get("movie_id").toString(), nested.get("type").toString(), nested.get("chatId").toString() );
                keys.add(key);
            }
        }
        return keys;
    }

    public static void findAllKeys(Object object, Set<String> finalKeys) {
        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;

            jsonObject.keySet().forEach(childKey -> {
                finalKeys.add((String) childKey);
                findAllKeys(jsonObject.get(childKey), finalKeys);
            });
        } else if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;

            IntStream.range(0, jsonArray.length())
                    .mapToObj(jsonArray::get)
                    .forEach(o -> findAllKeys(o, finalKeys));
        }
    }





    public synchronized void sendInlineKeyBoardMessage( JSONObject[] movie_titles, String chatId, String type) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();



        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < movie_titles.length; i++) {
            String keyTitle = null;
            String value = null;

            if (type == "\uD83C\uDFAC Популярные фильмы") {

                keyTitle= "original_title";
                value = "movie";
            }

            else if (type=="\uD83D\uDCFA Популярные сериалы") {

                keyTitle= "original_name";
                value = "tv" ;
            }

            else if (movie_titles[i].get("media_type").equals("movie")) {

                keyTitle= "original_title";
                value = "movie";
            }

            else if (movie_titles[i].get("media_type").equals("tv")) {

                keyTitle= "original_name";
                value = "tv" ;
            }
            System.out.println(movie_titles[i].get("media_type"));
            System.out.println(keyTitle);
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText((String) movie_titles[i].get(keyTitle));
            inlineKeyboardButton1.setCallbackData("getMovieInfo/"+value+"/"+(movie_titles[i].get("id").toString()));
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton1);
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Список  : " + type );
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage) ;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    public synchronized void sendInlineKeyBoardLikeMovies( List<LikeMovies> movie_titles, String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();



        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < movie_titles.size(); i++) {

            System.out.println(movie_titles.get(i));

            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText((String) movie_titles.get(i).title);
            inlineKeyboardButton1.setCallbackData("getMovieInfo/"+movie_titles.get(i).type+"/"+(movie_titles.get(i).movie_id));
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText("\uD83D\uDC94");
            inlineKeyboardButton2.setCallbackData("removeLikeMove/"+movie_titles.get(i).title);


            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton1);
            keyboardButtonsRow1.add(inlineKeyboardButton2);
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText("Список  : Понравившихся фильмов " );
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage) ;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendInlineKeyBoardForInfo( JSONObject movie_info, String chatId, String type) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        String keyTitle = null;
        String value = null;
        String title= null ;

        if (Objects.equals(type, "tv")){
            title = movie_info.get("name").toString();
        }
        else if (Objects.equals(type, "movie")) {
            System.out.println("nbg"+ type);
            title = movie_info.get("original_title").toString();
        }

        System.out.println("name "+ title);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();


//                    /// Кнопка Описания movie_info.get("overview").toString()
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("Описание \uD83D\uDCDC");
            inlineKeyboardButton1.setCallbackData("getOverview/"+type+"/"+(movie_info.get("id").toString()));
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(inlineKeyboardButton1);
                    /// Кнопка Трейлер
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText("Трейлер \uD83C\uDFAC");
            inlineKeyboardButton2.setCallbackData("getTrailer/"+type+"/"+(movie_info.get("id").toString()));
            List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
            keyboardButtonsRow2.add(inlineKeyboardButton2);
            /// Кнопка Трейлер
            InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
            inlineKeyboardButton4.setText("Смотреть позже ❤");
            inlineKeyboardButton4.setCallbackData("likeMovie/"+type+"/"+(movie_info.get("id").toString())+"/"+title);
            List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
            keyboardButtonsRow4.add(inlineKeyboardButton4);
                /// Кнопка похожие Фильмы
            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
            inlineKeyboardButton3.setText("Похожие Фильмы  \uD83C\uDF9E" );
            inlineKeyboardButton3.setCallbackData("getSimilar/"+type+"/"+(movie_info.get("id").toString()));
            List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
            keyboardButtonsRow3.add(inlineKeyboardButton3);


            rowList.add(keyboardButtonsRow1);
            rowList.add(keyboardButtonsRow2);
            rowList.add(keyboardButtonsRow4);
//            rowList.add(keyboardButtonsRow3);


        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("https://image.tmdb.org/t/p/w500/" + ( movie_info.get("poster_path") ));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);


//         URL url = new URL("https://image.tmdb.org/t/p/w500/" + ( movie_info.get("poster_path").toString()) + ".jpg");
//        BufferedReader  = new BufferedReader(new InputStreamReader(url.openStream()));
//        String line;
//        InputFile photo  = new URL(url).openStream();
//        SendPhoto sendPhoto = new SendPhoto();
//        sendPhoto.setChatId(chatId);
//        sendPhoto.setPhoto(photo);
//

        try {

            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    public synchronized void setButtons(SendMessage sendMessage) {

        // Create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Create a list of keyboard rows
        List keyboard = new ArrayList<>();
        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        // Add buttons to the first keyboard row

        keyboardFirstRow.add(new KeyboardButton("\uD83C\uDFAC Популярные фильмы"));

        // Second keyboard row
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton("\uD83D\uDD0E Поиск фильма"));

        KeyboardRow keyboardSecondRow2 = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow2.add(new KeyboardButton("\uD83D\uDCFA Популярные сериалы"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("❤ Понравившиеся фильмы"));

        KeyboardRow keyboardSecondRow3 = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow3.add(new KeyboardButton("\uD83C\uDFB2 Случайный фильм"));
        KeyboardRow keyboardSecondRow4 = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow4.add(new KeyboardButton("About"));

        // Add all of the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow2);
        keyboard.add(keyboardRow3);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardSecondRow3);
        keyboard.add(keyboardSecondRow4);
        // and assign this list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }




    public static final String  TMDB_KEY = "126c9b63ad6643b0be5be605e45bf5d5";
    public  static final String TOKEN = "5109053300:AAHNkl13Zb3dOWuMw5UuTfniNBoOEVPkg7o";
    public static final String USERNAME ="java_movie_bot";

    @Override
    public  String getBotToken(){return TOKEN;}
    @Override
    public String getBotUsername(){return USERNAME;}


}

