package com.inf;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by davidboutet on 17-11-07.
 */
public class History{
    private ArrayList<Employe> employeeList;

    public History(ArrayList<Employe> employeeList) throws FileNotFoundException{
        try{
            this.employeeList = employeeList;
            Utils.writeJsonHistory("history.json", buildJsonHistory());

            JSONArray jsonArray = buildJsonHistory();
//            System.out.println(jsonArray.toString(2));
        }catch (Exception e){
            throw new FileNotFoundException(e.getMessage());
        }
    }

    private JSONArray buildJsonHistory() throws FileNotFoundException{

        JSONArray historicJson = Utils.getJsonArrayFromFile("output/history.json");

        JSONObject jO = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        jO.accumulate("date", dateFormat.format(date));
        jO.accumulate("total_employe", getTotalEmploye());
        jO.accumulate("less_than_50k", getNumberOfEmployeBySalaryRange(0));
        jO.accumulate("between_50k_and_100k", getNumberOfEmployeBySalaryRange(1));
        jO.accumulate("more_than_100k", getNumberOfEmployeBySalaryRange(2));
        jO.accumulate("employe_in_department_2", getNumberOfEmployeByDepartment(2));
        jO.accumulate("maximal_charge_of_work", getMaximalChargeOfWork());
        jO.accumulate("maximal_salary", getMaximalSalary());
        jO.accumulate("maximal_salary", getMaximalSalary());

        historicJson.add(jO);

        return historicJson;
    }

    private Integer getTotalEmploye(){
        return !employeeList.isEmpty()?employeeList.size():0;
    }

    private Integer getNumberOfEmployeBySalaryRange(Integer action){
        Integer numberOfEmploye = 0;
        Integer minimum = 50000,
                maximum = 100000;
        for(Employe employe:this.employeeList){
            //less than 50000
            if(action == 0){
                if(employe.calculateTotalSalary() <= minimum){
                    numberOfEmploye++;
                }
            }
            //between 50000 and 100000
            else if(action == 1){
                if(employe.calculateTotalSalary()>=minimum&&employe.calculateTotalSalary()<=maximum){
                    numberOfEmploye++;
                }
            }
            //more than 100000
            else{
                if(employe.calculateTotalSalary() >= maximum){
                    numberOfEmploye++;
                }
            }
        }
        return numberOfEmploye;
    }

    private Integer getNumberOfEmployeByDepartment(Integer department){
        Integer numberOfEmploye = 0;
        for(Employe employe:this.employeeList){
            if(employe.getDepartmentType().equals(department)){
                numberOfEmploye++;
            }
        }
        return numberOfEmploye;
    }

    private Double getMaximalChargeOfWork(){
        Double maxWorkedHours = 0.00;
        for(Employe employe:this.employeeList){
            if(employe.getWorkedHours() > maxWorkedHours){
                maxWorkedHours = employe.getWorkedHours();
            }
        }
        return maxWorkedHours;
    }

    private String getMaximalSalary(){
        Double maxSalary = 0.00;
        for(Employe employe:this.employeeList){
            if(employe.calculateTotalSalary() > maxSalary){
                maxSalary = employe.calculateTotalSalary();
            }
        }
        return Employe.twoDigits(Employe.roundToFive(maxSalary)) + " $";
    }

    private String getMinimalSalary(){
        Double minSalary = this.employeeList.size()>0?this.employeeList.get(0).calculateTotalSalary():0.00;
        for(Employe employe:this.employeeList){
            if(employe.calculateTotalSalary() < minSalary){
                minSalary = employe.calculateTotalSalary();
            }
        }
        return Employe.twoDigits(Employe.roundToFive(minSalary)) + " $";
    }
}
