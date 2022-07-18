package service;

import dao.BillDAO;
import dao.MenuDAO;
import domain.Bill;


import java.util.List;
import java.util.UUID;
@SuppressWarnings("all")
public class BillService {

    MenuDAO menuDAO = new MenuDAO();
    MenuService mS = new MenuService();
    DiningTableService dtS = new DiningTableService();
    BillDAO billDAO = new BillDAO();
    /**
     * 点餐功能
     * 1.生成账单
     * 2.更新餐桌状态
     * @param menuId    菜品编号
     * @param nums      菜品数量
     * @param diningTableId     餐桌Id
     */
    public boolean orderMenu(int menuId,int nums,int diningTableId){
        String billId = UUID.randomUUID().toString();
        String sql = "INSERT INTO bill VALUES (null,?,?,?,?,?,?,?,now(),'未结账')";
        //1.生成账单
        int rows = menuDAO.update(sql,billId,menuId,mS.getMenuById(menuId).getName(),mS.getMenuById(menuId).getPrice(),nums,mS.getMenuById(menuId).getPrice()*nums,diningTableId);
        if (rows<=0){
            return false;
        }
        //2.点餐成功之后才更新餐桌状态
        return dtS.updateDiningTableState(diningTableId,"就餐中");

    }


    /**
     * 查看所有账单
     * @return
     */
    public List<Bill> getBill(){
        String sql = "select * from bill";
        return billDAO.queryMany(sql,Bill.class);
    }

    /**
     * 根据餐桌Id查看未结账的账单
     * @param tableId
     * @return
     */
    public boolean hasPayByTableId(int tableId){
        String sql = "select * from bill where diningTableId = ? and state = ? limit 0,1";
        Bill bill = billDAO.querySingle(sql, Bill.class, tableId,"未结账");
        return bill!=null;
    }

    /**
     * 根据餐桌id结账
     * @param tableId   餐桌id
     * @param payMethod  结账方式
     * @return
     */
    public boolean payBill(int tableId,String payMethod){
        //更新bill表
        String sql1 = "update bill set state = ? where diningTableId = ? and state = '未结账'";
        int rows = billDAO.update(sql1,payMethod,tableId);
        if (rows<=0){
            return false;
        }
        //更新diningtable表
        //餐桌结账后,state=空,orderName和orderTel也清空
        if (!dtS.resetDiningTableState(tableId)){
            return false;
        }
        return true;
    }
}
