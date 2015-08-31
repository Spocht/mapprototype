package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Event")
public class Event extends ParseData {

    public Event(){;}
    public void setName(String name)
    {
        put("name", name);
    }
    public void setGame(Game game)
    {
        addUnique("games", ParseObject.createWithoutData(Game.class, game.getObjectId()));
    }
    public String getName()
    {
        return(getString("name"));
    }
    public List<Game> getGames()
    {
        List<Game> games=getList("games");
        if(null == games)
        {
            try {
                games = this.getParseObject("game").fetchIfNeeded();
            }
            catch (com.parse.ParseException e)
            {
                //todo log?!
                games= new ArrayList<Game>();
            }
        }
        return games;
    }
}
