package server.mvp.Model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import resources.Cell;
import resources.CellState;
import resources.Game;
import server.mvp.Presenter.IPresenter;


class ModelServer implements IModelServer{

    ArrayList<Game> games = new ArrayList<>();
    ArrayList<IPresenter> list_players = new ArrayList<>();
    HashMap<Integer, Integer> presenter_game = new HashMap<>();
    ArrayDeque<Integer> free_games = new ArrayDeque<>();
    ArrayList<Cell> buffer = new ArrayList<>();

    public ModelServer() {}

    public void setCell(int p_id, Cell new_c) {
        int game_id = getGameId(p_id);
        games.get(game_id).setCell(new_c);
        buffer.set(game_id, new_c);
        refresh(game_id);
    }

    public void generateNewItem(int p_id) {
        int game_id = getGameId(p_id);
        buffer.set(game_id, games.get(game_id).generateNewItem()); // what if return is (-1, -1, -1)?
        refresh(game_id);
    }

    void refresh(int game_id)
    {
        for (int i = 0; i < list_players.size(); i++) {
            if(getGameId(i) == game_id) {// add additional HashMap game_id->p_ids
                list_players.get(i).update();
            }
        }
    }

    public Cell getBuffer(int p_id) {
        int game_id = getGameId(p_id);
        System.out.printf("buffer p_id = %d, game = %d, x = %d, y = %d, state = %s\n", p_id, game_id, buffer.get(game_id).x, buffer.get(game_id).y, buffer.get(game_id).state);
        return buffer.get(game_id);
    }

    public void addPresenter(int p_id, IPresenter p) {
        list_players.add(p);
        if (free_games.isEmpty()) {
            games.add(new Game());
            buffer.add(new Cell());
            presenter_game.put(p_id, games.size()-1);
            generateNewItem(p_id);
            free_games.offerLast(games.size()-1);
        } else {
            presenter_game.put(p_id, free_games.pollFirst());
        }
        refresh(getGameId(p_id));
    }

    public void removePresenter(IPresenter p) {
        list_players.remove(p);
    }

    int getGameId(int p_id) {
        return presenter_game.get(p_id);
    }
}
