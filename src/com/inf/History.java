package com.inf;

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

    public History(ArrayList<Employe> employeeList, String historyAction) throws FileNotFoundException{
        try{
            this.employeeList = employeeList;
            actionDispatcher(historyAction);
        }catch (Exception e){
            throw new FileNotFoundException(e.getMessage());
        }
    }

    private JSONObject buildJsonHistory() throws FileNotFoundException{
        JSONObject historicJson = Utils.getJsonFromFile("output/history.json");

        JSONObject jO = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        jO.accumulate("date", dateFormat.format(date));

        if(!historicJson.isEmpty()){
            jO.accumulateAll(writeWithHistory(historicJson));
        }else{
            jO.accumulateAll(writeWithoutHistory());
        }
        return jO;
    }

    private JSONObject writeWithHistory(JSONObject historicJson){
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("total_employe", getTotalEmploye() + historicJson.getInt("total_employe"));
        jsonObject.accumulate("less_than_50k", getNumberOfEmployeBySalaryRange(0) + historicJson.getInt("less_than_50k"));
        jsonObject.accumulate("between_50k_and_100k", getNumberOfEmployeBySalaryRange(1) + historicJson.getInt("between_50k_and_100k"));
        jsonObject.accumulate("more_than_100k", getNumberOfEmployeBySalaryRange(2) + historicJson.getInt("more_than_100k"));
        jsonObject.accumulate("employe_in_department_0", getNumberOfEmployeByDepartment(0) + historicJson.getInt("employe_in_department_0"));
        jsonObject.accumulate("employe_in_department_1", getNumberOfEmployeByDepartment(1) + historicJson.getInt("employe_in_department_1"));
        jsonObject.accumulate("employe_in_department_2", getNumberOfEmployeByDepartment(2) + historicJson.getInt("employe_in_department_2"));
        jsonObject.accumulate("maximal_charge_of_work", Math.max(getMaximalChargeOfWork(), historicJson.getDouble("maximal_charge_of_work")));
        jsonObject.accumulate("maximal_salary", Employe.twoDigits(Math.max(getMaximalSalary(), Employe.stringToDouble(historicJson.getString("maximal_salary")))) + " $");
        jsonObject.accumulate("minimal_salary", Employe.twoDigits(Math.min(getMinimalSalary(), Employe.stringToDouble(historicJson.getString("minimal_salary")))) + " $");
        return jsonObject;
    }

    private JSONObject writeWithoutHistory(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("total_employe", getTotalEmploye());
        jsonObject.accumulate("less_than_50k", getNumberOfEmployeBySalaryRange(0));
        jsonObject.accumulate("between_50k_and_100k", getNumberOfEmployeBySalaryRange(1));
        jsonObject.accumulate("more_than_100k", getNumberOfEmployeBySalaryRange(2));
        jsonObject.accumulate("employe_in_department_0", getNumberOfEmployeByDepartment(0));
        jsonObject.accumulate("employe_in_department_1", getNumberOfEmployeByDepartment(1));
        jsonObject.accumulate("employe_in_department_2", getNumberOfEmployeByDepartment(2));
        jsonObject.accumulate("maximal_charge_of_work", getMaximalChargeOfWork());
        jsonObject.accumulate("maximal_salary", Employe.twoDigits(getMaximalSalary()) + " $");
        jsonObject.accumulate("minimal_salary", Employe.twoDigits(getMinimalSalary()) + " $");
        return jsonObject;
    }

    private Integer getTotalEmploye(){
        return !employeeList.isEmpty()?employeeList.size():0;
    }

    private Integer getNumberOfEmployeBySalaryRange(Integer action){
        Integer numberOfEmploye = 0;
        Integer minimum = 50000,
                maximum = 100000;
        for(Employe employe:this.employeeList){
            switch(action){
            //less than 50000
            case 0:
                if(employe.calculateTotalSalary() <= minimum){
                    numberOfEmploye++;
                }
            break;
            //between 50000 and 100000
            case 1:
                if(employe.calculateTotalSalary()>=minimum&&employe.calculateTotalSalary()<=maximum){
                    numberOfEmploye++;
                }
            break;
            //more than 100000
            default:
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

    private Double getMaximalSalary(){
        Double maxSalary = 0.00;
        for(Employe employe:this.employeeList){
            if(employe.calculateTotalSalary() > maxSalary){
                maxSalary = employe.calculateTotalSalary();
            }
        }
        return Employe.roundToFive(maxSalary);
    }

    private Double getMinimalSalary(){
        Double minSalary = this.employeeList.size()>0?this.employeeList.get(0).calculateTotalSalary():0.00;
        for(Employe employe:this.employeeList){
            if(employe.calculateTotalSalary() < minSalary){
                minSalary = employe.calculateTotalSalary();
            }
        }
        return Employe.roundToFive(minSalary);
    }

    public void actionDispatcher(String action) throws Exception{
        if(action.equals("-S")){
            showHistoryOnConsole();
            Utils.writeJsonHistory("history.json", buildJsonHistory());
        }else if(action.equals("-SR")){
            Utils.writeJsonHistory("history.json", buildJsonHistory());
            resetHistory();
        }else{
            Utils.writeJsonHistory("history.json", buildJsonHistory());
        }
    }

    private void showHistoryOnConsole() throws FileNotFoundException{
        JSONObject jsonObject = buildJsonHistory();
        System.out.println(jsonObject.toString(2));
    }

    private void resetHistory() throws FileNotFoundException{
        try{
            Utils.writeJsonHistory("history.json", new JSONObject());
            System.out.println("History has been reset.");
        }catch (Exception e){
            throw new FileNotFoundException("History file not found.");
        }
    }
}
