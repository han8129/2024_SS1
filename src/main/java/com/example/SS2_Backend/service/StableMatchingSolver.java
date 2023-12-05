package com.example.SS2_Backend.service;

import com.example.SS2_Backend.dto.request.StableMatchingProblemDTO;
import com.example.SS2_Backend.dto.request.StableMatchingUIResult;
import com.example.SS2_Backend.dto.response.Progress;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.model.GameSolutionInsights;
import com.example.SS2_Backend.model.StableMatching.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class StableMatchingSolver {
//    SimpMessageSendingOperations simpMessagingTemplate;
//    private static final int RUN_COUNT_PER_ALGORITHM = 10; // for insight running, each algorithm will be run for 10 times

    public static List<PreferenceList> preferencesList;

    public static ArrayList<Double> coupleFitnessList = new ArrayList<>();

    public ResponseEntity<Response> solveStableMatching(StableMatchingProblemDTO request) {

        try{
            StableMatchingProblem problem = new StableMatchingProblem();

            problem.setPopulation(request.getIndividuals());
            problem.setAllPropertyNames(request.getAllPropertyNames());
            problem.setFitnessFunction(request.getFitnessFunction());

//            System.out.println(problem.printPreferenceLists());

            long startTime = System.currentTimeMillis();


            NondominatedPopulation results = solveProblem(
                    problem,
                    request.getAlgorithm(),
                    request.getPopulationSize(),
                    request.getGeneration(),
                    request.getMaxTime(),
                    request.getDistributedCores()
            );

            preferencesList = problem.getPreferenceLists();
            System.out.println(preferencesList);
            Matches m = (Matches) results.get(0).getAttribute("matches");
//            System.out.println(m.getMatches());
//            calculateFitnessList(preferencesList, m);
//            System.out.println(coupleFitnessList);

            ArrayList<Individual> individualsList = request.getIndividuals();

            long endTime = System.currentTimeMillis();
            double runtime = ((double) (endTime - startTime) / 1000 / 60);
            runtime = (runtime * 1000.0);
            System.out.println("Runtime: " + runtime + " Millisecond(s).");
            MatchingSolution matchingSolution = formatSolution(problem, results, runtime);
            matchingSolution.setIndividuals(individualsList);
//            System.out.println("RESPOND TO FRONT_END:");
//            System.out.println(matchingSolution);
            return ResponseEntity.ok(
                    Response.builder()
                            .status(200)
                            .message("Solve stable matching problem successfully!")
                            .data(matchingSolution)
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error solving stable matching problem.")
                            .data(null)
                            .build());
        }
    }
    private MatchingSolution formatSolution(StableMatchingProblem problem, NondominatedPopulation result, double Runtime){
        Solution solution = result.get(0);
        MatchingSolution matchingSolution = new MatchingSolution();
        double fitnessValue = solution.getObjective(0);
        Matches matches = (Matches) solution.getAttribute("matches");


        matchingSolution.setFitnessValue(-fitnessValue);
        matchingSolution.setMatches(matches);
        matchingSolution.setAlgorithm(problem.getAlgorithm());
        matchingSolution.setRuntime(Runtime);
        matchingSolution.setCoupleFitness(coupleFitnessList);

        return matchingSolution;
    }


    public String convertObjectToJson(Object object) {
        try {
            // Create an instance of ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            StableMatchingProblemDTO stableMatchingProblem = objectMapper.convertValue(object, StableMatchingProblemDTO.class);

            return objectMapper.writeValueAsString(stableMatchingProblem);
        } catch (Exception e) {
            // Handle exception if needed
            e.printStackTrace();
            return "Error converting object to JSON";
        }
    }



    private NondominatedPopulation solveProblem(StableMatchingProblem problem,
                                                       String algorithm,
                                                       int populationSize,
                                                       int generation,
                                                       int maxTime,
                                                       String distributedCores) {
        NondominatedPopulation result;
        problem.setAlgorithm(algorithm);
        try {
            if (distributedCores.equals("all")) {
                result = new Executor()
                        .withProblem(problem)
                        .withAlgorithm(algorithm)
                        .withMaxEvaluations(generation * populationSize) // we are using the number of generations and population size to calculate the number of evaluations
                        .withProperty("populationSize", populationSize)
                        .withProperty("maxTime", maxTime)
                        .distributeOnAllCores()
                        .run();


            } else {
                int numberOfCores = Integer.parseInt(distributedCores);
                result = new Executor()
                        .withProblem(problem)
                        .withAlgorithm(algorithm)
                        .withMaxEvaluations(generation * populationSize) // we are using the number of generations and population size to calculate the number of evaluations
                        .withProperty("populationSize", populationSize)
                        .withProperty("maxTime", maxTime)
                        .distributeOn(numberOfCores)
                        .run();
            }
            return result;
        } catch (Exception e) {

            // second attempt to solve the problem if the first run got some error
            if (distributedCores.equals("all")) {
                result = new Executor()
                        .withProblem(problem)
                        .withAlgorithm(algorithm)
                        .withMaxEvaluations(generation * populationSize) // we are using the number of generations and population size to calculate the number of evaluations
                        .withProperty("populationSize", populationSize)
                        .withProperty("maxTime", maxTime)
                        .distributeOnAllCores()
                        .run();


            } else {
                int numberOfCores = Integer.parseInt(distributedCores);
                result = new Executor()
                        .withProblem(problem)
                        .withAlgorithm(algorithm)
                        .withMaxEvaluations(generation * populationSize) // we are using the number of generations and population size to calculate the number of evaluations
                        .withProperty("populationSize", populationSize)
                        .withProperty("maxTime", maxTime)
                        .distributeOn(numberOfCores)
                        .run();
            }
            return result;
        }

//        for (Solution solution : result) {
//            System.out.println("Randomized Individuals Input Order (by MOEA): " + solution.getVariable(0).toString());
//            Matches matches = (Matches) solution.getAttribute("matches");
//            System.out.println("Output Matches (by Gale Shapley):\n" + matches.toString());
//            System.out.println("Fitness Score: " + -solution.getObjective(0));
    }



//    public ResponseEntity<Response> getProblemResultInsights(StableMatchingProblemDTO request, String sessionCode) {
////        log.info("Received request: " + request);
//        String[] algorithms = {"NSGAII", "NSGAIII", "eMOEA", "PESA2", "VEGA"};
//
//
//
//        simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", createProgressMessage("Initializing the problem..."));
//        StableMatchingProblem problem = new StableMatchingProblem(new ArrayList<Individual>(), "BCA", "ABC");
//
//        problem.setCompositeWeightFunction(request.getCompositeWeightFunction());
//        problem.setFitnessFunction(request.getFitnessFunction());
//        problem.setSpecifiedAlgorithm(request.getSpecifiedAlgorithm());
//
//        problem.setPopulationSize(request.getPopulationSize());
//        problem.setEvolutionRate(request.getEvolutionRate());
//        problem.setMaximumExecutionTime(request.getMaximumExecutionTime());
//
//        GameSolutionInsights gameSolutionInsights = initGameSolutionInsights(algorithms);
//
//        int runCount = 1;
//        int maxRunCount = algorithms.length * RUN_COUNT_PER_ALGORITHM;
//        // solve the problem with different algorithms and then evaluate the performance of the algorithms
////        log.info("Start benchmarking the algorithms...");
//        simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", createProgressMessage("Start benchmarking the algorithms..."));
//
//        for (String algorithm : algorithms) {
////            log.info("Running algorithm: " + algorithm + "...");
//            for (int i = 0; i < RUN_COUNT_PER_ALGORITHM; i++) {
//                System.out.println("Iteration: " + i);
//                long start = System.currentTimeMillis();
//
//                NondominatedPopulation results = solveProblem(
//                        problem,
//                        request.getSpecifiedAlgorithm(),
//                        request.getPopulationSize(),
//                        request.getFitnessFunction(),
//
//                        request.getEvolutionRate(),
//                        request.getCompositeWeightFunction(),
//                        request.getMaximumExecutionTime()
//                );
//
//                long end = System.currentTimeMillis();
//
//                double runtime = (double) (end - start) / 1000;
//                double fitnessValue = getFitnessValue(results);
//
//                // send the progress to the client
//                String message = "Algorithm " + algorithm + " finished iteration: #" + (i + 1) + "/" + RUN_COUNT_PER_ALGORITHM;
//                Progress progress = createProgress(message, runtime, runCount, maxRunCount);
//                System.out.println(progress);
//                simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", progress);
//                runCount++;
//
//                // add the fitness value and runtime to the insights
//                gameSolutionInsights.getFitnessValues().get(algorithm).add(fitnessValue);
//                gameSolutionInsights.getRuntimes().get(algorithm).add(runtime);
//
//
//            }
//
//        }
////        log.info("Benchmarking finished!");
//        simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", createProgressMessage("Benchmarking finished!"));
//
//        return ResponseEntity.ok(
//                Response.builder()
//                        .status(200)
//                        .message("Get problem result insights successfully!")
//                        .data(gameSolutionInsights)
//                        .build()
//        );
//    }

//    private GameSolutionInsights initGameSolutionInsights(String[] algorithms) {
//        GameSolutionInsights gameSolutionInsights = new GameSolutionInsights();
//        Map<String, List<Double>> fitnessValueMap = new HashMap<>();
//        Map<String, List<Double>> runtimeMap = new HashMap<>();
//
//        gameSolutionInsights.setFitnessValues(fitnessValueMap);
//        gameSolutionInsights.setRuntimes(runtimeMap);
//
//        for (String algorithm : algorithms) {
//            fitnessValueMap.put(algorithm, new ArrayList<>());
//            runtimeMap.put(algorithm, new ArrayList<>());
//        }
//
//        return gameSolutionInsights;
//    }
//
//    private Progress createProgressMessage(String message) {
//        return Progress.builder()
//                .inProgress(false) // this object is just to send a message to the client, not to show the progress
//                .message(message)
//                .build();
//    }
//
//    private Progress createProgress(String message, Double runtime, Integer runCount, int maxRunCount) {
//        int percent = runCount * 100 / maxRunCount;
//        int minuteLeff = (int) Math.ceil(((maxRunCount - runCount) * runtime) / 60); // runtime is in seconds
//        return Progress.builder()
//                .inProgress(true) // this object is just to send to the client to show the progress
//                .message(message)
//                .runtime(runtime)
//                .minuteLeft(minuteLeff)
//                .percentage(percent)
//                .build();
//    }
//
//    private double getFitnessValue(NondominatedPopulation result) {
//
//        Solution solution = result.get(0);
//        double fitnessValue = solution.getObjective(0);
//        return fitnessValue;
//
//    }

    private void calculateFitnessList(List<PreferenceList> pList, Matches m) {
        ArrayList<Double> p = new ArrayList<>();
        List<MatchItem> b = m.getMatches();
        for (MatchItem c : b) {
            int d = c.getIndividual1Index();
            int e = c.getIndividual2Index();
            List<PreferenceList.IndexValue> ofD = pList.get(d).getPreferenceList();
            List<PreferenceList.IndexValue> ofE = pList.get(e).getPreferenceList();
            double dScore = 0.0;
            double eScore = 0.0;
                for (int i = 0; i < ofD.size(); i++) {
                    if (ofD.get(i).getIndividualIndex() == e) {
                        dScore += ofD.get(i).getValue();
                    }
                }
                for (int i = 0; i < ofE.size(); i++) {
                    if (ofE.get(i).getIndividualIndex() == d) {
                        eScore += ofE.get(i).getValue();
                    }
                }
//            System.out.println(dScore + eScore);
            p.add(dScore+eScore);
        }
        coupleFitnessList = p;
    }
}
