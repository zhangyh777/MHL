package service;

import dao.MenuDAO;
import domain.Menu;

import java.util.List;

public class MenuService {

    private MenuDAO menuDAO = new MenuDAO();

    /**
     * 显示菜单
     * @return
     */
    public List<Menu> getMenu(){
        String sql = "SELECT * FROM menu";
        return menuDAO.queryMany(sql, Menu.class);
    }
    public Menu getMenuById(int id){
        return menuDAO.querySingle("select * from menu where id = ?",Menu.class,id);
    }
}
