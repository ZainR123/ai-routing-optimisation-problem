package com.aim.project.sdsstp.instance.reader;


import com.aim.project.sdsstp.SDSSTPObjectiveFunction;
import com.aim.project.sdsstp.instance.SDSSTPInstance;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceReaderInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 * <p>
 * Methods needing to be implemented:
 * - public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random)
 */
public class SDSSTPInstanceReader implements SDSSTPInstanceReaderInterface {

    private static SDSSTPInstanceReader oInstance;

    private SDSSTPInstanceReader() {
    }

    public static SDSSTPInstanceReader getInstance() {

        if (oInstance == null) {
            oInstance = new SDSSTPInstanceReader();
        }
        return oInstance;
    }

    @Override
    public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random) {

        int i;
        int iNumberOfLandmarks = 0;
        int[][] aiTimeDistanceMatrix = null;
        String strInstanceName = null;
        SDSSTPLocation oTourOffice = null;
        SDSSTPLocation[] aoLandmarks = null;
        int[] aiTimeDistancesFromTourOffice = new int[0];
        int[] aiTimeDistancesToTourOffice = new int[0];
        int[] aiVisitingDurations = new int[0];

        try {

            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)));
            String currentLine = reader.readLine();

            while (currentLine != null) {

                switch (currentLine) {
                    case "NAME:":
                        strInstanceName = reader.readLine();
                        break;
                    case "LANDMARKS:":
                        iNumberOfLandmarks = Integer.parseInt(reader.readLine());
                        break;
                    case "TIME_MATRIX:":
                        aiTimeDistanceMatrix = new int[iNumberOfLandmarks][iNumberOfLandmarks];
                        for (i = 0; i < iNumberOfLandmarks; i++) {
                            currentLine = reader.readLine();
                            for (int j = 0; j < iNumberOfLandmarks; j++) {
                                aiTimeDistanceMatrix[i][j] = Integer.parseInt(currentLine.split(" ")[j]);
                            }
                        }
                        break;
                    case "TIME_FROM_OFFICE:":
                        currentLine = reader.readLine();
                        aiTimeDistancesFromTourOffice = new int[currentLine.split(" ").length];
                        for (i = 0; i < currentLine.split(" ").length; i++) {
                            aiTimeDistancesFromTourOffice[i] = Integer.parseInt(currentLine.split(" ")[i]);
                        }
                        break;
                    case "TIME_TO_OFFICE:":
                        currentLine = reader.readLine();
                        aiTimeDistancesToTourOffice = new int[currentLine.split(" ").length];
                        for (i = 0; i < currentLine.split(" ").length; i++) {
                            aiTimeDistancesToTourOffice[i] = Integer.parseInt(currentLine.split(" ")[i]);
                        }
                        break;
                    case "VISIT_DURATION:":
                        currentLine = reader.readLine();
                        aiVisitingDurations = new int[currentLine.split(" ").length];
                        for (i = 0; i < currentLine.split(" ").length; i++) {
                            aiVisitingDurations[i] = Integer.parseInt(currentLine.split(" ")[i]);
                        }
                        break;
                    case "OFFICE_LOCATION:":
                        currentLine = reader.readLine();
                        oTourOffice = new SDSSTPLocation(Double.parseDouble(currentLine.split(" ")[0]), Double.parseDouble(currentLine.split(" ")[1]));
                        break;
                    case "LANDMARK_LOCATIONS:":
                        aoLandmarks = new SDSSTPLocation[iNumberOfLandmarks];

                        for (i = 0; i < iNumberOfLandmarks; i++) {
                            currentLine = reader.readLine();
                            aoLandmarks[i] = new SDSSTPLocation(Double.parseDouble(currentLine.split(" ")[0]), Double.parseDouble(currentLine.split(" ")[1]));
                        }
                        break;
                }
                currentLine = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        SDSSTPObjectiveFunction f = new SDSSTPObjectiveFunction(aiTimeDistanceMatrix, aiTimeDistancesFromTourOffice, aiTimeDistancesToTourOffice, aiVisitingDurations);
        return new SDSSTPInstance(strInstanceName, iNumberOfLandmarks, oTourOffice, aoLandmarks, random, f);
    }
}