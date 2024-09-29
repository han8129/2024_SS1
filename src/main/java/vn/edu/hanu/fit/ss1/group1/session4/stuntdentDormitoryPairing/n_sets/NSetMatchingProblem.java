package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing.n_sets;

import lombok.experimental.FieldDefaults;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.*;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class NSetMatchingProblem extends AbstractProblem {

    List<Set<Entity>> entitySets;
    Set<List<Entity>> excludedMatches;

    public NSetMatchingProblem(List<Set<Entity>> entitySets) {
        super(calculateTotalEntities(entitySets), 3);
        this.entitySets = entitySets;
        this.excludedMatches = new HashSet<>();
    }

    private static int calculateTotalEntities(List<Set<Entity>> entitySets) {
        return entitySets.stream().mapToInt(Set::size).sum();
    }

    public void addExcludedMatch(List<Entity> match) {
        if (match.size() != entitySets.size())
            throw new IllegalArgumentException("Excluded match must include one entity from each set");
        excludedMatches.add(match);
    }

    @Override
    public void evaluate(Solution solution) {
        Map<Entity, List<Entity>> matching = decodeMatching(solution);
        int unmatchedEntities = 0;
        int capacityViolations = 0;
        int preferenceMismatch = 0;

        for (int i = 0; i < entitySets.size(); i++) {
            Set<Entity> entitySet = entitySets.get(i);
            for (Entity entity : entitySet) {
                List<Entity> match = matching.get(entity);
                if (match == null || match.size() != entitySets.size() - 1) unmatchedEntities++;
                else {
                    if (isExcludedMatch(entity, match)) continue;
                    if (entity.getCapacity() < match.size()) capacityViolations++;
                    preferenceMismatch += calculatePreferenceMismatch(entity, match);
                }
            }
        }

        solution.setObjective(0, unmatchedEntities);
        solution.setObjective(1, capacityViolations);
        solution.setObjective(2, preferenceMismatch);
    }

    private Map<Entity, List<Entity>> decodeMatching(Solution solution) {
        Map<Entity, List<Entity>> matching = new HashMap<>();
        int variableIndex = 0;

        for (int i = 0; i < entitySets.size(); i++) {
            Set<Entity> entitySet = entitySets.get(i);
            for (Entity entity : entitySet) {
                List<Entity> match = new ArrayList<>();
                for (int j = 0; j < entitySets.size(); j++) {
                    if (j != i) {
                        int matchIndex = EncodingUtils.getInt(solution.getVariable(variableIndex++));
                        if (matchIndex >= 0 && matchIndex < entitySets.get(j).size()) match.add(entitySets.get(j).toArray(new Entity[0])[matchIndex]);
                    }
                }
                matching.put(entity, match);
            }
        }
        return matching;
    }

    private int calculatePreferenceMismatch(Entity entity, List<Entity> match) {
        int mismatch = 0;
        for (Entity matchedEntity : match) {
            if (!entity.getPreferences().contains(matchedEntity)) mismatch++;
            if (!matchedEntity.getPreferences().contains(entity)) mismatch++;
        }
        return mismatch;
    }

    private boolean isExcludedMatch(Entity entity, List<Entity> match) {
        List<Entity> fullMatch = new ArrayList<>(match);
        fullMatch.add(entity);
        return excludedMatches.contains(fullMatch);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), 3);
        int variableIndex = 0;
        for (int i = 0; i < entitySets.size(); i++) {
            Set<Entity> entitySet = entitySets.get(i);
            for (int j = 0; j < entitySets.size(); j++) {
                if (i != j) {
                    for (int k = 0; k < entitySet.size(); k++)
                        solution.setVariable(variableIndex++, EncodingUtils.newInt(0, entitySets.get(j).size() - 1));
                }
            }
        }
        return solution;
    }

}
