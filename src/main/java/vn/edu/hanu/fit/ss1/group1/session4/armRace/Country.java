package vn.edu.hanu.fit.ss1.group1.session4.armRace;

public class Country {
    String countryName;
    int amountOfMoney;
    boolean hasWeapon;

    public Country(String countryName, int amountOfMoney) {
        this.countryName = countryName;
        this.amountOfMoney = amountOfMoney;
        this.hasWeapon = false;
    }

    public double increaseMoney(int currentAmount) {
        return currentAmount * 1.1;
    }

    public void doNothing() {
        System.out.println(this.countryName + "chose to do nothing!");
    }

    public void buyWeapons() {
        this.amountOfMoney = (int) (this.amountOfMoney - Math.round(this.amountOfMoney * 0.3));
        this.hasWeapon = false;
    }

    public void attack(Country anotherCountry) {

    }
}
