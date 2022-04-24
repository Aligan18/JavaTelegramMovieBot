package classes.java;

public  class LikeMovies {
    public  String title ;
    public String movie_id ;
    public String type;
    public String chatId ;

    public LikeMovies(String newTitle, String newMovie_id, String newType, String newChatId){
        this.title = newTitle;
        this.movie_id=newMovie_id;
        this.type = newType;
        this.chatId = newChatId;
    }
}