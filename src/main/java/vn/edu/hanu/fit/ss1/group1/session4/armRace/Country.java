package vn.edu.hanu.fit.ss1.group1.session4.armRace;

/*
* Create a new Country instance which includes:
* - amountOfMoney: Current amount of money which changes during the battle
* - hasWeapons: Check whether the country has weapons to perform `attack()` action
* */

/* Nhắc nhỏ
* Em vẫn chưa nghĩ được gì nhiều, vì nhiều phần:
* - Mục đích của thuật toán ở đây có phải là: Với một số lượng giai đoạn được cho sẵn thì sẽ tối ưu lối chơi
* Hay "lựa chọn chiến lược giữa các quốc gia"?
* - Vũ khí ở đây chỉ đơn giản là có hay không chứ không phân loại vũ khí và nếu chỉ là tấn công về một phía
* thì vẫn sẽ tiếp tục trò chơi đúng không? Trừ khi cả hai cùng tấn công trong một lượt thì sẽ kết thúc?
* **Vậy thì**: Bài toán này muốn tối ưu gì?
*
* */
public class Country {
    public int amountOfMoney;
    public boolean hasWeapon;

    public Country(int amountOfMoney, boolean hasWeapon) {
        this.amountOfMoney = amountOfMoney;
        this.hasWeapon = hasWeapon;
    }
}
