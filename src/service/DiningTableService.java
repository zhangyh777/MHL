package service;

import dao.DiningTableDAO;
import domain.DiningTable;

import java.util.List;
@SuppressWarnings("all")
public class DiningTableService {
    DiningTableDAO diningTableDAO = new DiningTableDAO();

    /**
     * 获取所有餐桌的状态
     * @return
     */
    public List<DiningTable> getAllTableState(){
        String sql = "SELECT id,state FROM diningtable";
        return diningTableDAO.queryMany(sql,DiningTable.class);
    }

    /**
     * 查询指定id餐桌的状态
     * @param id
     * @return
     */
    public DiningTable getTableById(int id){
        String sql = "SELECT state FROM diningtable WHERE id = ?";
        return (DiningTable) diningTableDAO.querySingle(sql,DiningTable.class,id);
    }

    /**
     * 预定餐桌
     * @return
     */
    public boolean orderTable(int id,String name,String tel){
        String sql = "UPDATE diningtable SET state = ? ,orderName = ?,orderTel = ? WHERE id = ?";
        int update = diningTableDAO.update(sql,"已经预定",name,tel,id);
        return update > 0;
    }

    /**
     * 更新餐桌状态
     * @param id
     * @param state
     * @return
     */
    public boolean updateDiningTableState(int id,String state){
        String sql = "update diningtable set state = ? where id = ?";
        int rows = diningTableDAO.update(sql,state,id);
        return rows > 0;
    }
    public boolean resetDiningTableState(int id){
        String sql = "update diningtable set state = ?,orderName = '',orderTel = '' where id = ?";
        int rows = diningTableDAO.update(sql,"空",id);
        return rows > 0;
    }


}
