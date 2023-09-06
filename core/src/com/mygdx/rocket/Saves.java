package com.mygdx.rocket;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.rocket.build.AttachmentPoint;
import com.mygdx.rocket.build.Engine;
import com.mygdx.rocket.build.FuelTank;
import com.mygdx.rocket.build.Part;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Saves {

    public static void encode(Rocket rocket) throws IOException{
        File myFile = new File("C:\\Program Files\\Rocket Flight Game\\assets\\saves\\" + rocket.getName() + ".txt");
        try{
            FileWriter fileWriter = new FileWriter(myFile);
            // Rocket name
            fileWriter.write(rocket.getName());
            // Number of parts
            fileWriter.write("\n" + rocket.getParts().size);

            // for each part
            for(Part part : rocket.getParts()) {
                // part name
                fileWriter.write("\n" + part.getName());
                // part type
                fileWriter.write("\n" + part.getType());
                // part mass
                fileWriter.write("\n" + part.getMass());
                // part size x and y
                fileWriter.write("\n" + part.getSize().x);
                fileWriter.write("\n" + part.getSize().y);
                // part location x and y
                fileWriter.write("\n" + part.getLocation().x);
                fileWriter.write("\n" + part.getLocation().y);
                // rotation
                fileWriter.write("\n" + part.getRotation());

                // number of attachment points in part
                fileWriter.write("\n" + part.getAttachmentPoints().size);

                // each attachment point of array
                for (AttachmentPoint point : part.getAttachmentPoints()) {
                    // position
                    fileWriter.write("\n" + point.getPosition().x);
                    fileWriter.write("\n" + point.getPosition().y);
                    // location
                    fileWriter.write("\n" + point.getLocation().x);
                    fileWriter.write("\n" + point.getLocation().y);
                    // direction
                    fileWriter.write("\n" + point.getDirection());
                    // is occupied or not
                    fileWriter.write("\n" + point.isOccupied());
                }

                // fuelCapacity for FUELTANK
                if(part instanceof FuelTank) {
                    FuelTank fuelTank = (FuelTank) part;
                    fileWriter.write("\n" + fuelTank.getFuelCapacity());
                }
                //thrust and consumption for ENGINE
                if(part instanceof Engine) {
                    Engine engine = (Engine) part;
                    fileWriter.write("\n" + engine.getThrust());
                    fileWriter.write("\n" + engine.getFuelConsumptionRate());
                }
            }
            fileWriter.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static Rocket decode(File file) throws FileNotFoundException {
        Rocket rocket = null;
        System.out.println(rocket);
        try {
            Scanner sc = new Scanner(file);
            // get name
            String rocketName = sc.nextLine();
            System.out.println(rocketName);
            rocket = new Rocket(rocketName);
            // num parts
            int numParts = sc.nextInt();
            System.out.println(numParts);
            //for each part
            for (int i = 0; i < numParts; i++) {
                Array<AttachmentPoint> points = new Array<>();
                float fuelCapacity = 0f;
                float thrust = 0f;
                float fuelConsumptionRate = 0f;
                Part part;
                // get name
                String name = sc.nextLine();
                System.out.println(name);
                sc.nextLine();
                // get type
                String type = sc.nextLine();
                System.out.println(type);
                // get mass
                float mass = Float.parseFloat(sc.nextLine());
                System.out.println(mass);
                // get size x and y
                float partX = sc.nextFloat();
                float partY = sc.nextFloat();
                System.out.println(partX + ", " + partY);
                // get location x and y
                float locationX = sc.nextFloat();
                float locationY = sc.nextFloat();
                System.out.println(locationX + ", " + locationY);
                // get rotation
                int rotation = sc.nextInt();
                System.out.println(rotation);
                // attachment points
                int numPoints = sc.nextInt();
                System.out.println(numPoints);

                // for each point
                for (int j = 0; j < numPoints; j++) {
                    // position x and y
                    float pointX = sc.nextFloat();
                    float pointY = sc.nextFloat();
                    System.out.println(pointX + ", " + pointY);
                    // location x and y
                    float pointLX = sc.nextFloat();
                    float pointLY = sc.nextFloat();
                    System.out.println(pointLX + ", " + pointLY);
                    // direction point
                    int direction = sc.nextInt();
                    System.out.println(direction);
                    // is occupied point
                    boolean isOccupied = sc.nextBoolean();
                    System.out.println(isOccupied);
                    // add point to thing
                    AttachmentPoint point = new AttachmentPoint(new Vector2(pointX, pointY), direction);
                    point.setLocation(new Vector2(pointLX, pointLY));
                    point.setOccupied(isOccupied);
                    points.add(point);
                }

                // FUELTANK SPECIAL VARIABLES
                if (type.equals("fuelTank")) {
                    fuelCapacity = sc.nextFloat();
                    System.out.println(fuelCapacity);
                }
                // ENGINE SPECIAL VARIABLES
                if (type.equals("engine")) {
                    thrust = sc.nextFloat();
                    fuelConsumptionRate = sc.nextFloat();
                }

                if (type.equals("fuelTank")) {
                    part = new FuelTank(name, mass, new Vector2(partX, partY), points, fuelCapacity);
                } else if (type.equals("engine")) {
                    part = new Engine(name, mass, new Vector2(partX, partY), points, thrust, fuelConsumptionRate);
                } else {
                    part = new Part(name, mass, new Vector2(partX, partY), points);
                }
                part.setLocation(new Vector2(locationX, locationY));
                part.setRotation(rotation);
                part.setType(type);
                // add part to rocket
                rocket.addPart(part);
            }
            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rocket;
    }
}
