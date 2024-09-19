package vn.edu.hanu.fit.ss1.group1.session4.armRace;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.ArrayList;
import java.util.List;

public class ArmRaceProblem extends AbstractProblem {
    ArrayList<Integer> listOfPeriods;
    // TODO: Initialize the problem (array of n number of periods)
    public ArmRaceProblem(ArrayList<Integer> listOfPeriods) {
        super(listOfPeriods.size(), 2);
        this.listOfPeriods = listOfPeriods;
    }

    @Override
    public void evaluate(Solution solution) {
        int totalMoney;
        Country firstCountry = new Country("X", 1000);
        Country secondCountry = new Country("Y", 2000);
        for (int i = 1; i < this.listOfPeriods.size(); i++) {
//            int sth = EncodingUtils.getInt(solution.getVariable(i));

        }
    }

    @Override
    public Solution newSolution() {
        return null;
    }
}
