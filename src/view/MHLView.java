package view;

import domain.*;
import service.*;
import utils.Utility;


import java.util.List;

public class MHLView {
    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    private boolean loop = true;
    private String key = "";
    private EmployeeService employeeService = new EmployeeService();
    private DiningTableService diningTableService = new DiningTableService();
    private MenuService menuService = new MenuService();
    private BillService billService = new BillService();
    /**
     * 显示所有餐桌的状态
     */
    public void listTableState(){
        List<DiningTable> list = diningTableService.getAllTableState();
        System.out.println("\n餐桌编号\t\t\t"+"预定状态");
        for (DiningTable d:list
        ) {
            System.out.println(d);
        }
        System.out.println("===============显示完毕===============");
    }

    /**
     * 预定餐桌
     */
    public void orderTable(){
        System.out.println("===============预定餐桌===============");
        System.out.println("请选择要预定的餐桌编号(-1退出): ");
        int orderId = Utility.readInt();
        //终止预定餐桌
        if (orderId == -1) {
            System.out.println("===============退出预定===============");
            return;
        }
        //是否预定餐桌
        char key = Utility.readConfirmSelection();
        //确认预定
        if (key=='Y'){
            //判断要预定的餐桌是否存在
            DiningTable tableStateById = diningTableService.getTableById(orderId);
            //1.要预定的餐桌不存在
            if (tableStateById==null){
                System.out.println("预定的餐桌不存在！");
                return;
            }
            //2.要预定的餐桌存在但是已经被占用或者被预定
            if (!"空".equals(tableStateById.getState())){
                System.out.println("该餐桌正在用餐或已被预定");
                return;
            }
            //3.可以预定到指定餐桌
            System.out.println("输入订餐人姓名:");
            String orderName = Utility.readString(50);
            System.out.println("输入订餐人电话:");
            String orderTel = Utility.readString(50);
            if (diningTableService.orderTable(orderId,orderName,orderTel)){
                System.out.println("预定成功");
            }else {
                System.out.println("预定失败");
            }
        }else {
            System.out.println("客户取消预订");
            return;
        }
    }

    /**
     * 点餐功能
     */
    public void orderMenu(){
        System.out.println("======点餐服务======");
        System.out.println("输入要点餐的桌号(-1退出点餐):");
        int diningTableId = Utility.readInt();
        if (diningTableId==-1){
            System.out.println("退出点餐");
            return;
        }
        System.out.println("输入菜品编号(-1退出点餐):");
        int menuId = Utility.readInt();
        if (menuId==-1){
            System.out.println("退出点餐");
            return;
        }
        System.out.println("输入菜品数量(-1退出点餐):");
        int nums = Utility.readInt();
        if (nums==-1){
            System.out.println("退出点餐");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable = diningTableService.getTableById(diningTableId);
        if (diningTable==null){
            System.out.println("餐桌不存在");
            return;
        }
        //验证菜品是否存在
        Menu menu = menuService.getMenuById(menuId);
        if (menu==null){
            System.out.println("菜品不存在");
            return;
        }
        boolean b = billService.orderMenu(menuId, nums, diningTableId);
        if (b==true){
            System.out.println("点餐成功");
        }else {
            System.out.println("点餐失败");
        }


    }

    /**
     * 显示账单信息
     * @return
     */
    public void getBill() {

        System.out.println("\n编号\t\t菜品号\t\t菜品名\t\t价格\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
        for (Bill b: billService.getBill()
             ) {
            System.out.println(b);
        }
        System.out.println("===============显示完毕===============");
    }

    /**
     * 结账
     */
    public void payBill(){
        System.out.println("======结账服务======");
        System.out.println("输入要结账的餐桌id(-1退出结账)");
        int tableId = Utility.readInt();
        if (tableId==-1){
            System.out.println("取消结账");
            return;
        }
        //验证餐桌是否存在
        if (diningTableService.getTableById(tableId)==null){
            System.out.println("餐桌不存在");
            return;
        }
        //验证是否有未结账的账单
        if (!billService.hasPayByTableId(tableId)){
            System.out.println("该餐桌账单已结清");
            return;
        }
        System.out.println("输入结账方式（微信/支付宝/现金）,回车取消结账");
        String payMethod = Utility.readString(20,"");
        if ("".equals(payMethod)){
            System.out.println("取消结账");
            return;
        }

        char key = Utility.readConfirmSelection();
        if (key == 'Y') {
            if (billService.payBill(tableId,payMethod)) {
                System.out.println("结账成功");
            } else {
                System.out.println("结账失败");
            }
        } else {
            billService.payBill(tableId,payMethod);
            System.out.println("取消结账");
        }
    }

    /**
     * 主菜单
     */
    public void mainMenu(){
        while (loop){
            //一级菜单
            System.out.println("===============满汉楼==============");
            System.out.println("\t\t 1 登录满汉楼");
            System.out.println("\t\t 2 退出满汉楼");
            System.out.println("请输入你的选择: ");
            key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.print("请输入员工号: ");
                    String empId = Utility.readString(50);
                    System.out.print("请输入密码: ");
                    String pwd = Utility.readString(50);
                    //连接到数据库验证用户名和密码
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empId,pwd);
                    if (employee!=null){
                        System.out.println(employee.getName()+"登陆成功");
                        while (loop){
                            //二级菜单
                            System.out.println("\n===============满汉楼二级菜单==============");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 查看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.println("请输入您的选择: ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listTableState();
                                    break;
                                case "2":
                                    orderTable();
                                    break;
                                case "3":
                                    System.out.println("id\t"+"name\t"+"type\t"+"price");
                                    for (Menu m: menuService.getMenu()
                                         ) {
                                        System.out.println(m);
                                    }
                                    break;
                                case "4":
                                    orderMenu();
                                    break;
                                case "5":
                                    getBill();
                                    break;
                                case "6":
                                    payBill();
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("您的输入有误，请重新输入: ");
                            }
                        }
                    }else {
                        System.out.println("登陆失败");
                    }
                    break;
                case "2":
                    loop=false;
                    break;
                default:
                    System.out.println("您的输入有误，请重新输入: ");
            }
        }
    }
}
