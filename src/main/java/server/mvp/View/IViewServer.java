package server.mvp.View;


import resources.Cell;

public interface IViewServer {
    int getOp();
    void setCell(final Cell point);
    Cell getCell();
    Integer getDirection();
    void setOp(int op);
}
