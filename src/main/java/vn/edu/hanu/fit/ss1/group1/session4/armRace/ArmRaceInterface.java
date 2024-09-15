package vn.edu.hanu.fit.ss1.group1.session4.armRace;

public interface ArmRaceInterface {
    /* Number -> Number
    What this function does?
    - Buy new weapons
    - Subtract the amount of money
    */
    public int buyWeapon(int countryCurrentMoney);
    /*
    *
    * */
    public void attack();
    /*
    * As the function name said, it does nothing
    * */
    public void doNothing();

    /*
    * Number -> Number
    * increaseMoney(int currentAmount) -> Number
    * -> Increase the current amount of money by 10% (just approximately, since it follows )
    * */
    public int increaseMoney(int currentAmount);
}
