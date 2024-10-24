package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import scheduling.FCFS;
import scheduling.PS;
import scheduling.RR;
import scheduling.SJF;
import scheduling.SchedulingAlgorithm;

public class Driver {

    @SuppressWarnings("UnnecessaryTemporaryOnConversionFromString")
    public static void main(String[] args) {
        //create scanner object
        Scanner scan;
        //try/catch to catch missing file
        try {
            //get inputs
            Scanner inputScanner = new Scanner(System.in);
            int simulationMode = -1;
            while (!(simulationMode == 0 || simulationMode == 1)) {
                System.out.println("Please select simulation mode [0 >> AUTO, 1 >> MANUAL] >> ");
                simulationMode = inputScanner.nextInt();
            }
            System.out.println("Please enter simulation unit time (ms) [1000 RECOMMENDED] >> ");
            int simulationUnit = inputScanner.nextInt();
            int quantumTime = -1;
            while (quantumTime <= 0) {
                System.out.println("Please enter Quantum (time slice) >> ");
                quantumTime = inputScanner.nextInt();
            }
            inputScanner.close();
            //get input file
            scan = new Scanner(new File("src/proc.txt"));
            //find the input algorithm from the first line
            String alg = scan.nextLine();
            //create a PCB array list for storing all processes
            ArrayList<PCB> allProcesses = new ArrayList<>();
            //create int id
            int id = 0;
            //for each process in the input file
            while (scan.hasNextLine()) {
                //get process line
                String line = scan.nextLine();
                //split the line into a string array
                String[] params = line.split(",\\s*");
                //process name
                String name = params[0];
                //arrival time
                int arrivalTime = Integer.parseInt(params[1]);
                //priority
                int priority = Integer.parseInt(params[2]);

                //cpu and io bursts
                List<Integer> cpuBursts = new ArrayList<>();
                List<Integer> ioBursts = new ArrayList<>();
                for (int i = 3; i < params.length; i += 2) {
                    cpuBursts.add(Integer.parseInt(params[i]));
                    if (i + 1 < params.length) {
                        ioBursts.add(Integer.parseInt(params[i + 1]));
                    }
                }

                //add new PCB to the all processes arraylist
                allProcesses.add(new PCB(name, id, arrivalTime, priority, cpuBursts, ioBursts, quantumTime, quantumTime));
                //increment id
                id++;
            }
            //close scanner
            scan.close();
            //create new scheduler
            SchedulingAlgorithm scheduler = null;
            //pick scheduling algorithm from input file
            switch (alg) {
                case "FCFS":
                    scheduler = new FCFS(allProcesses, simulationMode, simulationUnit);
                    break;
                case "SJF":
                    scheduler = new SJF(allProcesses, simulationMode, simulationUnit);
                    break;
                case "PS":
                    scheduler = new PS(allProcesses, simulationMode, simulationUnit);
                    break;
                case "RR":
                    scheduler = new RR(allProcesses, simulationMode, simulationUnit);
                    break;
                default:
                    System.err.println("Unsupported algorithm!");
            }
            //schedule
            scheduler.schedule();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
